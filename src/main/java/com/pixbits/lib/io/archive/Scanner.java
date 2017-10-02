package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.io.archive.handles.ArchiveHandle;
import com.pixbits.lib.io.archive.handles.BinaryHandle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;
import com.pixbits.lib.io.archive.handles.NestedArchiveHandle;
import com.pixbits.lib.io.archive.support.MemoryArchive;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class Scanner
{
  private final ScannerOptions options;
  
  public Scanner(ScannerOptions options)
  {
    this.options = options;
  }
  
  private boolean skipAndReport(VerifierEntry string)
  {
    if (options.shouldSkip.test(string))
    {
      options.onSkip.accept(string);
      return true;
    }
    return false;
  }
  
  private IInArchive openArchive(Path path, boolean keepOpen) throws FormatUnrecognizedException
  {
    try
    {
      RandomAccessFileInStream rfile = new RandomAccessFileInStream(new RandomAccessFile(path.toFile(), "r"));
      ArchiveFormat format = ArchiveFormat.guessFormat(path);
      
      IInArchive archive = SevenZip.openInArchive(format.nativeFormat, rfile);

      if (archive.getArchiveFormat() == null)
        throw new FormatUnrecognizedException(path, "Archive format unrecognized");
      
      if (!keepOpen)
        rfile.close();
      return archive;
    }
    catch (IOException e)
    {
      throw new FormatUnrecognizedException(path, "Archive format unrecognized");
    }
  }
  
  private VerifierEntry scanNestedArchive(IInArchive archive, int index, ArchiveHandle outer) throws IOException
  {
    NestedArchiveBatch batch = null;

    /* extract archive in memory */
    int size = (int)(long)archive.getProperty(index, PropID.SIZE);
    String fileName = (String)archive.getProperty(index, PropID.PATH);

    MemoryArchive memoryArchive = MemoryArchive.load(archive, index, size);

    ArchiveFormat format = ArchiveFormat.guessFormat(fileName);
    IInArchive marchive = memoryArchive.open(format.nativeFormat);
    
    int itemCount = marchive.getNumberOfItems();
    
    List<NestedArchiveHandle> handlesForArchive = new ArrayList<>();    
    
    /* for each item in nested archive use scanArchiveItem */
    for (int i = 0; i < itemCount; ++i)
    {
      /* if we found a valid binary in the nested archive add it to the batch */
      NestedArchiveHandle inner = (NestedArchiveHandle)scanArchiveItem(marchive, i, outer.path(), outer);
      if (inner != null)
      { 
        if (skipAndReport(inner))
          continue;
        else
        {
          handlesForArchive.add(inner);
        }
      }
    }
      
    marchive.close();
    
    if (!handlesForArchive.isEmpty())
      batch = new NestedArchiveBatch(handlesForArchive);
    
    return batch;
  }
  
  private VerifierEntry scanArchiveItem(IInArchive archive, int i, Path path, ArchiveHandle outer) throws IOException
  {    
    long size = (long)archive.getProperty(i, PropID.SIZE);
    Long lcompressedSize = (Long)archive.getProperty(i, PropID.PACKED_SIZE);
    long compressedSize = lcompressedSize != null ? lcompressedSize : -1;
    String fileName = (String)archive.getProperty(i, PropID.PATH);
    
    Boolean isFolder = (Boolean)archive.getProperty(i, PropID.IS_FOLDER);
    
    ArchiveFormat format = ArchiveFormat.formatForNative(archive.getArchiveFormat());
    
    /* if it's a folder then just return */
    if (isFolder != null && isFolder)
      return null;
    
    ArchiveFormat internalFormat = ArchiveFormat.guessFormat(fileName);
    
    long crc = options.assumeCRCisCorrect ? Integer.toUnsignedLong((Integer)archive.getProperty(i, PropID.CRC)) : -1;
    ArchiveHandle archiveHandle = new ArchiveHandle(path, format, fileName, i, size, compressedSize, crc);
    
    /* if internal file name is not a known filename for an archive then it's a binary */
    if (internalFormat == null)
    {
      /* if outer is null then this is a binary inside an archive */
      if (outer == null)
      {
        if (skipAndReport(archiveHandle))
          ;
        else
          return archiveHandle;
      }
      /* else it's a binary inside a nested archive */
      else
      {
        NestedArchiveHandle handle = new NestedArchiveHandle(outer.path(), outer.format, outer.internalName, outer.indexInArchive, 
            format, fileName, i, size, compressedSize, crc);
        
        if (skipAndReport(handle))
          ;
        else
          return handle;
      }    
    }
    /* else it's an archive nested inside another archive so we should inspect it */
    else if (outer == null)
    {
      if (options.scanNestedArchives)
      {
        return scanNestedArchive(archive, i, archiveHandle);
      }
    }
    
    return null;
  }
  
  public List<VerifierEntry> scanPaths(Stream<Path> paths) throws IOException
  {
    return paths
      .map(StreamException.rethrowFunction(this::scanSinglePath))
      .flatMap(l -> l.stream())
      .collect(Collectors.toList());
  }

  public List<VerifierEntry> scanSinglePath(Path path) throws IOException
  {
    List<VerifierEntry> results = new ArrayList<>();
    
    ArchiveFormat format = ArchiveFormat.guessFormat(path);
    
    /* if format is null then it's not a known readable archive extension */
    if (format == null)
    {
      BinaryHandle handle = new BinaryHandle(path);
      
      if (skipAndReport(handle))
        ;
      else if (options.scanBinaries)
        results.add(handle);
    }
    /* file could be an archive, we should cache IInArchive and use the specific method to scan it */
    else
    {
      if (options.scanArchives)
      {
        /* for each element in the archive, scan it, it could return an ArchiveHandle or a NestedArchiveHandle */
        try
        {
          IInArchive archive = openArchive(path, true);
          int itemCount = archive.getNumberOfItems();
          
          for (int i = 0; i < itemCount; ++i)
          {
            /* TODO: check extension of file? */
            VerifierEntry entry = scanArchiveItem(archive, i, path, null);
            
            if (entry != null)
              results.add(entry);
          }
          
          archive.close();
        }
        catch (FormatUnrecognizedException e)
        {
          options.onFaultyArchive.accept(path.toString());
        }
      }
    }
    
    return results;
  }
}

package com.pixbits.lib.io.archive.handles;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Objects;

import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.io.archive.ExtractionCanceledException;
import com.pixbits.lib.io.archive.support.ArchiveExtractCallback;
import com.pixbits.lib.io.archive.support.ArchiveExtractPipedStream;
import com.pixbits.lib.io.archive.support.MemoryArchive;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class NestedArchiveHandle extends Handle
{  
  private Path path;
  public final int indexInArchive;
  public final String internalName;
  public final ArchiveFormat format;
  
  public final int nestedIndexInArchive;
  public final String nestedInternalName;
  public final ArchiveFormat nestedFormat;
  
  public final long size;
  public final long compressedSize;
  public final long crc;
  
  private MemoryArchive innerArchive;
  private IInArchive mappedArchive;
  
  public NestedArchiveHandle(Path path, ArchiveFormat format, String internalName, Integer indexInArchive, ArchiveFormat nestedFormat, String nestedInternalName, int nestedIndexInArchive, long size, long compressedSize, long crc)
  {
    this.path = path.normalize();
    
    this.internalName = internalName;
    this.indexInArchive = indexInArchive;
    this.format = format;
    
    this.nestedIndexInArchive = nestedIndexInArchive;
    this.nestedInternalName = nestedInternalName;
    this.nestedFormat = nestedFormat;
    
    this.size = size;
    this.compressedSize = compressedSize;
    this.innerArchive = null;
    this.mappedArchive = null;
    this.crc = crc;
  }
  
  @Override public boolean equals(Object object)
  {
    if (object instanceof NestedArchiveHandle)
    {
      NestedArchiveHandle handle = (NestedArchiveHandle) object;
      return handle.path().equals(path()) && handle.indexInArchive == indexInArchive && handle.nestedIndexInArchive == nestedIndexInArchive; 
    }
    else
      return false;
  }
  
  @Override public int hashCode() { return Objects.hash(path, indexInArchive, nestedIndexInArchive); }
  
  public MemoryArchive getMemoryArchive() { return innerArchive; }
  public void setMemoryArchive(MemoryArchive archive) { this.innerArchive = archive; } 
  
  public IInArchive getMappedArchive() { return mappedArchive; }
  public void setMappedArchive(IInArchive archive) { this.mappedArchive = archive; }
    
  public void loadArchiveInMemory()
  {
    try
    {      
      RandomAccessFileInStream rfile = new RandomAccessFileInStream(new RandomAccessFile(path.toFile(), "r"));
      IInArchive archive = SevenZip.openInArchive(ArchiveFormat.guessFormat(path).nativeFormat, rfile);
      int outerSize = (int)(long)archive.getProperty(indexInArchive, PropID.SIZE);
      innerArchive = MemoryArchive.load(archive, indexInArchive, outerSize);
      ArchiveFormat format = ArchiveFormat.guessFormat(internalName);
      mappedArchive = innerArchive.open(format.nativeFormat);
      archive.close();
      innerArchive.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  protected IInArchive open()
  {
    if (innerArchive == null || mappedArchive == null)
      loadArchiveInMemory();
    
    return mappedArchive;
  }
  
  @Override public final boolean isArchive() { return true; }
  
  @Override public Path path() { return path; }
  @Override public String relativePath() { return path.getFileName().toString() + "/" + internalName + "/" + nestedInternalName; } 

  @Override public String fileName() { return internalName; }
  
  @Override public String toString() { return path.getFileName().toString() + "/" + internalName + "/" + nestedInternalName; }
  @Override public String plainName() { return path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf('.')); }
  
  //TODO: makes sense to return the innermost name?
  @Override public String plainInternalName() { return internalName.substring(0, internalName.toString().lastIndexOf('.')); }
  @Override public String getInternalExtension() { return nestedInternalName.substring(nestedInternalName.toString().lastIndexOf('.')+1); }
  
  @Override public long size() { return size; }
  @Override public long compressedSize() { return compressedSize; }
  @Override public long crc() { return crc; }
  
  

  public boolean renameInternalFile(String newName)
  {       
    return false;
  }
  
  @Override
  public void relocate(Path file)
  {
    this.path = file;
  }
  
  @Override
  public Handle relocateInternal(String internalName)
  {
    return null;//new Zip7Handle(file, internalName);
  }
  
  @Override
  public InputStream getInputStream() throws IOException
  {
    final IInArchive archive = open();    
    final ArchiveExtractPipedStream stream = new ArchiveExtractPipedStream(archive, nestedIndexInArchive);
    final ArchiveExtractCallback callback = new ArchiveExtractCallback(stream); 
    
    Runnable r = () -> {
      //System.out.println("Extract Thread Started");
      try
      {
        archive.extract(new int[] { nestedIndexInArchive }, false, callback);
        callback.close();
      }
      catch (ExtractionCanceledException e)
      {
        
      }
      catch (IOException e)
      {
        System.err.println(String.format("Exception while extracting file %s from nested archive %s (index: %d) contained in %s (index: %d)", 
            nestedInternalName, internalName, nestedIndexInArchive, path.getFileName().toString(), indexInArchive)); 
        
        e.printStackTrace();
      }
      //System.out.println("Extract Thread Stopped");
    };
    
    new Thread(r).start();
 
    return stream.getInputStream();
  }

  @Override public Handle getVerifierHandle() { return this; }

}
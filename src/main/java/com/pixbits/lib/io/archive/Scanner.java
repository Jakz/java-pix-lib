package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import com.pixbits.lib.io.FolderScanner;
import com.pixbits.lib.io.archive.handles.ArchiveHandle;
import com.pixbits.lib.io.archive.handles.BinaryHandle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;
import com.pixbits.lib.io.archive.handles.NestedArchiveHandle;
import com.pixbits.lib.io.archive.support.MemoryArchive;
import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.log.Log;
import com.pixbits.lib.log.ProgressLogger;
import com.pixbits.lib.functional.StreamException;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class Scanner
{
  private static final Comparator<Pair<Integer,String>> pairComparator = (o1, o2) -> o1.first.compareTo(o2.first); 
  
  private class ScannerEnvironment
  {
    final List<BinaryHandle> binaryHandles;
    final List<ArchiveHandle> archiveHandles;
    final Map<Path, Set<Pair<Integer,String>>> nestedArchives;
    final Set<ScannerEntry> skipped;
    final Set<Path> faulty;
    
    ScannerEnvironment()
    {
      this.binaryHandles = new ArrayList<>();
      this.archiveHandles = new ArrayList<>();
      this.nestedArchives = new HashMap<>();
      this.skipped = new HashSet<>();
      this.faulty = new HashSet<>();
    }
  }
  
  private Consumer<ScannerEntry> onEntryFound = e -> {};
  
  private ProgressLogger progressLogger = Log.getProgressLogger(Scanner.class);
  
  final public static PathMatcher archiveMatcher = ArchiveFormat.getReadableMatcher();
  
  //final private GameSet set;
  final private ScannerOptions options;
  
  public void setProgressLogger(ProgressLogger logger) { this.progressLogger = logger; }
  
  public void setOnEntryFound(Consumer<ScannerEntry> onEntryFound)
  {
    this.onEntryFound = onEntryFound;
  }
  
  public Scanner(ScannerOptions options)
  {
    this.options = options;
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
  
  private List<NestedArchiveBatch> scanNestedArchives(ScannerEnvironment env) throws IOException
  {
    List<NestedArchiveBatch> handles = new ArrayList<>();
    
    env.nestedArchives.entrySet().stream().forEach(StreamException.rethrowConsumer(entry -> {
      Path archivePath = entry.getKey();
      Set<Pair<Integer,String>> indices = entry.getValue();
      
      IInArchive archive = openArchive(archivePath, true);
      
      for (Pair<Integer,String> pair : indices)
      {
        int index = pair.first;
        String internalName = pair.second;
        
        /* extract archive in memory */
        int size = (int)(long)archive.getProperty(index, PropID.SIZE);
        String fileName = (String)archive.getProperty(index, PropID.PATH);

        MemoryArchive memoryArchive = MemoryArchive.load(archive, index, size);

        ArchiveFormat format = ArchiveFormat.guessFormat(fileName);
        IInArchive marchive = memoryArchive.open(format.nativeFormat);
        
        int itemCount = marchive.getNumberOfItems();
        
        List<NestedArchiveHandle> handlesForArchive = new ArrayList<>();      
  
        for (int i = 0; i < itemCount; ++i)
        {
          ScannerEntry.Archived data = scanArchive(marchive, i, archivePath, fileName, env);
          if (data != null)
          { 
            if (options.shouldSkip.test(data))
              env.skipped.add(data);
            else
            {               
              NestedArchiveHandle handle = new NestedArchiveHandle(archivePath, ArchiveFormat.formatForNative(archive.getArchiveFormat()), fileName, index, 
                ArchiveFormat.formatForNative(marchive.getArchiveFormat()), data.fileName, i, data.size, data.compressedSize, data.crc);
                        
              handlesForArchive.add(handle);
            }
          }
        }
        
        marchive.close();
        
        if (!handlesForArchive.isEmpty())
          handles.add(new NestedArchiveBatch(handlesForArchive));
      }
      
      archive.close();
      

    }));
    
    return handles;
  }
  
  public ScannerEntry.Archived scanArchive(IInArchive archive, int i, Path path, String nestedPath, ScannerEnvironment env) throws IOException
  {
    long size = (long)archive.getProperty(i, PropID.SIZE);
    Long lcompressedSize = (Long)archive.getProperty(i, PropID.PACKED_SIZE);
    long compressedSize = lcompressedSize != null ? lcompressedSize : -1;
    String fileName = (String)archive.getProperty(i, PropID.PATH);
    
    Boolean isFolder = (Boolean)archive.getProperty(i, PropID.IS_FOLDER);
    
    if (isFolder != null && isFolder)
      return null;
    
    /* if file ends with archive extension */
    if (nestedPath == null && ArchiveFormat.guessFormat(fileName) != null)
    {                
      // TODO: if archived file has archive extension but it's binary then it's skipped, maybe move check to outside condition
      if (options.scanNestedArchives)
      {    
        env.nestedArchives.computeIfAbsent(path, p -> new TreeSet<>(pairComparator)).add(new Pair<>(i, fileName));
      }
      
      /* don't return anything since it's a nested archive and it will be checked later */
      return null;
    }
    else
    {
      /* if crc is considered valid then we can get it and check size to filter out elements */
      long crc = options.assumeCRCisCorrect ? Integer.toUnsignedLong((Integer)archive.getProperty(i, PropID.CRC)) : -1;
      ScannerEntry.Archived entry = null;
      
      if (nestedPath == null)
        entry = new ScannerEntry.Archived(path.getFileName().toString(), fileName, size, compressedSize, crc);
      else
        entry = new ScannerEntry.Nested(path.getFileName().toString(), nestedPath, fileName, size, compressedSize, crc);
   
      /* if predicate tells that entry should be skipped */
      if (options.shouldSkip.test(entry))
        env.skipped.add(entry);
      /* otherwise return the entry */
      else
      {
        onEntryFound.accept(entry);
        return entry;
      }
    }
    
    return null;
  }

  public HandleSet computeHandles(List<Path> spaths) throws IOException
  {
    FolderScanner folderScanner = new FolderScanner(options.scanSubfolders);
    
    Set<Path> paths = folderScanner.scan(spaths);
    
    progressLogger.startProgress(Log.INFO2, "Finding files...");
    final float count = paths.size();
    final AtomicInteger current = new AtomicInteger(0);
    
    final ScannerEnvironment env = new ScannerEnvironment();
    
    paths.stream().forEach(StreamException.rethrowConsumer(path -> {
      progressLogger.updateProgress(current.getAndIncrement() / count, "");
      
      boolean shouldBeArchive = archiveMatcher.matches(path.getFileName());
            
      if (options.scanArchives && shouldBeArchive)
      {      
        try (IInArchive archive = openArchive(path, false))
        {
          int itemCount = archive.getNumberOfItems();
          
          if (true)
          {   
            for (int i = 0; i < itemCount; ++i)
            {
              /* TODO: check extension of file? */
              ScannerEntry.Archived entry = scanArchive(archive, i, path, null, env);
              if (entry != null)
              {
                if (options.shouldSkip.test(entry))
                  env.skipped.add(entry);
                else
                {
                  onEntryFound.accept(entry);
                  env.archiveHandles.add(new ArchiveHandle(path, ArchiveFormat.formatForNative(archive.getArchiveFormat()), entry.fileName, i, entry.size, entry.compressedSize, entry.crc));
                }              
              }
            }
          }
        }
        catch (FormatUnrecognizedException e)
        {
          env.faulty.add(path);
        }
      }
      else
      {
        long size = Files.size(path);
        ScannerEntry entry = new ScannerEntry.Binary(path.getFileName().toString(), size, -1);
       
        /* check if file should be skipped according to predicate */
        if (options.shouldSkip.test(entry))
          env.skipped.add(entry);    
        else
        {
          onEntryFound.accept(entry);
          env.binaryHandles.add(new BinaryHandle(path));
        }
      }    
    }));
    
    progressLogger.endProgress();
    
    List<NestedArchiveBatch> nestedHandles = scanNestedArchives(env);

    return new HandleSet(env.binaryHandles, env.archiveHandles, nestedHandles, env.faulty, env.skipped);
  }
}

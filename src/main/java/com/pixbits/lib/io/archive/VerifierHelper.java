package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.io.archive.handles.Handle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;
import com.pixbits.lib.io.digest.DigestOptions;
import com.pixbits.lib.io.digest.Digester;
import com.pixbits.lib.io.digest.HashCache;
import com.pixbits.lib.log.Log;
import com.pixbits.lib.log.Logger;
import com.pixbits.lib.log.ProgressLogger;

public class VerifierHelper<U extends Verifiable>
{
  private static final ProgressLogger progressLogger = Log.getProgressLogger(Verifier.class);
  private static final Logger logger = Log.getLogger(Verifier.class);
  
  private final boolean multiThreaded;
  private final Verifier<U> verifier;
  private final Digester digester;
  
  private float total;
  private AtomicInteger current = new AtomicInteger();
  
  public VerifierHelper(VerifierOptions options, boolean multiThreaded, HashCache<U> cache)
  {
    digester = new Digester(new DigestOptions(true, options.matchMD5, options.matchSHA1, multiThreaded)); 
    verifier = new Verifier<>(options, digester, cache);
    this.multiThreaded = multiThreaded;
  }
  
  public int verify(HandleSet handles) throws IOException
  {
    progressLogger.startProgress(Log.INFO1, "Verifying roms...");
    current.set(0);
    total = handles.binaries.size() + handles.archives.size() 
      + handles.nestedArchives.stream().mapToInt(NestedArchiveBatch::size).sum();
    
    int found = 0;

    found = verify(handles.binaries) + verify(handles.archives);
    found += verifyNested(handles.nestedArchives);
      
    progressLogger.endProgress();
    
    return found;
  }
  
  private int verifyNested(List<NestedArchiveBatch> archives) throws IOException
  {
    Stream<NestedArchiveBatch> stream = archives.stream();
    AtomicInteger count = new AtomicInteger();
    
    if (multiThreaded)
      stream = stream.parallel();
    
    stream.forEach(StreamException.rethrowConsumer(batch -> {
      count.addAndGet(verifier.verifyNestedArchive(batch));
    }));
    
    return count.get();
  }
    
  
  private int verify(List<? extends Handle> handles) throws IOException
  {
    Stream<? extends Handle> stream = handles.stream();
    
    AtomicInteger count = new AtomicInteger();
    
    if (multiThreaded)
      stream = stream.parallel();
        
    stream.forEach(StreamException.rethrowConsumer(handle -> {      
      progressLogger.updateProgress(current.getAndIncrement() / total, handle.file().getFileName().toString());
      U element = verifier.verify(handle);
      
      if (element != null)
      {
        if (element.alreadyHasHandle())
        {
          logger.w("Duplicate ROM found for %s: %s", element.name(), handle.toString());
        }
        else
        {
          element.setHandle(handle);
          count.incrementAndGet();
        }
      }
    }));
    
    return count.get();
  }
}
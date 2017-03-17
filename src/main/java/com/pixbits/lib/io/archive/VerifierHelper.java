package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.io.archive.handles.Handle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;
import com.pixbits.lib.io.digest.DigestOptions;
import com.pixbits.lib.io.digest.Digester;
import com.pixbits.lib.io.digest.HashCache;
import com.pixbits.lib.log.Reporter;

public class VerifierHelper<U extends Verifiable>
{
  public static class Report
  {
    public static enum Type
    {
      START,
      END,
      SUCCESS_BINARY,
      SUCCESS_ARCHIVED,
      SUCCESS_NESTED
    }
    
    public final Type type;
    public final boolean success;
    
    Report(Type type, boolean success)
    {
      this.type = type;
      this.success = success;
    }
    
    Report(Type type) { this(type, false); }
  }
    
  private Reporter<Report> reporter;
  
  private final boolean multiThreaded;
  private final Verifier<U> verifier;
  private final Digester digester;
    
  public VerifierHelper(VerifierOptions options, boolean multiThreaded, HashCache<U> cache, BiConsumer<U, Handle> callback)
  {
    digester = new Digester(new DigestOptions(true, options.matchMD5, options.matchSHA1, multiThreaded)); 
    verifier = new Verifier<>(options, digester, cache);
    verifier.setCallback(callback);
    this.multiThreaded = multiThreaded;
    this.reporter = new Reporter<>();
  }
  
  public void setReporter(Consumer<Report> reporter)
  {
    this.reporter.setDestination(reporter);
  }
  
  public void verify(HandleSet handles) throws IOException
  {
    reporter.report(() -> new Report(Report.Type.START));
    

    verify(handles.binaries);
    verify(handles.archives);
    verifyNested(handles.nestedArchives);
      
    reporter.report(() -> new Report(Report.Type.END));
  }
  
  private void verifyNested(List<NestedArchiveBatch> archives) throws IOException
  {
    Stream<NestedArchiveBatch> stream = archives.stream();
    
    if (multiThreaded)
      stream = stream.parallel();
    
    stream.forEach(StreamException.rethrowConsumer(batch -> verifier.verifyNestedArchive(batch)));
  }
    
  
  private void verify(List<? extends Handle> handles) throws IOException
  {
    Stream<? extends Handle> stream = handles.stream();
        
    if (multiThreaded)
      stream = stream.parallel();
        
    stream.forEach(StreamException.rethrowConsumer(handle -> {      
      U element = verifier.verify(handle);
      verifier.callback().accept(element, handle);
    }));
  }
}
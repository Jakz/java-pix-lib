package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
    
  public VerifierHelper(VerifierOptions options, boolean multiThreaded, HashCache<U> cache, Consumer<List<VerifierResult<U>>> callback)
  {
    digester = new Digester(new DigestOptions(true, options.matchMD5, options.matchSHA1, multiThreaded)); 
    verifier = new Verifier<>(options, digester, cache);
    verifier.setCallback(callback);
    
    verifier.setTransformer(options.transformer());
    
    this.multiThreaded = multiThreaded;
    this.reporter = new Reporter<>();
  }
  
  public void setReporter(Consumer<Report> reporter)
  {
    this.reporter.setDestination(reporter);
  }
  
  public void verify(HandleSet handles) throws IOException, NoSuchAlgorithmException
  {
    reporter.report(() -> new Report(Report.Type.START));
    
    for (VerifierEntry entry : handles)
      verifier.verify(entry);
      
    reporter.report(() -> new Report(Report.Type.END));
  }
}
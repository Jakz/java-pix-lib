package com.pixbits.lib.io.archive.support;

import java.io.IOException;
import java.util.function.Consumer;

import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZipException;

public class ArchiveExtractCallback implements IArchiveExtractCallback
{
  private ArchiveExtractStream stream;
  
  public ArchiveExtractCallback(ArchiveExtractStream stream)
  {
    this.stream = stream;
  }
  
  public ArchiveExtractStream stream() { return stream; }
  
  public void close() throws IOException { stream.close(); }
  
  public ISequentialOutStream getStream(int index, ExtractAskMode mode)
  {
    //System.out.println("ArchiveExtractCallback::getStream("+index+","+mode+")");

    if (mode != ExtractAskMode.EXTRACT) return null;
    return stream;
  }
  
  public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException
  {
    //System.out.println("ArchiveExtractCallback::prepareOperation("+extractAskMode+")");
  }
  
  public void setOperationResult(ExtractOperationResult result) throws SevenZipException
  {
    //System.out.println("ArchiveExtractCallback::setOperationResult("+result+")");
  }
  
  public void setCompleted(long completeValue) throws SevenZipException
  {
    //System.out.println("ArchiveExtractCallback::setCompleted("+completeValue+")");
  }

  public void setTotal(long total) throws SevenZipException
  {
    //System.out.println("ArchiveExtractCallback::setTotal("+total+")");
  }
  
  public static class Logging extends ArchiveExtractCallback
  {
    private final Consumer<Float> onProgress;
    private final Consumer<Boolean> onComplete;
    private float total;
    
    public Logging(ArchiveExtractStream stream, Consumer<Float> onProgress, Consumer<Boolean> onComplete)
    {
      super(stream);
      this.onProgress = onProgress;
      this.onComplete = onComplete;
    }
    
    public void setCompleted(long completeValue) throws SevenZipException
    {
      onProgress.accept(completeValue / total);
    }

    public void setTotal(long total) throws SevenZipException
    {
      this.total = total;
    }
    
    public void setOperationResult(ExtractOperationResult result) throws SevenZipException
    {
      onComplete.accept(result == ExtractOperationResult.OK);
    }
  }
  
  public static class Blocking extends ArchiveExtractCallback
  {
    private final Object lock;
    private boolean finished;
    
    public Blocking(ArchiveExtractStream stream)
    {
      super(stream);
      lock = new Object();
    }
    
    public void setOperationResult(ExtractOperationResult result) throws SevenZipException
    {
      super.setOperationResult(result);
      synchronized (lock)
      {
        finished = true;
        lock.notifyAll();
      }
    }
    
    public void await()
    {
      try
      {
        synchronized (lock)
        {
          while (!finished)
           lock.wait();
        }
      } 
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
}
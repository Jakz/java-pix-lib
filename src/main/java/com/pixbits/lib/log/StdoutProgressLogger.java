package com.pixbits.lib.log;

public class StdoutProgressLogger
{
  public static final ProgressLoggerFactory BACKSPACED_BUILDER = s -> new BackspacedLogger(s);
  public static final ProgressLoggerFactory PLAIN_BUILDER = s -> new PlainLogger(s);
  public static final ProgressLoggerFactory ONE_LINER_BUILDER = s -> new OneLineLogger(s);
  
  private static class BackspacedLogger implements ProgressLogger
  {
    private int lastProgress;
    final static int PROGRESS_LENGTH = 20;
    
    BackspacedLogger(LogScope scope)
    {
      
    }
    
    @Override public void startProgress(Log type, String message)
    {
      System.out.println("["+type+"] "+message);
      lastProgress = -1;
    }
    
    @Override synchronized public void updateProgress(float percent, String message)
    {
      int toPrint = (int)(percent*PROGRESS_LENGTH);
      int ipercent = (int)(percent*100);
            
      if (ipercent != lastProgress)
      {
        lastProgress = ipercent;
        
        System.out.print("\r[");
  
        
        int i = 0;
        for (; i < toPrint; ++i)
          System.out.print(".");
        for (; i < PROGRESS_LENGTH; ++i)
          System.out.print(" ");
        
        System.out.printf("] %3d%% %s", ipercent, message.length() < 40 ? message : (message.substring(0, 36)+"..."));
        System.out.flush();
      }
    }
    
    @Override public void endProgress()
    {
      System.out.print("\r[");
      for (int i = 0; i < PROGRESS_LENGTH; ++i)
        System.out.print(".");
      System.out.println("] 100%                                         ");
    }
  }
  
  private static class PlainLogger implements ProgressLogger
  {
    PlainLogger(LogScope s) { }
    
    @Override
    public void startProgress(Log type, String message)
    {
      System.out.println(/*"["+type+"] "+*/message);
      
    }

    @Override
    public void updateProgress(float percent, String message)
    {
      System.out.printf("%2.2f%% %s\n", (percent*100), message);
    }

    @Override
    public void endProgress()
    {
      
    }   
  }
  
  private static class OneLineLogger implements ProgressLogger
  {
    private final int barLength = 20;
    private int currentLength = 0;
    
    OneLineLogger(LogScope s) { }
    
    @Override
    public void startProgress(Log type, String message)
    {
      System.out.println(/*"["+type+"] "+*/message);
      currentLength = 0;
    }

    @Override
    public void updateProgress(float percent, String message)
    {
      int newLength = (int)(barLength*percent);
      
      if (newLength > currentLength)
      {
        System.out.printf(".");
        currentLength = newLength;
      }
    }

    @Override
    public void endProgress()
    {
      System.out.println("");
    }  
  }

}

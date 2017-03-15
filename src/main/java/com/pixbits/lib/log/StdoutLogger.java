package com.pixbits.lib.log;

class StdoutLogger extends Logger implements ProgressLogger
{
  private int lastProgress;
  boolean showProgress = true;
    
  StdoutLogger(LogScope scope)
  {
    super(scope);
  }
    
  @Override public void doLog(Log type, String message, LogAttribute attribute)
  {
    if (type != null && type.ordinal() > logLevel.ordinal())
      return;
    
    if (type == Log.DEBUG || type == Log.ERROR)
      System.err.println("["+type+"] "+message);
    else if (type != null)
      System.out.println("["+type+"] "+message);
    else
      System.out.println(message);
  }
  
  final static int PROGRESS_LENGTH = 20;
      
  @Override public void startProgress(Log type, String message)
  {
    System.out.println("["+type+"] "+message);
    lastProgress = -1;
  }
  
  @Override synchronized public void updateProgress(float percent, String message)
  {
    if (!showProgress) return;
    
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
package com.pixbits.lib.log;

public interface ProgressLogger
{
  public void startProgress(Log type, String message);
  public void updateProgress(float percent, String message);
  public void endProgress();
  
  public static final ProgressLogger NULL_LOGGER = new ProgressLogger()
  {
    @Override public void startProgress(Log type, String message) { }
    @Override public void updateProgress(float percent, String message) { }
    @Override public void endProgress() { }
  };
  
  public static final ProgressLogger STDOUT_PROGRESS = new StdoutProgressLogger();     
}

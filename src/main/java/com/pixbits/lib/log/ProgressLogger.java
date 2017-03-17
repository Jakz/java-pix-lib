package com.pixbits.lib.log;

public interface ProgressLogger
{
  public void startProgress(Log type, String message);
  public void updateProgress(float percent, String message);
  public void endProgress();
}

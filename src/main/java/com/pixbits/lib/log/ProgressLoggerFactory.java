package com.pixbits.lib.log;

import java.awt.Frame;

@FunctionalInterface
public interface ProgressLoggerFactory
{
  ProgressLogger build(LogScope scope);
  
  public static final ProgressLoggerFactory NULL_FACTORY = s ->
    new ProgressLogger()
    {
      @Override public void startProgress(Log type, String message) { }
      @Override public void updateProgress(float percent, String message) { }
      @Override public void endProgress() { }
    };
    
  public static final ProgressLoggerFactory DIALOG_FACTORY = new ProgressLoggerFactory()
  {
    private final Frame parent = null;
    
    @Override
    public ProgressLogger build(LogScope scope)
    {
      return new ProgressDialogLogger(scope, parent);
    }
  };
}

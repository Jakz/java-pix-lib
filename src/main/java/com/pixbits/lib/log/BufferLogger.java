package com.pixbits.lib.log;

public class BufferLogger extends Logger
{
  private final LogBuffer buffer;
  
  BufferLogger(LogScope scope, LogBuffer buffer)
  {
    super(scope);
    this.buffer = buffer;
  }

  @Override protected <T extends LogAttribute> void doLog(Log level, String message, T attribute)
  {
    buffer.addEntry(level, scope, message, attribute);
  }

  @Override
  public void startProgress(Log type, String message)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateProgress(float percent, String message)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void endProgress()
  {
    // TODO Auto-generated method stub
    
  }
}

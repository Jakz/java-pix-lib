package com.pixbits.lib.log;

@FunctionalInterface
public interface LoggerFactory
{
  Logger build(LogScope scope);
  
  public static final LoggerFactory STDOUT = s -> new StdoutLogger(s);
  public static final LoggerFactory STDOUT_PROGRESS = s -> new StdoutProgressLogger(s);
  
  public static final LoggerFactory DEFAULT = STDOUT;
  
  public static class BufferLoggerFactory implements LoggerFactory
  {
    private final LogBuffer buffer;
    
    public BufferLoggerFactory(LogBuffer buffer) { this.buffer = buffer; }

    @Override
    public Logger build(LogScope scope)
    {
      return new BufferLogger(scope, buffer);
    }   
  }
}

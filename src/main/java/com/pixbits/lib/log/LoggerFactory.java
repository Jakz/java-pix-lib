package com.pixbits.lib.log;

@FunctionalInterface
public interface LoggerFactory
{
  Logger build(LogScope scope);
  
  
  public static final LoggerFactory STDOUT = s -> new StdoutLogger(s);
  public static final LoggerFactory DEFAULT = STDOUT;
  public static final LoggerFactory NULL_FACTORY = s -> new Logger(s) {
    @Override
    protected <T extends LogAttribute> void doLog(Log type, String message, T attribute) { }
  };
    
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

package com.pixbits.lib.log;

@FunctionalInterface
public interface LoggerFactory
{
  Logger build(LogScope scope);
  
  public static final LoggerFactory STDOUT = s -> new StdoutLogger(s);
  public static final LoggerFactory STDOUT_PROGRESS = s -> new StdoutProgressLogger(s);
  
  public static final LoggerFactory DEFAULT = STDOUT;
}

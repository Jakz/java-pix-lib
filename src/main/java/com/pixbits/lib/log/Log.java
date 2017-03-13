package com.pixbits.lib.log;

import java.util.HashMap;
import java.util.Map;

public enum Log
{
  ERROR,
  WARNING,
  INFO1,
  INFO2,
  INFO3,
  DEBUG
  ;

  public boolean isError() { return this == ERROR; }
  public boolean isWarning() { return this == WARNING; }
  
  
  private static final Map<LogScope, Logger> loggers;
  
  private static LoggerFactory factory;
  private static Log level = Log.DEBUG;
  
  

  static
  {
    loggers = new HashMap<>();
    factory = LoggerFactory.DEFAULT;
  }
  
  private static Logger build(LogScope scope)
  {
    Logger logger = factory.build( scope);
    logger.setLevel(level);
    return logger;
  }
  
  public static Logger getLogger()
  {
    return loggers.computeIfAbsent(null, s -> build(s));
  }
 
  public static Logger getLogger(Class<?> scope) { return getLogger(new LogScope.ClassScope(scope)); }
  public static Logger getLogger(String scope) { return getLogger(new LogScope.StringScope(scope)); }
    
  public static Logger getLogger(LogScope scope) 
  { 
    return loggers.computeIfAbsent(scope, s -> build(s));
  }
  
  public static void setFactory(LoggerFactory factory) { setFactory(factory, true); }
  public static void setFactory(LoggerFactory factory, boolean replaceExisting)
  {
    Log.factory = factory;
    
    if (replaceExisting)
      loggers.replaceAll((k, v) -> build(k));
  }
  
  public static void setLevel(Log level, boolean updateExisting)
  {
    Log.level = level;
    
    if (updateExisting)
      loggers.values().forEach(logger -> logger.setLevel(level));
  }
};
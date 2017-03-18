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
  public boolean isDebug () { return this == DEBUG; }
  
  
  private static final Map<LogScope, Logger> loggers;
  private static final Map<LogScope, ProgressLogger> progressLoggers;
  
  private static LoggerFactory factory;
  private static ProgressLoggerFactory progressFactory;
  private static Log level = Log.DEBUG;
  
  

  static
  {
    loggers = new HashMap<>();
    factory = LoggerFactory.DEFAULT;
    
    progressLoggers = new HashMap<>();
    progressFactory = ProgressLoggerFactory.NULL_FACTORY;
  }
  
  private static ProgressLogger buildProgress(LogScope scope)
  {
    ProgressLogger logger = progressFactory.build(scope);
    return logger;
  }
  
  private static Logger build(LogScope scope)
  {
    Logger logger = factory.build(scope);
    logger.setLevel(level);
    return logger;
  }
  
  public static Logger getLogger()
  {
    return loggers.computeIfAbsent(null, s -> build(s));
  }
 
  public static Logger getLogger(LogScope scope) 
  { 
    return loggers.computeIfAbsent(scope, s -> build(s));
  }
  public static Logger getLogger(Class<?> scope) { return getLogger(new LogScope.ClassScope(scope)); }
  public static Logger getLogger(String scope) { return getLogger(new LogScope.StringScope(scope)); }
    

  
  public static ProgressLogger getProgressLogger(LogScope scope)
  {
    return progressLoggers.computeIfAbsent(scope, s -> buildProgress(s));
  }
  public static ProgressLogger getProgressLogger(Class<?> scope) { return getProgressLogger(new LogScope.ClassScope(scope)); }
  public static ProgressLogger getProgressLogger(String scope) { return getProgressLogger(new LogScope.StringScope(scope)); }
  
  public static void setFactory(ProgressLoggerFactory factory) { setFactory(factory, true); }
  public static void setFactory(ProgressLoggerFactory factory, boolean replaceExisting)
  {
    Log.progressFactory = factory;
    
    if (replaceExisting)
      progressLoggers.replaceAll((k, v) -> buildProgress(k));
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
package com.pixbits.lib.log;

public abstract class Logger
{  
  protected Log logLevel;
  protected LogScope scope;
  
  public Logger(LogScope scope, Log logLevel)
  {
    this.logLevel = logLevel;
    this.scope = scope;
  }
  
  public Logger(LogScope scope)
  {
    this(scope, Log.INFO1);
  }
  
  abstract protected <T extends LogAttribute> void doLog(Log type, String message, T attribute);
  
  public final void setLevel(Log level) { this.logLevel = level; }
  
  private final void log(Log type, LogAttribute attr, String message, Object... args)
  {   
    log(type, String.format(message, args), attr);
  }

  private final void log(Log type, String message, LogAttribute attr)
  {
    doLog(type, message, attr);
  }
  
  public final void d(String message, Object... args) { log(Log.DEBUG, null, message, args); }
  public final void e(String message, Object... args) { log(Log.ERROR, null, message, args); }
  public final void w(String message, Object... args) { log(Log.WARNING, null, message, args); }
  public final void i1(String message, Object... args) { log(Log.INFO1, null, message, args); }
  public final void i2(String message, Object... args) { log(Log.INFO2, null, message, args); }
  public final void i3(String message, Object... args) { log(Log.INFO3, null, message, args); }
  public final void i(String message, Object... args) { i1(message, args); }
  
  public final void d(LogAttribute attr, String message, Object... args) { log(Log.DEBUG, attr, message, args); }
  public final void e(LogAttribute attr, String message, Object... args) { log(Log.ERROR, attr, message, args); }
  public final void w(LogAttribute attr, String message, Object... args) { log(Log.WARNING, attr, message, args); }
  public final void i1(LogAttribute attr, String message, Object... args) { log(Log.INFO1, attr, message, args); }
  public final void i2(LogAttribute attr, String message, Object... args) { log(Log.INFO2, attr, message, args); }
  public final void i3(LogAttribute attr, String message, Object... args) { log(Log.INFO3, attr, message, args); }
  public final void i(LogAttribute attr, String message, Object... args) { i1(attr, message, args); }
}

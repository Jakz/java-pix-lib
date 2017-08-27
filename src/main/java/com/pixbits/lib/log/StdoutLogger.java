package com.pixbits.lib.log;

class StdoutLogger extends Logger
{    
  StdoutLogger(LogScope scope)
  {
    super(scope);
  }
    
  @Override public void doLog(Log type, String message, LogAttribute attribute)
  {
    if (type != null && type.ordinal() > logLevel.ordinal())
      return;
    
    String attr = attribute != null ? ("["+attribute.toString()+"] ") : "";
    
    if (type == Log.DEBUG || type == Log.ERROR)
      System.err.println("["+type+"] "+attr+message);
    else if (type != null)
      System.out.println("["+type+"] "+attr+message);
    else
      System.out.println(message);
  }
}
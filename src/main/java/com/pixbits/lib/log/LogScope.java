package com.pixbits.lib.log;

public interface LogScope
{
  public static class ClassScope implements LogScope
  {
    final public Class<?> scope;
    
    ClassScope(Class<?> scope) { this.scope = scope; }   
    @Override public String name() { return scope.getName(); }
  }
  
  public static class StringScope implements LogScope
  {
    final public String scope;  
    StringScope(String scope) { this.scope = scope; }
    @Override public String name() { return scope; }
  }
  
  public static final LogScope ANY = new LogScope() { @Override public String name() { return ""; } };
  
  String name();
}

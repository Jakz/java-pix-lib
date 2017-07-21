package com.pixbits.lib.yaml;

public class YamlException extends RuntimeException
{
  public YamlException(String message)
  {
    super(message);
  }
  
  public YamlException(String message, Throwable cause)
  {
    super(message, cause);
  }
}

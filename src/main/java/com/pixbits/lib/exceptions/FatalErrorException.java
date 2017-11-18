package com.pixbits.lib.exceptions;

public class FatalErrorException extends RuntimeException
{
  public FatalErrorException(Exception e, String message)
  {
    super(message, e);
  }
  
  public FatalErrorException(Exception e)
  {
    super(e);
  }
  
  public FatalErrorException(String message)
  {
    super(message);
  }
  
  public FatalErrorException(String message, Object... args)
  {
    this(String.format(message, args));
  }
  
}

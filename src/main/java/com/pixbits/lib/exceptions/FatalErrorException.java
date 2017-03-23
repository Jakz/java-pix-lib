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
}

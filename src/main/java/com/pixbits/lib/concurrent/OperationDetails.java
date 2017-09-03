package com.pixbits.lib.concurrent;

public interface OperationDetails
{
  public String getTitle();
  public String getProgressText();
  
  public static OperationDetails of(final String title, final String progress)
  {
    return new OperationDetails()
    {
      @Override
      public String getTitle() { return title; }
      @Override
      public String getProgressText() { return progress; }      
    };
  }
}

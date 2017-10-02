package com.pixbits.lib.ui;

public interface ConsoleInterface
{
  public void setCaretPosition(int position);
  
  public int getTextLength();
  
  public String getCurrentCommand();
  public void replaceCurrentCommand(String text);
}

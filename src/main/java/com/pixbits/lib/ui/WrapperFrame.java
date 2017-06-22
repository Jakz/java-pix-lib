package com.pixbits.lib.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WrapperFrame<T extends JPanel> extends JFrame
{
  private final T panel;
  
  WrapperFrame(T panel)
  {
    this.panel = panel;
  }
  
  public T panel() 
  {
    return panel;
  }
  
  public void exitOnClose()
  {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

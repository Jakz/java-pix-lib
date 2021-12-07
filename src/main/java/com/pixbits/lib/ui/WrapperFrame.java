package com.pixbits.lib.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
  
  public void centerOnScreen()
  {
    this.setLocationRelativeTo(null);
  }
  
  public void onClose(Runnable lambda)
  {
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        lambda.run();
      }
    });
  }
}

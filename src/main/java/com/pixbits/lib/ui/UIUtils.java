package com.pixbits.lib.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.table.TableColumn;

public class UIUtils
{
  public static void resizeColumn(TableColumn column, int width)
  {
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setPreferredWidth(width);
  }
  
  public static <T extends JPanel> WrapperFrame<T> buildFrame(T panel, String title)
  {
    WrapperFrame<T> frame = new WrapperFrame<T>(panel);
    frame.setTitle(title);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.pack();
    
    return frame;
  }
}

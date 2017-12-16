package com.pixbits.lib.ui.table.renderers;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.table.DefaultTableCellRenderer;

public class IntArrayRenderer extends DefaultTableCellRenderer
{
  private static final long serialVersionUID = 1L;

  public IntArrayRenderer() { super(); }

  public void setValue(Object o)
  {
    int[] a = (int[])o;
    
    if (a != null)
    {
      String text = Arrays.stream(a)
          .boxed()
          .map(Object::toString)
          .collect(Collectors.joining(","));
      
      setText(text);
    }
    else
      setText("");
  }
}
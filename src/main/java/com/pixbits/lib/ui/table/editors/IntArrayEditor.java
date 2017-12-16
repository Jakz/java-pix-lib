package com.pixbits.lib.ui.table.editors;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class IntArrayEditor extends DefaultCellEditor
{
  public IntArrayEditor()
  {
    super(new JTextField());
    ((JTextField)getComponent()).setBorder(new LineBorder(Color.black));
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
  {
    try
    {
      int[] a = (int[])value;

      JTextField comp = (JTextField)getComponent();

      if (a != null)
      {
        String text = Arrays.stream(a)
            .boxed()
            .map(Object::toString)
            .collect(Collectors.joining(","));
        
        comp.setText(text);
      }
      else
        comp.setText("");

      return comp;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Object getCellEditorValue()
  {
    String s = (String)super.getCellEditorValue();
    
    String[] ss = s.split(",");

    try 
    {
      int[] i = new int[ss.length];
      for (int j = 0; j < i.length; ++j)
        i[j] = Integer.valueOf(ss[j]);
      return i;
    }
    catch (NullPointerException|NumberFormatException e)
    {
      return null;
    }
  }
}
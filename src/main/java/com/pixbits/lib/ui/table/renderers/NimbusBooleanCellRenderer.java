package com.pixbits.lib.ui.table.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class NimbusBooleanCellRenderer extends JCheckBox implements TableCellRenderer
{
  public NimbusBooleanCellRenderer()
  {
    super();
    setHorizontalAlignment(SwingConstants.CENTER);
  }

  @Override
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {  
    if (isSelected) 
    {
      setBackground(table.getSelectionBackground());
    } 
    else
    {
      if ((row % 2) == 1)
        setBackground(UIManager.getColor("Table.alternateRowColor"));
      else
        setBackground(Color.WHITE);
    }
  
    setSelected((value != null && ((Boolean)value).booleanValue()));
    return this; 
  }
  
  @Override public boolean isOpaque() { return true; }
}
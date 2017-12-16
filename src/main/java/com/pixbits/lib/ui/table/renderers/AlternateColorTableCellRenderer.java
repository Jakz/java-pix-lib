package com.pixbits.lib.ui.table.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class AlternateColorTableCellRenderer implements TableCellRenderer
{
  public static Color evenColor = Color.white;
  public static Color oddColor = UIManager.getColor("Table.alternateRowColor");
  public static Color foreground = UIManager.getColor("Table.textForeground");
  
  public static Color selectedColorBackground = UIManager.getColor("Table[Enabled+Selected].textBackground");
  public static Color selectedColorForeground = UIManager.getColor("Table[Enabled+Selected].textForeground");
  
  public static Color focusBorderColor = UIManager.getColor("nimbusFocus");

  
  private TableCellRenderer renderer;
  
  public AlternateColorTableCellRenderer(TableCellRenderer renderer)
  {
    this.renderer = renderer;
  }
  
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean cellHasFocus, int r, int c)
  {
    JComponent component = (JComponent)renderer.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, r, c);
    setBackgroundColor(component, isSelected, r);
    return component;
  }
  
  public static void setBackgroundColor(JComponent component, int r)
  {
    setBackgroundColor(component, false, r);
  }

  public static void setBackgroundColor(JComponent component, boolean isSelected, int r)
  {
    component.setOpaque(true);
    
    if (isSelected)
    {
      component.setBackground(selectedColorBackground);
      component.setForeground(selectedColorForeground);
    }
    else
    {
      component.setBackground(r % 2 == 0 ? evenColor : oddColor);
      component.setForeground(foreground);
    }
  }
  
  public static void setBackgroundColor(JComponent component, boolean isSelected, boolean hasFocus, int r)
  {
    if (hasFocus)
      component.setBorder(BorderFactory.createLineBorder(focusBorderColor, 1));
    else
      component.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

    setBackgroundColor(component, isSelected, r);
  }

}
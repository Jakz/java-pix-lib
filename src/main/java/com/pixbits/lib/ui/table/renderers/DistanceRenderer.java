package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DistanceRenderer extends DefaultTableCellRenderer
{
  public enum Unit
  {
    KM,
    MT
  };
  
  private final Unit unit;
  
  public DistanceRenderer()
  {
    this(Unit.KM);
  }
  
  public DistanceRenderer(Unit unit)
  {
    this.unit = unit;
  }

  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    double value = (double)object; 
    
    if (unit == Unit.MT)
      value /= 1000;
    
    if (Double.isNaN(value))
      label.setText("");
    else if (value < 1)
      label.setText((int)(value*1000) + "m");
    else
      label.setText(String.format("%.2fkm", value));
    
    return label;
  }
}

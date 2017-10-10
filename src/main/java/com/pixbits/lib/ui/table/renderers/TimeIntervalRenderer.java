package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.pixbits.lib.util.TimeInterval;

public class TimeIntervalRenderer extends DefaultTableCellRenderer
{  
  public TimeIntervalRenderer(String pattern)
  {
    //formatter = DateTimeFormatter.ofPattern(pattern);
  }
  
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    TimeInterval value = (TimeInterval)object; 
    
    if (value == null)
      label.setText("");
    else if (value.hours() < 1)
      label.setText(String.format("%02d:%02d", value.minutes(), value.seconds()));
    else
      label.setText(String.format("%02d:%02d:%02d", value.hours(), value.minutes(), value.seconds()));

    return label;
  }
}

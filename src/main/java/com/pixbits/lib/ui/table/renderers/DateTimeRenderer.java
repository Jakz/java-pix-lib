package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateTimeRenderer extends DefaultTableCellRenderer
{
  private final DateTimeFormatter formatter;
  
  public DateTimeRenderer(String pattern)
  {
    formatter = DateTimeFormatter.ofPattern(pattern);
  }
  
  public DateTimeRenderer(boolean date, boolean time, FormatStyle style)
  {
    if (date && time)
      formatter = DateTimeFormatter.ofLocalizedDateTime(style, style);
    else if (date)
      formatter = DateTimeFormatter.ofLocalizedDate(style);
    else if (time)
      formatter = DateTimeFormatter.ofLocalizedTime(style);
    else throw new IllegalArgumentException("DateTimeRenderer::DateTimeRenderer(): at least date or time must be true");
  }
  
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    ZonedDateTime value = (ZonedDateTime)object; 
    
    if (value == null)
      label.setText("");
    else
      label.setText(formatter.format(value));

    return label;
  }
}

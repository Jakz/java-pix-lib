package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public abstract class DefaultTableAndListRenderer<T>  implements ListCellRenderer<T>, TableCellRenderer
{
  private final DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
  private final TableCellRenderer tableRenderer = new DefaultTableCellRenderer();
  
  abstract public void decorate(JLabel label, JComponent source, T value, int index, boolean isSelected, boolean hasFocus);
  
  @Override
  public final Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel)tableRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    decorate(label, table, (T)value, row, isSelected, hasFocus);
    return label;
  }

  @Override
  public final Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus)
  {
    JLabel label = (JLabel)listRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    decorate(label, list, value, index, isSelected, cellHasFocus);
    return label;
  }
}

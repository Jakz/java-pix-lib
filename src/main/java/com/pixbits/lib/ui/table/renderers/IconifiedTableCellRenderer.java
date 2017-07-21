package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class IconifiedTableCellRenderer<T> implements TableCellRenderer
{  
  private final TableCellRenderer renderer;
  private final Function<T, ImageIcon> fetcher;
  
  public IconifiedTableCellRenderer(TableCellRenderer renderer, Function<T, ImageIcon> fetcher)
  {
    this.renderer = renderer;
    this.fetcher = fetcher;
  }
  
  @SuppressWarnings("unchecked")
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean cellHasFocus, int r, int c)
  {
    JLabel component = (JLabel)renderer.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, r, c);
    T t = (T)value;
    
    component.setIcon(fetcher.apply(t));
    return component;
  }
}
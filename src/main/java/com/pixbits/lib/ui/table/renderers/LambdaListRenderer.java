package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;
import java.util.function.BiConsumer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LambdaListRenderer<T> extends DefaultListCellRenderer
{
  private final BiConsumer<T, JLabel> lambda;
    
  public LambdaListRenderer(BiConsumer<T, JLabel> lambda)
  {
    this.lambda = lambda;
  }
    
  @SuppressWarnings("unchecked")
  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    T tvalue = (T)value;
    lambda.accept(tvalue, label);
    return label;
  }
  
  public static <U> LambdaListRenderer<U> of(BiConsumer<U, JLabel> lambda) { return new LambdaListRenderer<>(lambda); }
}

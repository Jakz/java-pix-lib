package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;
import java.util.function.BiConsumer;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LambdaLabelTableRenderer<T> extends DefaultTableCellRenderer
{
  private final BiConsumer<T, JLabel> lambda;
  
  public LambdaLabelTableRenderer(BiConsumer<T, JLabel> lambda)
  {
    this.lambda = lambda;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel)super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
    T value = (T)object;
    lambda.accept(value, label);
    return label;
  }

}

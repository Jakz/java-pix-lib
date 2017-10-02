package com.pixbits.lib.ui.table.renderers;

import java.awt.Component;
import java.util.function.BiConsumer;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.pixbits.lib.functional.TriConsumer;
import com.pixbits.lib.ui.table.DataSource;

public class LambdaLabelTableRendererWithSource<U,T> extends DefaultTableCellRenderer
{
  private final DataSource<U> source;
  private final TriConsumer<U, T, JLabel> lambda;
  
  public LambdaLabelTableRendererWithSource(DataSource<U> source, TriConsumer<U, T, JLabel> lambda)
  {
    this.source = source;
    this.lambda = lambda;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel)super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
    T value = (T)object;
    U source = this.source.get(table.convertRowIndexToModel(row));
    lambda.accept(source, value, label);
    return label;
  }
}

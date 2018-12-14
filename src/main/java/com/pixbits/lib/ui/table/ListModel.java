package com.pixbits.lib.ui.table;

import javax.swing.AbstractListModel;
import javax.swing.JList;

public class ListModel<T> extends AbstractListModel<T>
{
  private final JList<T> list;
  private DataSource<T> source;
  
  public ListModel(JList<T> list, DataSource<T> source)
  {
    this.list = list;
    this.source = source;
    
    list.setModel(this);
  }
  
  @Override public int getSize() { return source.size(); }
  @Override public T getElementAt(int index) { return source.get(index); }

  public void refresh() { fireContentsChanged(this, 0, source.size()); }
}

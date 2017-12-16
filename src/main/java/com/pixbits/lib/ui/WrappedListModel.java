package com.pixbits.lib.ui;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

public class WrappedListModel<T> extends AbstractListModel<T>
{
  public final static long serialVersionUID = 1L;
  private ArrayList<T> list;

  public WrappedListModel(ArrayList<T> list)
  {
    this.list = list;
  }

  public void update(ArrayList<T> list)
  {
    this.list = list;
  }

  public int getSize()
  {
    return list.size();
  }

  public T getElementAt(int i)
  {
    return list.get(i);
  }

  public void fireChanges()
  {
    super.fireContentsChanged(this, 0, this.getSize());
  }
}

package com.pixbits.lib.ui.table;

import java.util.Iterator;
import java.util.List;

public class ListDataSource<T> implements DataSource<T>
{
  private List<T> data;
  
  public ListDataSource(List<T> data)
  {
    this.data = data;
  }
  
  @Override
  public Iterator<T> iterator() { return data.iterator(); }

  @Override
  public T get(int index) { return data.get(index); }

  @Override
  public int size() { return data.size(); }

  @Override
  public int indexOf(T object) { return data.indexOf(object); }
}

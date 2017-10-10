package com.pixbits.lib.ui.table;

import java.util.Iterator;
import java.util.List;

public interface ModifiableDataSource<T> extends DataSource<T>
{
  public void add(int index, T element);
  
  public static <U> ModifiableDataSource<U> of(final List<U> list)
  {
    return new ModifiableDataSource<U>() {
      @Override public Iterator<U> iterator() { return list.iterator(); }
      @Override public U get(int index) { return list.get(index); }
      @Override public int size() { return list.size(); }
      @Override public int indexOf(U object) { return list.indexOf(object); }
      @Override public void add(int index, U element) { list.add(index, element); }
    };
  }
}

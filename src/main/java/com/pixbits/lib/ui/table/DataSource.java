package com.pixbits.lib.ui.table;

import java.util.Iterator;
import java.util.List;

public interface DataSource<T> extends Iterable<T>
{
  T get(int index);
  int size();
  int indexOf(T object);
  
  public static <U> DataSource<U> of(final List<U> list)
  {
    return new DataSource<U>() {
      @Override public Iterator<U> iterator() { return list.iterator(); }
      @Override public U get(int index) { return list.get(index); }
      @Override public int size() { return list.size(); }
      @Override public int indexOf(U object) { return list.indexOf(object); }    
    };
  }
}

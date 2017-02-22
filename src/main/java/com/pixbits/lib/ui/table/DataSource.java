package com.pixbits.lib.ui.table;

public interface DataSource<T> extends Iterable<T>
{
  T get(int index);
  int size();
  int indexOf(T object);
}

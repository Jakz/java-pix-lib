package com.pixbits.lib.ui.table;

public interface DataSource<T>
{
  T get(int index);
  int size();
}

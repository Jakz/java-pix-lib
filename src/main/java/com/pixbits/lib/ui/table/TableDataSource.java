package com.pixbits.lib.ui.table;

public interface TableDataSource<T>
{
  T get(int index);
  int size();
}

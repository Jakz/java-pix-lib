package com.pixbits.lib.ui.table;

@FunctionalInterface
public interface TableDataSetter<T,V>
{
  void set(T object, V value);
}

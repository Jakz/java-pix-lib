package com.pixbits.lib.ui.table;

@FunctionalInterface
public interface TableDataGetter<T, V>
{
  V get(T object);
}
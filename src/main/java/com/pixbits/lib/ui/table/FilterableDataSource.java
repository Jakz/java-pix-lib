package com.pixbits.lib.ui.table;

import java.util.function.Predicate;

public interface FilterableDataSource<T> extends DataSource<T>
{
  public void clearFilter();
  public void filter(Predicate<T> predicate);
}

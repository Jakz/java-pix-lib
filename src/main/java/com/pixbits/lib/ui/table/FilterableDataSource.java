package com.pixbits.lib.ui.table;

import java.util.List;
import java.util.function.Predicate;

public interface FilterableDataSource<T> extends DataSource<T>
{
  public void clearFilter();
  public void filter(Predicate<T> predicate);
  
  public static <U> FilterableDataSource<U> of(final List<U> data)
  {
    return new FilterableListDataSource<>(data);
  }
}

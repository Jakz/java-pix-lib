package com.pixbits.lib.ui.table;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface FilterableDataSource<T> extends DataSource<T>
{
  public void clearFilter();
  public void filter(Predicate<? super T> predicate);
  
  public static <U> FilterableDataSource<U> of(final Collection<U> data)
  {
    return new FilterableListDataSource<>(data);
  }
  
  public static <U> FilterableDataSource<U> empty()
  {
    return of(Collections.emptyList());
  }
}

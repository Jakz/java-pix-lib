package com.pixbits.lib.ui.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class FilterableListDataSource<T> implements FilterableDataSource<T>
{
  private final List<T> data;
  private final List<T> filtered;
  
  public FilterableListDataSource(List<T> data)
  {
    this.data = data;
    this.filtered = new ArrayList<T>(data);
  }
  
  public void clearFilter()
  {
    filtered.clear();
    filtered.addAll(data);
  }
  
  public void filter(Predicate<T> predicate)
  {
    filtered.clear();
    data.stream()
      .filter(predicate)
      .forEach(filtered::add);
  }
  
  @Override public Iterator<T> iterator() { return filtered.iterator(); }
  @Override public T get(int index) { return filtered.get(index); }
  @Override public int size() { return filtered.size(); }
  @Override public int indexOf(T object) { return filtered.indexOf(object); }
}

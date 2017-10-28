package com.pixbits.lib.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class FilterableListDataSource<T> implements FilterableDataSource<T>
{
  private final List<T> data;
  private final List<T> filtered;
  private Predicate<T> predicate;
  private Comparator<? super T> sorter;
  
  public FilterableListDataSource()
  {
    this.data = new ArrayList<>();
    this.filtered = new ArrayList<>();
    predicate = null;
    sorter = null;
  }

  public FilterableListDataSource(List<T> data)
  {
    this.data = data;
    this.filtered = new ArrayList<T>(data);
    predicate = null;
    sorter = null;

  }
  
  private void manageSort()
  {
    if (sorter != null)
      Collections.sort(filtered, sorter);
  }
  
  public void clearFilter()
  {
    filtered.clear();
    filtered.addAll(data);
    predicate = null;
    manageSort();
  }
  
  public void clearSorter()
  {
    sorter = null;
    filter(predicate);
  }
  
  public void filter(Predicate<T> predicate)
  {
    if (predicate == null)
    {
      clearFilter();
      return;
    }
    
    this.predicate = predicate;
    this.filtered.clear();
    this.data.stream()
      .filter(predicate)
      .forEach(filtered::add);
    manageSort();
  }
  
  public void sort(Comparator<? super T> sorter)
  {
    this.sorter = sorter;
    manageSort();
  }
  
  public void setData(final Collection<? extends T> data)
  {
    this.data.clear();
    this.data.addAll(data);    
    filter(predicate);
    manageSort();
  }
    
  @Override public Iterator<T> iterator() { return filtered.iterator(); }
  @Override public T get(int index) { return filtered.get(index); }
  @Override public int size() { return filtered.size(); }
  @Override public int indexOf(T object) { return filtered.indexOf(object); }
}

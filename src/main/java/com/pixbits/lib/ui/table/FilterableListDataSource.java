package com.pixbits.lib.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilterableListDataSource<T> implements FilterableDataSource<T>
{
  private final List<T> data;
  private final List<T> filtered;
  private Predicate<? super T> predicate;
  private Comparator<? super T> sorter;
  
  public FilterableListDataSource()
  {
    this.data = new ArrayList<>();
    this.filtered = new ArrayList<>();
    predicate = null;
    sorter = null;
  }

  public FilterableListDataSource(Collection<T> data)
  {
    this.data = new ArrayList<T>(data);
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
  
  public void filter(Predicate<? super T> predicate)
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
  
  public void add(T element)
  {
    data.add(element);
    
    if (predicate == null || predicate.test(element))
    {
      filtered.add(element);
      manageSort();
    }
  }
     
  @Override public Iterator<T> iterator() { return filtered.iterator(); }
  @Override public T get(int index) { return filtered.get(index); }
  @Override public int size() { return filtered.size(); }
  @Override public int indexOf(T object) { return filtered.indexOf(object); }
  
  public Stream<T> originalStream() { return data.stream(); }
}

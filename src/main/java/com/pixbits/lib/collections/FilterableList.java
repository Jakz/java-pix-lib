package com.pixbits.lib.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class FilterableList<T> implements Iterable<T>
{
  private final Collection<T> data;
  private final Collector<T,?,List<T>> collector;
  
  public FilterableList(Collection<T> data, Collector<T,?,List<T>> collector)
  {
    this.data = data;
    this.collector = collector;
  }

  @Override public Iterator<T> iterator() { return data.iterator(); }
  public Stream<T> stream() { return data.stream(); }

  public FilteredSlice<T> filter(Predicate<T> predicate)
  {
    Collection<T> result = data.stream().filter(predicate).collect(collector);
    
    return new FilteredSlice<>(data, result);
  }
}

package com.pixbits.lib.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

public class FilteredSlice<T> implements Iterable<T>
{
  private final Collection<T> origin;
  private final Collection<T> data;
  
  FilteredSlice(Collection<T> origin, Collection<T> data)
  {
    this.origin = origin;
    this.data = data;
  }
  
  public Collection<T> origin() { return Collections.unmodifiableCollection(origin); }
  
  public int size() { return data.size(); }
  public Stream<T> stream() { return data.stream(); }
  @Override public Iterator<T> iterator() { return data.iterator(); }
}

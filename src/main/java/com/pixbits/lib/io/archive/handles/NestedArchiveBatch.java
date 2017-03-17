package com.pixbits.lib.io.archive.handles;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class NestedArchiveBatch implements Iterable<NestedArchiveHandle>
{
  public final List<NestedArchiveHandle> handles;
  
  public NestedArchiveBatch(List<NestedArchiveHandle> handles)
  {
    this.handles = handles;
  }
  
  public Stream<NestedArchiveHandle> stream() { return handles.stream(); }
  public Iterator<NestedArchiveHandle> iterator() { return handles.iterator(); }
  public int size() { return handles.size(); }
  
}

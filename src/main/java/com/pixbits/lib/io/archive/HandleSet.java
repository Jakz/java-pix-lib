package com.pixbits.lib.io.archive;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pixbits.lib.io.archive.handles.ArchiveHandle;
import com.pixbits.lib.io.archive.handles.BinaryHandle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;

public class HandleSet implements Iterable<VerifierEntry>
{
  public final List<VerifierEntry> entries;
  
  public final long totalEntries;
  public final long totalHandles;
  public final long binaryCount;
  public final long archiveCount;
  public final long nestedArchiveCount;
  public final long nestedArchiveInnerCount;
  
  public HandleSet(List<VerifierEntry> entries)
  {
    this.entries = entries;

    Map<Class<? extends VerifierEntry>, Long> counts = entries.stream().collect(Collectors.groupingBy(e -> e.getClass(), HashMap::new, Collectors.counting()));
    
    binaryCount = counts.getOrDefault(BinaryHandle.class, 0L);
    archiveCount = counts.getOrDefault(ArchiveHandle.class, 0L);
    nestedArchiveCount = counts.getOrDefault(NestedArchiveBatch.class, 0L);
    nestedArchiveInnerCount = entries.stream()
      .filter(e -> e instanceof NestedArchiveBatch)
      .map(e -> (NestedArchiveBatch)e)
      .flatMap(e -> e.stream())
      .count();
    
    totalEntries = binaryCount + archiveCount + nestedArchiveCount;
    totalHandles = binaryCount + archiveCount + nestedArchiveInnerCount;
  }

  public Stream<? extends VerifierEntry> stream()
  {
    return entries.stream().flatMap(e -> e instanceof NestedArchiveBatch ? ((NestedArchiveBatch)e).stream() : Stream.of(e));
  }
  
  @Override public Iterator<VerifierEntry> iterator() { return entries.iterator(); }
}

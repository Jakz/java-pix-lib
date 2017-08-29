package com.pixbits.lib.io.archive;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pixbits.lib.io.archive.handles.ArchiveHandle;
import com.pixbits.lib.io.archive.handles.BinaryHandle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;

public class HandleSet implements Iterable<VerifierEntry>
{
  public final List<VerifierEntry> entries;

  public HandleSet(List<VerifierEntry> entries)
  {
    this.entries = entries;
  }
  
  public int size() { return entries.size(); }
  
  public Stats stats()
  {
    return new Stats(this);
  }

  public Stream<? extends VerifierEntry> stream()
  {
    return entries.stream().flatMap(e -> e.stream());
  }
  
  @Override public Iterator<VerifierEntry> iterator() { return entries.iterator(); }
  
  public static class Stats
  {
    public final long totalEntries;
    public final long totalHandles;
    public final long binaryCount;
    public final long archiveCount;
    public final long nestedArchiveCount;
    public final long nestedArchiveInnerCount;
    
    public Stats(HandleSet set)
    {
      Map<Class<? extends VerifierEntry>, List<VerifierEntry>> byType = set.entries.stream()
          .collect(Collectors.groupingBy(e -> e.getClass(), HashMap::new, Collectors.toList()));
      
      Map<Class<? extends VerifierEntry>, Long> counts = byType.entrySet().stream()
          .collect(Collectors.toMap(e -> e.getKey(), e -> (long)e.getValue().size()));
      
      totalEntries = counts.values().stream().count();
      binaryCount = counts.getOrDefault(BinaryHandle.class, 0L);
      archiveCount = counts.getOrDefault(ArchiveHandle.class, 0L);
      nestedArchiveCount = counts.getOrDefault(NestedArchiveBatch.class, 0L);
      
      nestedArchiveInnerCount = set.entries.stream()
          .filter(e -> e instanceof NestedArchiveBatch)
          .map(e -> (NestedArchiveBatch)e)
          .flatMap(e -> e.stream())
          .count();
      
      totalHandles = binaryCount + archiveCount + nestedArchiveInnerCount;
    }
    
  }
}

package com.pixbits.lib.io.archive;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.pixbits.lib.io.archive.handles.ArchiveHandle;
import com.pixbits.lib.io.archive.handles.BinaryHandle;
import com.pixbits.lib.io.archive.handles.Handle;
import com.pixbits.lib.io.archive.handles.NestedArchiveHandle;

public class HandleSet
{
  public final List<BinaryHandle> binaries;
  public final List<ArchiveHandle> archives;
  public final List<List<NestedArchiveHandle>> nestedArchives;
  public final Set<Path> faultyArchives;
  public final Set<ScannerEntry> skipped;
  
  HandleSet(List<BinaryHandle> binaries, List<ArchiveHandle> archives, List<List<NestedArchiveHandle>> nestedArchives, Set<Path> faultyArchives, Set<ScannerEntry> skipped)
  {
    this.binaries = binaries;
    this.archives = archives;
    this.nestedArchives = nestedArchives;
    this.faultyArchives = faultyArchives;
    this.skipped = skipped;
  }
  
  public long total() { return binaries.size() + archives.size() + nestedArchives.stream().flatMap(List::stream).count(); }
  public long binaryCount() { return binaries.size(); }
  public long archivedCount() { return archives.size(); }
  public long nestedCount() { return nestedArchives.stream().flatMap(List::stream).count(); }
  
  public Stream<Handle> stream()
  {
    return Stream.concat(
        binaries.stream(), 
        Stream.concat(
            archives.stream(), 
            nestedArchives.stream().flatMap(List::stream)
        )
    );
  }
}

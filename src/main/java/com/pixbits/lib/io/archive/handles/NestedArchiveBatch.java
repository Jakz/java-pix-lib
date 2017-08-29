package com.pixbits.lib.io.archive.handles;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.pixbits.lib.io.archive.VerifierEntry;
import com.pixbits.lib.io.archive.support.MemoryArchive;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZipException;

public class NestedArchiveBatch implements Iterable<NestedArchiveHandle>, VerifierEntry
{
  public final List<NestedArchiveHandle> handles;
  
  private MemoryArchive archive = null;
  private IInArchive iarchive = null;
  
  public NestedArchiveBatch(List<NestedArchiveHandle> handles)
  {
    this.handles = handles;
  }
  
  public Stream<VerifierEntry> stream() { return handles.stream().map(nah -> (VerifierEntry)nah); }
  public Iterator<NestedArchiveHandle> iterator() { return handles.iterator(); }
  public int size() { return handles.size(); }
  
  @Override public String toString() { return handles.get(0).path().getFileName().toString() + "/" + handles.get(0).internalName; }
  
  @Override public Handle getVerifierHandle() { throw new UnsupportedOperationException("Can't get a real Handle form a NestedArchiveBatch"); }
  
  @Override public int verifierEntryCount() { return handles.size(); }
  @Override public boolean isSingleVerifierEntry() { return false; }
  @Override public VerifierEntry getVerifierEntry(int index) { return handles.get(index); }
  
  @Override public void preloadForVerification(boolean requiresRealAccess, int index)
  { 
    NestedArchiveHandle handle = handles.get(index);
    
    if (requiresRealAccess)
    {
      if (archive == null)
      {
        handle.loadArchiveInMemory();
        archive = handle.getMemoryArchive();
        iarchive = handle.getMappedArchive();
      }
      else
      {
        archive.close();
        handle.setMemoryArchive(archive);
        handle.setMappedArchive(iarchive);
      }  
    }
  }
  
  @Override public void unloadAfterVerification()
  { 
    handles.stream()
      .forEach(handle -> { 
        handle.setMemoryArchive(null);
        handle.setMappedArchive(null);
      }
    );  
    
    if (archive != null)
    {
      archive.close();
      archive = null;
    }
    if (iarchive != null)
    {
      try
      {
        iarchive.close();
      } 
      catch (SevenZipException e)
      {
        // TODO maybe rethrow
        e.printStackTrace();
      }
      iarchive = null;
    }
    
  }
}

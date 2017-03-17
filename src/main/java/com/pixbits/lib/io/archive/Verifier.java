package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.pixbits.lib.io.archive.handles.Handle;
import com.pixbits.lib.io.archive.handles.NestedArchiveBatch;
import com.pixbits.lib.io.archive.handles.NestedArchiveHandle;
import com.pixbits.lib.io.archive.support.MemoryArchive;
import com.pixbits.lib.io.digest.DigestInfo;
import com.pixbits.lib.io.digest.Digester;
import com.pixbits.lib.io.digest.HashCache;
import com.pixbits.lib.log.Log;
import com.pixbits.lib.log.ProgressLogger;

import net.sf.sevenzipjbinding.IInArchive;

public class Verifier<T extends Verifiable>
{
  private final VerifierOptions voptions;
  private final HashCache<T> cache;
  
  private BiConsumer<T, Handle> callback = (t,h) -> {};

  private final Digester digester;
  
  private boolean hasCustomStreamWrapper;
  private Function<InputStream, InputStream> streamWrapper;
  
  public Verifier(VerifierOptions options, Digester digester, HashCache<T> cache)
  {
    this.voptions = options;
    this.cache = cache;
    this.digester = digester;
    this.streamWrapper = is -> is;
    this.hasCustomStreamWrapper = false;
  }
  
  public void setCallback(BiConsumer<T, Handle> callback)
  {
    this.callback = callback;
  }
  
  public BiConsumer<T, Handle> callback() { return callback; }

  
  private T verifyRawInputStream(Handle handle, InputStream is) throws IOException, NoSuchAlgorithmException
  {
    DigestInfo info = digester.digest(canUseCachedCrcIfAvailable() ? handle : null, is);
        
    T element = cache.elementForCrc(info.crc);
    
    return element != null &&
        (info.md5 == null || Arrays.equals(info.md5, element.md5())) &&
        (info.sha1 == null || Arrays.equals(info.sha1, element.sha1())) ?
            element : null;
  }
  
  private T verifyJustCRC(Handle handle)
  {
    return cache.elementForCrc(digester.digestOnlyCRC(handle).crc);
  }
  
  private boolean canUseCachedCrcIfAvailable()
  {
    return voptions.verifyJustCRC() && !hasCustomStreamWrapper;
  }
  
  public void verifyNestedArchive(NestedArchiveBatch batch) throws IOException, NoSuchAlgorithmException
  {
    final boolean onlyCRC = canUseCachedCrcIfAvailable();

    MemoryArchive archive = null;
    IInArchive iarchive = null;
    // TODO: can use multithread?
    for (NestedArchiveHandle handle : batch)
    {
      if (!onlyCRC)
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
      
      T element = verify(handle);    
      callback.accept(element, handle);
    }
    
    batch.stream().forEach(handle -> { handle.setMemoryArchive(null); handle.setMappedArchive(null); });  
  }
  
  T verify(Handle handle) throws IOException, NoSuchAlgorithmException
  {       
    T element = null;
    
    if (canUseCachedCrcIfAvailable())
      element = verifyJustCRC(handle);
    else
    {
      try (InputStream is = streamWrapper.apply(handle.getInputStream()))
      {
        element = verifyRawInputStream(handle, is);
      }
    }
    
    return element;
  }
}

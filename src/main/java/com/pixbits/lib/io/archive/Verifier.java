package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.pixbits.lib.io.archive.handles.Handle;
import com.pixbits.lib.io.digest.DigestInfo;
import com.pixbits.lib.io.digest.Digester;
import com.pixbits.lib.io.digest.HashCache;

public class Verifier<T extends Verifiable>
{
  private final VerifierOptions voptions;
  private final HashCache<T> cache;
  
  private boolean hasTransformer;
  private Function<Handle,Handle> transformer;
  
  private Optional<Consumer<List<VerifierResult<T>>>> callback;

  private final Digester digester;
  
  public Verifier(VerifierOptions options, Digester digester, HashCache<T> cache)
  {
    this.voptions = options;
    this.cache = cache;
    this.digester = digester;
    this.callback = Optional.empty();
    this.hasTransformer = false;
    this.transformer = h -> h;
  }
  
  public void setCallback(Consumer<List<VerifierResult<T>>> callback)
  {
    this.callback = Optional.of(callback);
  }
  
  public void setTransformer(Function<Handle,Handle> transformer)
  {
    hasTransformer = true;
    this.transformer = transformer;
  }
  
  protected Optional<Consumer<List<VerifierResult<T>>>> callback() { return callback; }

  
  private T verifyRawInputStream(Handle handle, InputStream is) throws IOException, NoSuchAlgorithmException
  {
    DigestInfo info = digester.digest(canUseCachedCrcIfAvailable() && handle.crc() != -1 ? handle : null, is);
        
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
    return voptions.verifyJustCRC();
  }

  private List<VerifierResult<T>> verifyBatch(VerifierEntry batch) throws NoSuchAlgorithmException, IOException
  {
    final int size = batch.verifierEntryCount();
    final boolean onlyCRC = canUseCachedCrcIfAvailable();

    
    List<VerifierResult<T>> results = null;
    
    if (!callback.isPresent())
      results = new ArrayList<>();
    
    for (int i = 0; i < size; ++i)
    {
      batch.preloadForVerification(!onlyCRC, i);
      
      VerifierEntry entry = batch.getVerifierEntry(i);
      
      List<VerifierResult<T>> elements = verify(entry);
      
      if (!callback.isPresent())
        results.addAll(elements);
    }
    
    batch.unloadAfterVerification();
    return results;
  }
  
  public List<VerifierResult<T>> verify(VerifierEntry entry) throws IOException, NoSuchAlgorithmException
  {       
    if (entry.isSingleVerifierEntry())
    {
      T element = null;
      Handle handle = entry.getVerifierHandle();
      handle = transformer.apply(handle);
      
      if (canUseCachedCrcIfAvailable() && handle.crc() != -1)
        element = verifyJustCRC(handle);
      else
      {
        try (InputStream is = handle.getInputStream())
        {
          element = verifyRawInputStream(handle, is);
        }
      }
      
      List<VerifierResult<T>> result = Collections.singletonList(new VerifierResult<>(element, handle));
      
      callback.ifPresent(cb -> cb.accept(result));      
      return !callback.isPresent() ? result : null;
    }
    else
    {
      return verifyBatch(entry);
    }
  }
}

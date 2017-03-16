package com.pixbits.lib.io.digest;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class HashCache<T extends DigestableCRC>
{
  private final Map<Long, T> cache;
  private final Set<Long> sizes;
  
  HashCache()
  {
    cache = new HashMap<>();
    sizes = new HashSet<>();
  }
  
  public HashCache(Stream<T> stream)
  {
    this();
    stream.forEach(e -> {
      cache.put(e.crc(), e);
      sizes.add(e.size());      
    });
  }
  
  public HashCache(Collection<T> set)
  {
    cache = new HashMap<>(set.size());
    sizes = new HashSet<>();
    precompute(set.stream());
  }
  
  public void precompute(Stream<T> stream)
  {
    cache.clear();
    sizes.clear();
    
    stream.forEach(e -> {
      cache.put(e.crc(), e);
      sizes.add(e.size());      
    });
  }
  
  public boolean isValidSize(long size) { return sizes.contains(size); }
  public T romForCrc(long crc) { return cache.get(crc); }
}

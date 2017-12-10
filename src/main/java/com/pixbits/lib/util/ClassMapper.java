package com.pixbits.lib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pixbits.lib.lang.Pair;

public class ClassMapper<T>
{
  private final Map<Class<?>, T> typeAdapters;
  private final List<Pair<Class<?>, T>> hierarchyAdapters;
  
  public ClassMapper()
  {
    this.typeAdapters = new HashMap<>();
    this.hierarchyAdapters = new ArrayList<>();
  }
  
  public void registerTypeAdapter(Class<?> type, T adapter)
  {
    typeAdapters.put(type, adapter);
  }
  
  public void registerHierarchyAdapter(Class<?> type, T adapter)
  {
    hierarchyAdapters.add(new Pair<>(type, adapter));
  }
  
  public T find(Class<?> type)
  {
    T adapter = typeAdapters.get(type);
    
    if (adapter != null)
      return adapter;
    
    Pair<Class<?>, T> hierarchyAdapter = hierarchyAdapters.stream()
        .filter(p -> p.first.isAssignableFrom(type))
        .findAny()
        .orElse(null);
    
    if (hierarchyAdapter != null)
      return hierarchyAdapter.second;
    
    return null;
  }
}

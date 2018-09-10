package com.pixbits.lib.algorithm.graphs;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pixbits.lib.lang.Pair;

public interface DirectedGraph<V>
{
  Set<Vertex<V>> vertices();
  Set<Edge<V>> successorsOf(Vertex<V> vertex);
  
  
  public static <T> DirectedGraph<T> of(T... vertexPairs)
  {
    final Map<T, Vertex<T>> cache = new HashMap<>();
    
    for (int i = 0; i < vertexPairs.length; i += 2)
    {
      T first = vertexPairs[i];
      T second = vertexPairs[i+1];
      
      cache.put(first, Vertex.of(first));
      cache.put(second, Vertex.of(second));
    }
    
    final Map<Vertex<T>, Set<Edge<T>>> edges = new HashMap<>();
    
    return new DirectedGraph<>()
    {
      private final Set<Vertex<T>> vertices = new HashSet<>(cache.values());
      @Override public Set<Vertex<T>> vertices() { return vertices; }
      @Override public Set<Edge<T>> successorsOf(Vertex<T> vertex) { return edges.getOrDefault(vertex, Collections.emptySet()); }     
    };
  }
}

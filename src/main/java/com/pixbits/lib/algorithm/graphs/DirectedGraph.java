package com.pixbits.lib.algorithm.graphs;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pixbits.lib.lang.Pair;

public interface DirectedGraph<V>
{
  Set<Edge<V>> edges();
  Set<Vertex<V>> vertices();
  Set<Edge<V>> successorsOf(Vertex<V> vertex);
  
  default Vertex<V> vertexForData(V data)
  {
    return vertices().stream()
      .filter(v -> v.data().equals(data))
      .findFirst()
      .orElse(null);
  }
  
  
  public static <T> DirectedGraph<T> of(T... vertexPairs)
  {
    final Map<T, Vertex<T>> cache = new HashMap<>();
    final Map<Vertex<T>, Set<Edge<T>>> edgesPerVertex = new HashMap<>();
    final Set<Edge<T>> edges = new HashSet<>();
    
    for (int i = 0; i < vertexPairs.length; i += 2)
    {
      T first = vertexPairs[i];
      T second = vertexPairs[i+1];
      
      Vertex<T> fv = Vertex.of(first), sv = Vertex.of(second);
      
      cache.put(first, fv);
      cache.put(second, sv);
      
      Edge<T> edge = new Edge<>(fv, sv);
      
      edgesPerVertex.computeIfAbsent(fv, v -> new HashSet<>()).add(edge);
      edges.add(edge);
    }
    
    
    return new DirectedGraph<>()
    {
      private final Set<Vertex<T>> vertices = new HashSet<>(cache.values());
      @Override public Set<Edge<T>> edges() { return edges; }
      @Override public Set<Vertex<T>> vertices() { return vertices; }
      @Override public Set<Edge<T>> successorsOf(Vertex<T> vertex) { return edgesPerVertex.getOrDefault(vertex, Collections.emptySet()); }     
    };
  }
}

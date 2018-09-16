package com.pixbits.lib.algorithm.graphs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.pixbits.lib.lang.Pair;

public interface DirectedGraph<V,D>
{
  Set<Vertex<V>> vertices();
  Set<DirectedEdge<V, D>> edges();
  Set<DirectedEdge<V, D>> successorsOf(Vertex<V> vertex);
 
  public static <T, D> DirectedGraph<T, D> of(T... vertexPairs)
  {
    final Map<T, Vertex<T>> cache = new HashMap<>();
    
    for (T vertex : vertexPairs)
      cache.put(vertex, Vertex.of(vertex));
    
    final Map<Vertex<T>, Set<DirectedEdge<T, D>>> edgesPerVertex = new HashMap<>();
    final Set<DirectedEdge<T, D>> edges = new HashSet<>();
    
    for (int i = 0; i < vertexPairs.length; i += 2)
    {
      Vertex<T> first = cache.get(vertexPairs[i]);
      Vertex<T> second = cache.get(vertexPairs[i+1]);

      DirectedEdge<T, D> edge = DirectedEdge.of(first, second);
      
      edgesPerVertex.computeIfAbsent(first, v -> new HashSet<>()).add(edge);
      edges.add(edge);
    }

    return new DirectedGraph<>()
    {
      private final Set<Vertex<T>> vertices = new HashSet<>(cache.values());
      @Override public Set<Vertex<T>> vertices() { return vertices; }
      @Override public Set<DirectedEdge<T,D>> edges() { return edges; }
      @Override public Set<DirectedEdge<T,D>> successorsOf(Vertex<T> vertex) { return edgesPerVertex.getOrDefault(vertex, Collections.emptySet()); }
    };
  }
  
  public static <T,D,U> DirectedGraph<T,D> of(Collection<U> pairs, Function<U, Pair<T,T>> vertexMapper, Function<U, D> edgeDataMapper)
  {
    final Map<T, Vertex<T>> vertices = new HashMap<>();
    Map<Vertex<T>, Set<DirectedEdge<T,D>>> edgesPerVertex = new HashMap<>();
    Set<DirectedEdge<T,D>> edges = new HashSet<>();
    
    for (U u : pairs)
    {
      Pair<T,T> vp = vertexMapper.apply(u);
      
      Vertex<T> fv = Vertex.of(vp.first);
      Vertex<T> sv = Vertex.of(vp.second);
      DirectedEdge<T,D> edge = DirectedEdge.of(fv, sv, edgeDataMapper.apply(u));
      
      vertices.put(vp.first, fv);
      vertices.put(vp.second, sv);
      
      edgesPerVertex.computeIfAbsent(fv, k -> new HashSet<>()).add(edge);
      edges.add(edge);
    }
    
    return new DirectedGraph<>()
    {
      private final Set<Vertex<T>> verticesSet = new HashSet<>(vertices.values());
      @Override public Set<Vertex<T>> vertices() { return verticesSet; }
      @Override public Set<DirectedEdge<T,D>> edges() { return edges; }
      @Override public Set<DirectedEdge<T,D>> successorsOf(Vertex<T> vertex) { return edgesPerVertex.getOrDefault(vertex, Collections.emptySet()); }
    };
  }
}

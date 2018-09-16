package com.pixbits.lib.algorithm.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class TarjanSCC<T,D>
{
  private final DirectedGraph<T,D> graph;
  private int index;
  private final Deque<Vertex<T>> S;
  
  private final Map<Vertex<T>, Integer> lowlinks;
  private final Set<Vertex<T>> visited;
  
  private final Set<Set<Vertex<T>>> sccs;


  TarjanSCC(DirectedGraph<T,D> graph)
  {
    this.graph = graph;
    this.index = 0;
    this.S = new ArrayDeque<>();
    
    this.lowlinks = new HashMap<>();
    this.visited = new HashSet<>();
    
    sccs = new HashSet<>();
  }
  
  Set<Set<Vertex<T>>> compute()
  {    
    for (Vertex<T> vertex : graph.vertices())
    {
      System.out.println((visited.contains(vertex) ? "Skipping " : "Visiting ")+" "+vertex.data());
      if (!visited.contains(vertex))
      {
        strongConnect(vertex);
      }
    }
    
    return sccs;
  }
  
  void strongConnect(Vertex<T> v)
  {
    lowlinks.put(v, index++);
    visited.add(v);
    
    S.push(v);
    int min = lowlinks.get(v);
    
    System.out.println("Strong connect "+v.data());
    System.out.println("Successors: "+graph.successorsOf(v).stream().map(DirectedEdge::to).map(Vertex::data).map(Object::toString).collect(Collectors.joining(", ")));
    
    for (DirectedEdge<T,D> e : graph.successorsOf(v))
    {
      Vertex<T> w = e.to();

      System.out.println((visited.contains(v) ? "Skipping " : "Visiting ")+" "+v.data());
      if (!visited.contains(w))
      {
        strongConnect(w);
      }
      if (lowlinks.get(w) < min)
        min = lowlinks.get(w);
    }
    
    if (min < lowlinks.get(v))
    {
      lowlinks.put(v, min);
      return;
    }

    Set<Vertex<T>> scc = new HashSet<>();
    
    Vertex<T> k;  
    do
    {
      k = S.pop();
      scc.add(k);
      lowlinks.put(k, graph.vertices().size());
    } while (k != v);
      
    sccs.add(scc);
  }
}

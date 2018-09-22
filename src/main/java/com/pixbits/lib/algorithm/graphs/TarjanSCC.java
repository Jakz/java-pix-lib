package com.pixbits.lib.algorithm.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pixbits.lib.lang.Pair;

public class TarjanSCC<T,D>
{
  public static class SCCEntry<T,D>
  {
    final Vertex<T> vertex;
    final DirectedEdge<T, D> edge;
    
    public SCCEntry(Vertex<T> vertex, DirectedEdge<T,D> edge)
    {
      this.vertex = vertex;
      this.edge = edge;
    }
    
    public Vertex<T> vertex() { return vertex; }
    public DirectedEdge<T,D> origin() { return edge; }
  }
  
  public static class SCC<T,D> implements Iterable<SCCEntry<T,D>>
  {
    final Set<SCCEntry<T,D>> vertices;
    
    public SCC()
    {
      vertices = new HashSet<>();
    }
    
    public int size() { return vertices.size(); }
    
    public void add(Vertex<T> vertex, DirectedEdge<T,D> edge)
    {
      vertices.add(new SCCEntry<>(vertex, edge));
    }
    
    public void add(SCCEntry<T,D> entry)
    {
      vertices.add(entry);
    }
    
    public Stream<SCCEntry<T,D>> stream() { return vertices.stream(); }
    public Iterator<SCCEntry<T,D>> iterator() { return vertices.iterator(); }
  }
  
  private final DirectedGraph<T,D> graph;
  private int index;
  private final Deque<SCCEntry<T,D>> S;
  
  private final Map<Vertex<T>, Integer> lowlinks;
  private final Set<Vertex<T>> visited;
  
  private final Set<SCC<T,D>> sccs;


  public TarjanSCC(DirectedGraph<T,D> graph)
  {
    this.graph = graph;
    this.index = 0;
    this.S = new ArrayDeque<>();
    
    this.lowlinks = new HashMap<>();
    this.visited = new HashSet<>();
    
    sccs = new HashSet<>();
  }
  
  public Set<SCC<T,D>> compute()
  {    
    for (Vertex<T> vertex : graph.vertices())
    {
      System.out.println((visited.contains(vertex) ? "Skipping " : "Visiting ")+" "+vertex.data());
      if (!visited.contains(vertex))
      {
        strongConnect(vertex, null);
      }
    }
    
    return sccs;
  }
  
  void strongConnect(Vertex<T> v, DirectedEdge<T,D> origin)
  {
    lowlinks.put(v, index++);
    visited.add(v);
    
    S.push(new SCCEntry<>(v, origin));
    int min = lowlinks.get(v);
    
    System.out.println("Strong connect "+v.data());
    System.out.println("Successors: "+graph.successorsOf(v).stream().map(DirectedEdge::to).map(Vertex::data).map(Object::toString).collect(Collectors.joining(", ")));
    
    for (DirectedEdge<T,D> e : graph.successorsOf(v))
    {
      Vertex<T> w = e.to();

      System.out.println((visited.contains(v) ? "Skipping " : "Visiting ")+" "+v.data());
      if (!visited.contains(w))
      {
        strongConnect(w, e);
      }
      if (lowlinks.get(w) < min)
        min = lowlinks.get(w);
    }
    
    if (min < lowlinks.get(v))
    {
      lowlinks.put(v, min);
      return;
    }

    SCC<T,D> scc = new SCC<>();
    
    SCCEntry<T,D> k;  
    do
    {
      k = S.pop();
      scc.add(k);
      lowlinks.put(k.vertex, graph.vertices().size());
    } while (k != v);
      
    sccs.add(scc);
  }
}

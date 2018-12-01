package com.pixbits.lib.algorithm.graphs;

public abstract class DirectedEdge<T, D> extends Edge<T, D>
{
  public Vertex<T> from() { return v1(); }
  public Vertex<T> to() { return v2(); }
  
  public static <T, D> DirectedEdge<T, D> of(Vertex<T> v1, Vertex<T> v2, D data)
  {
    return new DirectedEdge<>()
    {
      @Override public Vertex<T> v1() { return v1; }
      @Override public Vertex<T> v2() { return v2; }
      @Override public D data() { return data; }
    };
  }
  
  public static <T, D> DirectedEdge<T, D> of(Vertex<T> v1, Vertex<T> v2)
  {
    return of(v1, v2, null);
  }
  
  public String toString()
  { 
    if (data() == null)
      return String.format("[%s -> %s]", v1().toString(), v2().toString());
    else
      return String.format("[%s -(%s)> %s]", v1().toString(), data(), v2().toString());

  }
}

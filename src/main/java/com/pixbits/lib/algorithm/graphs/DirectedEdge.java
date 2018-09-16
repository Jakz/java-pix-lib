package com.pixbits.lib.algorithm.graphs;

public interface DirectedEdge<T, D> extends Edge<T, D>
{
  public default Vertex<T> from() { return v1(); }
  public default Vertex<T> to() { return v2(); }
  
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
}

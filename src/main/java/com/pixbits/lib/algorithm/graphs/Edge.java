package com.pixbits.lib.algorithm.graphs;

public interface Edge<T, D>
{
  public Vertex<T> v1();
  public Vertex<T> v2();
  public D data();
  
  public static <T, D> Edge<T, D> of(Vertex<T> v1, Vertex<T> v2)
  {
    return new Edge<>()
    {
      @Override public Vertex<T> v1() { return v1; }
      @Override public Vertex<T> v2() { return v2; }
      @Override public D data() { return null; }
    };
  }
}

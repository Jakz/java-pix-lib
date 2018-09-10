package com.pixbits.lib.algorithm.graphs;

public class Edge<T>
{
  public final Vertex<T> v1, v2;
  
  public Edge(Vertex<T> v1, Vertex<T> v2)
  {
    this.v1 = v1;
    this.v2 = v2;
  }
}

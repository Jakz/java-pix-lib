package com.pixbits.lib.algorithm.graphs;

import java.util.Objects;

public abstract class Edge<T, D>
{
  public abstract Vertex<T> v1();
  public abstract Vertex<T> v2();
  public abstract D data();
  
  public static <T, D> Edge<T, D> of(Vertex<T> v1, Vertex<T> v2)
  {
    return new Edge<>()
    {
      @Override public Vertex<T> v1() { return v1; }
      @Override public Vertex<T> v2() { return v2; }
      @Override public D data() { return null; }
    };
  }
  
  public int hashCode() { return Objects.hash(v1(), v2(), data()); }
  public boolean equals(Object oo)
  {
    if (oo instanceof Edge)
    {
      Edge<?,?> o = (Edge<?,?>)oo;
      
      boolean vertexMatch = 
          (v1().equals(o.v1()) && v2().equals(o.v2())) ||
          (v1().equals(o.v2()) && v2().equals(o.v1()));
      
      boolean dataMatch = 
          (data() == null && o.data() == null) || 
          (data() != null && o.data() != null && data().equals(o.data()));
      
      return vertexMatch && dataMatch;

    }
    else
      return false;
  }
  
  public String toString() { return String.format("[%s -- %s]", v1().toString(), v2().toString()); }
}

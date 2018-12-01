package com.pixbits.lib.algorithm.graphs;

public abstract class Vertex<T>
{
  public abstract T data();

  public static <K> Vertex<K> of(K k)
  {
    return new Vertex<>() {
      public K data() { return k; }
    };
  }
  
  public static <K> Vertex<K> ofs(K k)
  {
    return new Vertex<>() {
      public K data() { return k; }
      public int hashCode() { return k.hashCode(); }
      public boolean equals(Object o) { return o instanceof Vertex && ((Vertex<?>)o).data().equals(data()); }
    };
  }
  
  public String toString() { return data().toString(); }
}

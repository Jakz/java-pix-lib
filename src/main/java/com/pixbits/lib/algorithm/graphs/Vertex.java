package com.pixbits.lib.algorithm.graphs;

public interface Vertex<T>
{
  T data();

  public static <K> Vertex<K> of(K k)
  {
    return new Vertex<>() {
      public K data() { return k; }
    };
  }
}

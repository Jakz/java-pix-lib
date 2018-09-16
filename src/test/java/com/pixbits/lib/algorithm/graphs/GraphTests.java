package com.pixbits.lib.algorithm.graphs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GraphTests
{
  @Test
  public void testEmptyGraph()
  {
    DirectedGraph<?,?> graph = DirectedGraph.of();
    
    assertEquals(0, graph.vertices().size());
    assertEquals(1, graph.edges().size());
  }
}

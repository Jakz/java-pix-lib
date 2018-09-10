package com.pixbits.lib.algorithm.graphs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GraphTests
{
  @Test
  public void testEmptyDirectedGraph()
  {
    DirectedGraph<?> graph = DirectedGraph.of();
    
    assertEquals(0, graph.vertices().size());
    assertEquals(0, graph.edges().size());
  }
  
  @Test
  public void testSingleVertexDirectedGraph()
  {
    DirectedGraph<?> graph = DirectedGraph.of(5, 5);
    
    assertEquals(1, graph.vertices().size());
    assertEquals(1, graph.edges().size());
  }
  
  @Test
  public void testTwoeVerticesDirectedGraph()
  {
    DirectedGraph<Integer> graph = DirectedGraph.of(5, 6);
    
    assertEquals(2, graph.vertices().size());
    assertEquals(1, graph.edges().size());
    
    assertEquals(1, graph.successorsOf(graph.vertexForData(5)).size());
    assertEquals(0, graph.successorsOf(graph.vertexForData(1)).size());
  }
  
}

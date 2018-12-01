package com.pixbits.lib.algorithm.graphs;

import com.pixbits.lib.lang.Pair;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

public class GraphTests
{
  private <T,U> DirectedEdge<T,U> dedge(T v1, T v2)
  {
    return DirectedEdge.of(Vertex.ofs(v1), Vertex.ofs(v2));
  }
  
  private <T,U> DirectedEdge<T,U> dedge(Vertex<T> v1, Vertex<T> v2)
  {
    return DirectedEdge.of(v1, v2);
  }
  
  private <T,U> DirectedEdge<T,U> dedge(List<Vertex<T>> vs, int i1, int i2)
  {
    return dedge(vs.get(i1), vs.get(i2));
  }
    
  private <T> void assertSetsEquals(Set<T> expected, Set<T> actual)
  {
    /*String e = expected.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]"));
    String a = actual.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]"));
    System.out.println(e+" != "+a);
    System.out.println(expected.iterator().next().equals(actual.iterator().next()));
    System.out.println(actual.iterator().next().equals(expected.iterator().next()));*/
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVertexEquality()
  {
    assertThat(Vertex.ofs(1), is(Vertex.ofs(1)));
    assertThat(Vertex.of(1), is(not(Vertex.of(1))));
  }
  
  @Test
  public void testEdgeEqualityWithNoData()
  {
    Edge<?,?> e1 = Edge.of(Vertex.ofs(1), Vertex.ofs(2));
    Edge<?,?> e2 = Edge.of(Vertex.ofs(1), Vertex.ofs(2));
    Edge<?,?> e3 = Edge.of(Vertex.ofs(2), Vertex.ofs(1));

    assertEquals(e1, e1);
    assertEquals(e1, e2);
    assertEquals(e2, e1);
    assertEquals(e1, e3);
    assertEquals(e3, e1);
  }
  
  @Test
  public void testEmptyGraph()
  {
    DirectedGraph<Integer, Void> graph = DirectedGraph.of();
    
    assertEquals(0, graph.vertices().size());
    assertEquals(0, graph.edges().size());
  }
  
  @Test
  public void testMappingConstructor()
  {
    DirectedGraph<Character, Integer> graph = DirectedGraph.<Character, Integer, String>of(
        Arrays.asList("fo", "of"), 
        s -> Pair.of(s.charAt(0), s.charAt(1)),
        e -> null
    );
    
    assertThat(graph.edges().size(), is(2));
    assertThat(graph.vertices().size(), is(2));
    assertThat(graph.edges(), is(new HashSet<>(Arrays.asList(dedge('f', 'o'), dedge('o','f')))));
    assertThat(graph.successorsOf(Vertex.ofs('f')), is(Collections.singleton(dedge('f','o'))));
    assertThat(graph.successorsOf(Vertex.ofs('o')), is(Collections.singleton(dedge('o','f'))));
  }
  
  @Test
  public void testTwoNodesDirectedEdge()
  {
    List<Vertex<Integer>> v = Arrays.asList(Vertex.ofs(1), Vertex.ofs(2));
    DirectedGraph<Integer, Void> graph = DirectedGraph.ofIndices(v,  0,1);
    
    assertEquals(2, graph.vertices().size());
    assertEquals(1, graph.edges().size());
    assertSetsEquals(Collections.singleton(dedge(v,0,1)), graph.edges());
    assertEquals(Collections.emptySet(), graph.successorsOf(Vertex.ofs(2)));
    assertEquals(Collections.singleton(dedge(v,0,1)), graph.successorsOf(Vertex.ofs(1)));
  }
  
  @Test
  public void testTwoNodesBothDirectedEdge()
  {
    List<Vertex<Integer>> v = Arrays.asList(Vertex.ofs(1), Vertex.ofs(2));
    DirectedGraph<Integer, Void> graph = DirectedGraph.ofIndices(v,  0,1, 1,0);
    
    assertEquals(2, graph.vertices().size());
    assertEquals(2, graph.edges().size());
    assertSetsEquals(new HashSet<>(Arrays.asList(dedge(v,0,1), dedge(v,1,0))), graph.edges());
    assertEquals(Collections.singleton(dedge(v,1,0)), graph.successorsOf(Vertex.ofs(2)));
    assertEquals(Collections.singleton(dedge(v,0,1)), graph.successorsOf(Vertex.ofs(1)));
  }
}

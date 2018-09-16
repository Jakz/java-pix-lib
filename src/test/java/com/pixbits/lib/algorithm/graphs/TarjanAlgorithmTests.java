package com.pixbits.lib.algorithm.graphs;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class TarjanAlgorithmTests
{
   private void assertSCCS(Set<Set<Vertex<Integer>>> actual, int[][] expected)
   {
     Set<Set<Integer>> cactual = actual.stream()
         .map(set -> 
           set.stream()
             .map(Vertex::data)
             .collect(Collectors.toSet())
         )  
         .collect(Collectors.toSet()); 
     
     Set<Set<Integer>> cexpected = Arrays.stream(expected)
         .map(array ->
           Arrays.stream(array)
             .mapToObj(Integer::valueOf)
             .collect(Collectors.toSet())
         )
         .collect(Collectors.toSet());
     
     assertEquals(cexpected, cactual);
   }
  
   //@Test
   public void testSingleVertexSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,123);
     Set<Set<Vertex<Integer>>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123 } });
   }
   
   @Test
   public void testTwoVerticesSingleSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123);
     Set<Set<Vertex<Integer>>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 } });
   }
   
   @Test
   public void testTwoVerticesSingleWithAdditionalExitingEdgeSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123,  123,125);
     Set<Set<Vertex<Integer>>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 }, { 125 } });
   }
   
   @Test
   public void testTwoVerticesSingleWithAdditionalEnteringEdgeSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123,  125,123);
     Set<Set<Vertex<Integer>>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 }, { 125 } });
   }
   
   @Test
   public void testFourVerticesTwoSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123,  125,126,  126,125);
     Set<Set<Vertex<Integer>>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 }, { 125, 126 } });
   }
}

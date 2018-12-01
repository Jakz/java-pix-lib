package com.pixbits.lib.algorithm.graphs;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.pixbits.lib.lang.Pair;

public class TarjanAlgorithmTests
{
   private void assertSCCS(Set<TarjanSCC.SCC<Integer, Void>> actual, int[][] expected)
   {     
     Set<Set<Integer>> cactual = actual.stream()
         .map(set -> 
           set.stream()
             .map(TarjanSCC.SCCEntry::vertex)
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
     Set<TarjanSCC.SCC<Integer,Void>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123 } });
   }
   
   @Test
   public void testTwoVerticesSingleSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123);
     Set<TarjanSCC.SCC<Integer,Void>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 } });
   }
   
   @Test
   public void testTwoVerticesSingleWithAdditionalExitingEdgeSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123,  123,125);
     Set<TarjanSCC.SCC<Integer,Void>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 }, { 125 } });
   }
   
   @Test
   public void testTwoVerticesSingleWithAdditionalEnteringEdgeSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123,  125,123);
     Set<TarjanSCC.SCC<Integer,Void>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 }, { 125 } });
   }
   
   @Test
   public void testFourVerticesTwoSCC()
   {
     DirectedGraph<Integer, Void> graph = DirectedGraph.of(123,124,  124,123,  125,126,  126,125);
     Set<TarjanSCC.SCC<Integer,Void>> sccs = new TarjanSCC<>(graph).compute();
     assertSCCS(sccs, new int[][] { { 123, 124 }, { 125, 126 } });
   }
}

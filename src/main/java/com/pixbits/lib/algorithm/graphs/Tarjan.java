package com.pixbits.lib.algorithm.graphs;

import java.util.*;
import java.util.stream.Collectors;
 
/** class Tarjan **/
class Tarjan
{
    /** number of vertices **/
    private int V;    
    /** preorder number counter **/
    private int preCount;
    /** low number of v **/
    private int[] low;
    /** to check if v is visited **/
    private boolean[] visited;      
    /** to store given graph **/
    private List<Integer>[] graph;
    /** to store all scc **/
    private List<List<Integer>> sccComp;
    private Stack<Integer> stack;
 
    /** function to get all strongly connected components **/
    public List<List<Integer>> getSCComponents(List<Integer>[] graph) 
    {
        V = graph.length;
        this.graph = graph;
        low = new int[V];
        visited = new boolean[V];
        stack = new Stack<Integer>();
        sccComp = new ArrayList<>();
 
        for (int v = 0; v < V; v++)
        {
          System.out.println((visited[v] ? "Skipping " : "Visiting ")+" "+v);

          if (!visited[v])
                dfs(v);
        }
 
        return sccComp;
    }
    /** function dfs **/
    public void dfs(int v) 
    {
        low[v] = preCount++;
        visited[v] = true;
        stack.push(v);
        
        System.out.println("Strong connect "+v);
        System.out.println("Successors: "+graph[v].stream().map(Object::toString).collect(Collectors.joining(", ")));
        
        int min = low[v];
        for (int w : graph[v]) 
        {
          System.out.println((visited[w] ? "Skipping " : "Visiting ")+" "+w);  
          
          if (!visited[w])
                dfs(w);
            if (low[w] < min) 
                min = low[w];
        }
        if (min < low[v]) 
        { 
            low[v] = min; 
            return; 
        }        
        List<Integer> component = new ArrayList<Integer>();
        int w;
        do
        {
            w = stack.pop();
            component.add(w);
            low[w] = V;                
        } while (w != v);
        
        System.out.println("SCC: "+component.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")));

        sccComp.add(component);        
    }    
    /** main **/
    public static void main(String[] args)
    {    
        /** make graph **/
        List<Integer>[] g = new List[] {
          new ArrayList<>(Arrays.asList(1)),
          new ArrayList<>(Arrays.asList(0))
        };

        Tarjan t = new Tarjan();        
        System.out.println("\nSCC : ");
        /** print all strongly connected components **/
        List<List<Integer>> scComponents = t.getSCComponents(g);
           System.out.println(scComponents);        
    }    
}
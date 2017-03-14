package mst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import mst.Graph.Edge;

public class Prims{
//	private Collection verts;
//	private Collection edges;
	
	
	private Prims(){ }
	
	public static List<Graph.Edge<Integer>> getMSTPath(Collection<Graph.Vertex<Integer>> verts, Collection<Graph.Edge<Integer>> edges, Graph.Vertex<Integer> start){
		final List<Edge<Integer>> path = new ArrayList<Edge<Integer>>();
		final Queue<Graph.Edge<Integer>> edgesAvailable = new PriorityQueue<Graph.Edge<Integer>>();
		final Set<Graph.Vertex<Integer>> unvisited = new HashSet<Graph.Vertex<Integer>>();
		Graph.Vertex<Integer> vertex = start;
		while(vertex.getEdges().isEmpty()){
			vertex = verts.iterator().next();
		}
		unvisited.addAll(verts);
		unvisited.remove(vertex);
		while (!unvisited.isEmpty()) {
			// Add all edges to unvisited vertices
			for (Graph.Edge<Integer> e : vertex.getEdges()) {
				if (unvisited.contains(e.getToVertex()) || unvisited.contains(e.getFromVertex()))
                   edgesAvailable.add(e);
			}

			// Remove the lowest cost edge
			Graph.Edge<Integer> e = edgesAvailable.remove();
			while(!(unvisited.contains(e.getToVertex()) || unvisited.contains(e.getFromVertex()))){
				e = edgesAvailable.remove();
			}
			
			Graph.Vertex<Integer> unvis = null;
			Graph.Vertex<Integer> vis = null;
			//add path leading from unvisited to visited
			if(unvisited.contains(e.getFromVertex())){
				unvis = e.getFromVertex();
				vis = e.getToVertex();
			}else if(unvisited.contains(e.getToVertex())){
				unvis = e.getToVertex();
				vis = e.getFromVertex();
			}
			path.add(new Graph.Edge<>(e.getCost(), unvis, vis)); // O(1)
			vertex = unvis;
			unvisited.remove(vertex); // O(1)
		}
		return path;
	}
	
}
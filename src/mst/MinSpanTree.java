package mst;
import mst.Graph;
import mst.Graph.Edge;
import mst.Graph.Vertex;
import mst.Prims;

import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import mst.FileHandler;

public class MinSpanTree {
	
	//private int[] tree;
	private Collection edges;
	private Collection verts;
	private Vertex[][] vertGrid;
	private int width;
	private int height;
	//private String[] objectives;
	FileHandler fh;
	
	public MinSpanTree(String filename) {
		// TODO Auto-generated constructor stub
		fh = new FileHandler(filename);
		this.width = fh.getWidth();
		this.height = fh.getHeight();
		this.vertGrid = new Vertex[this.height][this.width];
		this.verts = createVertexColl();
		this.edges = addEdges();
		//this.edges = createEdgeColl();
		//this.objectives = objectives;
	}
	
	public Collection<Vertex<Integer>> createVertexColl(){
		int vCount = 0;
		ArrayList<Vertex<Integer>> coll = new ArrayList<Vertex<Integer>>();
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				Vertex v = new Vertex(vCount);
				vCount++;
				v.setColor(fh.getRGB(i, j));
				v.setCoords(i, j);
				coll.add(v);
				this.vertGrid[i][j] = v;
			}
		}
		return coll;
	}
	//create each vertex
	
	public Collection<Edge<Integer>> addEdges(){
		ArrayList<Edge<Integer>> edges = new ArrayList<Edge<Integer>>();
		//add edges to each vertex
//		for (Iterator iterator = this.verts.iterator(); iterator.hasNext();) {
//			Vertex v = (Vertex) iterator.next();
//			Vertex[] neighbors = getNeighbors(v);
//			for (int i = 0; i < neighbors.length; i++) {
//				//add edge
//				Vertex neighbor = neighbors[i];
//				if(neighbor != null){
//					float cost = utils.Euclidian.getRGBEuclid(v.getColor(), neighbor.getColor());
//					Edge e = new Edge(cost, v, neighbor);
//					v.addEdge(e);
//					edges.add(e);
//				}
//			}
//		}
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				Vertex v = this.vertGrid[i][j];
				Vertex[] neighbors = getNeighbors(v);
				for (int m = 0; m < neighbors.length; m++) {
				//add edge
					Vertex neighbor = neighbors[m];
					if(neighbor != null){
						float cost = utils.Euclidian.getRGBEuclid(v.getColor(), neighbor.getColor());
						Edge e = new Edge(cost, v, neighbor);
						v.addEdge(e);
						edges.add(e);
					}
				}
			}
			
		}
		return edges;
	}
	
	private Vertex[] getNeighbors(Vertex v){
		Vertex[] neighbors = new Vertex[2];
		int[] coords = v.getCoords();
		int x = coords[0];
		int y = coords[1];
		//System.out.println(y);
		//try north neighbor
//		if(x > 0){
//			neighbors[0] = this.vertGrid[x-1][y];
//		}else{
//			neighbors[0] = null;
//		}
		//try east neighbor
		if(y < this.width-1){
			neighbors[0] = this.vertGrid[x][y+1];
		}else{
			neighbors[0] = null;
		}
		//try south neighbor
		if(x < this.height-1){
			neighbors[1] = this.vertGrid[x+1][y];
		}else{
			neighbors[1] = null;
		}
//		//try west neighbor
//		if(y > 0){
//			neighbors[3] = this.vertGrid[x][y-1];
//		}else{
//			neighbors[3] = null;
//		}
		return neighbors;
	}
	
	public static void main(String[] args) {
		String filename = "mini";
		MinSpanTree mst = new MinSpanTree(filename);
		//Prims prim = new Prims();
		Graph g = new Graph(mst.verts, mst.edges);
		System.out.println(g.getEdges());
//		for (Iterator iterator = mst.verts.iterator(); iterator.hasNext();) {
//			Vertex v = (Vertex) iterator.next();
//			System.out.println(v.getColor().getGreen());
//		}
		//System.out.println(g);
		//System.out.println(mst.vertGrid[0][0].getCoords());
		Graph.CostPathPair<Integer> primMST = Prims.getMinimumSpanningTree(g, mst.vertGrid[0][0]);
		System.out.println("tree");
		System.out.println(primMST);
//		
	}
}

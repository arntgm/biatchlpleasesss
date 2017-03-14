package mst;
import mst.Graph.Edge;
import mst.Graph.Vertex;
import mst.Prims;
import printPackage.PicPrinter;
import printPackage.SegmentHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import mst.FileHandler;

@SuppressWarnings("unchecked")
public class MinSpanTree {
	//private int[] tree;
	private Collection<Edge<Integer>> edges;
	private Collection<Vertex<Integer>> verts;
	private Vertex<Integer>[][] vertGrid;
	private int width;
	private int height;
	//private String[] objectives;
	FileHandler fh;
	
	public MinSpanTree(String filename, FileHandler filehandler) {
		// TODO Auto-generated constructor stub
		fh = filehandler;
		this.width = fh.getWidth();
		this.height = fh.getHeight();
		this.vertGrid = new Vertex[this.height][this.width];
		this.verts = createVertexColl();
		this.edges = addEdges();
		//this.edges = createEdgeColl();
		//this.objectives = objectives;
	}
	
	
	public List<int[]> generateChromosomes(int population, List<Edge<Integer>> origMSTPath, int[] origGene){
//		origMSTPath.sort(new Comparator<Edge<T>());
		List<int[]> geneList = new ArrayList<int[]>();
		Collections.sort(origMSTPath);
//		edgeQ.addAll(origMST);
		
		for (int i = 0; i < population; i++) {
			int[]newGene = copyGene(origGene);
			for (int j = 0; j < i; j++) {
				Edge<Integer>removeEdge = origMSTPath.get(origMSTPath.size()-1-j);
				int index = removeEdge.getFromVertex().getValue();
				newGene[index] = index;
			}
			geneList.add(newGene);
		}
		return geneList;
	}
	
	public int[] copyGene(int[] gene){
		int[] newGene = new int[gene.length];
		for (int i = 0; i < gene.length; i++) {
			newGene[i] = new Integer(gene[i]);
		}
		return newGene;
	}
	
	
	public Collection<Vertex<Integer>> createVertexColl(){
		int vCount = 0;
		ArrayList<Vertex<Integer>> coll = new ArrayList<Vertex<Integer>>();
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				Vertex<Integer> v = new Vertex<Integer>(vCount, i, j,fh.getRGB(i, j) );
				coll.add(v);
				vCount++;
				this.vertGrid[i][j] = v;
			}
		}
		return coll;
	}
	//create each vertex
	
	public Collection<Edge<Integer>> addEdges(){
		ArrayList<Edge<Integer>> edges = new ArrayList<Edge<Integer>>();
//		//add edges to each vertex
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
				if(i == this.height && j == this.width)
					break;
				Vertex<Integer> v = this.vertGrid[i][j];
				Vertex<Integer>[] neighbors = getNeighbors(i, j);
				for (int m = 0; m < neighbors.length; m++) {
				//add edge
					Vertex<Integer> neighbor = neighbors[m];
					if(neighbor != null){
						float cost = utils.Euclidian.getRGBEuclid(v.getColor(), neighbor.getColor());
						Edge<Integer> e = new Edge<Integer>(cost, v, neighbor);
						v.addEdge(e);
						neighbor.addEdge(e);
						edges.add(e);
					}
				}
			}
			
		}
		return edges;
	}
	
	public Collection<Edge<Integer>> getMSTPath(){
		return Prims.getMSTPath(this.verts, this.edges, (Vertex<Integer>)this.verts.iterator().next());
	}
	
	private Vertex<Integer>[] getNeighbors(int x, int y){
		Vertex<Integer>[] neighbors = new Vertex[2];
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
	
	public int[] getGenes(Collection<Edge<Integer>> mstPath){
		int[] gene = new int[this.height*this.width];
		Arrays.fill(gene, -1);
		for (Iterator<Edge<Integer>> iterator = mstPath.iterator(); iterator.hasNext();) {
			Edge<Integer> edge = (Edge<Integer>) iterator.next();
			int index = (int)edge.getFromVertex().getValue();
			gene[index] = (int)edge.getToVertex().getValue();
		}
		return gene;
	}
	
	
	public static void main(String[] args) {
		String filename = "Test_image";
		
		FileHandler fh = new FileHandler(filename);
		MinSpanTree mst = new MinSpanTree(filename, fh);
//		System.out.println(mst.verts.size());
//		System.out.println(mst.edges.size());
//		for (Iterator<Vertex<Integer>> iterator = mst.verts.iterator(); iterator.hasNext();) {
//			Vertex<Integer> v = (Vertex<Integer>) iterator.next();
//			System.out.println(v.getEdges());
//		}
		ArrayList<Edge<Integer>> MST = (ArrayList<Edge<Integer>>) mst.getMSTPath();
		System.out.println(MST.size());
//		for (Iterator<Edge<Integer>> iterator = MST.iterator(); iterator.hasNext();) {
//			Edge<Integer> edge = (Edge<Integer>) iterator.next();
//			System.out.println(edge);
//		}
		int[] genes = mst.getGenes(MST);
		System.out.println(genes.length);
//		for (int i = 0; i < genes.length; i++) {
//		System.out.print(genes[i]+" ");
//		}
//		System.out.println("next");
		List<int[]> pop = mst.generateChromosomes(4, MST, genes);
		SegmentHandler ss = new SegmentHandler(pop.get(pop.size()-1));
		ss.updateSegments();
		List<List<Integer>> seg = ss.getSegments();
		PicPrinter pp = new PicPrinter(seg, fh);
		pp.generateImage(seg, fh);
//		for(int[] chromo : pop){
//			for (int i = 0; i < chromo.length; i++) {
//				System.out.print(chromo[i]+" ");
//			}
//			System.out.println("next");
//		}

	}
}

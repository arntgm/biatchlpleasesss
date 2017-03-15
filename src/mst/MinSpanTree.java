package mst;
import mst.Graph.Edge;
import mst.Graph.Vertex;
import mst.Prims;
import printPackage.ImageDrawer;
import printPackage.PicPrinter;
import printPackage.SegmentHandler;
import utils.Euclidian;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mst.FileHandler;

@SuppressWarnings("unchecked")
public class MinSpanTree {
	//private int[] tree;
	private Collection<Edge<Integer>> edges;
	private Collection<Vertex<Integer>> verts;
	private Vertex<Integer>[][] vertGrid;
	private int width;
	private int height;
	private Random r;
	//private String[] objectives;
	FileHandler fh;
	
	public MinSpanTree(String filename, FileHandler filehandler) {
		// TODO Auto-generated constructor stub
		fh = filehandler;
		this.r = new Random();
		this.width = fh.getWidth();
		this.height = fh.getHeight();
		this.vertGrid = new Vertex[this.height][this.width];
		this.verts = createVertexColl();
		this.edges = addEdges();
		//this.edges = createEdgeColl();
		//this.objectives = objectives;
	}
	
	
	public List<int[]> generateGeneArrays(int population, int removeLimit, List<Edge<Integer>> origMSTPath, int[] origGene){
//		origMSTPath.sort(new Comparator<Edge<Integer>());
		List<int[]> geneList = new ArrayList<int[]>();
//		Collections.sort(origMSTPath);
//		edgeQ.addAll(origMST);
		
		for (int i = 0; i < population; i++) {
			int[]newGene = copyGene(origGene);
			for (int j = 0; j+i < removeLimit; j++) {
//				Edge<Integer>removeEdge = origMSTPath.get(origMSTPath.size()-1-j); //expensive edge
//				System.out.println(origMSTPath.size());
				Edge<Integer>removeEdge = origMSTPath.remove(this.r.nextInt(origMSTPath.size())); //random edge
				int index = removeEdge.getFromVertex().getValue();
				
				
				newGene[index] = index; //point to self
				
				//point to random neighbor
//				int oldLink = newGene[index];
//				List<Vertex<Integer>> neighbors = getAllNeighbors(Math.floorDiv(index, this.width), index%this.width);
//				int newLink = neighbors.get(r.nextInt(neighbors.size())).getValue();
//				while(newLink == oldLink){
//					newLink = neighbors.get(r.nextInt(neighbors.size())).getValue();
//				}
//				newGene[index] = newLink;
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
	
	public Collection<Edge<Integer>> addEdges(){
		ArrayList<Edge<Integer>> edges = new ArrayList<Edge<Integer>>();
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
	
	private List<Vertex<Integer>> getAllNeighbors(int x, int y){
		List<Vertex<Integer>> neighbors = new ArrayList<Vertex<Integer>>();
		if(y < this.width-1) //east
			neighbors.add(this.vertGrid[x][y+1]);
		if(x < this.height-1) //south
			neighbors.add(this.vertGrid[x+1][y]);
		if(y > 0) //west
			neighbors.add(this.vertGrid[x][y-1]);
		if(x > 0) //north
			neighbors.add(this.vertGrid[x-1][y]);
		return neighbors;
	}
	
	
	public Vertex<Integer>[] getNeighbors(int x, int y){
		Vertex<Integer>[] neighbors = new Vertex[2];
		if(y < this.width-1){
			neighbors[0] = this.vertGrid[x][y+1];
		}else{
			neighbors[0] = null;
		}
		if(x < this.height-1){
			neighbors[1] = this.vertGrid[x+1][y];
		}else{
			neighbors[1] = null;
		}
		return neighbors;
	}
	
	public int[] getGenes(Collection<Edge<Integer>> mstPath){
		int[] gene = new int[this.height*this.width];
		for (int i = 0; i < gene.length; i++) {
			gene[i] = i;
		}
		for (Iterator<Edge<Integer>> iterator = mstPath.iterator(); iterator.hasNext();) {
			Edge<Integer> edge = (Edge<Integer>) iterator.next();
			int index = (int)edge.getFromVertex().getValue();
			gene[index] = (int)edge.getToVertex().getValue();
		}
		return gene;
	}
	
	
	public static void main(String[] args) {
		
//		for(int[] chromo : pop){
//			for (int i = 0; i < chromo.length; i++) {
//				System.out.print(chromo[i]+" ");
//			}
//			System.out.println("next");
//		}

	}
}

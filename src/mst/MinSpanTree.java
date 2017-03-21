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
	
	public List<int[]> generateGeneArrays2(int population, int removeLimit, List<Edge<Integer>> origMSTPath, int[] origGene) {
		List<int[]> geneList = new ArrayList<int[]>();
//		Collections.sort(origMSTPath);
		int[] in =  new int[origGene.length];
		int[] out = new int[origGene.length];
		int[] starters = new int[origGene.length];
		for (int i = 0; i < origGene.length; i++) {
			in[i] = 0;
			out[i] = 0;
			starters[origGene[i]] = 1;
		}
		System.out.println("starters done");
		for (int i = 0; i < starters.length; i++) {
			if (starters[i] == 0) {
				int[] new_in = exploreBranch(origGene, i);
				System.out.println("Explored branch: "+i);
				int max = 0;
				for (int j = 0; j < origGene.length; j++) {
					if (new_in[j]>max) {
						max = new_in[j];
					}
				}
				int[] new_out = new int[origGene.length];
				new_out[i]=max;
				for (int j = 0; j < in.length; j++) {
					if (new_in[j] > 0) {
						new_out[j] = max-new_in[j];
					}
				}
				for (int j = 0; j < origGene.length; j++) {
					in[j] += new_in[j];
					out[j] += new_out[j];
				}
			}
		}
		
		for (int i = 0; i < population; i++) {
			List<Edge<Integer>> mstCopy = new ArrayList<Edge<Integer>>(origMSTPath);
			int[]newGene = new int[origGene.length];
			System.arraycopy(origGene, 0, newGene, 0, origGene.length);
			for (int j = 0; j < removeLimit; j++) {
				Edge<Integer>removeEdge = mstCopy.remove(mstCopy.size()-1); //expensive edge
//				Edge<Integer>removeEdge = mstCopy.remove(this.r.nextInt(mstCopy.size())); //random edge
				System.out.println("Orig still contains edge? "+origMSTPath.contains(removeEdge));
				int index = removeEdge.getFromVertex().getValue();
				int counter = 0;
				while (in[index] < 10000 || out[index] < 10000) {
					removeEdge = mstCopy.remove(mstCopy.size()-1-this.r.nextInt(10000)); //expensive edge
					index = removeEdge.getFromVertex().getValue();
					counter ++;
					if (counter == 1000)
						break;
				}
				System.out.println("IN: "+in[index]+"  OUT: "+out[index]);
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
	
	private int[] exploreBranch(int[] origGene, int i) {
		int[] visited = new int[origGene.length];
		int[] in = new int[origGene.length];
		boolean loop = false;
		while (! loop) {
			if (origGene[i] == i || visited[i] > 0) {
				loop = true;
			} else {
				visited[i] = 1;
				in[origGene[i]] += in[i]+1;
				i = origGene[i];
			}
		}
		return in;
	}
	
	
	public List<int[]> generateGeneArrays(int population, int removeLimit, List<Edge<Integer>> origMSTPath, int[] origGene){
		System.out.println("Generating gene arrays");
		List<int[]> geneList = new ArrayList<int[]>();
//		Collections.sort(origMSTPath);
		for (int i = 0; i < population; i++) {
			List<Edge<Integer>> mstCopy = new ArrayList<Edge<Integer>>(origMSTPath);
			int[]newGene = new int[origGene.length];
			System.arraycopy(origGene, 0, newGene, 0, origGene.length);
			for (int j = 0; j+i < removeLimit; j++) {
//				Edge<Integer>removeEdge = origMSTPath.get(origMSTPath.size()-1-j); //expensive edge
				Edge<Integer>removeEdge = mstCopy.remove(this.r.nextInt(mstCopy.size())); //random edge
				System.out.println("Orig still contains edge? "+origMSTPath.contains(removeEdge));
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
			System.out.println("Original MST: "+origMSTPath.size());
			System.out.println("Copy size: "+mstCopy.size());
			geneList.add(newGene);
		}
		System.out.println(geneList.size());
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

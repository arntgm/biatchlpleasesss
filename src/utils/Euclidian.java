package utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Euclidian {
	
	private int width;
	private int height;
	private Color[][] pixels;
	
	public Euclidian(int width, int height, Color[][] pixels){
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public List<Integer> getNeighborNumbers(Integer gene){
		List<Integer> neighbors = new ArrayList<Integer>();
		if (Math.floorDiv(gene, this.width)!= 0) {
			neighbors.add(gene-this.width);
		}
		if (Math.floorDiv(gene, this.width)!=this.height-1) {
			neighbors.add(gene+this.width);
		}
		if (gene%this.width != 0) {
			neighbors.add(gene-1);
		}
		if ((gene+1)%this.width != 0) {
			neighbors.add(gene+1);
		}
//		System.out.println("Node: "+gene);
//		System.out.println("Neighbors: "+neighbors.toString());
		return neighbors;
	}
	
	public HashSet<Integer> getEdgeSet(HashSet<Integer> segment){
		HashSet<Integer> edgeGenes = new HashSet<Integer>();
		for (Iterator<Integer> iterator = segment.iterator(); iterator.hasNext();) {
			Integer gene = (Integer) iterator.next();
//			System.out.println(gene);
			List<Integer> neighbors = getNeighborNumbers(gene);
//			for (Integer integer : neighbors) {
//				System.out.println(integer);
//			}
			for (Integer neighbor : neighbors) {
				if (!segment.contains(neighbor)){
					edgeGenes.add(gene);
					break;
				}
			}
		}
		return edgeGenes;
	}
	
	public static float getRGBEuclid(Color RGBone, Color RGBtwo){
		
		int rOne = RGBone.getRed();
		int rTwo = RGBtwo.getRed();
		int gOne = RGBone.getGreen();
		int gTwo = RGBtwo.getGreen();
		int bOne = RGBone.getBlue();
		int bTwo = RGBtwo.getBlue();
		return (float)Math.sqrt(Math.pow((rTwo-rOne), 2) + Math.pow((gTwo-gOne),2) + Math.pow((bTwo-bOne),2));
	}
	
	public float getRGBdeviation(HashSet<Integer> segment, Color centroid){
		float deviation = 0;
		for (Iterator<Integer> iterator = segment.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			int[] coords = toGridCoords(integer);
			deviation += getRGBEuclid(this.pixels[coords[0]][coords[1]],centroid);
		}
		return deviation;
	}
	
	public Color getRGBCentroid(HashSet<Integer> segment){
		int[] RGBsums = new int[3];
		for (Integer point : segment) {
			int[] coords = toGridCoords(point);
			Color pointCol = this.pixels[coords[0]][coords[1]];
			RGBsums[0] += pointCol.getRed();
			RGBsums[1] += pointCol.getGreen();
			RGBsums[2] += pointCol.getBlue();
		}
		for (int i = 0; i < RGBsums.length; i++) {
			RGBsums[i] = RGBsums[i]/segment.size();
		}
		return new Color(RGBsums[0], RGBsums[1], RGBsums[2]);
			
	}
	
	public static HashSet<Integer> getSegment(List<HashSet<Integer>> segments, int t) {
		for (HashSet<Integer> segment : segments) {
			if (segment.contains(t)) {
				return segment;
			}
		}
		return new HashSet<Integer>();
	}
	
	public int[] toGridCoords(int pos){
		int[] coords = new int[2];
		coords[0] = Math.floorDiv(pos, this.width);
		coords[1] = pos%this.width;
		return coords;
	}

}

package utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Euclidian {
	
	private int width;
	private int height;
	private Color[][] pixels;
	private Color[] listPixels;
	
	public Euclidian(int width, int height, Color[][] pixels, Color[] listPixels){
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		this.listPixels = listPixels;
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
		for (Integer gene : segment) {			
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
	
	public float getChromosomeConnValue(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgeMap){
		float totEdgeValue = 0;
		for (HashSet<Integer> segment : segments) {
			totEdgeValue += getSegmentConnValue(segment, edgeMap.get(segment));
		}
		return totEdgeValue;
	}
	
	public float getSegmentConnValue(HashSet<Integer> segment, HashSet<Integer> edgeSet){
		float segmentEdgeValue = 0;
		for (Integer gene : edgeSet) {
			float conn = 0;
			List<Integer> neighbors = getNeighborNumbers(gene);
			for (Integer neighbor : neighbors) {
				if(!segment.contains(neighbor)){
					conn +=1;
					segmentEdgeValue += 1/conn;
				}
			}
		}
		return segmentEdgeValue;
	}
	
	//returns both edge value and connectivity objectives//
	//uses L = 4 for connectivity//
	public float[] getSegEdgeAndConnValue(HashSet<Integer> segment, HashSet<Integer> edgeSet){
		float[] segmentObjValues = new float[2];
		for (Integer gene : edgeSet) {
			int penalty = 0;
			List<Integer> neighbors = getNeighborNumbers(gene);
			for (Integer neighbor : neighbors) {
				if(!segment.contains(neighbor)){
					Color edgeColor = this.listPixels[gene];
					Color neighborColor = this.listPixels[neighbor];
					segmentObjValues[0] -= getRGBEuclid(edgeColor, neighborColor);
					penalty +=1;
					segmentObjValues[1] += 1/penalty;
				}
			}
		}
		return segmentObjValues;
	}
	
	public float getSegmentEdgeValue(HashSet<Integer> segment, HashSet<Integer> edgeSet){
		float segmentEdgeValue = 0;
		for (Integer gene : edgeSet) {
			List<Integer> neighbors = getNeighborNumbers(gene);
			for (Integer neighbor : neighbors) {
				if(!segment.contains(neighbor)){
					Color edgeColor = this.listPixels[gene];
					Color neighborColor = this.listPixels[neighbor];
					segmentEdgeValue -= getRGBEuclid(edgeColor, neighborColor);
				}
			}
		}
		return segmentEdgeValue;
	}
	
	public float[] getChromosomeEdgeAndConn(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgeMap){
		float[] totObjValues = new float[2];
		for (HashSet<Integer> segment : segments) {
			float[] segValues = getSegEdgeAndConnValue(segment, edgeMap.get(segment));
			totObjValues[0] += segValues[0];
			totObjValues[1] += segValues[1];
		}
		return totObjValues;
	}
	
	public float getChromosomeEdgeValue(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgeMap){
		float totEdgeValue = 0;
		for (HashSet<Integer> segment : segments) {
			totEdgeValue += getSegmentEdgeValue(segment, edgeMap.get(segment));
		}
		return totEdgeValue;
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
	
	public float getChromosomeRGBDev(List<HashSet<Integer>> segments, Map<HashSet<Integer>, Color> centroidMap){
		float totDev = 0;
		for (HashSet<Integer> segment : segments) {
			totDev+=getSegmentRGBdeviation(segment, centroidMap.get(segment));
		}
		return totDev;
	}
	
	public float getSegmentRGBdeviation(HashSet<Integer> segment, Color centroid){
		float deviation = 0;
		for (Iterator<Integer> iterator = segment.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
//			int[] coords = toGridCoords(integer);
//			deviation += getRGBEuclid(this.pixels[coords[0]][coords[1]],centroid);
			deviation += getRGBEuclid(listPixels[integer], centroid);
		}
		return deviation;
	}
	
	public Color getRGBCentroid(HashSet<Integer> segment){
		int[] RGBsums = new int[3];
		for (Integer point : segment) {
//			int[] coords = toGridCoords(point);
//			Color pointCol = this.pixels[coords[0]][coords[1]];
//			System.out.println(point);
			Color pointCol = this.listPixels[point];
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

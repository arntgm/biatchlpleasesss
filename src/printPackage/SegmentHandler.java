package printPackage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import mst.FileHandler;
import utils.Euclidian;

public class SegmentHandler {
	
//	private int[] neighborArray;
	private FileHandler fh;
	private Euclidian eu;
	private Random r;
	
	public SegmentHandler(FileHandler fh, Euclidian eu) {
//		this.neighborArray = neighborArray;
		this.fh = fh;
		this.eu = eu;
		this.r = new Random();
	}
	
//	public void updateNeighborArray(int[] neighborArray) {
//		this.neighborArray = neighborArray;
//	}
	
	
	
	public void mergeWithThreshold(int[] neighborArray, List<HashSet<Integer>> segments, int threshold, ArrayList<HashSet<Integer>> toSeg){ //HashMap<HashSet<Integer>,HashSet<Integer>> edgeMap,
		HashSet<HashSet<Integer>> removedSegments = new HashSet<HashSet<Integer>>();
		for (HashSet<Integer> segment : segments) {			
			if (segment.size() < threshold){
				mergeToNeighbor(neighborArray, segment, segments, toSeg); //edgeMap
				removedSegments.add(segment);
			}
		}
		segments.removeAll(removedSegments);
	}
	
	public void mergeToNeighbor(HashSet<Integer> segment, List<HashSet<Integer>>segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgeMap, ArrayList<HashSet<Integer>> toSeg){
		HashSet<Integer> edges = edgeMap.get(segment);
		edgeMap.remove(segment);
		Integer pixel = edges.iterator().next(); //random edge for merging
		List<Integer> neighbors = eu.getNeighborNumbers(pixel);
		Integer neighbor = neighbors.get(r.nextInt(neighbors.size()));
		HashSet<Integer> neighborSeg = toSeg.get(neighbor);
//		HashSet<Integer> neighborSeg = utils.Euclidian.getSegment(segments,neighbors.get(r.nextInt(neighbors.size())));
		while(neighborSeg == segment){
			neighbors.remove(neighbor);
			neighbor = neighbors.get(r.nextInt(neighbors.size()));
			neighborSeg = toSeg.get(neighbor);
		} 	
		edgeMap.remove(neighborSeg);
		for (Integer integer : segment) {
			neighborSeg.add(integer);
		}
		edgeMap.put(neighborSeg, eu.getEdgeSet(neighborSeg, toSeg));
	}
	
	public Integer getRandomEdge(int[] neighborArray, HashSet<Integer> segment, ArrayList<HashSet<Integer>> toSeg){
		for (Integer gene : segment) {
			if(segment.size()==1){
//				neighborArray[gene] = neighbors.get(r.nextInt(neighbors.size()));
				return gene;
			}
			ArrayList<Integer> sameSeg = new ArrayList<Integer>();
			ArrayList<Integer> otherSeg = new ArrayList<Integer>();
			boolean isEdge = false;
			List<Integer> neighbors = eu.getNeighborNumbers(gene);
			for (Integer neighbor : neighbors) {
				if (!toSeg.get(neighbor).equals(segment)){ //point is an edge
					isEdge = true;
					otherSeg.add(neighbor);
//					break;
				}else{
					sameSeg.add(neighbor);
				}
			}
			if(isEdge){
				for (Integer neigh : sameSeg) {
					if(neighborArray[neigh]==gene || neighborArray[gene]==gene)
//						neighborArray[gene] = otherSeg.get(r.nextInt(otherSeg.size())); //update gene string
						return gene;
				}
			}
			}
//		System.out.println("uh oh - no neighbor found!");
//		System.out.println("segment size was: "+segment.size());
		return 0;
	}
	
	public void mergeToNeighbor(int[] neighborArray, HashSet<Integer> segment, List<HashSet<Integer>>segments, ArrayList<HashSet<Integer>> toSeg){
		Integer pixel = getRandomEdge(neighborArray, segment, toSeg);
		List<Integer> neighbors = eu.getNeighborNumbers(pixel);
		Integer neighbor = neighbors.get(r.nextInt(neighbors.size()));
		HashSet<Integer> neighborSeg = toSeg.get(neighbor);
		while(neighborSeg.equals(segment)){
			neighbors.remove(neighbor);
			neighbor = neighbors.get(r.nextInt(neighbors.size()));
			neighborSeg = toSeg.get(neighbor);
		}
		for (Integer integer : segment) {
			neighborSeg.add(integer);
			toSeg.set(integer, neighborSeg);
			neighborArray[pixel] = neighbor;
		}
	}
	
//	public List<HashSet<Integer>> calculateSegments(int[] neighborArray, HashMap<Integer, HashSet<Integer>> segMap) {
//		List<HashSet<Integer>> segments = new ArrayList<HashSet<Integer>>();
//		Set<Integer> segment = new HashSet<Integer>();
//		for (int i = 0; i < neighborArray.length; i++) {
//			int next = neighborArray[i];
//			segment.add(next);
//			segMap.put(next, (HashSet<Integer>)segment);
//			
//			if(segment.contains(next) || next < i){
//				
//			}
//		}
//	}
	
	public List<HashSet<Integer>> calculateSegments(int[] neighborArray, ArrayList<HashSet<Integer>> toSeg) {
//		System.out.println("Calculating segments");
		long startTime = System.currentTimeMillis();
		List<HashSet<Integer>> segments = new ArrayList<HashSet<Integer>>();
		HashSet<Integer> segment = new HashSet<Integer>();
//		Set<Integer> unvisited = new HashSet<Integer>();
		int unvisited = neighborArray.length;
		boolean[] visited = new boolean[neighborArray.length];
		for (int i = 0; i < neighborArray.length; i++) {
			visited[i] = false;
			toSeg.add(null);
		}
		int next = 0;
		int merge = 0;
		int lastVisited = 0;
		while(unvisited > 0){
			segment.add((Integer)next);
			toSeg.set(next, segment);
			visited[next] = true;
			next = neighborArray[next];
			unvisited-=1;
			if (visited[next] || unvisited == 0){
				if(toSeg.get(next) != null && !toSeg.get(next).equals(segment)){ //next is in another segment
					mergeSegments(segment, next, toSeg);
					merge+=1;
				}else{ //next is in same segment
//					System.out.println("added segment!");
					segments.add(segment);
				}
				segment = new HashSet<Integer>();
				next = setNext(visited, lastVisited);
				lastVisited = next;
			}
		}
		long totTime = System.currentTimeMillis()-startTime;
//		System.out.println("Calculating segments took: "+totTime+" miliseconds.");
//		System.out.println("Merged: "+merge);
//		System.out.println("Segments calculated");
		return segments;
	}
	

	
	private int setNext(boolean[] visited, int lastVisited) {
		for (int i = lastVisited; i < visited.length; i++) {
			if(!visited[i]){
				return i;
			}
		}
		return 0;
//		if (unvisited.isEmpty()) {
//			return 0;
//		}
//		return unvisited.iterator().next(); //grab first available element
	}
	
	//prev took in List<HashSet<Integer>> segments as arg
	private void mergeSegments(HashSet<Integer> segment, int next, List<HashSet<Integer>> toSeg) { //HashMap<Integer, HashSet<Integer>> segMap
		HashSet<Integer> toAdd = toSeg.get(next);
		for (Integer integer : segment) {
			toSeg.set(integer, toAdd);
			toAdd.add(integer);
		}
//		toAdd.addAll(segment);
//		for (HashSet<Integer> s : segments) {
//			if (s.contains(next)) {
//				s.addAll(segment);
////				for (Integer integer : segment) {
////					s.add(integer);
////				}
//				return;
	}
}

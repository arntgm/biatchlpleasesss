package printPackage;

import java.awt.Color;
import java.util.ArrayList;
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
	
	
	
	public void mergeWithThreshold(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>,HashSet<Integer>> edgeMap, int threshold){
		HashSet<HashSet<Integer>> removedSegments = new HashSet<HashSet<Integer>>();
		
		for (Iterator<HashSet<Integer>> iterator = segments.iterator(); iterator.hasNext();) {
			
			HashSet<Integer> segment = (HashSet<Integer>) iterator.next();
			
			if (segment.size() < threshold){
//				System.out.println("hei hei se her, segment: "+segment.toString());
//				System.out.println("men finnes det i map? "+edgeMap.containsKey(segment));
				mergeToNeighbor(segment, segments, edgeMap); //HashSet<Integer> changedSeg = 
				removedSegments.add(segment);
			}
		}
		segments.removeAll(removedSegments);
	}
	
	public void mergeToNeighbor(HashSet<Integer> segment, List<HashSet<Integer>>segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgeMap){
//		HashSet<Integer> edges = eu.getEdgeSet(segment);
//		System.out.println(segment.size());
		HashSet<Integer> edges = edgeMap.get(segment);
		edgeMap.remove(segment);
		
//		System.out.println("size of edges is: "+edges.size());
		Integer pixel = edges.iterator().next(); //random edge for merging
		List<Integer> neighbors = eu.getNeighborNumbers(pixel);
		HashSet<Integer> neighborSeg = utils.Euclidian.getSegment(segments,neighbors.get(r.nextInt(neighbors.size())));
//		System.out.println("getSegment worked!");
		while(neighborSeg == segment){
			neighborSeg = utils.Euclidian.getSegment(segments,neighbors.get(r.nextInt(neighbors.size())));
		}
		edgeMap.remove(neighborSeg);
//		System.out.println("managed to randomiiiize");
//		neighborSeg.addAll(segment);
		for (Integer integer : segment) {
			neighborSeg.add(integer);
		}
		edgeMap.put(neighborSeg, eu.getEdgeSet(neighborSeg));
//		return neighborSeg;
//		System.out.println("nuuuhvel");
	}
	
	public List<HashSet<Integer>> calculateSegments(int[] neighborArray) {
		System.out.println("Calculating segments");
		List<HashSet<Integer>> segments = new ArrayList<HashSet<Integer>>();
		Set<Integer> segment = new HashSet<Integer>();
//		Set<Integer> visited = new HashSet<Integer>();
		Set<Integer> unvisited = new HashSet<Integer>();
		for (int i = 0; i < neighborArray.length; i++) {
			unvisited.add((Integer)i);
		}
		int next = 0;
		int old;
//		while (visited.size() < neighborArray.length) {
		while (!unvisited.isEmpty()){
			segment.add((Integer)next);
			unvisited.remove((Integer)next);
//			visited.add((Integer)next);
			old = next;
			next = neighborArray[old];
			if (segment.contains(next)) {
				segments.add((HashSet<Integer>) segment);
				segment = new HashSet<Integer>();
				next = setNext((HashSet<Integer>)unvisited);
				}
			else if (!unvisited.contains(next)) {
				mergeSegments(segments,(HashSet<Integer>) segment, next);
				segment = new HashSet<Integer>();
				next = setNext((HashSet<Integer>)unvisited);
			}
		}
		return segments;
	}
	

	
	private int setNext(HashSet<Integer> unvisited) {
		if (unvisited.isEmpty()) {
			return 0;
		}
		return unvisited.iterator().next(); //grab first available element
	}
	
	private void mergeSegments(List<HashSet<Integer>> segments, HashSet<Integer> segment, int next) {
		for (HashSet<Integer> s : segments) {
			if (s.contains(next)) {
				s.addAll(segment);
//				for (Integer integer : segment) {
//					s.add(integer);
//				}
				return;
			}
		}
	}
}

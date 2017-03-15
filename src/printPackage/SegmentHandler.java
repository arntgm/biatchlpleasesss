package printPackage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SegmentHandler {
	
	private int[] neighborArray;
	private List<HashSet<Integer>> segments;
	
	public SegmentHandler(int[] neighborArray) {
		this.neighborArray = neighborArray;
	}
	
	public void updateNeighborArray(int[] neighborArray) {
		this.neighborArray = neighborArray;
	}
	
	public void updateSegments() {
		segments = calculateSegments();
	}
	
	public List<HashSet<Integer>> getSegments() {
		return segments;
	}
	
	
	
	private List<HashSet<Integer>> calculateSegments() {
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
				segment.clear();
				next = setNext((HashSet<Integer>)unvisited);
			}
		}
		System.out.println("# of segments: "+segments.size());
		System.out.println(segments.get(1).size());
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

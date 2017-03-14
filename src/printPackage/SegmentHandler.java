package printPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SegmentHandler {
	
	private Integer[] neighborArray;
	private List<List<Integer>> segments;
	
	public SegmentHandler(Integer[] neighborArray) {
		this.neighborArray = neighborArray;
	}
	
	public void updateNeighborArray(Integer[] neighborArray) {
		this.neighborArray = neighborArray;
	}
	
	public void updateSegments() {
		segments = calculateSegments();
	}
	
	public List<List<Integer>> getSegments() {
		return segments;
	}
	

	
	private List<List<Integer>> calculateSegments() {
		List<List<Integer>> segments = new ArrayList<List<Integer>>();
		List<Integer> segment = new ArrayList<Integer>();
		List<Integer> visited = new ArrayList<Integer>();
		int next = 0;
		int old;
		while (visited.size() < neighborArray.length) {
			segment.add(next);
			visited.add(next);
			old = next;
			next = neighborArray[old];
			if (segment.contains(next)) {
				segments.add(new ArrayList<Integer>(segment));
				segment.clear();
				next = setNext(visited);
				}
			else if (visited.contains(next)) {
				mergeSegments(segments, segment, next);
				segment.clear();
				next = setNext(visited);
			}
		}
		System.out.println(segments);
		return segments;
	}
	
	private int setNext(List<Integer> visited) {
		int next;
		if (visited.size() == neighborArray.length) {
			next = 0;
		} else {
			next = ThreadLocalRandom.current().nextInt(0,neighborArray.length);
			while (visited.contains(next)) {
				next = ThreadLocalRandom.current().nextInt(0,neighborArray.length);
			}
		}
		return next;
	}
	
	private void mergeSegments(List<List<Integer>> segments, List<Integer> segment, int next) {
		for (List<Integer> s : segments) {
			if (s.contains(next)) {
				for (Integer integer : segment) {
					s.add(integer);
				}
				return;
			}
		}
	}
}

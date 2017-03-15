package printPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SegmentHandler {
	
	private int[] neighborArray;
	private List<List<Integer>> segments;
	private Random rand;
	
	public SegmentHandler(int[] neighborArray) {
		this.neighborArray = neighborArray;
		rand = new Random();
	}
	
	public void updateNeighborArray(int[] neighborArray) {
		this.neighborArray = neighborArray;
	}
	
	public void updateSegments() {
		segments = calculateSegments();
	}
	
	public List<List<Integer>> getSegments() {
		return segments;
	}
	

	
	private List<List<Integer>> calculateSegments() {
		System.out.println("Calculating segments");
		List<List<Integer>> segments = new ArrayList<List<Integer>>();
		List<Integer> segment = new ArrayList<Integer>();
		List<Integer> visited = new ArrayList<Integer>();
		List<Integer> unvisited = new ArrayList<Integer>();
		for (int i = 0; i < neighborArray.length; i++) {
			unvisited.add((Integer)i);
		}
		int next = 0;
		int old;
		boolean a;
		while (visited.size() < neighborArray.length) {
			segment.add((Integer)next);
//			for (int i = 0; i < unvisited.size(); i++) {
//				if (unvisited.get(i) == next) {
//					unvisited.remove(i);
//					break;
//				}
//			}
			unvisited.remove((Integer)next);
			visited.add((Integer)next);
			old = next;
			next = neighborArray[old];
			if (segment.contains(next)) {
				segments.add(new ArrayList<Integer>(segment));
				segment.clear(); //remove this by simply instancing new? O(n)...
				next = setNext(unvisited);
				}
			else if (visited.contains(next)) {
				mergeSegments(segments, segment, next);
				segment.clear();
				next = setNext(unvisited);
			}
		}
		System.out.println(segments);
		return segments;
	}
	
	private int setNext(List<Integer> unvisited) {
		int next;
		if (unvisited.isEmpty()) {
			return 0;
		}
		int r = rand.nextInt(unvisited.size());
		next = unvisited.get(r);
//		if (visited.size() == neighborArray.length) {
//			next = 0;
//		} else {
//			next = ThreadLocalRandom.current().nextInt(0,neighborArray.length);
//			while (visited.contains(next)) {
//				next = ThreadLocalRandom.current().nextInt(0,neighborArray.length);
//			}
//		}
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

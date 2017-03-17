package genetics;
import printPackage.SegmentHandler;
import utils.Euclidian;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class Chromosome implements Comparable<Chromosome> {

	private int[] neighborArray;
	private List<HashSet<Integer>> segments;
	private HashMap<HashSet<Integer>,HashSet<Integer>> edgeMap;
	private Map<HashSet<Integer>,Color> centroidMap;
	private SegmentHandler sh;
	private double devi;
	private double edge;
	private double conn;
	public int nonDomRank;
	public int dominatedByCounter;
	public double crowdDist;
	public List<Chromosome> dominationList;
	private Euclidian eu;
		
	public List<HashSet<Integer>> getSegments() {
		return segments;
	}
	
	public void setSegments(List<HashSet<Integer>> segments) {
		this.segments = segments;
	}
	
	public Map<HashSet<Integer>, Color> getCentroidMap() {
		return centroidMap;
	}
	
	public void setCentroidMap(Map<HashSet<Integer>, Color> centroidMap) {
		this.centroidMap = centroidMap;
	}

	public Chromosome(int[] neighborArray, Euclidian eu, SegmentHandler sh) {
		this.neighborArray = neighborArray;
		this.eu = eu;
		this.edgeMap = new HashMap<HashSet<Integer>, HashSet<Integer>>();
		this.centroidMap = new HashMap<HashSet<Integer>, Color>();
		this.sh = sh;
		this.segments = sh.calculateSegments(this.neighborArray);
		updateObjectiveValues();
	}
	
	public void setEdgeMap(HashMap<HashSet<Integer>,HashSet<Integer>> edgeMap){
		this.edgeMap = edgeMap;
	}
	
	public HashMap<HashSet<Integer>,HashSet<Integer>> getEdgeMap(){
		return this.edgeMap;
	}
	
	public void generateEdgeMap(){
		for (Iterator<HashSet<Integer>> iterator = this.segments.iterator(); iterator.hasNext();) {
			HashSet<Integer> segment = (HashSet<Integer>) iterator.next();
//			System.out.println("new segment: "+segment.toString());
			this.edgeMap.put(segment, eu.getEdgeSet(segment));
		}
	}
	
	public void generateCentroidMap(){
		for (Iterator<HashSet<Integer>> iterator = this.segments.iterator(); iterator.hasNext();) {
			HashSet<Integer> segment = (HashSet<Integer>) iterator.next();
			this.centroidMap.put(segment, eu.getRGBCentroid(segment));
		}
	}

	
	public void setCrowdDist(double crowdDist) {
		this.crowdDist = crowdDist;
	}
	
	public void addToCrowdDist(double crowdDist) {
		this.crowdDist += crowdDist;
	}
	
//	private void setValue(double value, String objective) {
//		if (objective.equals("devi")) {
//			devi = value;
//		} else if (objective.equals("edge")) {
//			edge = value;
//		} else if (objective.equals("conn")) {
//			conn = value;
//		}
//	}
	
	public void updateObjectiveValues() {
		updateDevi();
		updateEdge();
		updateConn();
	}
	
	// TODO Create update methods, to initialize and update objective values.
	private void updateConn() {
		
	}

	private void updateEdge() {
		
	}

	private void updateDevi() {
	}
	
	public int[] getNeighborArray() {
		return neighborArray;
	}
	
	public double getObjectiveValue(String objective) {
		if (objective.equals("devi")) {
			return devi;
		} else if (objective.equals("edge")) {
			return edge;
		} else {
			return conn;
		}
	}
	
	@Override
	public int compareTo(Chromosome c) {
		// TODO Auto-generated method stub
		return Comparators.DEVI.compare(this, c);
	}
	
	public static class Comparators {
		
		public static Comparator<Chromosome> DEVI = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.devi  < c2.devi)
					return -1;
				if(c1.devi > c2.devi)
					return 1;
				return 0;
			}
		};
		
		public static Comparator<Chromosome> EDGE = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.edge  < c2.edge)
					return -1;
				if (c1.edge > c2.edge)
					return 1;
				return 0;
			}
			
		};
		
		public static Comparator<Chromosome> CONN = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.conn < c2.conn)
					return -1;
				if (c1.conn > c2.conn)
					return 1;
				return 0;
			}
		};
		
		public static Comparator<Chromosome> CROWD = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.crowdDist < c2.crowdDist)
					return -1;
				if (c1.crowdDist > c2.crowdDist)
					return 1;
				return 0;
			}
		};
	}



}

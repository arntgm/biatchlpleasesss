package genetics;
import printPackage.SegmentHandler;
import utils.Euclidian;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class Chromosome implements Comparable<Chromosome> {

	private int[] neighborArray;
	private ArrayList<HashSet<Integer>> toSeg;
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
		this.segments = new ArrayList<HashSet<Integer>>();
		this.toSeg= new ArrayList<HashSet<Integer>>();
		this.devi = 0;
		this.conn = 0;
		this.edge = 0;
		this.sh = sh;
//		this.segments = sh.calculateSegments(this.neighborArray);
//		updateObjectiveValues();
	}
	
	public void setEdgeMap(HashMap<HashSet<Integer>,HashSet<Integer>> edgeMap){
		this.edgeMap = edgeMap;
	}
	
	public ArrayList<HashSet<Integer>> getSegmentMap(){
		return this.toSeg;
	}
	
	public HashMap<HashSet<Integer>,HashSet<Integer>> getEdgeMap(){
		return this.edgeMap;
	}
	
	public Chromosome copyChromo(){
		int[] geneCopy = new int[this.neighborArray.length];
		System.arraycopy(this.neighborArray, 0, geneCopy, 0, neighborArray.length);
		Chromosome newChromo = new Chromosome(geneCopy, this.eu, this.sh);
//		newChromo.setCentroidMap(new HashMap<HashSet<Integer>, Color>(this.centroidMap));
//		newChromo.setEdgeMap(new HashMap<HashSet<Integer>,HashSet<Integer>>(this.getEdgeMap()));
//		newChromo.setSegments(new ArrayList<HashSet<Integer>>(this.getSegments()));
//		newChromo.setCrowdDist(new Double(this.crowdDist));
		return newChromo;
	}
	
	public void generateEdgeMap(){
		for (HashSet<Integer> segment : this.segments) {
			this.edgeMap.put(segment, eu.getEdgeSet(segment, this.toSeg));
		}
	}
	
	public void generateCentroidMap(){
		for (HashSet<Integer> segment: this.segments) {
			this.centroidMap.put(segment, eu.getRGBCentroid(segment));
		}
	}
	
	public void generateSegments(){
		this.segments = sh.calculateSegments(this.neighborArray, this.toSeg);
//		System.out.println("# of segments: "+this.segments.size());
	}
	
	public void clearAll(){
		this.segments.clear();
		this.toSeg.clear();
		this.edgeMap.clear();
		this.centroidMap.clear();
		this.devi = 0;
		this.edge = 0;
		this.conn = 0;
	}
	
	
	public void updateAll(String[] objectives, int minSegSize, boolean init){
		clearAll();
		this.generateSegments();
		if(init)
			sh.mergeWithThreshold(this.neighborArray, this.segments, minSegSize, this.toSeg); // this.edgeMap, 
		this.generateEdgeMap();
		this.generateCentroidMap();
		for (int i = 0; i < objectives.length; i++) {
			updateObjectiveValue(objectives[i]);
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
	
	public void updateObjectiveValue(String objective) {
		switch (objective){
		case "devi":
			this.devi = calcDevi();
		case "edge":
			this.edge = calcEdge();
		case "conn":
			this.conn = calcConn();
		}
	}
	
	// TODO Create update methods, to initialize and update objective values.
	private double calcConn() {
		return eu.getChromosomeConnValue(this.segments, this.edgeMap, this.toSeg);
	}

	private double calcEdge() {
		return eu.getChromosomeEdgeValue(this.segments, this.edgeMap, this.toSeg);
	}

	private double calcDevi() {
		return eu.getChromosomeRGBDev(this.segments, this.centroidMap);
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
		
		public static Comparator<Chromosome> TOTAL = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.nonDomRank < c2.nonDomRank)
					return -1;
				if (c1.nonDomRank > c2.nonDomRank)
					return 1;
				else
					if (c1.crowdDist < c2.crowdDist)
						return -1;
					if (c1.crowdDist > c2.crowdDist)
						return 1;
				return 0;
			}
		};
	}



}

import printPackage.SegmentHandler;
import java.util.Comparator;
import java.util.List;
public class Chromosome implements Comparable<Chromosome> {

private Integer[] neighborArray;
private List<List<Integer>> segments;
private SegmentHandler sh;
private double devi;
private double edge;
private double conn;
public int nonDomRank;
public int dominatedByCounter;
public double crowdDist;
public List<Chromosome> dominationList;
	
	public Chromosome(Integer[] neighborArray) {
		this.neighborArray = neighborArray;
		sh = new SegmentHandler(neighborArray);
		sh.updateSegments();
		this.segments = sh.getSegments();
		updateObjectiveValues();
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
				if (c1.devi - c2.devi < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		};
		
		public static Comparator<Chromosome> EDGE = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.edge - c2.edge < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		};
		
		public static Comparator<Chromosome> CONN = new Comparator<Chromosome>() {
			public int compare(Chromosome c1, Chromosome c2) {
				if (c1.conn - c2.conn < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		};
	}



}

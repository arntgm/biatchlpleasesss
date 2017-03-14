import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class Chromosome implements Comparable<Chromosome> {
	
private double devi;
private double edge;
private double conn;
public int nonDomRank;
public int dominatedByCounter;
public double crowdDist;
public List<Chromosome> dominationList;
	
	public Chromosome(ArrayList<Color> image) {
		//buildRandomChromosome();
		crowdDist = 0;
	}
	
	public void setCrowdDist(double crowdDist) {
		this.crowdDist = crowdDist;
	}
	
	public void addToCrowdDist(double crowdDist) {
		this.crowdDist += crowdDist;
	}
	
	private void setValue(double value, String objective) {
		if (objective.equals("devi")) {
			devi = value;
		} else if (objective.equals("edge")) {
			edge = value;
		} else if (objective.equals("conn")) {
			conn = value;
		}
	}
	
	public void updateObjectiveValues() {
		updateDevi();
		updateEdge();
		updateConn();
	}
	
	private void updateConn() {
		// TODO Auto-generated method stub
		
	}

	private void updateEdge() {
		// TODO Auto-generated method stub
		
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

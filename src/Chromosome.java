import java.awt.Color;
import java.util.ArrayList;
public class Chromosome {
	
private ArrayList<Color> pixels;
private double devi;
private double edge;
private double conn;
private int nonDomRank;
private double crowdDist;
	
	public Chromosome(ArrayList<Color> image) {
		//buildRandomChromosome();
	}
	
	public void setCrowdDist(double crowdDist) {
		this.crowdDist = crowdDist;
	}
	
	public void addToCrowdDist(double crowdDist) {
		this.crowdDist += crowdDist;
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

}

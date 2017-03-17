package genetics;

import utils.Euclidian;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import printPackage.SegmentHandler;

public class Mutator {
	
	private Random rand;
	private SegmentHandler sh;
	
	public Mutator() {
		this.rand = new Random();
	}
	
	//Takes in two parent Chromosomes, returns two offspring neighborArrays.
	public int[][] crossover(Chromosome p1, Chromosome p2) {
		int[] genes1 = p1.getNeighborArray();
		int[] genes2 = p2.getNeighborArray();
		int[][] newGenes = new int[2][genes1.length];
		for (int i = 0; i < genes1.length; i++) {
			if(rand.nextBoolean()) {
				newGenes[0][i] = genes1[i];
				newGenes[1][i] = genes2[i];
			} else {
				newGenes[0][i] = genes2[i];
				newGenes[1][i] = genes1[i];
			}
		}
		return newGenes;
	}
	
	//Takes in Chromosome, returns the mutated neighborArray.
	public boolean mutateChromosome(Chromosome p, Euclidian e) {
		int[] genes = p.getNeighborArray();		
		return mutateGenes(genes, e, p.getSegments());
	}
	
	//mutates array in place
	public boolean mutateGenes(int[] genes, Euclidian e, List<HashSet<Integer>> segments) {
		boolean updateSegments = false;
		int toNode;
		List<Integer> neighbors;
		for (int i = 0; i < genes.length; i++) {
			if (rand.nextDouble()<0.001) {
				neighbors = e.getNeighborNumbers(i);
				toNode = neighbors.get(rand.nextInt(neighbors.size()-1));
				if(!Euclidian.getSegment(segments, genes[i]).contains(toNode))
					updateSegments = true;
				genes[i] = toNode;
			}
		}
		return updateSegments;
	}

}

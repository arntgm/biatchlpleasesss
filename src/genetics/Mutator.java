package genetics;

import utils.Euclidian;
import java.util.List;
import java.util.Random;

public class Mutator {
	
	private Random rand;
	
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
	public int[] mutateChromosome(Chromosome p, Euclidian e) {
		int[] genes = p.getNeighborArray();
		return mutateGenes(genes, e);
	}
	
	//Takes in a neighborArray, returns an mutated one.
	public int[] mutateGenes(int[] genes, Euclidian e) {
		int toNode;
		List<Integer> neighbors;
		for (int i = 0; i < genes.length; i++) {
			if (rand.nextDouble()<0.001) {
				neighbors = e.getNeighborNumbers(i);
				toNode = neighbors.get(rand.nextInt(neighbors.size()-1));
				genes[i] = toNode;
			}
		}
		return genes;
	}

}

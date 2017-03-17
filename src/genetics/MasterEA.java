package genetics;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import mst.FileHandler;
import mst.MinSpanTree;
import mst.Graph.Edge;
import printPackage.ImageDrawer;
import printPackage.PicPrinter;
import printPackage.SegmentHandler;
import utils.Euclidian;
public class MasterEA {
	
	private Color[][] newColor;
	private String[] objectives;
	private FileHandler fh;
	private MinSpanTree mst;
	private Euclidian eu;
	private PicPrinter pp;
	private SegmentHandler sh;
	private List<List<Chromosome>> chromoTiers;
	private List<Chromosome> newPopulation;
	private List<Chromosome> oldPopulation;
	private double crossoverRate;
	private double mutationRate;
	private Random r;
	private Mutator mut;
	
	private MasterEA(String filename, double crossover, double mutation) {
		//this.fh = new FileHandler(filename);
		this.objectives = new String[] {"devi", "edge", "conn"};
		this.fh = new FileHandler(filename);
		this.mst = new MinSpanTree(filename, fh);
		this.eu = new Euclidian(fh.getWidth(), fh.getHeight(), fh.getPixels(), fh.getListPixels());
		this.pp = new PicPrinter(fh, eu);
		this.sh = new SegmentHandler(fh, eu);
		this.crossoverRate = crossover;
		this.mutationRate = mutation;
		this.r = new Random();
		this.mut = new Mutator();
	}
	
//	private void init() {
//		//newColor = fh.getPixels();
//		//run();
//	}
	
//	private void run() {
//		for (int i = 0; i < fh.getWidth(); i++) {
//			for (int j = 50; j < 60; j++) {
//				newColor[j][i] = new Color(255, 255, 255);
//			}
//		}
//		fh.saveNewImage(newColor);
//	}
	
	private List<List<Chromosome>> fastNonDominatedSort(List<Chromosome> pop) {
		int dom;
		List<List<Chromosome>> F = new ArrayList<List<Chromosome>>();
		List<Chromosome> F1 = new ArrayList<Chromosome>();
		for (Chromosome p : pop) {
			p.dominationList = new ArrayList<Chromosome>();
			p.dominatedByCounter = 0;
			for (Chromosome q : pop) {
				dom = dominates(p,q);
				if (dom == 3) {
					p.dominationList.add(q);
				} else if (dom == -3) {
					p.dominatedByCounter += 1;
				}
			}
			if (p.dominatedByCounter == 0) {
				p.nonDomRank = 1;
				F1.add(p);
			}
		}
		F.add(F1);
		int i = 0;
		List<Chromosome> Q;
		while (! F.get(i).isEmpty()) {
			Q = new ArrayList<Chromosome>();
			for (Chromosome p : F.get(i)) {
				for (Chromosome q : p.dominationList) {
					q.dominatedByCounter -= 1;
					if (q.dominatedByCounter == 0) {
						q.nonDomRank = i+2;
						Q.add(q);
					}
				}
			}
			i += 1;
			List<Chromosome> newQ = new ArrayList<Chromosome>(Q);
			F.add(newQ);
		}
		return F;
	}
	
	private int dominates(Chromosome p, Chromosome q) {
		int counter = 0;
		for (String objective : objectives) {
			if (p.getObjectiveValue(objective) < q.getObjectiveValue(objective)) {
				counter += 1;
			} else if (p.getObjectiveValue(objective) > q.getObjectiveValue(objective)) {
				counter -= 1;
			}
		}
		return counter;
	}
	
	private void crowdingDistanceAssignment(List<Chromosome> F) {
		int l = F.size();
		double max;
		double min;
		for (Chromosome chromosome : F) {
			chromosome.setCrowdDist(0);
		}
		for (String objective : objectives) {
			sortByObjective(F, objective);
			F.get(0).setCrowdDist(Double.MAX_VALUE);
			F.get(l-1).setCrowdDist(Double.MAX_VALUE);
			max = F.get(l-1).getObjectiveValue(objective);
			min = F.get(0).getObjectiveValue(objective);
			for (int i = 1; i < l-1; i++) {
				F.get(i).addToCrowdDist((F.get(i+1).getObjectiveValue(objective)
						-F.get(i-1).getObjectiveValue(objective)) / (max-min));
			}
		}
	}
	
	private void sortByObjective(List<Chromosome> F, String objective) {
		if (objective.equals("devi")) {
			Collections.sort(F, Chromosome.Comparators.DEVI);
		} else if (objective.equals("edge")) {
			Collections.sort(F, Chromosome.Comparators.EDGE);
		} else if (objective.equals("conn")) {
			Collections.sort(F, Chromosome.Comparators.CONN);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List<Chromosome> spawnChromosomes(List<int[]>pop, int threshold){
		List<Chromosome> spawns = new ArrayList<Chromosome>();
		for (int[] gene : pop) {
			System.out.println("new chromo!");
			Chromosome c = new Chromosome(gene, this.eu, this.sh);
			c.generateEdgeMap();
			Collection<HashSet<Integer>> edges = c.getEdgeMap().values();
//			System.out.println("# of segments: "+c.getSegments().size());
//			for (Iterator iterator = edges.iterator(); iterator.hasNext();) {
//				HashSet<Integer> hashSet = (HashSet<Integer>) iterator.next();
////				System.out.println(hashSet.toString());
//			}
			c.generateCentroidMap();
			sh.mergeWithThreshold(c.getSegments(), c.getEdgeMap(), threshold);
			System.out.println("heihå");
			spawns.add(c);
		}
		return spawns;
	}
	
	public List<Chromosome> makeNewPop(List<Chromosome> oldPop){
		List<Chromosome> newPop = new ArrayList<Chromosome>();
		while(newPop.size() < oldPop.size()) {
			Chromosome[] parents = selection(oldPop);
			Chromosome[] children = new Chromosome[2];
			double cross = r.nextDouble();
			if(cross <= this.crossoverRate){
				
			}else{
				children[0] = parents[0].copyChromo();
				children[1] = parents[1].copyChromo();
			}
			cross = r.nextDouble();
		}
		for (Chromosome chromosome : newPop) {
//			boolean update = false;
			if(mut.mutateChromosome(chromosome, eu))
				chromosome.updateAll(this.objectives);
		}
	}
	
	public void run(int population, int removeLimit, int minSegmentSize, int maxGenerations){
		int genCounter = 0;
		ArrayList<Edge<Integer>> MST = (ArrayList<Edge<Integer>>) mst.getMSTPath();
		int[] genes = this.mst.getGenes(MST);
		List<int[]> pop = this.mst.generateGeneArrays(population, removeLimit, MST, genes);
		this.oldPopulation = spawnChromosomes(pop, minSegmentSize);
		System.out.println("Initial chromosomes created");
		// TODO Initiate chromosome objective values!
		this.chromoTiers = fastNonDominatedSort(oldPopulation);
		oldPopulation.clear();
		for (List<Chromosome> tier : chromoTiers) {
			this.oldPopulation.addAll(tier);
		}
		newPopulation = makeNewPop(oldPopulation);
		genCounter++;
		
		while (genCounter < maxGenerations) {
			oldPopulation.addAll(new ArrayList<Chromosome>(newPopulation));
			chromoTiers = fastNonDominatedSort(oldPopulation);
			newPopulation.clear();
			int i = 0;
			while (newPopulation.size() + chromoTiers.get(i).size() <= population) {
				crowdingDistanceAssignment(chromoTiers.get(i));
				newPopulation.addAll(chromoTiers.get(i));
				i++;
			}
			Collections.sort(chromoTiers.get(i), Chromosome.Comparators.CROWD);
			while (newPopulation.size() < population) {
				newPopulation.add(chromoTiers.get(i).remove(0));
			}
			oldPopulation = new ArrayList<Chromosome>(newPopulation);
			newPopulation = makeNewPop(oldPopulation);
			genCounter++;
		}
			
		chromoTiers = fastNonDominatedSort(newPopulation);
		List<Chromosome> topSols = new ArrayList<Chromosome>();
		int tier = 0;
		int n = 0;
		while (topSols.size() < 5) {
			topSols.add(chromoTiers.get(tier).get(n));
			n++;
			if (n > chromoTiers.get(tier).size() - 1) {
				break;
			}
		}
		for (int i = 0; i < topSols.size(); i++) {
//			System.out.println(eu.getChromosomeEdgeAndConn(topSols.get(i).getSegments(), topSols.get(i).getEdgeMap())[1]);
			pp.generateImage(topSols.get(i).getSegments(), (HashMap)topSols.get(i).getEdgeMap());
			ImageDrawer.drawImage("saved"+i+".jpg");
		}
		
	}
	
	public static void main(String[] args) {
		String filename = "Test_image_2";
		int population = 2;
		int mstRemoveLimit = 100;
		int minSegmentSize = 150;
		int maxGenerations = 15;
		MasterEA m = new MasterEA(filename, 0.7, 0.001);
		m.run(population, mstRemoveLimit, minSegmentSize, maxGenerations);
		//MasterEA master = new MasterEA("Test_image");

	}
}

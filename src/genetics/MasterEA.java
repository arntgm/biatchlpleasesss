package genetics;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Set;
import mst.FileHandler;
import mst.MinSpanTree;
import mst.Graph.Edge;
import mst.Kmeans;
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
	private Kmeans km;
	private SegmentHandler sh;
	private List<List<Chromosome>> chromoTiers;
	private List<Chromosome> newPopulation;
	private List<Chromosome> oldPopulation;
	private double crossoverRate;
	private double mutationRate;
	private double chromMutRate;
	private Random r;
	private Mutator mut;
	private int tourneySize;
	private int minSegmentSize;
	
	private MasterEA(String filename, double crossover, double geneMut, double chromMut, String[] objectives, int tourney, int minSeg) {
		//this.fh = new FileHandler(filename);
		this.objectives = objectives;
		this.fh = new FileHandler(filename);
		this.mst = new MinSpanTree(filename, fh);
		this.eu = new Euclidian(fh.getWidth(), fh.getHeight(), fh.getPixels(), fh.getListPixels());
		this.pp = new PicPrinter(fh, eu);
		this.sh = new SegmentHandler(fh, eu);
		this.km = new Kmeans(fh, eu);
		this.crossoverRate = crossover;
		this.mutationRate = geneMut;
		this.chromMutRate = chromMut;
		this.r = new Random();
		this.mut = new Mutator();
		this.tourneySize = tourney;
		this.minSegmentSize = minSeg;
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
				if (dom == objectives.length) {
					p.dominationList.add(q);
				} else if (dom == -objectives.length) {
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
		while (i<F.size() && !F.get(i).isEmpty() ) {
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
			if(!Q.isEmpty()){				
				F.add(new ArrayList<Chromosome>(Q));
			}
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
			Chromosome c = new Chromosome(gene, this.eu, this.sh);
			spawns.add(c);
		}
		return spawns;
	}
	
	public Chromosome[] selection(List<Chromosome> population){
		Chromosome[] sel = new Chromosome[2];
		List<Chromosome> candidates = new ArrayList<Chromosome>();
		for (int j = 0; j < sel.length; j++) {
			for (int i = 0; i < this.tourneySize; i++) {
				Chromosome newCand = population.get(r.nextInt(population.size()));
				while(candidates.contains(newCand)){
					newCand = population.get(r.nextInt(population.size()));
				}
				candidates.add(newCand);
			}
			Collections.sort(candidates, Chromosome.Comparators.TOTAL);
			double threshold = r.nextDouble();
			if(threshold<0.1){
				sel[j] = candidates.remove(r.nextInt(candidates.size()));
			}else{
				sel[j] = candidates.remove(0);
			}			
		}
		return sel;
	}
	
	public List<Chromosome> makeNewPop(List<Chromosome> oldPop, boolean init){
		System.out.println("Breeding new population...");
		List<Chromosome> newPop = new ArrayList<Chromosome>();
		while(newPop.size() < oldPop.size()) {
			boolean crossed = false;
			Chromosome[] parents = selection(oldPop);
			Chromosome[] children = new Chromosome[2];
			double cross = r.nextDouble();
			if(cross <= this.crossoverRate){
				crossed = true;
				int[][] newGenes = mut.crossover(parents[0], parents[1]);
//				System.out.println("length of gene used: "+newGenes[0].length);
				children[0] = new Chromosome(newGenes[0], eu, sh);
				children[1] = new Chromosome(newGenes[1], eu, sh);
			}else{
				children[0] = parents[0].copyChromo();
				children[1] = parents[1].copyChromo();
			}
			newPop.add(children[0]);
			newPop.add(children[1]);
			for (int i = 0; i < children.length; i++) {
				Chromosome chromosome = children[i];
				mut.mutateChromosome(chromosome, this.eu); //gene mutation
				double mutateChrom = r.nextDouble();
				if(mutateChrom < this.chromMutRate){ //chromosome mutation
//					sh.mergeToLimit(chromosome, chromosome.getSegments().size(), objectives);
				}
				chromosome.updateAll(this.objectives, this.minSegmentSize, init);
//				System.out.println("# of segments: "+chromosome.getSegments().size());
//				if(mutated || crossed){					
//				}
			}
		}
//		for (Chromosome chromosome : newPop) {
//			boolean update = false;
//			if(mut.mutateChromosome(chromosome, eu))
//				chromosome.updateAll(this.objectives);
//		}
		return newPop;
	}
	
	public void run(int removeLimit, int maxGenerations, int KmeanSens, int pEachK, int Kstart, int Krange, int pMST) throws IOException{
		int genCounter = 0;
		
		System.out.println("Initializing run of NSGA-II...");
		System.out.println("Generating initial population...");
		//create pop using Kmeans
		int iters = 0;
		List<int[]> pop = new ArrayList<int[]>();
		for (int i = Kstart; i < Kstart+Krange; i++) {
			for (int j = 0; j < pEachK; j++) {
				if(i == 2)
					iters = 50;
				else
					iters = 20;
				List<Integer> kmeans = km.getKmeans(i, iters);
				pop.add(km.getKgenes(kmeans));
			}
		}

		
		//create pop using MST
		if(pMST>0){			
			ArrayList<Edge<Integer>> MST = (ArrayList<Edge<Integer>>) mst.getMSTPath();
			int[] genes = this.mst.getGenes(MST);
			pop.addAll(this.mst.generateGeneArrays(pMST, removeLimit, MST, genes));
		}
		
		
		this.oldPopulation = spawnChromosomes(pop, minSegmentSize);
		System.out.println("Initial chromosomes created.");
		boolean init = true;

		System.out.println("Updating fields for initial chromosomes...");
		for (Chromosome chrome : oldPopulation) {
//			System.out.println("Updating chromosome...");
			chrome.updateAll(this.objectives, this.minSegmentSize, init); //
		}
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(0).getSegments(), oldPopulation.get(0).getEdgeMap()), "", new String[2], new String[2]);
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(0).getSegments(), oldPopulation.get(0).getEdgeMap()));
		int  naxGenerations = 0;
		System.out.println("Beginning evolutionary loop...");
		for (Chromosome c : oldPopulation) {
			for (int i = 0; i < 2; i++) {
				sh.mergeCentroids(c, 150-40*i);
//				c.updateAll(objectives, this.minSegmentSize, false);
				c.generateSegments();
				c.generateEdgeMap();
				c.generateCentroidMap();
			}
		}
		//TEST PRINTS
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(0).getSegments(), oldPopulation.get(0).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedBlackAndWhite(oldPopulation.get(0).getSegments(), oldPopulation.get(0).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(1).getSegments(), oldPopulation.get(1).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedBlackAndWhite(oldPopulation.get(1).getSegments(), oldPopulation.get(1).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(2).getSegments(), oldPopulation.get(2).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedBlackAndWhite(oldPopulation.get(2).getSegments(), oldPopulation.get(2).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(3).getSegments(), oldPopulation.get(3).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedBlackAndWhite(oldPopulation.get(3).getSegments(), oldPopulation.get(3).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(4).getSegments(), oldPopulation.get(4).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedBlackAndWhite(oldPopulation.get(4).getSegments(), oldPopulation.get(4).getEdgeMap()));
//		System.out.println("merging....");
//		sh.mergeToLimit(oldPopulation.get(0), 15, this.objectives);
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(2).getSegments(), oldPopulation.get(2).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(1).getSegments(), oldPopulation.get(1).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(3).getSegments(), oldPopulation.get(3).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(4).getSegments(), oldPopulation.get(4).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(5).getSegments(), oldPopulation.get(5).getEdgeMap()));
//		ImageDrawer.drawImage(pp.generateBufferedImage(oldPopulation.get(6).getSegments(), oldPopulation.get(6).getEdgeMap()));

		
//		System.out.println("All chromosomes updated. Sorting...");
//		this.chromoTiers = fastNonDominatedSort(oldPopulation);
//		oldPopulation.clear();
//		for (List<Chromosome> tier : chromoTiers) {
//			this.oldPopulation.addAll(tier);
//		}
//		newPopulation = makeNewPop(oldPopulation, init);
		

		genCounter++;
		
		while (genCounter < naxGenerations) {
			System.out.println("Generation: "+genCounter+"...");
			oldPopulation.addAll(new ArrayList<Chromosome>(newPopulation));
			chromoTiers.clear();
			chromoTiers = fastNonDominatedSort(oldPopulation);
			newPopulation.clear();
			int i = 0;
			while (newPopulation.size() + chromoTiers.get(i).size() <= (pMST+pEachK*Krange)) {
				crowdingDistanceAssignment(chromoTiers.get(i));
				newPopulation.addAll(chromoTiers.get(i));
				i++;
			}
			Collections.sort(chromoTiers.get(i), Chromosome.Comparators.CROWD);
			while (newPopulation.size() < (pMST+pEachK*Krange)) {
				newPopulation.add(chromoTiers.get(i).remove(0));
			}
			oldPopulation = new ArrayList<Chromosome>(newPopulation);
			newPopulation.clear();
			newPopulation = makeNewPop(oldPopulation, init);
			genCounter++;
		}
		System.out.println("Generating images and saving solutions...");
//		chromoTiers.clear();
		chromoTiers = fastNonDominatedSort(oldPopulation);
		double[] x = new double[chromoTiers.get(0).size()];
		double[] y = new double[chromoTiers.get(0).size()];
		double[] z = new double[chromoTiers.get(0).size()];
		int count = 0;
		for (Chromosome c : chromoTiers.get(0)) {
			x[count] = c.getObjectiveValue("devi");
			y[count] = c.getObjectiveValue("conn");
			z[count] = c.getObjectiveValue("edge");
			count++;
		}
		HashMap<String, String> objMap = new HashMap<String, String>();
		String xx = Arrays.toString(x);//devi
		String yy = Arrays.toString(y);//conn
		String zz = Arrays.toString(z);//edge
		System.out.println(xx);
		System.out.println(yy);
		System.out.println(zz);
		System.out.println(chromoTiers.get(0).size());
		objMap.put("devi", xx);
		objMap.put("conn", yy);
		objMap.put("edge", zz);
		if(this.objectives.length >2){
			String[] cmd = {
					"python",
					"C:\\Users\\Bendik\\git\\biatchlpleasesss\\paretoPlot.py",
					xx.substring(1,xx.length()-1),
					yy.substring(1,yy.length()-1),
					zz.substring(1, zz.length()-1),
			};
			Runtime.getRuntime().exec(cmd);
		}else{
			String[] cmd = {
					"python",
					"C:\\Users\\Bendik\\git\\biatchlpleasesss\\paretoPlot2D.py",
					this.objectives[0],
					this.objectives[1],
					objMap.get(this.objectives[0]).substring(1,xx.length()-1),
					objMap.get(this.objectives[1]).substring(1,xx.length()-1)
			};
			Runtime.getRuntime().exec(cmd);
		}
		List<Chromosome> topSols = new ArrayList<Chromosome>();
		int n = 0;
		boolean sol = false;
		Chromosome addedChromo;
		for (Chromosome chromosome : chromoTiers.get(0)) {
			if (chromosome.getSegments().size() == KmeanSens) {
				topSols.add(chromosome);
				sol = true;
				break;
			}
		}
		if (! sol) {
			for (Chromosome chromosome : chromoTiers.get(0)) {
				if(chromosome.getSegments().size() > KmeanSens){
					sh.mergeToLimit(chromosome, KmeanSens, objectives);
					topSols.add(chromosome);
					break;
				}
			}
		}
		if(!topSols.isEmpty())
			chromoTiers.get(0).remove(topSols.get(0));
		while (topSols.size() < 5 && !chromoTiers.get(0).isEmpty()) {
			topSols.add(chromoTiers.get(0).get(n));
			n++;
			if (n > chromoTiers.get(0).size() - 1) {
				break;
			}
		}

		for (int i = 0; i < topSols.size(); i++) {
			int limit = 20+r.nextInt(10);
			Chromosome c = topSols.get(i);
			sh.mergeToLimit(c, limit, this.objectives);
			System.out.println("Number of segments in solution: "+c.getSegments().size());
			pp.generateImage(c.getSegments(), (HashMap)c.getEdgeMap(), "saved"+i+".jpg");
			pp.generateBlackAndWhite(c.getSegments(), (HashMap)c.getEdgeMap(), "saved_BW_"+i+".jpg");
			String[] values = new String[objectives.length];
			for (int j = 0; j < objectives.length; j++) {
				values[j] = c.getObjectiveValue(objectives[j]) + "";
			}
			ImageDrawer.drawImage(pp.generateBufferedImage(c.getSegments(), c.getEdgeMap()), c.getSegments().size()+"", objectives, values);
			ImageDrawer.drawImage(pp.generateBufferedBlackAndWhite(c.getSegments(), c.getEdgeMap()), c.getSegments().size()+"", objectives, values);
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		//parameters
		String filename = "Test_image_2";
		String[] objectives = new String[] {"devi", "edge"}; //"devi", "edge", "conn"
		int KmeanSens = 17;
		int mstRemoveLimit = 2500; //edges to cut in MST
		int pMST = 0;
		int pEachK = 4;
		int Kstart = 3;
		int Krange = 10;
		int minSegmentSize = 170;
		int minSegApt = 20;
		int maxSegApt = 10;
		int maxGenerations = 70;
		int tourneySize = 2; //binary
		double mutateGene = 0.0001;
		double mutateSeg = 0.1;
		double crossover = 0.7;
		MasterEA m = new MasterEA(filename, crossover, mutateGene, mutateSeg, objectives, 
				tourneySize,  minSegmentSize);
		m.run(mstRemoveLimit, maxGenerations, KmeanSens, pEachK, Kstart, Krange, pMST);
		//MasterEA master = new MasterEA("Test_image");
		

	}
}

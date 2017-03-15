package genetics;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
	private List<List<Chromosome>> chromoTiers;
	private List<Chromosome> newPopulation;
	private List<Chromosome> combinedPopulation;
	
	private MasterEA(String filename) {
		//this.fh = new FileHandler(filename);
		this.objectives = new String[] {"devi", "edge", "conn"};
		this.fh = new FileHandler(filename);
		this.mst = new MinSpanTree(filename, fh);
		this.eu = new Euclidian(fh.getWidth(), fh.getHeight(), fh.getPixels());
		this.pp = new PicPrinter(fh, eu);
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
			List newQ = new ArrayList<Chromosome>(Q);
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
	
	public void run(int population, int removeLimit){
		ArrayList<Edge<Integer>> MST = (ArrayList<Edge<Integer>>) mst.getMSTPath();
		int[] genes = this.mst.getGenes(MST);
		List<int[]> pop = this.mst.generateGeneArrays(population, removeLimit, MST, genes);
		
//		System.out.println(mst.verts.size());
//		System.out.println(mst.edges.size());
//		for (Iterator<Vertex<Integer>> iterator = mst.verts.iterator(); iterator.hasNext();) {
//			Vertex<Integer> v = (Vertex<Integer>) iterator.next();
//			System.out.println(v.getEdges());
//		}
//		for (Iterator<Edge<Integer>> iterator = MST.iterator(); iterator.hasNext();) {
//			Edge<Integer> edge = (Edge<Integer>) iterator.next();
//			System.out.println(edge);
//		}
		
		System.out.println(genes.length);
//		for (int i = 0; i < genes.length; i++) {
//		System.out.print(genes[i]+" ");
//		}
//		System.out.println("next");
		System.out.println("chromosomes generated");
		SegmentHandler ss = new SegmentHandler(pop.get(pop.size()-1), fh, eu);
//		ss.updateSegments();
		List<HashSet<Integer>> seg = ss.calculateSegments();
		ss.mergeWithThreshold(seg, 500);
		Color centroid = eu.getRGBCentroid(seg.get(4));
		System.out.println("centroid: "+centroid+", pixels: "+seg.get(4).size());
		System.out.println("deviation: "+eu.getRGBdeviation(seg.get(4), centroid));
		System.out.println("# of segments: "+seg.size());
//		System.out.println("location: "+seg.get(4).iterator().next());
		pp.generateImage(seg);
		ImageDrawer.drawImage("saved.jpg");
	}
	
	public static void main(String[] args) {
		String filename = "Test_image";
		int population = 50;
		int mstRemoveLimit = 300;
		MasterEA m = new MasterEA(filename);
		m.run(population, mstRemoveLimit);
		//MasterEA master = new MasterEA("Test_image");
	}
}

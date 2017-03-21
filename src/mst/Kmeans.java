package mst;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import genetics.Chromosome;
import printPackage.ImageDrawer;
import printPackage.PicPrinter;
import printPackage.SegmentHandler;
import utils.Euclidian;

public class Kmeans {
	private Random r;
	private int width;
	private int height;
	private FileHandler fh;
	private Color[] RGBarray;
	private Euclidian eu;
//	private Color[] kArray;
	
	public Kmeans(FileHandler filehandler, Euclidian eu){
		this.fh = filehandler;
		this.r = new Random();
		this.width = fh.getWidth();
		this.height = fh.getHeight();
		this.RGBarray = fh.getListPixels();
		this.eu = eu;
	}
	
	private Color getMeanRGB(Color[] dataPoints){
		int[] mean = new int[3];
		for (int i = 0; i < dataPoints.length; i++) {
			Color point = this.RGBarray[i];
			mean[0] += point.getRed();
			mean[1] += point.getGreen();
			mean[2] += point.getBlue();
		}
		for (int i = 0; i < mean.length; i++) {
			mean[i] = mean[i]/dataPoints.length;
		}
		return new Color(mean[0], mean[1], mean[2]);
	}
	
	private Color getMeanRGB(List<Color> dataPoints){
		int[] mean = new int[3];
		for (Color c : dataPoints) {
			mean[0] += c.getRed();
			mean[1] += c.getGreen();
			mean[2] += c.getBlue();
		}
		for (int i = 0; i < mean.length; i++) {
			mean[i] = mean[i]/dataPoints.size();
		}
		return new Color(mean[0], mean[1], mean[2]);
	}
	
	private int[] getRGBdist(Color RGBone, Color RGBtwo){
		int[] dist = new int[3];
		int rOne = RGBone.getRed();
		int rTwo = RGBtwo.getRed();
		int gOne = RGBone.getGreen();
		int gTwo = RGBtwo.getGreen();
		int bOne = RGBone.getBlue();
		int bTwo = RGBtwo.getBlue();
		dist[0] = (int) (Math.sqrt(Math.pow((rTwo-rOne), 2)));
		dist[1] = (int) (Math.sqrt(Math.pow((gTwo-gOne), 2)));
		dist[2] = (int) (Math.sqrt(Math.pow((bTwo-bOne), 2)));
		return dist;
	}
	
	private Color updateRGB(Color pixelColor, Color centroid){
		int[] dist = getRGBdist(pixelColor, centroid);
		int red = centroid.getRed()+dist[0]/2;
		int green = centroid.getGreen()+dist[1]/2;
		int blue = centroid.getBlue()+dist[2]/2;
		return new Color(red, green ,blue);
	}
	
	private Color getRandomRGB(){
		return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	}
	
	public ArrayList<Integer> getKmeans(int k, int iterations){
		ArrayList<Integer> clusterList = new ArrayList<Integer>();
		ArrayList<Color> centroids = new ArrayList<Color>();
		HashMap<Integer, ArrayList<Color>> centroidMap = new HashMap<Integer, ArrayList<Color>>();
		
		//initialization
		Color picMean = getMeanRGB(this.RGBarray);
		for (int i = 0; i < k; i++) { //set centroids to mean value
			centroids.add(picMean);
			centroidMap.put(i, new ArrayList<Color>());
		}
		for (int i = 0; i < this.RGBarray.length; i++) { //assign random cluster
			int rand = r.nextInt(k);
			clusterList.add(rand);
			centroidMap.get(rand).add(this.RGBarray[i]);
		}
		
		//main loop
		int iter = 0;
		while(iter < iterations){
			for (int i = 0; i < centroids.size(); i++) {
				List<Color> centroidPoints = centroidMap.get(i);
				if(!centroidPoints.isEmpty())
					centroids.set(i, getMeanRGB(centroidPoints));
				centroidMap.put(i, new ArrayList<Color>());
			}
			for (int i = 0; i < RGBarray.length; i++) {
				double minDist = Double.MAX_VALUE;
				int bestCentroid = -1;
				for (int j = 0; j < centroids.size(); j++) {
					double newDist = Euclidian.getRGBEuclid(RGBarray[i], centroids.get(j));
					if(newDist < minDist){
						minDist = newDist;
						bestCentroid = j;
					}
				}
				clusterList.set(i, bestCentroid);
				centroidMap.get(bestCentroid).add(RGBarray[i]);
			}
			
			int sum = 0;
			for(List<Color> lit : centroidMap.values()){
				sum+=lit.size();
//				System.out.println(lit.size());
			}
//			System.out.println("total size: "+sum);
			iter++;
		}
		return clusterList;
	}
	
	
	private int getNextStartPoint(int[] visited) {
		for (int i = 0; i < visited.length; i++) {
			if(visited[i] != 1) {
				return i;
			}
		}
		return -1;
	}
	
	public int[] getKgenes(List<Integer> clusters) {
		int[] visited = new int[clusters.size()];
		int[] genes = new int[clusters.size()];
		int current = 0;
		List<Integer> open = new ArrayList<Integer>();
		int[] closed = new int[clusters.size()];
		while (current != -1) {
			open.add(current);
			visited[current] = 1;
			List<Integer> neighbors;
			while (!open.isEmpty()) {
				current = open.remove(0);
				closed[current] = 1;
				neighbors = this.eu.getNeighborNumbers(current);
				for (Integer neighbor : neighbors) {
					if (clusters.get(neighbor) == clusters.get(current) && closed[neighbor] != 1 && visited[neighbor] != 1) {
						genes[neighbor] = current;
						open.add(neighbor);
						visited[neighbor] = 1;
					}
				}
			}
			int last = current;
			current = getNextStartPoint(visited);
		}
		genes = fixZero(genes);
		return genes;
	}
	
	private int[] fixZero(int[] genes) {
		for (int i = 2; i < genes.length; i++) {
			if (genes[i] == 0) {
				genes[i] = i;
			}
		}
		return genes;
	}
	
	public static void main(String[] args) {
		FileHandler f = new FileHandler("Test_image_5");
//		ImageDrawer id = new ImageDrawer();
		Euclidian eu = new Euclidian(f.getWidth(), f.getHeight(), f.getPixels(), f.getListPixels());;
		SegmentHandler sh = new SegmentHandler(f, eu);
		PicPrinter pp = new PicPrinter(f, eu);
		Kmeans k = new Kmeans(f, eu);

		ArrayList<Integer> clusters = k.getKmeans(4, 20);
		System.out.println("Clusters size "+clusters.size());
		int[] genes = k.getKgenes(clusters);
		for (int i = 0; i < 20; i++) {
			System.out.println(genes[i]);
		}
		Chromosome c = new Chromosome(genes, eu, sh);
		c.updateAll(new String[] {"devi", "edge", "conn"}, 50, true);
		ImageDrawer.drawImage(pp.generateBufferedImage(c.getSegments(), c.getEdgeMap()));
		System.out.println(clusters);
		System.out.println(clusters.size());
		ImageDrawer.drawImage(pp.genKmeansImg(clusters));
		
	}
	
	
	
	
	

}

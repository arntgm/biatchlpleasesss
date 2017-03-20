package mst;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import printPackage.ImageDrawer;
import printPackage.PicPrinter;
import utils.Euclidian;

public class Kmeans {
	private Random r;
	private int width;
	private int height;
	private FileHandler fh;
	private Color[] RGBarray;
//	private Color[] kArray;
	
	public Kmeans(FileHandler filehandler){
		this.fh = filehandler;
		this.r = new Random();
		this.width = fh.getWidth();
		this.height = fh.getHeight();
		this.RGBarray = fh.getListPixels();
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
				System.out.println(lit.size());
			}
			System.out.println("total size: "+sum);
			iter++;
		}
		
		return clusterList;
	}
	
	public static void main(String[] args) {
		FileHandler f = new FileHandler("Test_image_3");
//		ImageDrawer id = new ImageDrawer();
		Euclidian eu = new Euclidian(f.getWidth(), f.getHeight(), f.getPixels(), f.getListPixels());
		PicPrinter pp = new PicPrinter(f, eu);
		Kmeans k = new Kmeans(f);
		ArrayList<Integer> clusters = k.getKmeans(3, 20);
		System.out.println(clusters.size());
		ImageDrawer.drawImage(pp.genKmeansImg(clusters));
	}
	
	
	
	
	

}
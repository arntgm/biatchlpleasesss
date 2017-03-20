package printPackage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import mst.FileHandler;
import utils.Euclidian;

public class PicPrinter {
	
	private Integer[] neighborArray;
	private FileHandler fh;
	private Euclidian eu;
	private Color green;
	
	//used to take argument List<HashSet<Integer>> segments, 
	public PicPrinter(FileHandler fh, Euclidian eu) {
		this.fh = fh;
//		generateImage(segments, fh);
		this.eu = eu;
		this.green = new Color(0, 255, 0);
	}
	

	
	public BufferedImage generateBufferedImage(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgePoints){
		Color[][] pixels = fh.getPixels();
		for (HashSet<Integer> segment : segments) {
			if(edgePoints!=null && edgePoints.containsKey(segment)){	
				if(!edgePoints.get(segment).isEmpty()){
					
					for (Integer integer : edgePoints.get(segment)) {
						int[] coords = eu.toGridCoords(integer);
						pixels[coords[0]][coords[1]] = this.green;
					}
				}
			}
		}
		return fh.generateBufferedImage(pixels);
	}
	
	public BufferedImage genKmeansImg(List<Integer> centr){
		Color[][] pixels = fh.getPixels();
		Color[] cols = new Color[7];
		cols[0] = new Color(255, 0, 0);
		cols[1] = new Color(0,0, 255);
		cols[2] = new Color(0, 0, 0);
		cols[3] = new Color(255,255,255);
		cols[4] = new Color(100, 100, 100);
		cols[5] = new Color(255, 150, 150);
		cols[6] = new Color(0,255,0);
		for (int i = 0; i < centr.size(); i++) {
			int inte = centr.get(i);
//			System.out.println(centr.indexOf(inte));
			int[] coords = eu.toGridCoords(i);
			pixels[coords[0]][coords[1]] = cols[inte];
		}
		return fh.generateBufferedImage(pixels);
	}
	
	
	public void generateImage(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgePoints, String filename){
		Color[][] pixels = fh.getPixels();
		for (HashSet<Integer> segment : segments) {
			if(edgePoints!=null && edgePoints.containsKey(segment)){	
				if(!edgePoints.get(segment).isEmpty()){
					
					for (Integer integer : edgePoints.get(segment)) {
						int[] coords = eu.toGridCoords(integer);
						pixels[coords[0]][coords[1]] = this.green;
					}
				}
			}
		}
		fh.saveNewImage(pixels, filename);
	}
	
	
	public void generateImage(List<HashSet<Integer>> segments, ArrayList<HashSet<Integer>> toSeg, FileHandler fh, String filename) {
		System.out.println("Generating image");
		Color[][] pixels = fh.getPixels();
		int height = fh.getHeight();
		int width = fh.getWidth();
//		System.out.println(height);
//		System.out.println(width);
		int t = 0;
		Color green = new Color(0, 255, 0);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				HashSet<Integer> segment = utils.Euclidian.getSegment(toSeg, t);
				if (i != 0) {
					if (! segment.contains(t-width)) {
						pixels[i][j] = green;
					}
				}
				if (i != height-1) {
					if (! segment.contains(t+width)) {
						pixels[i][j] = green;
					}
				}
				if (j != 0) {
					if (! segment.contains(t-1)) {
						pixels[i][j] = green;
					}
				}
				if (j != width - 1) {
					if (! segment.contains(t+1)) {
						pixels[i][j] = green;
					}
				}
				t += 1;
			}
		}
		fh.saveNewImage(pixels, filename);
	}

	
//	public static void main(String[] args) {
//		FileHandler fh = new FileHandler("mini");
//		Integer[] n = {1,2,3,8,4,6,7,12,13,4,15,10,11,14,9,16,17,18,19,24,20,20,21,22,23};
//		PicPrinter pp = new PicPrinter(n, fh);
//	}
}

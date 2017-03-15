package printPackage;

import java.awt.Color;
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
	
	public PicPrinter(List<HashSet<Integer>> segments, FileHandler fh, Euclidian eu) {
		this.fh = fh;
//		generateImage(segments, fh);
		this.eu = eu;
		this.green = new Color(0, 255, 0);
	}
	

	
	
	//if change segment to hold set with edges, no need to calculate here
	public void generateImage(List<HashSet<Integer>> segments, HashMap<HashSet<Integer>, HashSet<Integer>> edgePoints){
		Color[][] pixels = fh.getPixels();
		for (HashSet<Integer> segment : segments) {
			for (Integer integer : edgePoints.get(segment)) {
				int[] coords = eu.toGridCoords(integer);
				pixels[coords[0]][coords[1]] = this.green;
			}
		}
		fh.saveNewImage(pixels);
	}
	
	
	public void generateImage(List<HashSet<Integer>> segments, FileHandler fh) {
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
				HashSet<Integer> segment = utils.Euclidian.getSegment(segments, t);
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
		fh.saveNewImage(pixels);
	}

	
//	public static void main(String[] args) {
//		FileHandler fh = new FileHandler("mini");
//		Integer[] n = {1,2,3,8,4,6,7,12,13,4,15,10,11,14,9,16,17,18,19,24,20,20,21,22,23};
//		PicPrinter pp = new PicPrinter(n, fh);
//	}
}

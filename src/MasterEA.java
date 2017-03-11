import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class MasterEA {
	
	private FileHandler fh;
	private Color[][] newColor;
	private String[] objectives;
	
	private MasterEA(String filename) {
		//this.fh = new FileHandler(filename);
		init();
	}
	
	private void init() {
		//newColor = fh.getPixels();
		//run();
	}
	
	private void run() {
		for (int i = 0; i < fh.getWidth(); i++) {
			for (int j = 50; j < 60; j++) {
				newColor[j][i] = new Color(255, 255, 255);
			}
		}
		fh.saveNewImage(newColor);
	}
	
	private void crowdingDistanceAssignment(ArrayList<Chromosome> F) {
		int l = F.size();
		for (Chromosome chromosome : F) {
			chromosome.setCrowdDist(0);
		}
		for (String objective : objectives) {
			sortByObjective(F,objective);
		}
	}
	
	private void sortByObjective(ArrayList<Chromosome> F, String objective) {
		int l = F.size();
		ArrayList<Chromosome> subA = new ArrayList<Chromosome>(F.subList(0, l/2));
		ArrayList<Chromosome> subB = new ArrayList<Chromosome>(F.subList(l/2, l));
		subA.remove(2);
	}
	
	public static void main(String[] args) {
		//MasterEA master = new MasterEA("Test_image");
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(2);
		a.add(5);
		a.add(19);
		a.add(6);
		a.add(1);
		System.out.println(a);
		int l = a.size();
		List<Integer> subA = a.subList(0, l/2);
		List<Integer> subB = a.subList(l/2, l);
		System.out.println(subA);
		System.out.println(subB);
		Collections.sort(subA);
		Collections.sort(subB);
		System.out.println(a);
	}
}

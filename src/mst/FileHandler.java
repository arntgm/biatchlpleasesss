package mst;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;


public class FileHandler {
	
	private Color [][] pixels;
	private Color[] listPixels;
	private int width;
	private int height;


	public FileHandler(String filename) {
		BufferedImage image = null;
	    try {
	    	image = ImageIO.read(new File(filename+".jpg"));
		    this.height = image.getHeight();
		    this.width = image.getWidth();
		    this.pixels = new Color[height][width];
		    this.listPixels = new Color[height*width];
		    setPixels(image);
		    for (int i = 0; i < this.height; i++) {
				for (int j = 0; j < this.width; j++) {
					this.listPixels[i*this.width+j] = this.pixels[i][j];
//					System.out.println(this.listPixels[i+j*i*this.width]);
				}
			}
		    } catch (IOException e) {
		    	System.err.println(e.getMessage());
		    }
	    }
	
	public Color[][] getPixels(){
		Color[][] newPixels = new Color[this.height][this.width];
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				newPixels[i][j] = new Color(this.pixels[i][j].getRGB());
			}
		}
		return newPixels;
	}
	
	public Color[] getListPixels(){
		return this.listPixels;
	}
	
	public Color getRGB(int x, int y){
		return this.pixels[x][y];
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public BufferedImage generateBufferedImage(Color[][] pixels){
			BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < this.height; i++) {
				for (int j = 0; j < this.width; j++) {
					image.setRGB(j,i,pixels[i][j].getRGB());
				}
			}
			return image;
	}
	
	public void saveNewImage(Color[][] newPixels, String filename){
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				image.setRGB(j,i,newPixels[i][j].getRGB());
			}
		}
		File outputfile = new File(filename);
		try {
			ImageIO.write(image, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void setPixels(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color color = new Color(image.getRGB(j,i));
				pixels[i][j] = color;
			}
		}
		for (int i = 0; i < image.getHeight(); i++) {
			//System.out.println("Row "+i);
			for (int j = 0; j < image.getWidth(); j++) {
				//System.out.println(pixels[i][j].toString());
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		int[] x = new int[]{1,2,3};
//		List<Integer> x = new ArrayList<Integer>();
//		x.add(1);
//		x.add(2);
//		x.add(3);
		int[] y = new int[]{2,3,4};
		int[] z = new int[]{6,7,8};
		String xx = Arrays.toString(x);
		String yy = Arrays.toString(y);
		String zz = Arrays.toString(z);
		System.out.println(Arrays.toString(x));
		String[] cmd = {
			      "python",
			      "C:\\Users\\Bendik\\git\\biatchlpleasesss\\paretoPlot.py", //paretoPlot
			      xx.substring(1, xx.length()-1),
			      yy.substring(1,yy.length()-1),
			      zz.substring(1,zz.length()-1),
			    };
			    Runtime.getRuntime().exec(cmd);
	}

}

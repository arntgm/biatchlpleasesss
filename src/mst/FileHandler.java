package mst;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
		return this.pixels;
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
	
	public void saveNewImage(Color[][] newPixels){
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				image.setRGB(j,i,newPixels[i][j].getRGB());
			}
		}
		File outputfile = new File("saved.jpg");
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
	
	public static void main(String[] args) {
		FileHandler fh = new FileHandler("Test_image");
		System.out.println(fh.getHeight());
	}

}

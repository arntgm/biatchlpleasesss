package printPackage;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ImageDrawer {	    
		private ImageDrawer() {}
	
	    public static void drawImage(String filename){
	        BufferedImage img;
			try {
				img = ImageIO.read(new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
	        ImageIcon icon=new ImageIcon(img);
	        JFrame frame=new JFrame();
	        frame.setLayout(new FlowLayout());
	        frame.setSize(img.getWidth(), img.getHeight()+100);;
	        JLabel lbl=new JLabel();
	        lbl.setText(filename);
	        lbl.setIcon(icon);
	        frame.add(lbl);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    
	    public static void drawImage(BufferedImage img, String nrOfSegs, String[] objectives, String[] values){
//	        BufferedImage img;
	        ImageIcon icon=new ImageIcon(img);
	        JFrame frame=new JFrame();
	        frame.setLayout(new FlowLayout());
	        frame.setSize(img.getWidth(), img.getHeight()+100);;
	        JLabel lbl=new JLabel();
	        String imageText = "Segments: " + nrOfSegs;
	        
	        for (int i = 0; i < values.length; i++) {
				imageText += "     " + objectives[i] + ": " + values[i];
			}
	        lbl.setText(imageText);
	        lbl.setVerticalTextPosition(SwingConstants.BOTTOM);
	        lbl.setHorizontalTextPosition(SwingConstants.CENTER);
	        lbl.setIcon(icon);
	        frame.add(lbl);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    
	    public static void main(String[] args) {
			ImageDrawer i = new ImageDrawer();
			i.drawImage("saved0.jpg");
		}
	    
}

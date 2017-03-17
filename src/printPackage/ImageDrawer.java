package printPackage;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

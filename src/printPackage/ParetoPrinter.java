package printPackage;
import javax.swing.JFrame;

import org.math.plot.*;
public class ParetoPrinter {
	public ParetoPrinter(){}
	
	public static void main(String[] args) {
		  
		  double[][] x = {3.0, 4.9, 4.5, 6.0};
		  double[] y = {4.1, 6.0};
		 
		  // create your PlotPanel (you can use it as a JPanel)
		  Plot3DPanel plot = new Plot3DPanel();
		 
		  // add a line plot to the PlotPanel
		  plot.addLinePlot("my plot", x, y);
		 
		  // put the PlotPanel in a JFrame, as a JPanel
		  JFrame frame = new JFrame("a plot panel");
		  frame.setContentPane(plot);
		  frame.setVisible(true);
	}
}

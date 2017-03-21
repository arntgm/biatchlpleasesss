package printPackage;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.Mapper;
import net.ericaro.surfaceplotter.surface.AbstractSurfaceModel;
import net.ericaro.surfaceplotter.surface.ArraySurfaceModel;
import net.ericaro.surfaceplotter.surface.SurfaceVertex;
import genetics.Chromosome;

public class ParetoFront {

	public void visualizeFront(List<Chromosome> front) {
		JSurfacePanel jsp = new JSurfacePanel();
		jsp.setTitleText("Pareto Front");

		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(jsp, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);
		
		float maxDevi = (float) Double.MIN_VALUE;
		float maxConn = (float) Double.MIN_VALUE;
		float minDevi = (float) Double.MAX_VALUE;
		float minConn = (float) Double.MAX_VALUE;

		for (Chromosome c : front) {
			float devi = (float) c.getObjectiveValue("devi");
			float conn = (float) c.getObjectiveValue("conn");
			if(devi > maxDevi){
				maxDevi = devi;
			}
			if(conn > maxConn){
				maxConn = conn;
			}
			if(devi < minDevi){
				minDevi = devi;
			}
			if(conn < minConn){
				minConn = conn;
			}
		}
		System.out.println(maxConn);
		System.out.println(maxDevi);
		float[][] z1 = new float[(int) Math.ceil(maxDevi)][(int)Math.ceil(maxConn)];
		for (Chromosome c : front) {
			int x = (int) Math.ceil(c.getObjectiveValue("devi"));
			int y = (int) Math.ceil(c.getObjectiveValue("conn"));
			float z = (float) Math.ceil(c.getObjectiveValue("edge"));
			z1[x][y] = z;
		}
		Random rand = new Random();
//		float[][] z2 = new float[max][max];
//				z2[i][j] = rand.nextFloat() * 20 - 10f;
		ArraySurfaceModel sm = new ArraySurfaceModel();
		sm.setValues(minDevi,maxDevi,minConn,maxConn, front.size(), z1, null);
		jsp.setModel(sm);
		// sm.doRotate();

		// canvas.doPrint();
		// sm.doCompute();
	}

	public static float f1(float x, float y) {
		// System.out.print('.');
		return (float) (Math.sin(x * x + y * y) / (x * x + y * y));
		// return (float)(10*x*x+5*y*y+8*x*y -5*x+3*y);
	}

	public static float f2(float x, float y) {
		return (float) (Math.sin(x * x - y * y) / (x * x + y * y));
		// return (float)(10*x*x+5*y*y+15*x*y-2*x-y);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				new ParetoFront().visualizeFront(null);
			}
		});

	}

}
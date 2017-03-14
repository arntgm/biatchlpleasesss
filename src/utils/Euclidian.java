package utils;

import java.awt.Color;

public class Euclidian {
	
	public static float getRGBEuclid(Color RGBone, Color RGBtwo){
		
		int rOne = RGBone.getRed();
		int rTwo = RGBtwo.getRed();
		int gOne = RGBone.getGreen();
		int gTwo = RGBtwo.getGreen();
		int bOne = RGBone.getBlue();
		int bTwo = RGBtwo.getBlue();
		return (float)Math.sqrt(Math.pow((rTwo-rOne), 2) + Math.pow((gTwo-gOne),2) + Math.pow((bTwo-bOne),2));
	}

}

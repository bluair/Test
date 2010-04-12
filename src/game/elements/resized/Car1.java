package game.elements.resized;

import javax.microedition.lcdui.Image;

import game.Images;
import game.elements.Road;

public class Car1 extends Bus1{

	public Car1(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		this.images = Images.imgsCar1;
	}
	

	public int getSequence() {
		return (int) -((System.currentTimeMillis() - startTime) / 250);
	}
	
	
	public void computePaintY() {
		super.computePaintY();

	}
	
	public void computePaintX() {
		x = road.getX() + ((y - road.getY()) * 8) / 10;
	}
	
	
	public Image getBestImage() {
		int dif = y - roadY;
		if (dif > 8) {
			int ret = Math.min(images.length - 1, Math.max(0,(dif / 13)));

			currImg = images[ret];
			return currImg;

		} else {
			currImg = null;
			return null;
		}
	}	

}

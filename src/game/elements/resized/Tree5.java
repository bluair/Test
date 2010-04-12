package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Tree5 extends ResizingElement{
	
	public Tree5(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		images = Images.imgsTree5;

	}

	public void computePaintY() {
		super.computePaintY();
		//x = road.getEdgeLeft(Math.max(0, (y - 1) - roadY);
		x = road.getX()+((y-road.getY())*18)/10;
	}

	public void paintResized(Graphics g, short xx) {
		computePaintY();
		Image img = getBestImage();
		if (img != null){
			this.setImage(img, img.getWidth(), img.getHeight());
			this.setPosition(x-getWidth(), y-getHeight());
			super.paint(g);
		}
	}

	public Image getBestImage() {
		int dif = y - roadY;
		if (dif > 1) {

			int ret = Math.min(images.length - 1, Math.max(0, Math.min(9, (dif / 10))));

			currImg = images[ret];
			return currImg;

		} else {
			currImg = null;
			return null;
		}
	}

	public int getCollisionMaxSequences() {
		return 7;
	}
	
	public int getCollisionMoveBack(int speed) {
		return 0;//Math.max(1, speed / 20);
	}
	
	public int getCollisionMove(int speed) {
		return speed / 10;
	}

	public int getCollisionSlow(int speed) {
		return speed / 10;
	}

	public int getCollisionRandom(int speed) {
		return 1;
	}
}

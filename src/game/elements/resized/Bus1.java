package game.elements.resized;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import game.Images;
import game.elements.Road;

public class Bus1 extends ResizingElement {

	public Bus1(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		images = Images.imgsBus6;

	}

	public void computePaintY() {
		super.computePaintY();

		//x = road.getX();// - ((y - road.getY()) * 8) / 10;

	}

	public void computePaintX() {

		x = road.getX();// - ((y - road.getY()) * 8) / 10;

	}
	

	public void paintResized(Graphics g, short xx) {
		computePaintY();
		computePaintX();
		Image img = getBestImage();
		if (img != null) {
			this.setImage(img, img.getWidth(), img.getHeight());
			this.setPosition(x - getWidth(), y - getHeight());
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

	// public int getCollisionMove(int speed) {
	// return -20;
	// }

	public int getCollisionMoveBack(int speed) {
		return Math.max(1, speed / 20);
	}

	public int getCollisionSlow(int speed) {
		return 100;
	}

	public boolean collidesX(Sprite res) {

		return collidesWith(res, true);

	}
	
	public int getSequence() {
		return (int) +((System.currentTimeMillis() - startTime) / 300);
	}

}

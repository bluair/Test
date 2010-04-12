package game.elements.resized;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import game.Images;
import game.elements.Road;

public class Oil4 extends ResizingElement {

	public static final int OIL_LEFT = 0;
	public static final int OIL_RIGHT = 1;
	public static final int OIL_CENTER = 2;

	private int type;

	public Oil4(Road road, short maxVisibleSequences, int type) {
		super(road, maxVisibleSequences);
		images = Images.imgsOil4;
		this.type = type;

	}

	public void computePaintY() {
		super.computePaintY();
		// x = road.getEdgeLeft(Math.max(0, (y - 1) - roadY);
		switch (type) {
		case OIL_LEFT:
			x = road.getX() - ((y - road.getY()) * 1) / 10;
			break;
		case OIL_RIGHT:
			x = road.getX() + ((y - road.getY()) * 8) / 10;
			break;
		case OIL_CENTER:
			x = road.getX();// - ((y - road.getY()) * 8) / 10;
			break;
		}
		
	}

	public void paintResized(Graphics g, short xx) {
		computePaintY();
		Image img = getBestImage();
		if (img != null) {
			this.setImage(img, img.getWidth(), img.getHeight());
			this.setPosition(x - getWidth(), y - getHeight());
			super.paint(g);
		}
	}

	public Image getBestImage() {
		int dif = y - roadY;
		if (dif > 8) {
			int ret = Math.min(images.length - 1, Math.max(0, Math.min(9, (dif / 14))));

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
	
	public int getCollisionMove(int speed) {
		return speed / 20;
	}

	public int getCollisionSlow(int speed) {
		return speed / 5;	
	}

	public int getCollisionRandom(int speed) {
		return 1;
	}

}

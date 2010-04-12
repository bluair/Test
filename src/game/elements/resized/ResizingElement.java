package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class ResizingElement extends Sprite implements ResizingElementInterface {
	Image[] images;
	Road road;
	int x, y;
	int roadHeight;
	int roadY;
	int speed = 50;
	long startTime;

	int sequencesLoop = Images.imgsRoad.length;
	short sequenceDifference;
	int maxVisibleSequences;
	Image currImg;

	/* compute y positon and which size img we will use */
	public void computePaintY() {
		y = roadY
				+ (((roadHeight - 1) * (maxVisibleSequences - sequenceDifference) * (maxVisibleSequences - sequenceDifference)) / (maxVisibleSequences * maxVisibleSequences));
	}

	public boolean collidesX(Sprite res) {
		if (currImg != null) {
			if (sequenceDifference < getCollisionMaxSequences()) {
				return collidesWith(res, true);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Image getBestImage() {
		int dif = y - roadY;
		if (dif > 4) {
			int roadWidth = road.getWidth(y - roadY);
			int ret = 0;
			for (ret = images.length - 1; ret >= 0; ret--) {

				if (roadWidth < images[ret].getWidth()) {
					break;
				}
			}
			if (ret >= 0) {
				currImg = images[ret];
				return images[ret];
			} else {
				currImg = null;
				return null;
			}
		} else {
			currImg = null;
			return null;
		}
	}

	public ResizingElement(Road road, short maxVisibleSequences) {
		super(Images.pixelSprite);
		this.road = road;
		this.maxVisibleSequences = maxVisibleSequences;
		y = road.getY();
		roadY = road.getY();
		roadHeight = road.getHeight();
		startTime = System.currentTimeMillis();
	}

	public void setSequenceDifference(short sequenceDifference) {
		this.sequenceDifference = sequenceDifference;
	}

	public int getCollisionMaxSequences() {
		return 5;
	}

	public int getSequence() {
		return (int) -((System.currentTimeMillis() - startTime) / 1000);
	}

	public int getCollisionMove(int speed) {
		return 0;
	}
	
	public int getCollisionMoveBack(int speed) {
		return 0;
	}

	public int getCollisionSlow(int speed) {
		return 0;
	}

	public int getCollisionRandom(int speed) {
		return 0;
	}

	// don't remove!!!
	public void paintResized(Graphics g, short x) {

	}

	public void startTime() {
		startTime = System.currentTimeMillis();
	}

}

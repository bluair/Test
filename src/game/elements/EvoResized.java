package game.elements;

import game.Images;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class EvoResized extends Evo {

	Image imgResized[][];
	int y, roadY, roadHeight, maxVisibleSequences, sequenceDifference;
	int lastPicture, resizedImages;

	Road road;
	private int lastX;
	int imagePerspective = 0;
	int lastPerpsective = 0;

	public EvoResized(Road road, int color, short maxVisibleSequences) {
		super(road, color);
		this.maxVisibleSequences = maxVisibleSequences;
		roadY = road.getY();

		roadHeight = road.getHeight();
		imgResized = Images.getEvoResized(color);
		lastPicture = -1;
		resizedImages = imgResized[0].length;
		this.road = road;
	}

	/* compute y positon and which size img we will use */

	public void computePaint() {
		y = roadY
				+ 8
				+ ((((41*roadHeight)/100) * (maxVisibleSequences - sequenceDifference) * (maxVisibleSequences - sequenceDifference)) / (maxVisibleSequences * maxVisibleSequences));
	}

	public void paintResized(Graphics g) {
		if (sequenceDifference < maxVisibleSequences && sequenceDifference > -8) {
			computePaint();

			int currPict = Math.min(resizedImages - 1, Math.max(0, (y - (roadY + 8)) / 5));

			if (currPict != lastPicture || lastPerpsective != imagePerspective) {
				lastPicture = currPict;
				lastPerpsective = imagePerspective;
				this.setImage(imgResized[imagePerspective][currPict], imgResized[imagePerspective][currPict]
						.getWidth(), imgResized[imagePerspective][currPict].getHeight());
			}
			this.setPosition(this.getX() + getWidth() / 2, y);

			super.paint(g);
		}
	}

	public synchronized void changePosition(int roadCenter, int xx, int seqDiff) {
		sequenceDifference = seqDiff;
		changePosition(roadCenter, xx);
	}

	public synchronized void changePosition(int roadCenter, int xx) {
		this.roadCenter = (short) roadCenter;
		this.lastX = xx;
		computePaint();
		setPosition();
	}
	
	
	public int getSequenceDifference(){
		return sequenceDifference;
	}

	public synchronized void setSequenceDifference(int seqDiff) {

		sequenceDifference = seqDiff;
		computePaint();
		setPosition();

	}

	private void setPosition() {
		int xPlus;
		xPlus = roadCenter + (lastX * road.getWidth(Math.max(0, y - road.getY()))) / 150;
		lastPosition = xPlus;
		setPosition(xPlus, y);
	}

	public void changePosition(int roadCenter) {
		changePosition(roadCenter, lastX);

	}

	public void setImageIndex(byte persp) {
		this.imagePerspective = persp;

	}

}

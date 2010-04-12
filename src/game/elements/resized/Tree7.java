package game.elements.resized;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import game.Images;
import game.elements.Road;

public class Tree7 extends Tree5 {

	public static final int TREE_LEFT = 0;
	public static final int TREE_RIGHT = 1;
	public static final int TREE_CENTER = 2;
	private int type;

	public Tree7(Road road, short maxVisibleSequences,int type) {
		super(road, maxVisibleSequences);
		images = Images.imgsTree7;
		this.type = type;
	}

	public void computePaintY() {
		super.computePaintY();
		// x = road.getEdgeLeft(Math.max(0, (y - 1) - roadY);
		switch (type) {
		case TREE_LEFT:
			x = road.getX() - ((y - road.getY()) * 16) / 10;
			break;
		case TREE_RIGHT:
			x = road.getX() + ((y - road.getY()) * 20) / 10;
			break;

		}

	}
	
	public int getCollisionMoveBack(int speed) {
		return 0;
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
	

	public int getCollisionMove(int speed) {
		return 0;
	}

	public int getCollisionSlow(int speed) {
		return 0;
	}

	public int getCollisionRandom(int speed) {
		return 0;
	}

}

package game.elements.resized;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import game.Images;
import game.elements.Road;

public class Brick3 extends ResizingElement {

	public Brick3(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		images = Images.imgsBrick3;

	}

	public void computePaintY() {
		super.computePaintY();
		if (road.getHeight() == 100) {
			x = 160 - road.getEdgeLeft(Math.max(0, (y - 1) - roadY));
		} else {
			x = 160 - road.getEdgeLeft(Math.max(0, (y - 1) - roadY));
		}
	}

	public void paintResized(Graphics g, short xx) {
		if (Images.porting != Images.SMALL) {
			computePaintY();
			Image img = getBestImage();
			if (img != null) {
				this.setImage(img, img.getWidth(), img.getHeight());
				this.setPosition(xx - (x * 11) / 10, y - getHeight());
				super.paint(g);
			}
		}
	}

	public Image getBestImage() {
		int dif = y - roadY;
		if (dif > 5) {

			int ret = Math.min(images.length - 1, Math.max(0, Math.min(images.length - 1, (dif / 8))));

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
}

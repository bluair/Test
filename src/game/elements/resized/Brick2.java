package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Brick2 extends ResizingElement {

	public Brick2(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		images = Images.imgsBrick2;

	}

	public void paintResized(Graphics g, short x) {
		computePaintY();
		Image img = getBestImage();
		if (img != null)
			g.drawImage(img, x, y, Graphics.HCENTER | Graphics.BOTTOM);
	}
	
	public int getCollisionMaxSequences() {
		return 4;
	}
	
	
	public Image getBestImage() {
		//return images[0];
		int dif = y-roadY;
		if (dif>1){
		int roadWidth = road.getWidth(y - roadY);
		int ret = 0;
		for (ret = 0;ret<images.length; ret++) {

			if ((roadWidth*9)/10 < images[ret].getWidth()) {
				break;
			}
		}
		//System.out.println("imidz            "+ret);
		if (ret <images.length)
			return images[ret];
		else
			return null;
		}else{
			return null;
		}
	}
	
//	public Image getBestImage() {
//		int dif = y - roadY;
//		if (dif > 5) {
//
//			int ret = Math.min(images.length - 1, Math.max(0, Math.min(images.length-1, (dif / 5))));
//
//			currImg = images[ret];
//			return currImg;
//
//		} else {
//			currImg = null;
//			return null;
//		}
//	}
}

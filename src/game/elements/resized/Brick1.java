package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Brick1 extends ResizingElement {

	public Brick1(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		//images = Images.imgsBrick1;

	}

	public void paintResized(Graphics g, short x) {
		computePaintY();

		Image img = getBestImage();
		currImg = img;
		if (img != null){
			this.setImage(img, img.getWidth(), img.getHeight());
			this.setPosition(x-img.getWidth()/2, y-img.getHeight());
			super.paint(g);
			//g.drawImage(img, x, y, Graphics.HCENTER | Graphics.BOTTOM);
		}
	}
	
	public Image getBestImage() {
		//return images[0];
		int dif = y-roadY;
		if (dif>4){
		int roadWidth = road.getWidth(y - roadY);
		int ret = 0;
		for (ret = images.length - 1; ret >= 0; ret--) {

			if ((roadWidth*9)/10 < images[ret].getWidth()) {
				break;
			}
		}
		//System.out.println("imidz            "+ret);
		if (ret >= 0)
			return images[ret];
		else
			return null;
		}else{
			return null;
		}
	}

	public int getCollisionMaxSequences() {
		return 4;
	}

}

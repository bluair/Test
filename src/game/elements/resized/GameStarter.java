package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class GameStarter {

	Image[] imgs;
	int currImage;
	int x, y;

	public GameStarter(Road road) {
		this.imgs = Images.imgsGameStarter;
		currImage = imgs.length - 1;
		this.x = road.getXCenter();
		this.y = road.getY();
	}

	public void paint(Graphics g) {
		System.out.println("imi "+currImage);
		g.drawImage(imgs[currImage], x, y, Graphics.HCENTER | Graphics.TOP);
		if (currImage > 0) {
			currImage--;
		}
	}

}

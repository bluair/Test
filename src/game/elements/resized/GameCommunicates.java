package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class GameCommunicates {

	public static final int COMM_WAITING_FOR_OPPONENT = 1;
	public static final int COMM_PUSH_BUTTON = 2;

	Image[] imgs;
	int x, y;
	int commType;

	public GameCommunicates(Road road) {
		this.imgs = Images.imgsGameCommunicates;
		this.x = road.getXCenter();
		this.y = road.getY() + (road.getHeight()) / 2;
	}

	public void setEndGame(int type) {
		commType = type;

	}

	public void paint(Graphics g) {

		switch (commType) {
		case COMM_PUSH_BUTTON:
			g.drawImage(imgs[0], x, y, Graphics.HCENTER | Graphics.VCENTER);
			break;
		case COMM_WAITING_FOR_OPPONENT:
			g.drawImage(imgs[1], x, y, Graphics.HCENTER | Graphics.VCENTER);
			break;
		}

	}
}

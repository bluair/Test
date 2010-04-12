package game.elements.resized;

import game.Images;
import game.elements.Road;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class GameEnder {

	public static final int GAME_WIN = 1;
	public static final int GAME_LOOSE = 2;
	public static final int GAME_TRY_AGAIN = 3;
	public static final int GAME_GOOD_RESULT = 4;

	Image[] imgs;
	int x, y;
	int endType;

	public GameEnder(Road road,int type) {
		this.imgs = Images.imgsGameEnder;
		this.x = road.getXCenter();
		this.y = road.getY()+(road.getHeight())/2;
		setEndGame(type);
	}

	public void setEndGame(int type) {
		endType = type;

	}

	public void paint(Graphics g) {

		switch (endType) {
		case GAME_WIN:
			g.drawImage(imgs[0], x, y, Graphics.HCENTER | Graphics.VCENTER);
			break;
		case GAME_LOOSE:
			g.drawImage(imgs[1], x, y, Graphics.HCENTER | Graphics.VCENTER);
			break;
		case GAME_TRY_AGAIN:
			g.drawImage(imgs[2], x, y, Graphics.HCENTER | Graphics.VCENTER);
			break;
		case GAME_GOOD_RESULT:
			g.drawImage(imgs[3], x, y, Graphics.HCENTER | Graphics.VCENTER);
			break;
		}

	}
}

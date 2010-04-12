package game.elements;

import game.Images;
import game.screens.Pit2;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Background {

	private Image imgBackgr;
	private short xx, yy;

	public Background(short x, short y, int level) {

		if (level == 0)
			imgBackgr = Images.imgBacgroundDay;
		else
			imgBackgr = Images.imgBacgroundNight;

		this.xx = x;
		this.yy = y;
	}

	public void move(short right) {
		xx += right;
	}

	public void paint(Graphics g) {
		//System.out.println("bpaint "+xx);
		g.drawImage(imgBackgr, xx, yy, Graphics.HCENTER | Graphics.TOP);
	}

	public void setLevel(byte b) {
		if (b == 0)
			imgBackgr = Images.imgBacgroundDay;
		else
			imgBackgr = Images.imgBacgroundNight;
		
	}
}

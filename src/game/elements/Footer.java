package game.elements;

import game.Images;
import game.screens.Pit2;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Footer {

	Image imgFooter;
	int x, y;

	public Footer(int x, int y) {
		imgFooter = Images.imgFoot;
		this.x = x;
		this.y = y;
	//	System.out.println("x "+x);
//		if (x<160)
//			this.x = 110;
	}

	public void paint(Graphics g) {
		
		g.drawImage(imgFooter, x, y, Graphics.HCENTER | Graphics.TOP);
	}

	public int getY(){
		return y;
	}
	
}

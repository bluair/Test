package game.elements;

import game.Images;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class SpeedMeter {

	Image[] digits;
	Image kmh;
	int speed = 0;
	boolean repaint = true;
	int x, y;

	public SpeedMeter(int x, int y) {
		digits = Images.imgsSMDigits;
		kmh = Images.imgKmh;
		this.x = x;
		this.y = y;

	}

	public void setSpeed(int speed) {
		if (this.speed != speed) {
			this.speed = speed;
		}
	}

	public void paint(Graphics g) {
		paintDigits(g);
		paintKmh(g);
	}

	private void paintDigits(Graphics g) {
		int d;
		d = speed % 10;
		g.drawImage(digits[d], x, y, Graphics.HCENTER | Graphics.TOP);
		d = ((speed - d) % 100) / 10;
		if (speed > 9) {
			g.drawImage(digits[d], x - 9, y, Graphics.HCENTER | Graphics.TOP);
			d = ((speed - d) % 1000) / 100;
			if (speed > 99) {
				g.drawImage(digits[d], x - 18, y, Graphics.HCENTER | Graphics.TOP);
			} else {
				// g.drawImage(digits[10], x - 18, y, Graphics.HCENTER |
				// Graphics.TOP);
			}
		} else {
			// g.drawImage(digits[10], x - 9, y, Graphics.HCENTER |
			// Graphics.TOP);
		}

	}

	private void paintKmh(Graphics g) {

		g.drawImage(kmh, x + 22, y, Graphics.HCENTER | Graphics.TOP);
	}

	public int getSpeed() {
		return speed;
	}

}

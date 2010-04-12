package game.elements;

import game.Images;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class TimeOfRide {

	Image[] digits;
	Image colon;
	int speed = 0;
	boolean repaint = true;
	boolean timeStopped = false;
	int x, y;
	int minutes, seconds;
	long pauseTime = 0;
	long pauseStart = 0;
	long mark = 0;

	long startTime, lastTime;

	public TimeOfRide(int x, int y) {
		digits = Images.imgsTORDigits;
		colon = Images.imgColon;
		this.x = x;
		this.y = y;

	}

	public void computeTime() {
		if (timeStopped == false) {
			lastTime = System.currentTimeMillis();
		}
		long diff = lastTime - startTime - pauseTime;
		seconds = (int) (diff / 1000) % 60;
		minutes = (int) (diff / (1000 * 60));
	}

	private void resetTime() {
		this.minutes = 0;
		this.seconds = 0;
	}

	public void startTime() {
		startTime = System.currentTimeMillis();
		lastTime = startTime;
		timeStopped = false;
	}

	public void stopTime() {
		computeTime();
		timeStopped = true;
	}

	public void pauseTime() {
		pauseStart = System.currentTimeMillis();
	}

	public void resumeTime() {
		pauseTime += System.currentTimeMillis() - pauseStart;
	}

	public void paint(Graphics g) {
		paintLast(g);
	}

	public void paint00(Graphics g) {
		resetTime();
		paintLast(g);
	}

	public void paintLast(Graphics g) {
		paintDigits(g);
		paintColon(g);
	}

	private void paintDigits(Graphics g) {

		int d = minutes % 10;
		g.drawImage(digits[d], x - 27, y, Graphics.HCENTER | Graphics.TOP);
		d = ((minutes - d) % 100) / 10;
		if (minutes > 9) {
			g.drawImage(digits[d], x - 36, y, Graphics.HCENTER | Graphics.TOP);
		} else {
			g.drawImage(digits[0], x - 36, y, Graphics.HCENTER | Graphics.TOP);
		}

		d = seconds % 10;
		g.drawImage(digits[d], x, y, Graphics.HCENTER | Graphics.TOP);
		d = ((seconds - d) % 100) / 10;
		if (seconds > 9) {
			g.drawImage(digits[d], x - 9, y, Graphics.HCENTER | Graphics.TOP);
		} else {
			g.drawImage(digits[0], x - 9, y, Graphics.HCENTER | Graphics.TOP);
		}

	}

	public int getSeconds() {
		return seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	private void paintColon(Graphics g) {
		g.drawImage(colon, x - 16, y, Graphics.HCENTER | Graphics.TOP);
	}

	public void setMark() {
		this.mark = System.currentTimeMillis();
	}

	public int getPreciseTimeOfRide() {
		return (int) (lastTime - startTime);
	}

	public boolean changedPeriod() {
		return mark + 1000 < lastTime;
	}

}

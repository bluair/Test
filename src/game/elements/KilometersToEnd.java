package game.elements;

import game.Images;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class KilometersToEnd {
	Image[] digits;
	Image flag,km;
	int kmToEnd;
	int kmStart =0;
	int x, y;
	int sequencesFromStart = 0;
	

	public KilometersToEnd(int x, int y) {
		digits = Images.imgsKMDigits;
		flag = Images.imgFlag;
		km = Images.imgKm;
		this.x = x;
		this.y = y;

	}

	
	public void setKmStart(int kmStart){
		this.kmStart = kmStart;
		sequencesFromStart = 0;
		kmToEnd = kmStart;
	}


	public void movedXSequences(int x) {
		sequencesFromStart += x;
		if (kmStart -(sequencesFromStart / Level.SEQUENCES_PER_KM) != kmToEnd) {
			kmToEnd = kmStart - (sequencesFromStart / Level.SEQUENCES_PER_KM);
		}
	}

	public void paint(Graphics g) {

		paintDigits(g);
		paintRest(g);
	}

	private void paintDigits(Graphics g) {
		int d;
		d = kmToEnd % 10;
		g.drawImage(digits[d], x, y, Graphics.HCENTER | Graphics.TOP);
		d = ((kmToEnd - d) % 100) / 10;
		if (kmToEnd > 9) {
			g.drawImage(digits[d], x - 9, y, Graphics.HCENTER | Graphics.TOP);
			d = ((kmToEnd - d) % 1000) / 100;
			if (kmToEnd > 99) {
				g.drawImage(digits[d], x - 18, y, Graphics.HCENTER | Graphics.TOP);
			}
		}
	}

	private void paintRest(Graphics g) {
		 g.drawImage(flag, x-27, y, Graphics.HCENTER | Graphics.TOP);
		 g.drawImage(km, x+13, y+1, Graphics.HCENTER | Graphics.TOP);
		 
	}

}

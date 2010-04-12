package game.elements;

import game.elements.resized.Brick1;
import game.elements.resized.Brick2;
import game.elements.resized.Brick3;
import game.elements.resized.Bus1;
import game.elements.resized.Car1;
import game.elements.resized.Car2;
import game.elements.resized.Finish;
import game.elements.resized.Lamp;
import game.elements.resized.Oil4;
import game.elements.resized.ResizingElement;
import game.elements.resized.Tree5;
import game.elements.resized.Tree7;

import javax.microedition.lcdui.Graphics;

public class Level {

	private static final int MAX_VISIBLE_RESIZING_ELEMENTS = 20;
	private static final int VISIBLE_ELEMENTS_TYPES = 15;
	int[] elements1level = { 8, 8, 11, 8, 8, 8, 8, 9, 3, 4, 9, 3, 9, 8, 9, 5, 6, 8, 8, 5, 9, 5, 8, 9, 5, 6,
			6, 9, 9, 1, 8, 4, 8, 3, 9, 9, 4, 8, 8, 9, 8, 8, 8, 2, 5, 5, 9, 9, 8, 9, 9, 8, 3, 8, 8, 9, 9, 3,
			9, 9, 3, 4, 9, 9, 8, 8, 4, 2, 9, 9, 8, 6, 11, 5, 8, 4, 9, 8, 8, 8, 9, 3, 9, 8, 8, 9, 3, 8, 8, 8,
			9, 5, 8, 9, 6, 6, 6, 9, 9, 1, 8, 8, 8, 9, 9, 9, 5, 3, 9, 9, 8, 8, 8, 2, 4, 4, 9, 9, 8, 9, 9, 8,
			3, 8, 8, 9, 9, 3, 9, 9, 3, 4, 9, 9, 8, 9, 1, 8, 5, 8, 9, 3, 9, 8, 8, 9, 8, 8, 8, 5, 9, 5, 8, 9,
			5, 6, 6, 9, 9, 1, 8, 8, 8, 9, 9, 9, 4, 3, 9, 9, 9, 8, 3, 8, 8, 9, 9, 3, 9, 9, 3, 9, 9, 9, 8, 5,
			1, 5, 9, 8, 3, 5, 9, 9, 8, 4, 8, 2, 9, 8, 8, 8, 11, 3, 8, 4, 9, 9, 8, 9, 9, 8, 9, 8, 8, 5, 5, 8,
			8, 8, 9, 3, 8, 9, 6, 6, 3, 9, 9, 1, 8, 8, 8, 9, 9, 9, 4, 3, 9, 9, 9, 3, 9, 8, 8, 9, 8, 8, 8, 5,
			9, 5, 8, 9, 5, 6, 6, 9, 9, 6, 3, 4, 9, 9, 8, 8, 1, 9, 9, 5, 8, 9, 5, 4, 8, 5, 9, 8, 11, 4, 8, 8,
			8, 8, 8, 9, 3, 8, 8, 9, 9, 3, 8, 9, 6, 4, 6, 9, 9, 6, 8, 8, 8, 2, 9, 9, 4, 3, 9, 9, 8, 8, 8, 8,
			4, 4, 9, 9, 8, 9, 9, 8, 3, 8, 8, 9, 1, 3, 9, 9, 9, 8, 9, 9, 8, 8, 6, 2, 9, 8, 9, 9, 9, 8, 8, 9,
			11, 8, 8, 5, 9, 5, 8, 9, 5, 6, 6, 9, 9, 1, 8, 8, 8, 9, 9, 9, 4, 3, 9, 9, 8, 8, 8, 2, 4, 4, 9, 9,
			8, 9, 9, 8, 3, 8, 8, 9, 9, 3, 9, 9, 3, 9, 9, 9, 8, 5, 9, 8, 12 // ,
	// 8};
	};

	int[] elements2level = { 8, 8, 2, 8, 8, 8, 8, 9, 3, 4, 9, 3, 9, 8, 9, 5, 6, 8, 8, 5, 9, 5, 8, 9, 3, 6, 6,
			9, 9, 1, 8, 4, 8, 3, 9, 9, 4, 8, 5, 9, 8, 8, 8, 2, 5, 5, 9, 9, 8, 9, 9, 8, 3, 8, 8, 9, 9, 3, 8,
			8, 9, 3, 8, 9, 6, 6, 3, 9, 9, 1, 8, 8, 8, 9, 9, 9, 4, 3, 9, 9, 9, 3, 9, 8, 8, 9, 8, 8, 8, 5, 9,
			9, 3, 4, 9, 9, 8, 8, 4, 2, 9, 9, 8, 6, 11, 5, 8, 4, 9, 8, 8, 8, 9, 3, 9, 8, 8, 9, 3, 8, 8, 8, 9,
			5, 8, 9, 6, 6, 6, 9, 9, 1, 8, 8, 8, 9, 9, 9, 5, 3, 9, 9, 8, 8, 8, 2, 4, 4, 9, 9, 8, 9, 9, 8, 3,
			8, 8, 9, 9, 3, 9, 9, 3, 4, 9, 9, 8, 9, 1, 8, 5, 8, 9, 3, 9, 8, 8, 9, 8, 8, 8, 5, 9, 5, 8, 9, 1,
			5, 9, 8, 3, 5, 9, 9, 8, 4, 8, 2, 9, 8, 8, 8, 11, 3, 8, 4, 9, 9, 8, 9, 9, 8, 9, 8, 8, 5, 5, 8, 5,
			6, 6, 9, 9, 1, 8, 8, 8, 9, 9, 9, 4, 3, 9, 9, 9, 8, 3, 8, 8, 9, 9, 3, 9, 9, 3, 9, 9, 9, 8, 5, 9,
			5, 8, 9, 5, 6, 6, 9, 9, 6, 3, 4, 9, 9, 8, 8, 1, 9, 9, 5, 8, 9, 5, 4, 8, 5, 9, 8, 11, 4, 8, 8, 8,
			8, 8, 9, 3, 8, 8, 9, 9, 3, 8, 9, 6, 4, 6, 9, 9, 3, 8, 8, 8, 2, 9, 9, 4, 3, 9, 9, 8, 8, 8, 8, 11,
			8, 8, 5, 9, 5, 8, 9, 5, 6, 6, 9, 9, 1, 8, 8, 8, 9, 3, 9, 4, 3, 9, 9, 8, 8, 8, 2, 4, 4, 9, 9, 4,
			4, 9, 9, 8, 9, 9, 8, 3, 8, 8, 9, 1, 3, 9, 9, 9, 8, 9, 9, 8, 8, 6, 2, 9, 8, 9, 9, 9, 8, 8, 9, 8,
			9, 9, 8, 3, 8, 8, 9, 9, 3, 9, 9, 3, 9, 9, 9, 8, 5, 9, 8, 12 // ,
	// 8};
	};

	int[] movingElements1level = { 14, 14, 13, 14, 7, 13, 7, 14, 13, 7, 14, 13, 13, 14, 7, 13, 7, 14, 13, 7 };

	int[] movingElements1levelMulti = { 14, 14, 7, 14, 7, 14, 7, 14, 14, 7, 14, 14, 7, 14, 7, 14, 7, 14, 14,
			7 };
	int[] movingElements2levelMulti = { 7, 14, 7, 14, 7, 14, 7, 14, 14, 7, 14, 14, 7, 14, 14, 14, 7, 14, 14,
			7 };

	int[] movingElementsSequences1levelMulti = { 150, 250, 400, 600, 700, 750, 800, 900, 950, 1000, 1050,
			1100, 1200, 1250, 1300, 1400, 1450, 1500, 1600, 1700 };
	int[] movingElementsSequences2levelMulti = { 150, 250, 400, 600, 700, 750, 800, 900, 950, 1000, 1050,
			1100, 1200, 1250, 1300, 1400, 1450, 1500, 1600, 1700 };

	int[] movingElementsSequences1level = { 150, 200, 250, 300, 350, 400, 600, 650, 700, 750, 800, 850, 900,
			1000, 1100, 1200, 1300, 1400, 1500, 1700 };

	int[] sequences1level = { 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100,
			105, 110, 115, 120, 125, 130, 135, 140, 145, 150, 155, 160, 165, 170, 175, 180, 185, 190, 195,
			200, 205, 210, 215, 220, 225, 230, 235, 240, 245, 250, 255, 260, 265, 270, 275, 280, 285, 290,
			295, 300, 305, 310, 315, 320, 325, 330, 335, 340, 345, 350, 355, 360, 365, 370, 375, 380, 385,
			390, 395, 400, 405, 410, 415, 420, 425, 430, 435, 440, 445, 450, 455, 460, 465, 470, 475, 480,
			485, 490, 495, 500, 505, 510, 515, 520, 525, 530, 535, 540, 545, 550, 555, 560, 565, 570, 575,
			580, 585, 590, 595, 600, 605, 610, 615, 620, 625, 630, 635, 640, 645, 650, 655, 660, 665, 670,
			675, 680, 685, 690, 695, 700, 705, 710, 715, 720, 725, 730, 735, 740, 745, 750, 755, 760, 765,
			770, 775, 780, 785, 790, 795, 800, 805, 810, 815, 820, 825, 830, 835, 840, 845, 850, 855, 860,
			865, 870, 875, 880, 885, 890, 895, 900, 905, 910, 915, 920, 925, 930, 935, 940, 945, 950, 955,
			960, 965, 970, 975, 980, 985, 990, 995, 1000, 1005, 1010, 1015, 1020, 1025, 1030, 1035, 1040,
			1045, 1050, 1055, 1060, 1065, 1070, 1075, 1080, 1085, 1090, 1095, 1100, 1105, 1110, 1115, 1120,
			1125, 1130, 1135, 1140, 1145, 1150, 1155, 1160, 1165, 1170, 1175, 1180, 1185, 1190, 1195, 1200,
			1205, 1210, 1215, 1220, 1225, 1230, 1235, 1240, 1245, 1250, 1255, 1260, 1265, 1270, 1275, 1280,
			1285, 1290, 1295, 1300, 1305, 1310, 1315, 1320, 1325, 1330, 1335, 1340, 1345, 1350, 1355, 1360,
			1365, 1370, 1375, 1380, 1385, 1390, 1395, 1400, 1405, 1410, 1415, 1420, 1425, 1430, 1435, 1440,
			1445, 1450, 1455, 1460, 1465, 1470, 1475, 1480, 1485, 1490, 1495, 1500, 1505, 1510, 1515, 1520,
			1525, 1530, 1535, 1540, 1545, 1550, 1555, 1560, 1565, 1570, 1575, 1580, 1585, 1590, 1595, 1600,
			1605, 1610, 1615, 1620, 1625, 1630, 1635, 1640, 1645, 1650, 1655, 1660, 1665, 1670, 1675, 1680,
			1685, 1690, 1695, 1700, 1705, 1710, 1715, 1720, 1725, 1730, 1735, 1740, 1745, 1750, 1755, 1760,
			1765, 1770, 1775, 1780, 1785, 1790, 1795, 1800, 1805, 1810, 1815, 1820, 1825, 1830, 1835, 1840,
			1845, 1850, 1855, 1860, 1865, 1870, 1875, 1880, 1885, 1890, 1895, 1900, 1905, 1910, 1915, 1920,
			1925, 1930, 1935, 1940, 1945, 1950, 1955, 1960, 1965, 1970, 1975, 1980, 1985, 1987, 1989, 2000 // ,2005

	};

	// int[] elements2level = elements1level;
	int[] sequences2level = sequences1level;
	int[] movingElements2level = movingElements1level;
	int[] movingElementsSequences2level = movingElementsSequences1level;

	int[] elements = null;
	int[] sequences = null;
	int[] movingElements = null;
	int[] movingElementsSequences = null;

	byte levelInt;
	int kilometers;
	ResizingElement[] visibleElements;
	int currentSequence;
	int currentFirst;
	public static final short MAX_VISIBLE_SEQUENCES = 50;
	static final int SEQUENCES_PER_KM = 200;
	Road road;
	short x, y;

	private void createVisibleElements() {
		visibleElements = new ResizingElement[VISIBLE_ELEMENTS_TYPES];
		visibleElements[0] = new Brick1(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[1] = new Brick2(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[2] = new Brick3(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[3] = new Oil4(road, MAX_VISIBLE_SEQUENCES, Oil4.OIL_CENTER);
		visibleElements[4] = new Oil4(road, MAX_VISIBLE_SEQUENCES, Oil4.OIL_LEFT);
		visibleElements[5] = new Oil4(road, MAX_VISIBLE_SEQUENCES, Oil4.OIL_RIGHT);
		visibleElements[6] = new Tree5(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[7] = new Bus1(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[8] = new Tree7(road, MAX_VISIBLE_SEQUENCES, Tree7.TREE_LEFT);
		visibleElements[9] = new Tree7(road, MAX_VISIBLE_SEQUENCES, Tree7.TREE_RIGHT);
		Lamp lr = new Lamp(road, MAX_VISIBLE_SEQUENCES, Lamp.LAMP_RIGHT);
		visibleElements[11] = lr;
		visibleElements[10] = lr;
		visibleElements[12] = new Finish(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[13] = new Car1(road, MAX_VISIBLE_SEQUENCES);
		visibleElements[14] = new Car2(road, MAX_VISIBLE_SEQUENCES);
	}

	public Level(Road road, byte levelInt, int numberOfPlayers) {
		this.road = road;
		this.x = road.x;
		this.levelInt = (byte) (levelInt);
		if (numberOfPlayers == 1) {
			if (levelInt == 0) {
				elements = elements1level;
				sequences = sequences1level;
				movingElements = movingElements1level;
				movingElementsSequences = movingElementsSequences1level;
			} else {
				elements = elements2level;
				sequences = sequences2level;
				movingElements = movingElements2level;
				movingElementsSequences = movingElementsSequences2level;
			}
		} else {
			if (levelInt == 0) {
				elements = elements1level;
				sequences = sequences1level;
				movingElements = movingElements1levelMulti;
				movingElementsSequences = movingElementsSequences1levelMulti;
			} else {
				elements = elements2level;
				sequences = sequences2level;
				movingElements = movingElements2levelMulti;
				movingElementsSequences = movingElementsSequences2levelMulti;
			}
		}
		createVisibleElements();
		reset();
	}

	public void reset() {
		currentSequence = 0;
		currentFirst = 0;
		kilometers = 10;
		this.x = road.x;
	}

	public int getCurrentSequence() {
		return currentSequence;
	}

	public int getRoadKilometers() {
		return kilometers;
	}

	private void moveCurrentFirst() {
		while ((currentFirst < elements.length) && (currentSequence > sequences[currentFirst])) {
			currentFirst++;
		}
	}

	public void paint(Graphics g, EvoResized evoOpponent) {
		moveCurrentFirst();

		// paint static
		if (currentFirst < elements.length) {

			for (int i = MAX_VISIBLE_RESIZING_ELEMENTS - 1; i >= 0; i--) {
				int ind = i + currentFirst;
				if (ind < elements.length) {

					int diff = sequences[ind] - currentSequence;

					if (diff <= MAX_VISIBLE_SEQUENCES) {
						visibleElements[elements[ind]].setSequenceDifference((short) diff);
						visibleElements[elements[ind]].paintResized(g, x);
					}
				}

			}
		}
		
		int sdiff  = 0;
		if (evoOpponent != null) {
			 sdiff = evoOpponent.getSequenceDifference();
		}
		
		// paint moving
		boolean p = false;
		for (int i = movingElements.length - 1; i >= 0; i--) {
			int seq = visibleElements[movingElements[i]].getSequence();
			int diff = (movingElementsSequences[i] - seq) - currentSequence;
			if (diff <= MAX_VISIBLE_SEQUENCES && diff > -4) {
				if (evoOpponent != null) {
					if (sdiff > diff) {
						evoOpponent.paintResized(g);
						p = true;
					}
				}
				visibleElements[movingElements[i]].setSequenceDifference((short) diff);
				visibleElements[movingElements[i]].paintResized(g, x);
			}
		}
		if (evoOpponent != null && !p) {
			evoOpponent.paintResized(g);
		}

	}

	public ResizingElement getCurrentCollisionElement(int maxColl, Evo evo) {
		ResizingElement ret = null;

		for (int i = movingElements.length - 1; i >= 0; i--) {
			int seq = visibleElements[movingElements[i]].getSequence();
			int diff = (movingElementsSequences[i] - seq) - currentSequence;

			if (visibleElements[movingElements[i]].collidesX(evo) && Math.abs(diff) <= 4) {
				// System.out.println("reta " + movingElements[i]);

				return visibleElements[movingElements[i]];
			}
		}

		moveCurrentFirst();
		if (currentFirst < elements.length) {
			for (int i = currentFirst; i < elements.length && i - currentFirst < 5; i++) {
				if (Math.abs(sequences[i] - currentSequence) <= maxColl
						&& visibleElements[elements[i]].collidesX(evo)) {
					// System.out.println("syrar " + elements[i]);
					return visibleElements[elements[i]];
				}
			}
		}

		return ret;
	}

	public int movedXSequences(int seqToMove) {

		return (currentSequence += seqToMove);

	}

	public void move(int moveS) {
		x += moveS;

	}

	public int getInt() {
		return levelInt;
	}

	public boolean endOfGame() {
		// System.out.println("currse "+currentSequence);
		return (currentSequence >= Level.SEQUENCES_PER_KM * kilometers);
	}

	public synchronized void startTime() {
		for (int i = 0; i < visibleElements.length; i++) {
			visibleElements[i].startTime();
		}

	}

}

package game.screens;

import game.EvoMidlet;
import game.Images;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class SplashAndGo extends GameCanvas {

	Image imgMain;
	int w, h;
	int wi, he;
	int xo = 0, yo = 0;
	Image[] imgsMenuNotActive;
	Image[] imgsMenuActive;
	// Image imgMenuBackground;
	Graphics g;
	int currentActive = 0;
	int currentMax = 6;
	int space = 18;
	int upOffset;
	int leftOffset;
	EvoMidlet midlet;
	int stage = 0;
	private int menuOffset = 0;
	private Image imgMenuInstructions;
	private int leftOffsetB;
	private int imgMenuBackWidth;
	private int imgMenuBackHeight;

	public SplashAndGo(EvoMidlet midlet) {
		super(false);

		this.midlet = midlet;
		try {
			createImages();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		g = this.getGraphics();
		try {

			setFullScreenMode(true);
			w = this.getWidth() / 2;
			h = this.getHeight() / 2;

			System.out.println("wid " + getWidth() + " hej " + getHeight());

			wi = getWidth();
			he = getHeight();

			if ((wi >= 240 && he >= 309) && (Images.porting == Images.BIG)) {
				imgMain = Image.createImage("/splash1.png");
				// imgMenuBackground = Image.createImage("/mbbig.png");
				imgMenuBackWidth = 178;
				imgMenuBackHeight = 157;
				imgMenuInstructions = Image.createImage("/instr1.png");
				upOffset = 29;
				leftOffsetB = 32;
				leftOffset = 120;
			} else {
				if (wi <= 176 && he <= 220) {
					imgMain = Image.createImage("/splash2.png");
					// imgMenuBackground = Image.createImage("/mb.png");
					imgMenuInstructions = Image.createImage("/instr2.png");
				} else {
					if (wi >= 300) {
						imgMain = Image.createImage("/splash2.png");
						// imgMenuBackground = Image.createImage("/mb.png");
						imgMenuInstructions = Image.createImage("/instr2.png");
					} else {
						imgMain = Image.createImage("/splash2.png");
						// imgMenuBackground = Image.createImage("/mb.png");
						imgMenuInstructions = Image.createImage("/instr2.png");
					}
				}
				imgMenuBackWidth = 130;
				imgMenuBackHeight = 116;

				xo = (getWidth() - imgMain.getWidth()) / 2;

				yo = (getHeight() - imgMain.getHeight()) / 2;
				leftOffsetB = 23 + xo;
				upOffset = 13 + yo;
				leftOffset = 88 + xo;
				if (wi <= 128 ) {
					upOffset +=18;
				}
			}

		} catch (IOException e) {
			System.out.println("bladda  " + e.getMessage());
		}
	}

	private void createImages() throws IOException {
		imgsMenuActive = new Image[9];
		imgsMenuNotActive = new Image[9];
		for (int i = 0; i < imgsMenuActive.length; i++) {
			imgsMenuActive[i] = Image.createImage("/ma" + (i + 1) + ".png");
			imgsMenuNotActive[i] = Image.createImage("/mna" + (i + 1) + ".png");
		}
	}

	public void paintSplashSequence() {

		try {
			paintSplash();

			System.gc();
			Thread.sleep(2000);

		} catch (InterruptedException e) {
		}
	}

	private void paintMenu() {

		if (stage != 3) {
			paintMenuBackground();
			for (int i = 0; i < currentMax; i++) {
				if (currentActive == i) {
					g.drawImage(imgsMenuActive[i + menuOffset], leftOffset, upOffset + (i * space),
							Graphics.HCENTER | Graphics.VCENTER);
				} else {
					g.drawImage(imgsMenuNotActive[i + menuOffset], leftOffset, upOffset + (i * space),
							Graphics.HCENTER | Graphics.VCENTER);
				}
			}
			flushGraphics();
		}

	}

	protected void paintSplash() {

		if (imgMain != null) {
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(imgMain, xo, yo, Graphics.LEFT | Graphics.TOP);
		}

		flushGraphics();

	}

	protected void keyPressed(int keyCode) {

		switch (getGameAction(keyCode)) {
		case Canvas.UP:
			currentActive = (currentActive - 1 + currentMax) % currentMax;
			break;
		case Canvas.DOWN:
			currentActive = (currentActive + 1) % currentMax;
			break;
		case Canvas.LEFT:
			break;
		case Canvas.RIGHT:
			break;
		case Canvas.FIRE:
			runCommand();
			break;
		case 0: {
			break;
		}
		default: {
		}
		}
		paintMenu();
	}

	private void runCommand() {

		if (stage == 0) {
			switch (currentActive) {
			case 0: // start single

				midlet.isServer = false;
				midlet.standalone = true;
				stage = 1;
				midlet.changeStage(1);
				break;

			case 1:// start server
				midlet.isServer = true;
				midlet.standalone = false;
				stage = 1;
				midlet.changeStage(1);
				break;
			case 2:// start client
				midlet.isServer = false;
				midlet.standalone = false;
				midlet.setCurrentDisplayProgressBar("wait...");
				GameStarter gs = new GameStarter(midlet.isServer, midlet.standalone, 0);
				Thread gsthread = new Thread(gs);
				gsthread.start();
				break;

			case 3:
				midlet.setCurrentDisplayWallOfFame();
				break;
			case 4: // instructions
				stage = 3;
				paintInstructions();
				// midlet.setCurrentDisplayInstructions(this);
				break;
			case 5: {// exit game
				midlet.destroyApp(true);
				return;

			}
			default:
				break;
			}
		} else {
			if (stage == 1) {
				// System.out.println("tu "+menuLevel.getSelectedIndex());
				if (currentActive == 2) {
					stage = 0;
					midlet.changeStage(0);
				} else {
					stage = 0;
					midlet.setCurrentDisplayProgressBar("wait...");
					GameStarter gs = new GameStarter(midlet.isServer, midlet.standalone, currentActive);
					Thread gsthread = new Thread(gs);
					gsthread.start();

				}
			} else {// instuctions
				stage = 0;
				currentActive = 0;

				paintMenu();
			}
		}
	}

	private class GameStarter implements Runnable {
		boolean isServer;

		boolean standalone;
		int level;

		public GameStarter(boolean isServer, boolean standalone, int level) {
			this.isServer = isServer;
			this.standalone = standalone;
			this.level = level;
		}

		public void run() {
			if (standalone) {
				midlet.startGameSingle(level);
			} else {
				if (isServer) {
					midlet.startGameServer(level);
				} else {
					midlet.startGameClient(level);
				}
			}
		}
	}

	public void go() {
		paintMenu();
	}

	private void paintMenuBackground() {
		// g.drawImage(imgMenuBackground, leftOffsetB, yo, Graphics.LEFT |
		// Graphics.TOP);
		g.fillRect(leftOffsetB, yo, imgMenuBackWidth, imgMenuBackHeight);
	}

	private void paintInstructions() {
		g.drawImage(imgMenuInstructions, leftOffsetB, yo, Graphics.LEFT | Graphics.TOP);
		flushGraphics();

	}

	public void setCurrent(int i) {
		switch (i) {
		case 1:
			currentMax = 6;
			menuOffset = 0;
			currentActive = 0;
			break;
		case 2:
			currentMax = 3;
			menuOffset = 6;
			currentActive = 0;
			break;
		}
		paintMenu();
	}

}

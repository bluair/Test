package game;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Images {

	public static final int SMALL = 0;
	public static final int MIDDLE = 1;
	public static final int BIG = 2;

	public static final int porting = SMALL;

	public static Image imgFoot;
	public static Image imgBluetooth;
	public static Image imgRoad1;

	public static Image[] imgRoad1Tab;
	public static Image[] imgsSMDigits;
	public static Image[] imgsTORDigits;
	public static Image[] imgsKMDigits;
	public static Image imgBacgroundDay, imgBacgroundNight;
	public static Image imgFlag, imgColon, imgKm;

	public static Image imgExit;
	public static Image imgPlay;

	private static int rgb[] = { -15597569 };

	public static Image pixelImage;

	public static Sprite pixelSprite;

	public static Image wallOfFameWinnerImage = null;
	public static Image wallOfFameOtherImage = null;
	public static Image[] imgsRoad;
	// public static Image[] imgsBrick1;
	public static Image[] imgsBrick2;
	public static Image[] imgsBrick3;
	public static Image[] imgsOil4;
	public static Image[] imgsTree5;
	public static Image[] imgsEvoBlue, imgsEvoYellow;
	public static Image imgKmh;
	public static Image[] imgsGameStarter;
	public static Image[] imgsGameEnder;
	public static Image[] imgsBus6;
	public static Image[] imgsTree7;
	// public static Image[] imgsLampL;
	public static Image[] imgsLampR;
	public static Image[] imgsMenuActive;
	public static Image[] imgsMenuNotActive;
	public static Image[] imgsFinish;
	public static Image[] imgsCar1;
	public static Image[] imgsGameCommunicates;

	public static boolean imagesCreated = false;
	public static Image[] imgsCar2;

	// public static final Font fontLevel = Font.getFont(Font.FACE_SYSTEM,
	// Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	//
	// public static final Font fontScroll = Font.getFont(Font.FACE_SYSTEM,
	// Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	//
	// public static final Font fontMenu = Font.getFont(Font.FACE_SYSTEM,
	// Font.STYLE_PLAIN, Font.SIZE_LARGE);
	//
	// public static final Font fontScore = Font.getFont(Font.FACE_SYSTEM,
	// Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	//
	// public static final Font exitFont = Font.getFont(Font.FACE_SYSTEM,
	// Font.STYLE_BOLD, Font.SIZE_LARGE);
	// public static final Font fontInfo = Font.getFont(Font.FACE_SYSTEM,
	// Font.STYLE_BOLD, Font.SIZE_SMALL);

	// return brick size
	public static void createImagesDependingOnScreen(int width, int height, EvoMidlet midlet, int userType) {

		if (imagesCreated == false) {
			try {

				midlet.addProgressInfo("b1");
				pixelImage = Image.createRGBImage(rgb, 1, 1, true);
				midlet.addProgressInfo("b2");
				pixelSprite = new Sprite(pixelImage, 1, 1);
				midlet.addProgressInfo("b3");
				imgsRoad = new Image[6];
				midlet.addProgressInfo("b4");
				imgsRoad[0] = Image.createImage("/d1.png");
				imgsRoad[1] = Image.createImage("/d2.png");
				imgsRoad[2] = Image.createImage("/d3.png");
				imgsRoad[3] = Image.createImage("/d4.png");
				imgsRoad[4] = Image.createImage("/d5.png");
				imgsRoad[5] = Image.createImage("/d6.png");
				midlet.addProgressInfo("b5");
				// if (userType == Pit2.USER_CLIENT) {

				int iu;
				if (porting == BIG)
					iu = 8;
				else if (porting == MIDDLE)
					iu = 8;
				else
					iu = 5;

				imgsEvoYellow = new Image[10];
				imgsEvoYellow[0] = Image.createImage("/zp" + iu + ".png");
				imgsEvoYellow[1] = Image.createImage("/zs" + iu + ".png");
				imgsEvoYellow[2] = Image.createImage("/zl" + iu + ".png");
				imgsEvoYellow[3] = Image.createImage("/zr" + iu + ".png");
				imgsEvoYellow[4] = Image.createImage("/zls" + iu + ".png");
				imgsEvoYellow[5] = Image.createImage("/zrs" + iu + ".png");
				if (porting == BIG) {
					imgsEvoYellow[6] = Image.createImage("/zll" + iu + ".png");
					imgsEvoYellow[7] = Image.createImage("/zrr" + iu + ".png");
					imgsEvoYellow[8] = Image.createImage("/zlls" + iu + ".png");
					imgsEvoYellow[9] = Image.createImage("/zrrs" + iu + ".png");
				} else {
					imgsEvoYellow[6] = imgsEvoYellow[2];
					imgsEvoYellow[7] = imgsEvoYellow[3];
					imgsEvoYellow[8] = imgsEvoYellow[4];
					imgsEvoYellow[9] = imgsEvoYellow[5];
				}
				// } else {
				imgsEvoBlue = new Image[10];
				imgsEvoBlue[0] = Image.createImage("/np" + iu + ".png");
				imgsEvoBlue[1] = Image.createImage("/ns" + iu + ".png");
				imgsEvoBlue[2] = Image.createImage("/nl" + iu + ".png");
				imgsEvoBlue[3] = Image.createImage("/nr" + iu + ".png");
				imgsEvoBlue[4] = Image.createImage("/nls" + iu + ".png");
				imgsEvoBlue[5] = Image.createImage("/nrs" + iu + ".png");

				if (porting == BIG) {
					imgsEvoBlue[6] = Image.createImage("/nll" + iu + ".png");
					imgsEvoBlue[7] = Image.createImage("/nrr" + iu + ".png");
					imgsEvoBlue[8] = Image.createImage("/nlls" + iu + ".png");
					imgsEvoBlue[9] = Image.createImage("/nrrs" + iu + ".png");
				} else {
					imgsEvoBlue[6] = imgsEvoBlue[2];
					imgsEvoBlue[7] = imgsEvoBlue[3];
					imgsEvoBlue[8] = imgsEvoBlue[4];
					imgsEvoBlue[9] = imgsEvoBlue[5];
				}
				// }
				System.gc();
				if (width <= 176 || porting == SMALL) {
					imgFoot = Image.createImage("/foot2.png");
				} else {
					imgFoot = Image.createImage("/foot.png");
				}
				midlet.addProgressInfo("b6");
				imgBacgroundNight = Image.createImage("/backN.png");
				imgBacgroundDay = Image.createImage("/backD.png");
				imgColon = Image.createImage("/timecolon.png");
				imgKm = Image.createImage("/timekm.png");
				imgKmh = Image.createImage("/kmh.png");

				imgsKMDigits = new Image[10];
				imgsTORDigits = new Image[10];
				imgsSMDigits = imgsKMDigits;

				imgFlag = Image.createImage("/metaico.png");
				imgsGameCommunicates = new Image[2];
				imgsGameCommunicates[0] = Image.createImage("/aaby.png");
				imgsGameCommunicates[1] = Image.createImage("/czekanie.png");

				midlet.addProgressInfo("b7");
				imgsMenuActive = new Image[8];
				imgsMenuNotActive = new Image[8];
				for (int i = 0; i < imgsMenuActive.length; i++) {
					imgsMenuActive[i] = Image.createImage("/ma" + (i + 1) + ".png");
					imgsMenuNotActive[i] = Image.createImage("/mna" + (i + 1) + ".png");
				}

				for (int i = 0; i < 10; i++) {
					imgsTORDigits[i] = Image.createImage("/time" + i + ".png");
					imgsKMDigits[i] = Image.createImage("/speed" + i + ".png");
				}

				// imgsBrick1 = new Image[9];
				// for (int i = 0; i < imgsBrick1.length; i++) {
				// imgsBrick1[i] = Image.createImage("/brick1_" + i + ".png");
				// }
				midlet.addProgressInfo("b8");

				if (porting == BIG)
					imgsBrick2 = new Image[14];
				else if (porting == MIDDLE)
					imgsBrick2 = new Image[10];
				else
					imgsBrick2 = new Image[9];

				for (int i = 0; i < imgsBrick2.length; i++) {
					imgsBrick2[i] = Image.createImage("/ban" + i + ".png");
				}
				midlet.addProgressInfo("b9");

				if (porting == BIG)
					imgsBrick3 = new Image[16];
				else if (porting == MIDDLE)
					imgsBrick3 = new Image[10];
				// else
				// imgsBrick3 = new Image[9];
				//
				// for (int i = 0; i < imgsBrick3.length; i++) {
				// imgsBrick3[i] = Image.createImage("/bilA" + i + ".png");
				// }

				midlet.addProgressInfo("b10");
				if (porting == BIG)
					imgsOil4 = new Image[10];
				else if (porting == MIDDLE)
					imgsOil4 = new Image[10];
				else
					imgsOil4 = new Image[8];
				for (int i = 0; i < imgsOil4.length; i++) {
					imgsOil4[i] = Image.createImage("/ol" + (i + 1) + ".png");
				}
				midlet.addProgressInfo("b11");

				if (porting == BIG)
					imgsTree5 = new Image[10];
				else if (porting == MIDDLE)
					imgsTree5 = new Image[8];
				else
					imgsTree5 = new Image[7];

				for (int i = 0; i < imgsTree5.length; i++) {
					imgsTree5[i] = Image.createImage("/tree" + (i + 1) + ".png");
				}
				midlet.addProgressInfo("b12");
				imgsGameStarter = new Image[3];
				for (int i = 0; i < imgsGameStarter.length; i++) {
					imgsGameStarter[i] = Image.createImage("/odl" + (i + 1) + ".png");
				}
				midlet.addProgressInfo("b13");
				imgsGameEnder = new Image[4];
				for (int i = 0; i < imgsGameEnder.length; i++) {
					imgsGameEnder[i] = Image.createImage("/end" + (i + 1) + ".png");
				}

				midlet.addProgressInfo("b14");
				if (porting == BIG)
					imgsBus6 = new Image[14];
				else if (porting == MIDDLE)
					imgsBus6 = new Image[8];
				else
					imgsBus6 = new Image[7];

				for (int i = 0; i < imgsBus6.length; i++) {
					imgsBus6[i] = Image.createImage("/bus" + i + ".png");
				}

				midlet.addProgressInfo("b15");
				if (porting == BIG)
					imgsTree7 = new Image[10];
				else if (porting == MIDDLE)
					imgsTree7 = new Image[8];
				else
					imgsTree7 = new Image[6];
				for (int i = 0; i < imgsTree7.length; i++) {
					imgsTree7[i] = Image.createImage("/tr" + i + ".png");
				}

				// midlet.addProgressInfo("b16");
				// imgsLampL = new Image[10];
				// for (int i = 0; i < imgsLampL.length; i++) {
				// imgsLampL[i] = Image.createImage("/lampL" + i + ".png");
				// }

				midlet.addProgressInfo("b17");
				if (porting == BIG)
					imgsLampR = new Image[10];
				else if (porting == MIDDLE)
					imgsLampR = new Image[9];
				else
					imgsLampR = new Image[8];
				for (int i = 0; i < imgsLampR.length; i++) {
					imgsLampR[i] = Image.createImage("/lampR" + i + ".png");
				}

				midlet.addProgressInfo("b18");
				if (porting == BIG)
					imgsFinish = new Image[14];
				else if (porting == MIDDLE)
					imgsFinish = new Image[11];
				else
					imgsFinish = new Image[10];
				for (int i = 0; i < imgsFinish.length; i++) {
					// midlet.addProgressInfo("b18"+i); // nie wczytuje sie nr 9
					imgsFinish[i] = Image.createImage("/meta" + i + ".png");
					// imgsFinish[i] = Image.createImage("/bus" + i + ".png");
				}

				midlet.addProgressInfo("b19");
				if (porting == BIG)
					imgsCar2 = new Image[11];
				else if (porting == MIDDLE)
					imgsCar2 = new Image[10];
				else
					imgsCar2 = new Image[7];

				for (int i = 0; i < imgsCar2.length; i++) {
					imgsCar2[i] = Image.createImage("/car" + i + ".png");
				}

				midlet.addProgressInfo("b20");
				if (porting == BIG)
					imgsCar1 = new Image[11];
				else if (porting == MIDDLE)
					imgsCar1 = new Image[11];
				else
					imgsCar1 = new Image[10];
				// if (porting == BIG) {
				for (int i = 0; i < imgsCar1.length; i++) {
					imgsCar1[i] = Image.createImage("/zp" + i + ".png");
				}
				// } else {
				// for (int i = 0; i < imgsCar1.length; i++) {
				// imgsCar1[i] = imgsCar2[i];
				// }
				// }

				midlet.addProgressInfo("b21");
				imagesCreated = true;

			} catch (Throwable e) {
				midlet.addProgressInfo("blad " + e.getClass() + " " + e.toString() + " " + e.getMessage());

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}
				System.out.println("Reading images problem " + e.getMessage());

			}
		}

	}

	public static Image[][] getEvoResized(int color) {
		int maxRes;
		if (porting == BIG)
			maxRes = 11;
		else if (porting == MIDDLE)
			maxRes = 10;
		else
			maxRes = 8;
		Image[][] ret = new Image[6][maxRes];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < maxRes; j++) {
				try {
					if (porting == BIG || porting == MIDDLE) {
						if (j != 8) {
							ret[i][j] = Image.createImage(getFName(color, i, j));
						} else {
							if (color == 0)// z
								ret[i][j] = Images.imgsEvoYellow[i];
							else
								ret[i][j] = Images.imgsEvoBlue[i];
						}
					} else {
						if (j != 5) {
							ret[i][j] = Image.createImage(getFName(color, i, j));
						} else {
							if (color == 0)// z
								ret[i][j] = Images.imgsEvoYellow[i];
							else
								ret[i][j] = Images.imgsEvoBlue[i];
						}
					}
				} catch (IOException e) {
					System.out.println("błąd!! " + e.getMessage() + " " + getFName(color, i, j));
					// e.printStackTrace();
				}
			}
		}
		return ret;

	}

	// public static Image[] createRoad(Image img, int roadHeight,int
	// roadHorizontWidth,int roadCarWidth){
	// Image[] ret = new Image[roadHeight];
	// int width;
	// int height;
	//
	// width = img.getWidth();
	// height = img.getHeight();
	//
	// int rgbInput[] = new int[width * height];
	// int rgbTmp[] = new int[width];
	//
	// img.getRGB(rgbInput, 0, width, 0, 0, width, height);
	//
	// int i, j;
	// }

	// public static Image[] createMoveImages(Image img) {
	// Image[] ret = new Image[img.getHeight()];
	// int width;
	// int height;
	//
	// width = img.getWidth();
	// height = img.getHeight();
	//
	// int rgbInput[] = new int[width * height];
	// int rgbTmp[] = new int[width];
	//
	// img.getRGB(rgbInput, 0, width, 0, 0, width, height);
	//
	// int i, j;
	//
	// ret[0] = Image.createRGBImage(rgbInput, width, height, true);
	// for (int move = 0; move < height - 1; move++) {
	// for (j = 0; j < width; j++) {
	// rgbTmp[j] = rgbInput[j + (height - 1) * width];
	// }
	// for (i = height - 1; i > 0; i--) {
	// for (j = 0; j < width; j++) {
	// rgbInput[j + i * width] = rgbInput[j + (i - 1) * width];
	// }
	// }
	// for (j = 0; j < width; j++) {
	// rgbInput[j] = rgbTmp[j];
	// }
	// ret[move + 1] = Image.createRGBImage(rgbInput, width, height, true);
	// }
	// return ret;
	// }

	// public static Image rotate90L(Image img) {
	//
	// int[] rgbOutput = null;
	// int width;
	// int height;
	//
	// width = img.getWidth();
	// height = img.getHeight();
	//
	// int rgbInput[] = new int[width * height];
	// rgbOutput = new int[width * height];
	//
	// img.getRGB(rgbInput, 0, width, 0, 0, width, height);
	//
	// int i, j;
	// for (i = 0; i < height; i++) {
	// for (j = 0; j < width; j++) {
	// // int r = rgbInput[i * width + (width - j - 1)];
	// rgbOutput[j * height + i] = rgbInput[i * width + (width - j - 1)];// |=
	// // 0xff
	// // <<
	// // 24;
	//
	// }
	// }
	//
	// return Image.createRGBImage(rgbOutput, height, width, true);
	//
	// }

	// public static Image rotate90LTransparent(Image img, int transparentWidth,
	// int transparentHeight) {
	//
	// int[] rgbOutput = null;
	// int width;
	// int height;
	//
	// width = img.getWidth();
	// height = img.getHeight();
	//
	// int rgbInput[] = new int[width * height];
	// rgbOutput = new int[width * height];
	//
	// img.getRGB(rgbInput, 0, width, 0, 0, width, height);
	//
	// int transparent = rgbInput[transparentHeight * height +
	// transparentWidth];
	//
	// int i, j;
	// for (i = 0; i < height; i++) {
	// for (j = 0; j < width; j++) {
	// int r = rgbInput[i * width + (width - j - 1)];
	// if (r == transparent) {
	// rgbOutput[j * height + i] = 0;
	// } else {
	// rgbOutput[j * height + i] = rgbInput[i * width + (width - j - 1)];// |=
	// // 0xff
	// // <<
	// // 24;
	// }
	//
	// }
	// }
	//
	// return Image.createRGBImage(rgbOutput, height, width, true);
	//
	// }

	// public static Image resizeImage(Image img, int newWidth, int newHeight) {
	//
	// int srcWidth = img.getWidth();
	// int srcHeight = img.getHeight();
	//
	// int horRatio = (srcWidth << 16) / newWidth;
	// int verRatio = (srcHeight << 16) / newHeight;
	//
	// int rgbInput[] = new int[srcWidth * srcHeight];
	// int rgbOutput[] = new int[srcWidth * newHeight];
	// int rgbOutput2[] = new int[newWidth * newHeight];
	//
	// img.getRGB(rgbInput, 0, srcWidth, 0, 0, srcWidth, srcHeight);
	//
	// // Vertical Resize
	// for (int i = 0; i < newWidth; i++) {
	// for (int j = 0; j < srcHeight; j++) {
	//
	// // System.out.println(srcWidth * srcHeight);
	// // System.out.println(srcWidth * newHeight);
	// // System.out.println(j * newWidth + i);
	// // System.out.println(Math.min(j * srcWidth + ((i *
	// // horRatio) >>
	// // 16), srcWidth * srcHeight));
	// if (j * newWidth + i < srcWidth * newHeight)
	// rgbOutput[j * newWidth + i] = rgbInput[Math.min(j * srcWidth + ((i *
	// horRatio) >> 16),
	// (srcWidth * srcHeight) - 1)];
	// }
	// }
	//
	// // Horizontal resize
	// for (int i = 0; i < newWidth; i++) {
	// for (int j = 0; j < newHeight; j++) {
	// if (j * newWidth + i < newWidth * newHeight)
	// rgbOutput2[j * newWidth + i] = rgbOutput[Math.min(((j * verRatio) >> 16)
	// * newWidth + i,
	// srcWidth * newHeight - 1)];
	// }
	// }
	// return Image.createRGBImage(rgbOutput2, newWidth, newHeight, true);
	//
	// }
	//
	// public static Image rescaleImage(Image image, int width, int height) {
	// int sourceWidth = image.getWidth();
	// int sourceHeight = image.getHeight();
	//
	// Image newImage = Image.createImage(width, height);
	// Graphics g = newImage.getGraphics();
	//
	// for (int y = 0; y < height; y++) {
	// for (int x = 0; x < width; x++) {
	// g.setClip(x, y, 1, 1);
	// int dx = x * sourceWidth / width;
	// int dy = y * sourceHeight / height;
	// g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
	// }
	// }
	//
	// return Image.createImage(newImage);
	// }

	// private static Image resizeImageByBricks(Image img, int brickSize, int
	// newBrickSize) {
	//
	// int srcWidth = img.getWidth();
	// int srcHeight = img.getHeight();
	//
	// int newWidth = (srcWidth / brickSize) * newBrickSize;
	// int newHeight = (srcHeight / brickSize) * newBrickSize;
	//
	// int horRatio = (srcWidth << 16) / newWidth;
	// int verRatio = (srcHeight << 16) / newHeight;
	//
	// int rgbInput[] = new int[srcWidth * srcHeight];
	// int rgbOutput[] = new int[srcWidth * newHeight];
	// int rgbOutput2[] = new int[newWidth * newHeight];
	//
	// img.getRGB(rgbInput, 0, srcWidth, 0, 0, srcWidth, srcHeight);
	//
	// // Horizontal Resize
	// for (int i = 0; i < newWidth; i++) {
	// for (int j = 0; j < srcHeight; j++) {
	// if (i % newBrickSize == 0) {
	// rgbOutput[j * newWidth + i] = rgbInput[j * srcWidth + (i / newBrickSize)
	// * brickSize];
	// } else {
	// rgbOutput[j * newWidth + i] = rgbInput[j * srcWidth + ((i * horRatio) >>
	// 16)];
	// }
	// }
	// }
	//
	// // Vertical resize
	// for (int i = 0; i < newWidth; i++) {
	// for (int j = 0; j < newHeight; j++) {
	// if (j % newBrickSize == 0) {
	// rgbOutput2[j * newWidth + i] = rgbOutput[(j / newBrickSize) * brickSize *
	// newWidth + i];
	// } else {
	// rgbOutput2[j * newWidth + i] = rgbOutput[((j * verRatio) >> 16) *
	// newWidth + i];
	// }
	// }
	// }
	//
	// return Image.createRGBImage(rgbOutput2, newWidth, newHeight, true);
	// }
	//
	// public static Image cutLine(Image img, int lineNumber, int brickSize) {
	//
	// int srcWidth = img.getWidth();
	// int srcHeight = img.getHeight();
	// int newHeight = srcHeight - brickSize;
	// if (newHeight <= 0)
	// return null;
	//
	// int rgbInput[] = new int[srcWidth * srcHeight];
	// int rgbOutput[] = new int[srcWidth * newHeight];
	//
	// img.getRGB(rgbInput, 0, srcWidth, 0, 0, srcWidth, srcHeight);
	//
	// int i, j, offset;
	// offset = 0;
	//
	// for (i = 0; i < newHeight; i++) {
	// if (i == lineNumber * brickSize) {
	// offset = srcWidth * brickSize;
	// }
	// for (j = 0; j < srcWidth; j++) {
	// rgbOutput[i * srcWidth + j] = rgbInput[i * srcWidth + j + offset];
	// }
	// }
	//
	// return Image.createRGBImage(rgbOutput, srcWidth, newHeight, true);
	// }
	//
	// public static Image cutLineCreateBorders(Image img, Image sImg, int
	// bordersToCreate, int lineNumber,
	// int brickSize) {
	//
	// int srcWidth = img.getWidth();
	// int srcHeight = img.getHeight();
	// int newHeight = srcHeight - brickSize;
	// if (newHeight <= 0)
	// return null;
	//
	// int rgbInput[] = new int[srcWidth * srcHeight];
	// int rgbOutput[] = new int[srcWidth * newHeight];
	//
	// img.getRGB(rgbInput, 0, srcWidth, 0, 0, srcWidth, srcHeight);
	//
	// int i, j, offset;
	// offset = 0;
	// // create image
	// for (i = 0; i < newHeight; i++) {
	// if (i == lineNumber * brickSize) {
	// offset = srcWidth * brickSize;
	// }
	// for (j = 0; j < srcWidth; j++) {
	// rgbOutput[i * srcWidth + j] = rgbInput[i * srcWidth + j + offset];
	// }
	// }
	//
	// // create borders
	//
	// return Image.createRGBImage(rgbOutput, srcWidth, newHeight, true);
	// }

	private static String getFName(int color, int i, int j) {
		String ret;
		if (color == 0) {
			ret = "/z";
		} else {
			ret = "/n";
		}
		switch (i) {
		case 0:
			ret += "p";
			break;
		case 1:
			ret += "s";
			break;
		case 2:
			ret += "l";
			break;
		case 3:
			ret += "r";
			break;
		case 4:
			ret += "ls";
			break;
		case 5:
			ret += "rs";
			break;
		}
		ret += j + ".png";
		// System.out.println(ret);
		return ret;
	}

}

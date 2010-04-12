package game.elements;

import game.Images;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Road {
	Image images[];
	int currentImage = 0;
	int[][] pixO;
	int[] pixT;
	int[][] roadEdgesO;
	// int[][][][] roadEdgesT;
	short x, y, startOffsetX;
	int width, height;
	long turnStart;
	int turnTimeLength;
	long lastPaint = 0;
	int test = 0;
	Image image;
	int cur = 0;
	int last = 0;
	int imagesLength;
	int xcenter;
	int rEdg;
	private int collisionsBack = 0;
	public static final int TRANSPARENT_COLOR = 0;

	public static final int KMH100_SEQUENCES_PER_1SEC = 13;
	//private static final int TRANSPARENT_PIXEL = -16775425;// -65536;

	public Road(short x, short y, short startOffsetX) {

		images = Images.imgsRoad;

		image = images[0];
		width = image.getWidth();
		height = image.getHeight();
		imagesLength = images.length;

		// pixO = new int[imagesLength][width * height];
		// pixT = new int[width * height];
		// roadEdgesT = new int[imagesLength][2][2][height];
		//roadEdgesO = new int[2][height];
		roadEdgesO = new int[2][100];
		rEdg = 0; // current road edges

		// putImagesToArrays();
		readEdges();
		// addTransparency();
		// recreateImages();

		this.x = (short) (x + startOffsetX);
		this.y = y;
		this.startOffsetX = startOffsetX;
		xcenter = x;

		// computeNewEdges();
		// transformToEdges();
		cur = (cur + 1) % imagesLength;
		// computeNewEdges();
		// transformToEdges();

	}
	
	public void reset(){
		lastPaint = 0;
		this.x = (short) (xcenter + startOffsetX);
	}

	public int getXCenter() {
		return xcenter;
	}

	private void readEdges() {
		//int j;
		//int c = (rEdg + 1) % 2;
		roadEdgesO[0][0] = 800;
		roadEdgesO[1][0] = 801;
		roadEdgesO[0][1] = 800;
		roadEdgesO[1][1] = 801;
		roadEdgesO[0][2] = 797;
		roadEdgesO[1][2] = 801;
		roadEdgesO[0][3] = 1115;
		roadEdgesO[1][3] = 1123;
		roadEdgesO[0][4] = 1434;
		roadEdgesO[1][4] = 1444;
		roadEdgesO[0][5] = 1752;
		roadEdgesO[1][5] = 1766;
		roadEdgesO[0][6] = 2071;
		roadEdgesO[1][6] = 2087;
		roadEdgesO[0][7] = 2389;
		roadEdgesO[1][7] = 2409;
		roadEdgesO[0][8] = 2707;
		roadEdgesO[1][8] = 2731;
		roadEdgesO[0][9] = 3026;
		roadEdgesO[1][9] = 3052;
		roadEdgesO[0][10] = 3344;
		roadEdgesO[1][10] = 3374;
		roadEdgesO[0][11] = 3663;
		roadEdgesO[1][11] = 3695;
		roadEdgesO[0][12] = 3981;
		roadEdgesO[1][12] = 4017;
		roadEdgesO[0][13] = 4300;
		roadEdgesO[1][13] = 4338;
		roadEdgesO[0][14] = 4618;
		roadEdgesO[1][14] = 4660;
		roadEdgesO[0][15] = 4936;
		roadEdgesO[1][15] = 4982;
		roadEdgesO[0][16] = 5255;
		roadEdgesO[1][16] = 5303;
		roadEdgesO[0][17] = 5573;
		roadEdgesO[1][17] = 5625;
		roadEdgesO[0][18] = 5892;
		roadEdgesO[1][18] = 5946;
		roadEdgesO[0][19] = 6210;
		roadEdgesO[1][19] = 6268;
		roadEdgesO[0][20] = 6529;
		roadEdgesO[1][20] = 6589;
		roadEdgesO[0][21] = 6847;
		roadEdgesO[1][21] = 6911;
		roadEdgesO[0][22] = 7165;
		roadEdgesO[1][22] = 7233;
		roadEdgesO[0][23] = 7484;
		roadEdgesO[1][23] = 7554;
		roadEdgesO[0][24] = 7802;
		roadEdgesO[1][24] = 7876;
		roadEdgesO[0][25] = 8121;
		roadEdgesO[1][25] = 8197;
		roadEdgesO[0][26] = 8439;
		roadEdgesO[1][26] = 8519;
		roadEdgesO[0][27] = 8758;
		roadEdgesO[1][27] = 8840;
		roadEdgesO[0][28] = 9076;
		roadEdgesO[1][28] = 9162;
		roadEdgesO[0][29] = 9395;
		roadEdgesO[1][29] = 9483;
		roadEdgesO[0][30] = 9713;
		roadEdgesO[1][30] = 9805;
		roadEdgesO[0][31] = 10031;
		roadEdgesO[1][31] = 10127;
		roadEdgesO[0][32] = 10350;
		roadEdgesO[1][32] = 10448;
		roadEdgesO[0][33] = 10668;
		roadEdgesO[1][33] = 10770;
		roadEdgesO[0][34] = 10987;
		roadEdgesO[1][34] = 11091;
		roadEdgesO[0][35] = 11305;
		roadEdgesO[1][35] = 11413;
		roadEdgesO[0][36] = 11624;
		roadEdgesO[1][36] = 11734;
		roadEdgesO[0][37] = 11942;
		roadEdgesO[1][37] = 12056;
		roadEdgesO[0][38] = 12260;
		roadEdgesO[1][38] = 12378;
		roadEdgesO[0][39] = 12579;
		roadEdgesO[1][39] = 12699;
		roadEdgesO[0][40] = 12897;
		roadEdgesO[1][40] = 13021;
		roadEdgesO[0][41] = 13216;
		roadEdgesO[1][41] = 13342;
		roadEdgesO[0][42] = 13534;
		roadEdgesO[1][42] = 13664;
		roadEdgesO[0][43] = 13853;
		roadEdgesO[1][43] = 13985;
		roadEdgesO[0][44] = 14171;
		roadEdgesO[1][44] = 14307;
		roadEdgesO[0][45] = 14490;
		roadEdgesO[1][45] = 14628;
		roadEdgesO[0][46] = 14808;
		roadEdgesO[1][46] = 14950;
		roadEdgesO[0][47] = 15126;
		roadEdgesO[1][47] = 15272;
		roadEdgesO[0][48] = 15445;
		roadEdgesO[1][48] = 15593;
		roadEdgesO[0][49] = 15763;
		roadEdgesO[1][49] = 15915;
		roadEdgesO[0][50] = 16082;
		roadEdgesO[1][50] = 16236;
		roadEdgesO[0][51] = 16400;
		roadEdgesO[1][51] = 16558;
		roadEdgesO[0][52] = 16719;
		roadEdgesO[1][52] = 16879;
		roadEdgesO[0][53] = 17037;
		roadEdgesO[1][53] = 17201;
		roadEdgesO[0][54] = 17355;
		roadEdgesO[1][54] = 17523;
		roadEdgesO[0][55] = 17674;
		roadEdgesO[1][55] = 17844;
		roadEdgesO[0][56] = 17992;
		roadEdgesO[1][56] = 18166;
		roadEdgesO[0][57] = 18311;
		roadEdgesO[1][57] = 18487;
		roadEdgesO[0][58] = 18629;
		roadEdgesO[1][58] = 18809;
		roadEdgesO[0][59] = 18948;
		roadEdgesO[1][59] = 19130;
		roadEdgesO[0][60] = 19266;
		roadEdgesO[1][60] = 19452;
		roadEdgesO[0][61] = 19585;
		roadEdgesO[1][61] = 19773;
		roadEdgesO[0][62] = 19903;
		roadEdgesO[1][62] = 20095;
		roadEdgesO[0][63] = 20221;
		roadEdgesO[1][63] = 20417;
		roadEdgesO[0][64] = 20540;
		roadEdgesO[1][64] = 20738;
		roadEdgesO[0][65] = 20858;
		roadEdgesO[1][65] = 21060;
		roadEdgesO[0][66] = 21177;
		roadEdgesO[1][66] = 21381;
		roadEdgesO[0][67] = 21495;
		roadEdgesO[1][67] = 21703;
		roadEdgesO[0][68] = 21814;
		roadEdgesO[1][68] = 22024;
		roadEdgesO[0][69] = 22132;
		roadEdgesO[1][69] = 22346;
		roadEdgesO[0][70] = 22450;
		roadEdgesO[1][70] = 22668;
		roadEdgesO[0][71] = 22769;
		roadEdgesO[1][71] = 22989;
		roadEdgesO[0][72] = 23087;
		roadEdgesO[1][72] = 23311;
		roadEdgesO[0][73] = 23406;
		roadEdgesO[1][73] = 23632;
		roadEdgesO[0][74] = 23724;
		roadEdgesO[1][74] = 23954;
		roadEdgesO[0][75] = 24043;
		roadEdgesO[1][75] = 24275;
		roadEdgesO[0][76] = 24361;
		roadEdgesO[1][76] = 24597;
		roadEdgesO[0][77] = 24679;
		roadEdgesO[1][77] = 24919;
		roadEdgesO[0][78] = 24998;
		roadEdgesO[1][78] = 25240;
		roadEdgesO[0][79] = 25316;
		roadEdgesO[1][79] = 25562;
		roadEdgesO[0][80] = 25635;
		roadEdgesO[1][80] = 25883;
		roadEdgesO[0][81] = 25953;
		roadEdgesO[1][81] = 26205;
		roadEdgesO[0][82] = 26272;
		roadEdgesO[1][82] = 26526;
		roadEdgesO[0][83] = 26590;
		roadEdgesO[1][83] = 26848;
		roadEdgesO[0][84] = 26909;
		roadEdgesO[1][84] = 27169;
		roadEdgesO[0][85] = 27227;
		roadEdgesO[1][85] = 27491;
		roadEdgesO[0][86] = 27545;
		roadEdgesO[1][86] = 27813;
		roadEdgesO[0][87] = 27864;
		roadEdgesO[1][87] = 28134;
		roadEdgesO[0][88] = 28182;
		roadEdgesO[1][88] = 28456;
		roadEdgesO[0][89] = 28501;
		roadEdgesO[1][89] = 28777;
		roadEdgesO[0][90] = 28819;
		roadEdgesO[1][90] = 29099;
		roadEdgesO[0][91] = 29138;
		roadEdgesO[1][91] = 29420;
		roadEdgesO[0][92] = 29456;
		roadEdgesO[1][92] = 29742;
		roadEdgesO[0][93] = 29774;
		roadEdgesO[1][93] = 30064;
		roadEdgesO[0][94] = 30093;
		roadEdgesO[1][94] = 30385;
		roadEdgesO[0][95] = 30411;
		roadEdgesO[1][95] = 30707;
		roadEdgesO[0][96] = 30730;
		roadEdgesO[1][96] = 31028;
		roadEdgesO[0][97] = 31048;
		roadEdgesO[1][97] = 31350;
		roadEdgesO[0][98] = 31367;
		roadEdgesO[1][98] = 31671;
		roadEdgesO[0][99] = 31685;
		roadEdgesO[1][99] = 31993;
		// for (int i = 0; i < height; i++) {
		// for (j = 0; j < width; j++) {
		//
		// if (pixO[1][(i * width) + j] != TRANSPARENT_PIXEL) {
		// roadEdgesO[0][i] = i * width + j;
		// // System.out.println("edge s "+j+" "+pixO[0][(i * width) +
		// // j] );
		// System.out.println("roadEdgesO[0]["+i+"] = "+(i * width + j)+";");
		// // for (int k = 0; k < imagesLength; k++) {
		// // roadEdgesT[k][c][0][i] = i * width + j;
		// // roadEdgesT[k][rEdg][0][i] = i * width + j;
		// // }
		// break;
		// }
		// }
		// for (j = width - 1; j >= 0; j--) {
		// if (pixO[1][i * width + j] != TRANSPARENT_PIXEL) {
		// System.out.println("roadEdgesO[1]["+i+"] = "+(i * width + j)+";");
		// roadEdgesO[1][i] = i * width + j;
		// // System.out.println("edge e "+j);
		// // for (int k = 0; k < imagesLength; k++) {
		// // roadEdgesT[k][c][1][i] = i * width + j;
		// // roadEdgesT[k][rEdg][1][i] = i * width + j;
		// // }
		// break;
		// }
		// }
		// }

	}

	public void recreateImages() {
		for (int i = 0; i < imagesLength; i++) {
			images[i] = Image.createRGBImage(pixO[i], width, height, true);
		}
	}

	public void setCollisionBack(int sequences) {
		this.collisionsBack = sequences;
	}

	public int setGetSequencesToMove(int speed) {
		int ret = 0;
		if (collisionsBack != 0) {
			ret = -1;
			collisionsBack--;
			cur = (cur + imagesLength + ret) % imagesLength;
		} else {

			long currT = System.currentTimeMillis();

			if (lastPaint == 0) {
				lastPaint = currT;
			} else {

				if (speed > 0) {
					ret = speed * (int) (((currT - lastPaint) / 100) * KMH100_SEQUENCES_PER_1SEC) / 1000;
				}

				if (ret > 0) {
					lastPaint = currT;
					last = cur;
					cur = (cur + ret) % imagesLength;
				}
			}
		}
		return ret;
	}

	public void shiftTime() {
		lastPaint = System.currentTimeMillis();
	}

	public int move(int right) {
		x += right;
		return xcenter - x;
	}

	public void paint(Graphics g) {
		// computeNewEdges();
		// transformToEdges();
		// transformSpeed();

		// Image img = Image.createRGBImage(pixT, width, height, true);

		// g.drawImage(img, x, y, Graphics.HCENTER | Graphics.TOP);
		g.drawImage(images[cur], x, y, Graphics.HCENTER | Graphics.TOP);
		// g.drawRGB(pixT, 0, width, x, y, width, height, true);
	}

	public short getX() {
		return x;
	}

	public short getY() {
		return y;
	}

	public int getHeight() {
		return images[cur].getHeight();
	}

	public int getWidth(int h) {

		int hh = h;
		if (hh >= roadEdgesO[0].length - 1)
			hh = roadEdgesO[0].length - 1;
		if (hh < 0)
			hh = 0;

		return (roadEdgesO[1][hh] - roadEdgesO[0][hh]);
	}

	public int getWidth() {
		return images[cur].getWidth();
	}

	public int getEdgeLeft(int h) {
		return (roadEdgesO[0][h]) % width;
	}

	public int getEdgeRight(int h) {
		return (roadEdgesO[1][h]) % width;
	}

	// private void transformToEdges() {
	//
	// // if (turnStart + turnTimeLength >= System.currentTimeMillis()) {
	// // int k;
	// int c = (rEdg + 1) % 2;
	//
	// // for (int i = 0; i < height; i++) {
	// // // for (int j = Math.max(width*i,
	// // // Math.min(roadEdgesT[cur][rEdg][0][i],
	// // // roadEdgesT[last][c][0][i])), k = roadEdgesO[0][i]; j < Math
	// // // .min(Math.max(roadEdgesT[cur][rEdg][1][i],
	// // // roadEdgesT[last][c][1][i]), width * (i + 1)); j++, k++) {
	// // int k;
	// // for (int j = width * i; j < width * (i + 1); j++) {
	// // if (j < roadEdgesT[cur][rEdg][0][i]) {
	// // pixT[j] = Road.TRANSPARENT_COLOR;
	// // } else {
	// // if (j > roadEdgesT[cur][rEdg][1][i]) {
	// // pixT[j] = Road.TRANSPARENT_COLOR;
	// //
	// // } else {
	// // int tt = roadEdgesT[cur][rEdg][1][i] - roadEdgesT[cur][rEdg][0][i];
	// // int rr = j - roadEdgesT[cur][rEdg][0][i];
	// // int ss = roadEdgesO[1][i] - roadEdgesO[0][i];
	// // if (tt != 0)
	// // pixT[j] = pixO[cur][roadEdgesO[0][i] + (ss * rr) / tt];
	// // else
	// // pixT[j] = pixO[cur][roadEdgesO[0][i]];
	// //
	// // }
	// // }
	// // }
	// // }
	// // rEdg = (rEdg + 1) % 2;
	// for (int i = 0; i < height; i++) {
	// for (int j = roadEdgesT[cur][rEdg][0][i]; j <
	// roadEdgesT[cur][rEdg][1][i]; j++) {
	// int tt = roadEdgesT[cur][rEdg][1][i] - roadEdgesT[cur][rEdg][0][i];
	// int rr = j - roadEdgesT[cur][rEdg][0][i];
	// int ss = roadEdgesO[1][i] - roadEdgesO[0][i];
	// if (tt != 0)
	// pixT[j] = pixO[cur][roadEdgesO[0][i] + (ss * rr) / tt];
	// else
	// pixT[j] = pixO[cur][roadEdgesO[0][i]];
	// }
	// }
	// }

	// private void transformSpeed() {
	//
	// }
	//
	// private void computeNewEdges() {
	// int t;
	// curX = x;
	// for (int i = 0; i < height; i++) {
	//
	// t = inTurn(i);
	// if (t != 0) {
	//
	// // roadEdgesT[cur][rEdg][0][i] = roadEdgesO[0][i] + t;
	// // roadEdgesT[cur][rEdg][1][i] = roadEdgesO[1][i] + t;
	// }
	// }
	//
	// }

	// private int inTurn(int w) {
	//
	// return Math.max(0, ((curX - xcenter) - w));
	// }

	// private void addTransparency() {
	// for (int j = 0; j < width * height; j++) {
	// for (int i = 0; i < imagesLength; i++) {
	// // System.out.println("kolo"+pixO[i][j]);
	//
	// if (pixO[i][j] == TRANSPARENT_PIXEL) {
	// pixO[i][j] = Road.TRANSPARENT_COLOR;
	// }
	//
	// }
	// if (pixT[j] == TRANSPARENT_PIXEL) {
	// pixT[j] = Road.TRANSPARENT_COLOR;
	// }
	// pixT[j] = Road.TRANSPARENT_COLOR;
	// }
	//
	// }

	// private void putImagesToArrays() {
	// for (int i = 0; i < imagesLength; i++) {
	// images[i].getRGB(pixO[i], 0, width, 0, 0, width, height);
	//
	// }
	//
	// // System.out.println("PIXI" + pixO[1][0]);
	// images[0].getRGB(pixT, 0, width, 0, 0, width, height);
	// }

}

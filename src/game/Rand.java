package game;

import java.util.Random;

public class Rand {

	private static Random r = new Random();

	public static int getRandFrom1(int to) {
		return ((Math.abs(r.nextInt() - r.nextInt())) % to) + 1;
	}

}

package game;

public class WallOfFameRecord {
	
	public WallOfFameRecord(String userName, int score) {
		this.userName = userName;
		this.score = score;
	}

	public String userName;
	public int score;

	public WallOfFameRecord(byte[] bytes) {
		String s = new String(bytes);
		int ind = s.indexOf(" ");

		this.score = Integer.parseInt(s.substring(0, ind));
		this.userName = s.substring(ind + 1);

	}

	public String getData() {
		String ret;
		int sc = (score % 100);
		ret = (score / 100) + " : ";
		if (sc < 10)
			ret += "0";
		ret += sc;
		return ret;
	}

	public byte[] getBytes() {
		return new StringBuffer((new Integer(score).toString()) + " " + userName).toString().getBytes();
	}

}

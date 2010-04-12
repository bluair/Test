package game;

import javax.microedition.rms.RecordComparator;

public class Comparator implements RecordComparator {

	public int compare(byte[] rec1, byte[] rec2) {

		String str1 = new String(rec1), str2 = new String(rec2);
		int ind1 = str1.indexOf(" ");
		int ind2 = str2.indexOf(" ");

		int score1 = Integer.parseInt(str1.substring(0, ind1));
		int score2 = Integer.parseInt(str2.substring(0, ind2));

		if (score1 == score2)
			return RecordComparator.EQUIVALENT;
		else if (score1 < score2)
			return RecordComparator.FOLLOWS;
		else
			return RecordComparator.PRECEDES;
	}
}
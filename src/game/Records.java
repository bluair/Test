package game;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class Records {

	private static String wallUrl = "EvoBest";
	private static int MAX_WALL_OF_FAME_RECORDS = 5;
	
	
	public static void setMaxEntries(int maxEntries){
		MAX_WALL_OF_FAME_RECORDS = maxEntries;
	}

	public static WallOfFameRecord[] getWallOfFameRecords(int type) {
		RecordStore rs = null;

		try {
			// RecordStore.deleteRecordStore(wallUrl);
			rs = RecordStore.openRecordStore(wallUrl + type, true);

			int numberOfRecords = rs.getNumRecords();

			if (numberOfRecords > 0) {
				WallOfFameRecord w[] = new WallOfFameRecord[numberOfRecords];
				Comparator c = new Comparator();

				RecordEnumeration re = rs.enumerateRecords(null, c, false);

				int i = w.length - 1;
				while (re.hasNextElement()) {

					byte byt1[] = re.nextRecord();

					w[i] = new WallOfFameRecord(byt1);
					i--;
				}
				rs.closeRecordStore();
				return w;
			} else {
				rs.closeRecordStore();
				return null;
			}

		} catch (Exception e) {
			try {
				if (rs != null)
					rs.closeRecordStore();
			} catch (RecordStoreNotOpenException e1) {
				e1.printStackTrace();
			} catch (RecordStoreException e1) {
				System.out.println("exc4" + e.getMessage());
			}
			System.out.println("exc0" + e.getMessage());
		}
		return null;
	}

	public static void addToWallOfFame(String userName, int score,int type) {
		RecordStore rs = null;

		try {
			rs = RecordStore.openRecordStore(wallUrl+type, true);
			WallOfFameRecord r = new WallOfFameRecord(userName, score);
			rs.addRecord(r.getBytes(), 0, r.getBytes().length);

			if (rs.getNumRecords() > MAX_WALL_OF_FAME_RECORDS) {
				removeWorst(type);
			}

			rs.closeRecordStore();

		} catch (Exception e) {
			try {
				if (rs != null)
					rs.closeRecordStore();
			} catch (Throwable e1) {
				System.out.println("exc5" + e1.getMessage());
			}
			System.out.println("exc1" + e.getMessage());
		}

	}

	private static void removeWorst(int type) {
		RecordStore rs = null;
		try {
			// RecordStore.deleteRecordStore(wallUrl);
			rs = RecordStore.openRecordStore(wallUrl+type, true);
			int numberOfRecords = rs.getNumRecords();

			if (numberOfRecords > 0) {
				WallOfFameRecord w[] = new WallOfFameRecord[numberOfRecords];
				Comparator c = new Comparator();

				RecordEnumeration re = rs.enumerateRecords(null, c, false);

				if (re.hasNextElement()) {
					int id = re.nextRecordId();
					rs.deleteRecord(id);
				}
				rs.closeRecordStore();

			} else {
				rs.closeRecordStore();
			}

		} catch (Exception e) {
			try {
				if (rs != null)
					rs.closeRecordStore();
			} catch (Throwable e1) {
			}
			System.out.println("exc0" + e.getMessage());
		}

	}

	public static int getWallOfFameAddLimit(int type) {

		WallOfFameRecord r[] = getWallOfFameRecords(type);

		if (r == null) {
			return 1000000;
		} else {
			int l = r.length;
			if (l < MAX_WALL_OF_FAME_RECORDS) {
				return 1000000;
			} else {
				return r[l - 1].score;
			}

		}

	}

}

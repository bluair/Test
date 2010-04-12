package game.log;

public class Logger {

	private static int maxEntries = 10;

	public static void setMaxVisibleEntries(int entries) {
		maxEntries = entries;
	}
	
	public static void addLog(String log){
		System.currentTimeMillis();
	}
	
	public String getLastLogs(){
		String ret = null;
		
		
		return ret;
	}
	
}

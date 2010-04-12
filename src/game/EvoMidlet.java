package game;

/*
 * @(#)Playevo.java	1.2 
 *
 */

import game.amrmusic.AmrPlayer;
import game.amrmusic.MusicPlayerListener;
import game.btl2cap.client.ConnectionService;
import game.btl2cap.server.ServerConnectionHandler;
import game.btl2cap.server.ServiceDiscoveryList;
import game.btl2cap.server.ServiceDiscoveryListener;
import game.screens.ErrorScreen;
import game.screens.FormGameOptions;
import game.screens.FormGetUserName;
import game.screens.FormProgress;
import game.screens.FormBestResults;
import game.screens.GameInstructions;
import game.screens.GamePauseMenu;
import game.screens.Pit2;
import game.screens.SplashAndGo;

import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

/**
 * 
 * @author Avram
 * 
 */
public final class EvoMidlet extends MIDlet implements ServiceDiscoveryListener,
		MusicPlayerListener {

	/** A list of start menu items */
//	private static String[] elements = { "1 Gracz", "2 Graczy - Serwer", "2 Graczy - Klient",
//			"Najlepsze wyniki", "Instrukcje", "Koniec" };
//
//	private static String[] elements2 = { "Dzien", "Noc", "<< Wstecz" };

	/** A menu list instance */
	//public List menu = new List("Play evolution.", List.IMPLICIT, elements, null);

//	/private List menuLevel = new List("Play Evolution.", List.IMPLICIT, elements2, null);

	// /////////////// BLUETOOTH ///////////////
	// General
	private final static String UUID = "4af244c22f8f46d6b8ba64595b0fd7e1";

	private final static String SERVICE_URL = "btl2cap://localhost:" + UUID;

	// Client server
	private ConnectionService connectionService = null;

	// Server client
	String serviceUrl = SERVICE_URL + ";authenticate=false;authorize=false;encrypt=false;name=btl2capEcho";

	private ServiceDiscoveryList serviceDiscoveryList = null;

	boolean serverUseAuthentication = false;

	boolean serverUseEncryption = false;

	Displayable pauseDisplayable = null;
	
	FormProgress pf;

	// //////////////////////////////

	Pit2 pit;

	SplashAndGo splash;

	//FormGameOptions fg;

	AmrPlayer musicPlayer;

	private Display display;

	List wallOfFame;

	private int stage = 0;

	public boolean isServer;

	public boolean standalone;

	public EvoMidlet() {
		display = Display.getDisplay(this);

	}

	public void startApp() {
		if (pauseDisplayable == null) {
			// System.out.println("start app");
			splash = new SplashAndGo(this);
			setCurrentDisplaySplash();
			splash.paintSplashSequence();
			//fg = new FormGameOptions(display, menu);
			setCurrentDisplayMainMenu();
			runMusic();
		} else {
			display.setCurrent(pauseDisplayable);
			if (pit != null) {
				pit.resumeGame();
			}
		}

	}

	private void runMusic() {
		// System.out.println("trying to play music");
		try {
			if (musicPlayer == null) {
				musicPlayer = new AmrPlayer(this);
				try {
					musicPlayer.start();
				} catch (Exception e) {
				}
			}
		} catch (Throwable e) {

		}
	}

	private void stopMusic() {
		if (musicPlayer != null) {
			musicPlayer.stop();
		}
	}

	/**
	 * Destroys the application.
	 */
	public void destroyApp(boolean unconditional) {

		// stopMusic();

		if (serviceDiscoveryList != null) {
			serviceDiscoveryList.cancelPendingSearches();
		}

		if (connectionService != null) {
			connectionService.close();
		}

		if (pit != null) {
			pit.destroy();
		}

		stopMusic();

		notifyDestroyed();
	}

	/**
	 * Does nothing. Redefinition is required by MIDlet class.
	 */
	protected void pauseApp() {
		// System.out.println("pause!");
		// pauseDisplayable = Display.getDisplay(this).getCurrent();
		// if (pit != null) {
		// pit.pauseGame();
		// }
	}

	public void startGameServer(int level) {

		createNewPit(Pit2.USER_SERVER, level);

		// if (GameOptions.playMusic == true) {
		// runMusic();
		// // wait
		// synchronized (this) {
		// try {
		// wait();
		// } catch (InterruptedException e) {
		// }
		// }
		// }

		runBTServer();
	}

	public void startGameClient(int level) {
		// if (GameOptions.playMusic == true) {
		// runMusic();
		// // wati
		// synchronized (this) {
		// try {
		// wait();
		// } catch (InterruptedException e) {
		// }
		// }
		// }

		createNewPit(Pit2.USER_CLIENT, level);
		runBTClient();
	}

	public void startGameSingle(int level) {
		// if (GameOptions.playMusic == true) {
		// runMusic();
		// synchronized (this) {
		// try {
		// wait();
		// } catch (InterruptedException e) {
		// }
		// }
		// }

		createNewPit(Pit2.USER_SINGLE, level);
		
		setCurrentDisplayPit();

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
				startGameSingle(level);
			} else {
				if (isServer) {
					startGameServer(level);
				} else {
					startGameClient(level);
				}
			}

		}

	}

//	public void commandAction(Command c, Displayable d) {
//		System.out.println(stage);
//		if (stage == 0) {
//			switch (menu.getSelectedIndex()) {
//			case 0: // start single
//
//				isServer = false;
//				standalone = true;
//				changeStage(1);
//				break;
//			case 2:// start client
//				isServer = false;
//				standalone = false;
//				setCurrentDisplayProgressBar("moment...");
//				GameStarter gs = new GameStarter(isServer, standalone, 0);
//				Thread gsthread = new Thread(gs);
//				gsthread.start();
//				break;
//			case 1:// start server
//				isServer = true;
//				standalone = false;
//				changeStage(1);
//				break;
//
//			case 3:
//				setCurrentDisplayWallOfFame(menu);
//				break;
//			case 4: // options
//				setCurrentDisplayInstructions(menu);
//				break;
//			case 5: {// exit game
//				destroyApp(true);
//				return;
//
//			}
//			default:
//				break;
//			}
//		} else {
//			// System.out.println("tu "+menuLevel.getSelectedIndex());
//			if (menuLevel.getSelectedIndex() == 2) {
//
//				changeStage(0);
//			} else {
//				this.stage = 0;
//				setCurrentDisplayProgressBar("moment...");
//				GameStarter gs = new GameStarter(isServer, standalone, menuLevel.getSelectedIndex());
//				Thread gsthread = new Thread(gs);
//				gsthread.start();
//			}
//
//		}
//	}

	public void changeStage(int stage) {
		switch (stage) {
		case 0:
			setCurrentDisplayMainMenu();
			break;
		default:
			setCurrentDisplayLevelMenu();
			break;

		}

	}

	private void runBTClient() {
		serviceDiscoveryList = new ServiceDiscoveryList(this, UUID, DiscoveryAgent.GIAC);
		setCurrentDisplayDiscoveryList();
	}

	private void runBTServer() {
 
		try {
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
		} catch (BluetoothStateException e) {
			String msg = "Nalezy wlaczyc Bluetooth w telefonie";// + e.getMessage();
			ErrorScreen.showError(msg, display, splash);
			
		}

		connectionService = new ConnectionService(serviceUrl, pit);

	}

	public void makeConnection(ServiceRecord serviceRecord) {

		ServerConnectionHandler newHandler = new ServerConnectionHandler(pit, serviceRecord,
				ServiceRecord.NOAUTHENTICATE_NOENCRYPT);
		newHandler.start(); // start reader & writer
	}

	/**
	 * Returns the displayable object of this screen - it is required for Alert
	 * construction for the error cases.
	 */
	Displayable getDisplayable() {

		return splash;
	}

	public void createNewPit(int userType, int level) {
		pit = new Pit2(this, userType, level);

		try {
			// Start the game in its own thread
			Thread pitThread = new Thread(pit);
			pitThread.start();

		} catch (Error e) {
			// destroyApp(false);
			// notifyDestroyed();
			setCurrentDisplayMainMenu();
		}
	}

	public void setCurrentDisplayPit() {
		// System.out.println("setCurrentDisplayPit");
		display.setCurrent(pit);
	}

	private void setCurrentDisplaySplash() {
		// System.out.println("setCurrentDisplayPit");
		display.setCurrent(splash);
	}

	public void setCurrentDisplayProgressBar(String description) {
		// System.out.println("setCurrentDisplayProgressBar");
		pf = new FormProgress(description);
		display.setCurrent(pf);
	}

	public void setCurrentDisplayGetUserName() {
		FormGetUserName f = new FormGetUserName(this);
		display.setCurrent(f);
	}

	public void setCurrentDisplayMainMenu() {
		// System.out.println("setCurrentDisplayMainMenu");
		splash.go();
		display.setCurrent(splash);
		splash.setCurrent(1);
	}

	public void setCurrentDisplayLevelMenu() {
		// System.out.println("setCurrentDisplayMainMenu");
		// display.setCurrent(menuLevel);
		splash.setCurrent(2);

	}

	public void setCurrentDisplayPauseGame() {
		// System.out.println("setCurrentDisplayPauseGame");
		Displayable pauseGameMenu = new GamePauseMenu(pit);
		display.setCurrent(pauseGameMenu);

	}

	public void setCurrentDisplayWallOfFame() {
		// ErrorScreen.showError("Here will be wall of fame", display, menu);
		wallOfFame = new FormBestResults(this);
		display.setCurrent(wallOfFame);

	}

	public void setCurrentDisplayInstructions(Displayable next) {
		GameInstructions instructions = new GameInstructions(this);
		// instructions.setString("sasdfasdfasdfasdf");
		display.setCurrent(instructions);

	}

//	private void setCurrentDisplayOptions(Displayable next) {
//
//		display.setCurrent(fg);
//	}

	public void setCurrentDisplayDiscoveryList() {
		display.setCurrent(serviceDiscoveryList);
	}

	public void serviceDiscoveryListBackRequest(Displayable next) {
		System.out.println("serviceDiscoveryListBackRequest");

	}

	public void serviceDiscoveryListError(String errorMessage) {
		System.out.println("serviceDiscoveryListError");
		// stopMusic();
		// wyjebka setCurrentDisplayMainMenu();

	}

	public void serviceDiscoveryListExitRequest() {
		System.out.println("serviceDiscoveryListExitRequest");
		// stopMusic();
		setCurrentDisplayMainMenu();
	}

	public void serviceDiscoveryListFatalError(String errorMessage) {
		// stopMusic();
		setCurrentDisplayMainMenu();
		System.out.println("serviceDiscoveryListFatalError");

	}

	public void serviceDiscoveryListOpen(Vector selectedServiceRecords) {
		// should be one connection only here
		ServiceRecord sr = (ServiceRecord) selectedServiceRecords.firstElement();
		makeConnection(sr);
		System.out.println("list");

	}

	public void serviceDiscoveryListViewLog(Displayable next) {
		// System.out.println("serviceDiscoveryListViewLog");

	}

	public void notifyGameFinished() {
		if (connectionService != null) {
			connectionService.close();
		}
		// if (musicPlayer != null) {
		// musicPlayer.stop();
		// }

	}

	private synchronized void notifyPlayerStarted() {
		//notify();
	}

	public void handlePlayerStarted() {
		notifyPlayerStarted();

	}

	public void handlePlayerRunError(String message) {
		notifyPlayerStarted();

	}

	public void wakeUpWaitingForUserName() {
		this.pit.notifyWaitingForGetUserName();

	}

	public void handleUserNameFilled(String userName) {
		pit.setUserName(userName);

	}

	
	public void addProgressInfo(String info){
		pf.addInfo(info);
	}

}

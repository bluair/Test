package game.screens;

import game.EvoMidlet;
import game.GameInterface;
import game.Images;
import game.Messages;
import game.Records;
import game.btl2cap.client.ClientConnectionHandler;
import game.btl2cap.client.ClientConnectionHandlerListener;
import game.btl2cap.server.ServerConnectionHandler;
import game.btl2cap.server.ServerConnectionHandlerListener;
import game.elements.Background;
import game.elements.Evo;
import game.elements.EvoResized;
import game.elements.Footer;
import game.elements.KilometersToEnd;
import game.elements.Level;
import game.elements.Road;
import game.elements.SpeedMeter;
import game.elements.TimeOfRide;
import game.elements.resized.GameCommunicates;
import game.elements.resized.GameEnder;
import game.elements.resized.GameStarter;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class Pit2 extends GameCanvas implements Runnable, ClientConnectionHandlerListener,
		ServerConnectionHandlerListener, GameInterface {

	// /////////////////// PIT PARAMETERS
	protected static int BRICK_SIZE;
	public static int LEFT_OFFSET = 40;
	public static int UP_OFFSET = 5;

	public final static int USER_SINGLE = 0;
	public final static int USER_CLIENT = 1;
	public final static int USER_SERVER = 2;

	private static final short MAX_UP = 0;
	private static short MAX_RIGHT = 176;
	private static final byte MESSAGE_CODE_LEVEL = 0;
	private static final byte MESSAGE_CODE_POSITION = 1;
	private static final byte MESSAGE_CODE_END_OF_GAME = 2;
	private static final byte MESSAGE_CODE_NEW_GAME = 3;
	private static final byte MESSAGE_CODE_BUMP = 4;
	private static short CENTER = 0;

	private int userType;

	int keyState;
	private int width;
	private int height;
	private String userName = "";
	private EvoMidlet midlet;
	private Level level;

	// GAME STATE
	private boolean gameDestroyed = false;
	private boolean gameEnd = true;
	private boolean gamePaused = true;
	private boolean gameStarted = false;

	private int numberOfPlayers;

	int opponentSequence = 0;

	// ///////////// BLUETOOTH //////////////
	private ClientConnectionHandler clientHandler = null;
	private ServerConnectionHandler serverHandler = null;
	private boolean connectionLost = false;

	// paint
	private Graphics g;
	private Road road;
	private Evo evo;
	private EvoResized evoOpponent;
	private Background background;
	private SpeedMeter speedMeter;
	private KilometersToEnd kilometersToEnd;
	private TimeOfRide timeOfRide;
	private Footer footer;
	private GameStarter gameStarter;
	private GameEnder gameEnder;
	private GameCommunicates gameCommunicates;

	private boolean backgroundPainted;

	// strings
	private boolean opponentReceivedLevel = false;
	private int levelInt;
	private boolean startingGame = false;
	private boolean endingGame = false;
	private int opponentTimeOfRide = 0;
	private boolean receivedLevel;
	private boolean receivedNewGame = false;
	private boolean firstGame = true;
	private boolean waitingForNewGame;
	private boolean waitingForOpponent;

	public Pit2(EvoMidlet midlet, int userType, int levelInt) {
		super(false);
		// try {
		midlet.addProgressInfo("1");
		this.levelInt = levelInt;
		midlet.addProgressInfo("10");
		g = this.getGraphics();
		midlet.addProgressInfo("11");
		setFullScreenMode(true);
		midlet.addProgressInfo("12");
		Images.createImagesDependingOnScreen(getWidth(), getHeight(), midlet, userType);
		midlet.addProgressInfo("2");

		MAX_RIGHT = (short) getWidth();

		CENTER = (short) (MAX_RIGHT / 2);
		// System.out.println("wi " + getWidth() + " he " + getHeight());

		this.userType = userType;
		if (userType == USER_SINGLE) {
			numberOfPlayers = 1;
		} else {
			numberOfPlayers = 2;
		}

		short yy = 70;
		short startOffsetX = -40;

		if (userType == USER_SINGLE) {
			road = new Road(CENTER, yy, startOffsetX);
			level = new Level(road, (byte) levelInt, 1);
			background = new Background((short) (CENTER + startOffsetX), Pit2.MAX_UP, levelInt);
			evo = new Evo(road, Evo.BLUE);

		}

		if (userType == USER_SERVER) {
			road = new Road(CENTER, yy, startOffsetX);
			level = new Level(road, (byte) levelInt, 2);
			background = new Background((short) (CENTER + startOffsetX), Pit2.MAX_UP, levelInt);
			int evoY = road.getY() + road.getHeight() / 2;
			evo = new Evo(road, Evo.BLUE);
			evoOpponent = new EvoResized(road, Evo.YELLOW, Level.MAX_VISIBLE_SEQUENCES);
			evoOpponent.setStartPosition(CENTER + 2 * startOffsetX, evoY);
			evo.setOpponent(evoOpponent);
		}

		if (userType == USER_CLIENT) {
			road = new Road(CENTER, yy, (short) -startOffsetX);
			level = new Level(road, (byte) levelInt, 2);
			background = new Background((short) (CENTER - startOffsetX), Pit2.MAX_UP, levelInt);
			int evoY = road.getY() + road.getHeight() / 2;
			evo = new Evo(road, Evo.YELLOW);
			evoOpponent = new EvoResized(road, Evo.BLUE, Level.MAX_VISIBLE_SEQUENCES);
			evoOpponent.setStartPosition(CENTER - 2 * startOffsetX, evoY);
			evo.setOpponent(evoOpponent);
		}

		int evoY = road.getY() + road.getHeight() / 2;
		evo.setStartPosition(CENTER, evoY);

		kilometersToEnd = new KilometersToEnd(MAX_RIGHT - 30, 10);
		timeOfRide = new TimeOfRide(50, 10);

		int off = 0;
		if (getWidth() == 240) {
			off = -10;
		}
		System.out.println("WI " + width + " of " + off);
		footer = new Footer(getWidth() / 2 + off, road.getY() + road.getHeight());
		speedMeter = new SpeedMeter(MAX_RIGHT - 42, footer.getY() + 18);

		this.midlet = midlet;
		init();
		midlet.addProgressInfo("3");
		gameCommunicates = new GameCommunicates(road);

		// } catch (Throwable e) {
		// e.printStackTrace();
		// System.out.println("error in pit2 run :" + e.getMessage());
		// }
	}

	public void getUserName() {
		midlet.setCurrentDisplayGetUserName();

		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				;
			}
		}
		midlet.setCurrentDisplayPit();
	}

	public boolean addToWallOfFame() {
		int score = timeOfRide.getMinutes() * 100 + timeOfRide.getSeconds();
		if (score < Records.getWallOfFameAddLimit(levelInt)) {
			finishGame(GameEnder.GAME_GOOD_RESULT);
			// userName variable should be filled

			getUserName();
			Records.addToWallOfFame(userName, score, levelInt);
			return true;
		}
		return false;

	}

	public void resetGame() {

		this.backgroundPainted = false;
		paint();
		midlet.setCurrentDisplayPit();

		this.gameDestroyed = false;
		this.gameEnd = true;
		this.gamePaused = false;
		this.gameStarted = false;
		this.backgroundPainted = false;

	}

	private void init() {

		width = getWidth();
		height = getHeight();
	}

	private synchronized void closeConnection() {
		if (clientHandler != null) {
			clientHandler.close();
			clientHandler = null;
		}
		if (serverHandler != null) {
			serverHandler.close();
			serverHandler = null;
		}
		connectionLost = true;

	}

	public synchronized void destroy() {
		closeConnection();

		this.gameDestroyed = true;
		this.gameEnd = true;
		notify();

	}

	private void checkAndSetPause() {
		keyState = getKeyStates();
		if (((keyState & GAME_A_PRESSED) > 0) || ((keyState & GAME_B_PRESSED) > 0)
				|| ((keyState & GAME_C_PRESSED) > 0) || ((keyState & GAME_D_PRESSED) > 0)) {
			pauseGame();

		}
	}

	private void waitForBluetooth() {
		// wait for bluetooth initialization
		if (numberOfPlayers > 1) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e1) {
				}
			}
		}

	}

	public void run() {

		// try {
		// DeviceControl.setLights(0,100);
		// Display.getDisplay(midlet).flashBacklight(100000);
		receivedLevel = false;
		gameDestroyed = false;
		opponentReceivedLevel = false;

		// wait for bluetooth
		waitForBluetooth();
		gamePaused = false;
		if (userType > 1) {
			waitForConnection();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}

		level.reset();
		// System.out.println("00");
		// client disconnects if lost connection
		if (userType == USER_CLIENT && connectionLost()) {
			exitGame();
			return;
		}
		// System.out.println("11");
		// send level
		if (userType == USER_SERVER && !connectionLost()) {
			System.out.println("level send");
			sendLevel(level.getInt());
			opponentReceivedLevel = true;
			// System.out.println("111");
			// waitForOpponentReceivedLevel();
			// System.out.println("112");
		}
		// System.out.println("22");
		// wait for level
		if (userType == USER_CLIENT && !connectionLost()) {
			waitForLevelInfo();
		}
		// System.out.println("33");

		while (!gameDestroyed) {
			System.out.println("USER " + userType);
			road.reset();
			level.reset();
			evo.reset();

			if (userType == USER_CLIENT) {
				evoOpponent.changePosition(road.getX(), 40, 0);
			} else if (userType == USER_SERVER) {
				System.out.println(" sssfs " + road.getX());
				evoOpponent.changePosition(road.getX(), -40, 0);
			}

			paint();

			waitSendNewGame();

			if (evoOpponent != null) {
				evoOpponent.reset();
				opponentTimeOfRide = 0;
				opponentSequence = 0;
			}

			gameStarted = false;
			kilometersToEnd.setKmStart(level.getRoadKilometers());

			evo.setLevel(level);
			backgroundPainted = false;
			gameEnd = false;
			level.startTime();
			startupGame();
			timeOfRide.startTime();
			System.gc();
			// if (evoOpponent != null)
			// evoOpponent.changePosition(road.getX());

			while (!gameEnd) {
				try {

					Thread.sleep(3);
					checkAndSetPause();
					evo.currentVibra = 0;

					short moveS = evo.wheel(keyState & LEFT_PRESSED, keyState & RIGHT_PRESSED);

					if (evo.bump != 0) {
						sendBump(evo.bump);
					}

					evo.brake(keyState & DOWN_PRESSED);
					// evo.speedup(keyState & UP_PRESSED);
					int posX = -1;
					if (moveS != 0) {

						posX = road.move(moveS);
						// posX = (posX * 100) / road.getHeight();
						background.move(moveS);
						// System.out.println("moved " + moveS);
						level.move(moveS);
						if (evoOpponent != null) {
							evoOpponent.changePosition(road.getX());
						}
					}

					speedMeter.setSpeed(evo.getSpeed());
					if (evoOpponent == null) {
						int seqToMove = road.setGetSequencesToMove(evo.getSpeed());
						kilometersToEnd.movedXSequences(seqToMove);
						int seq = level.movedXSequences(seqToMove);
					} else {

						// if (!evoOpponent.collidesWith(evo, true)) {

						int seqToMove = road.setGetSequencesToMove(evo.getSpeed());
						kilometersToEnd.movedXSequences(seqToMove);
						int seq = level.movedXSequences(seqToMove);
						if (Images.porting == Images.SMALL) {
							sendPosition(seq, (posX * 100) / road.getHeight(), evo
									.getCurrentImageIndexToSend());
						} else {
							sendPosition(seq, posX, evo.getCurrentImageIndexToSend());
						}
						// } else {
						// int seqToMove = road.setGetSequencesToMove(0);
						// kilometersToEnd.movedXSequences(seqToMove);
						// int seq = level.movedXSequences(seqToMove);
						// sendPosition(seq, posX, evo.getCurrentImageIndex());
						// }
					}

					if (evo.currentVibra > 0) {
						// System.out.println("vibra");
						Display.getDisplay(midlet).vibrate(evo.currentVibra);
					}

					timeOfRide.computeTime();
					// if (timeOfRide.changedPeriod()){
					// timeOfRide.setMark();
					// Display.getDisplay(midlet).flashBacklight(1000000);
					// }

					// check if end of game
					if (level.endOfGame()) {
						endGame();
						break;
					}
					paint();

				} catch (InterruptedException e) {

				} catch (IllegalMonitorStateException e) {

				}
			}
		}
		// } catch (Throwable e) {
		// e.printStackTrace();
		// System.out.println("error in pit2 run :" + e.getMessage());
		// }
	}

	private void waitSendNewGame() {

		if (!firstGame) {
			waitingForNewGame = true;
			waitForNewGamePushed();
			waitingForNewGame = false;
			sendNewGame();
			waitingForOpponent = true;
			while (!receivedNewGame && !connectionLost()) {
				try {
					checkAndSetPause();
					Thread.sleep(2);
					backgroundPainted = false;
					paint();
				} catch (InterruptedException e) {
				}
			}
			receivedNewGame = false;
			waitingForOpponent = false;
		} else {
			firstGame = false;
		}

	}

	private void waitForNewGamePushed() {
		while (!connectionLost() && !gameDestroyed) {
			try {
				keyState = getKeyStates();
				if ((keyState & FIRE_PRESSED) > 0) {
					break;
				}
				checkAndSetPause();
				Thread.sleep(2);
				paint();
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private void waitForLevelInfo() {

		while (!receivedLevel && !connectionLost() && !gameDestroyed) {
			try {
				checkAndSetPause();
				Thread.sleep(2);
				backgroundPainted = false;
				paint();
			} catch (InterruptedException e) {
				break;
			}
		}

	}

	private void endGame() {
		gameEnd = true;
		timeOfRide.stopTime();
		if (userType == USER_SINGLE) {
			if (addToWallOfFame()) {
				exitGame();
				midlet.setCurrentDisplayWallOfFame();
			} else {
				finishGame(GameEnder.GAME_TRY_AGAIN);
				exitGame();
			}
		} else {
			System.out.println("okończone");
			int myTimeOfRide = timeOfRide.getPreciseTimeOfRide();
			sendFinishedGame(myTimeOfRide);
			// System.out.println("my time " + myTimeOfRide + " " +
			// opponentTimeOfRide);
			if (opponentTimeOfRide <= 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			if (opponentTimeOfRide >= myTimeOfRide || opponentTimeOfRide == 0) {
				finishGame(GameEnder.GAME_WIN);
				// exitGame();
			} else {
				finishGame(GameEnder.GAME_LOOSE);
				// exitGame();
			}
		}
	}

	private boolean connectionLost() {
		return connectionLost;
	}

	private void waitForOpponentReceivedLevel() {

		while (!opponentReceivedLevel && !connectionLost()) {
			try {
				checkAndSetPause();
				// setCurrentInfo((getCurrentInfo() == null) ?
				// INFO_WAITING_FOR_OPPONENT_PUSH_PLAY : null);
				Thread.sleep(10);
				backgroundPainted = false;
				paint();

			} catch (InterruptedException e) {
				break;
			}
		}
		// setCurrentInfo(null);

	}

	private void waitForConnection() {
		while (!connectionIsReady()) {
			try {
				// setCurrentInfo((getCurrentInfo() == null) ?
				// INFO_WAITING_FOR_CONNECTIONS : null);
				checkAndSetPause();
				Thread.sleep(10);
				backgroundPainted = false;
				paint();

			} catch (InterruptedException e) {
				break;
			}
		}
		connectionLost = false;
		// setCurrentInfo(null);

	}

	private void startupGame() {
		gameStarter = new GameStarter(road);
		startingGame = true;
		try {
			Thread.sleep(1000);
			for (int i = 2; i >= 0; i--) {

				// setCurrentInfo((getCurrentInfo() == null) ?
				// INFO_PRESS_START_TO_PLAY : null);
				paint();
				Thread.sleep(1000);
				backgroundPainted = false;

			}
			backgroundPainted = false;
			paint();
		} catch (InterruptedException e) {
		}

		startingGame = false;
		gameStarted = true;
	}

	private void finishGame(int type) {

		gameEnder = new GameEnder(road, type);
		endingGame = true;
		for (int i = 2; i >= 0; i--) {
			try {
				paint();
				Thread.sleep(2000);
				backgroundPainted = false;
			} catch (InterruptedException e) {
			}
		}
		backgroundPainted = false;
		paint();
		endingGame = false;
	}

	private synchronized void sendPosition(int sequences, int x, byte carPerspective) {
		// BLUETOOTH send NEW BRICK FOR EXAMPLE
		byte message[] = new byte[10];

		message[0] = MESSAGE_CODE_POSITION;
		message[1] = (byte) (sequences >> 24);
		message[2] = (byte) (sequences >> 16);
		message[3] = (byte) (sequences >> 8);
		message[4] = (byte) sequences;
		message[5] = (byte) (x >> 24);
		message[6] = (byte) (x >> 16);
		message[7] = (byte) (x >> 8);
		message[8] = (byte) x;
		message[9] = carPerspective;

		if (serverHandler != null) {
			serverHandler.queueMessageForSending(Messages.MESSAGE_CODE1, message);
		} else if (clientHandler != null) {
			clientHandler.queueMessageForSending(Messages.MESSAGE_CODE1, message);
		}
	}

	private void sendBump(byte bump) {
		byte message[] = new byte[2];
		message[0] = MESSAGE_CODE_BUMP;
		message[1] = (byte) bump;
		// System.out.println("bump send");
		if (serverHandler != null) {
			serverHandler.queueMessageForSending(Messages.MESSAGE_CODE_BUMP, message);
		} else if (clientHandler != null) {
			clientHandler.queueMessageForSending(Messages.MESSAGE_CODE_BUMP, message);
		}
	}

	private void sendLevel(int level) {
		byte message[] = new byte[2];
		// System.out.println("level send");
		message[0] = MESSAGE_CODE_LEVEL;
		message[1] = (byte) level;
		if (serverHandler != null) {
			serverHandler.queueMessageForSending(Messages.MESSAGE_CODE_LEVEL, message);
		} else if (clientHandler != null) {
			clientHandler.queueMessageForSending(Messages.MESSAGE_CODE_LEVEL, message);
		}
	}

	private void sendNewGame() {
		byte message[] = new byte[1];

		message[0] = MESSAGE_CODE_NEW_GAME;
		if (serverHandler != null) {
			serverHandler.queueMessageForSending(Messages.MESSAGE_CODE_NEW_GAME, message);
		} else if (clientHandler != null) {
			clientHandler.queueMessageForSending(Messages.MESSAGE_CODE_NEW_GAME, message);
		}
	}

	public void sendFinishedGame(int myTimeOfRide) {
		byte message[] = new byte[5];
		message[1] = (byte) (myTimeOfRide >> 24);
		message[2] = (byte) (myTimeOfRide >> 16);
		message[3] = (byte) (myTimeOfRide >> 8);
		message[4] = (byte) myTimeOfRide;

		message[0] = MESSAGE_CODE_END_OF_GAME;
		if (serverHandler != null) {
			serverHandler.queueMessageForSending(Messages.MESSAGE_CODE_END_GAME, message);
		} else if (clientHandler != null) {
			clientHandler.queueMessageForSending(Messages.MESSAGE_CODE_END_GAME, message);
		}
	}

	public void pauseGame() {

		gamePaused = true;
		backgroundPainted = false;
		pauseTime();
		midlet.setCurrentDisplayPauseGame();
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

	}

	private void pauseTime() {
		timeOfRide.pauseTime();
		evo.pauseSpeed();
	}

	private void resumeTime() {
		road.shiftTime();
		timeOfRide.resumeTime();
	}

	public void resumeGame() {
		gamePaused = false;
		synchronized (this) {
			notify();
		}
		resumeTime();
		midlet.setCurrentDisplayPit();

	}

	public void startGame() {
		gameDestroyed = false;
		gameEnd = false;

	}

	public void exitGame() {

		this.gameDestroyed = true;
		this.gameEnd = true;

		destroy();

		midlet.notifyGameFinished();
		midlet.setCurrentDisplayMainMenu();

	}

	protected void hideNotify() {
		this.backgroundPainted = false;
	}

	protected void showNotify() {
		this.backgroundPainted = false;
	}

	public synchronized void BTReady() {
		midlet.setCurrentDisplayPit();
		notify();
	}

	private boolean connectionAccept() {
		return true;
	}

	// //////////// BLUETOOTH CONNECTION ////////////////

	// //////// server
	public void handleWaitingForConnections() {
		// setCurrentInfo("Waiting for opponents...");
		BTReady();
		Display.getDisplay(midlet).setCurrent(this);
	}

	public void handleAcceptAndOpen(ClientConnectionHandler handler) {

		if (connectionAccept()) {
			// setCurrentInfo(INFO_PRESS_START_TO_PLAY);
			clientHandler = handler;
			clientHandler.start();
		}

	}

	public void handleClose(ClientConnectionHandler handler) {
		// System.out.println("handleClose");
		closeConnection();
		// exitGame();

	}

	public void handleErrorClose(ClientConnectionHandler handler, String errorMessage) {
		// System.out.println("handleErrorClose");
		closeConnection();
		// exitGame();

	}

	public void handleQueuedMessageWasSent(ClientConnectionHandler handler, Integer id) {
		// System.out.println("handleQueuedMessageWasSent");
		// if (id.intValue() == Messages.MESSAGE_CODE_END_GAME.intValue()) {
		// exitGame();
		// }
		// if (id.intValue() == Messages.MESSAGE_CODE_LEVEL.intValue()) {
		// opponentReceivedLevel = true;
		// }

	}

	public void handleStreamsOpen(ClientConnectionHandler handler) {
		// System.out.println("streams opened");

	}

	public void handleStreamsOpenError(ClientConnectionHandler handler, String errorMessage) {
		// System.out.println("handleStreamsOpenError");
		closeConnection();
		// exitGame();

	}

	// ////////client
	public void handleClose(ServerConnectionHandler handler) {
		// System.out.println("ServerConnectionHandler");
		closeConnection();
		// exitGame();
	}

	public void handleErrorClose(ServerConnectionHandler handler, String errorMessage) {
		// System.out.println("handleErrorClose ServerConnectionHandler");
		// closeConnection();
		// exitGame();
	}

	public void handleError(ClientConnectionHandler handler, String errorMessage) {
		// System.out.println("handleError ClientConnectionHandler");
		// exitGame();
		closeConnection();
	}

	public void handleReceivedMessage(ClientConnectionHandler handler, byte[] messageBytes) {
		receiveMessage(messageBytes);
	}

	public void handleReceivedMessage(ServerConnectionHandler handler, byte[] messageBytes) {
		receiveMessage(messageBytes);
	}

	private synchronized void receiveMessage(byte[] messageBytes) {
		// String msg = new String(messageBytes);
		byte byt = messageBytes[0];

		// System.out.println("message received " + messageBytes[0]);
		switch (byt) {
		case MESSAGE_CODE_NEW_GAME:
			receivedNewGame = true;
			break;
		case MESSAGE_CODE_LEVEL:
			setLevel(messageBytes[1]);
			level.startTime();
			receivedLevel = true;
			break;
		case MESSAGE_CODE_BUMP:
			evo.startBump(messageBytes[1]);
			break;
		case MESSAGE_CODE_POSITION:

			int x = 0;
			byte persp = messageBytes[9];
			opponentSequence = messageBytes[1] << 24 | (messageBytes[2] & 0xff) << 16
					| (messageBytes[3] & 0xff) << 8 | (messageBytes[4] & 0xff);

			x = messageBytes[5] << 24 | (messageBytes[6] & 0xff) << 16 | (messageBytes[7] & 0xff) << 8
					| (messageBytes[8] & 0xff);
			// System.out.println("received x" + x);
			int seqDiff = opponentSequence - level.getCurrentSequence();
			if (x != -1) {
				// System.out.println("seq " + seq + " x" + x);
				// x = (x * road.getHeight()) / 100;

				evoOpponent.changePosition(road.getX(), x, seqDiff);

				// System.out.println("received position " + pos + " x" + x);
			} else {
				evoOpponent.setSequenceDifference(seqDiff);

				// evoOpponent.setSequenceDifference(50);
			}
			evoOpponent.setImageIndex(persp);
			break;
		case MESSAGE_CODE_END_OF_GAME:
			opponentTimeOfRide = messageBytes[1] << 24 | (messageBytes[2] & 0xff) << 16
					| (messageBytes[3] & 0xff) << 8 | (messageBytes[4] & 0xff);
			break;
		default:
			break;
		}
		// System.out.println("msg:" + msg);
		// int ind = msg.indexOf(' ');
		// if (ind > 0) { // we have additional data = it's wall of fame entry
		// System.out.println("entry:" + msg);
		// String user = msg.substring(ind + 1);
		// msg = msg.substring(0, ind);
		// int score = Integer.parseInt(msg);
		// if (score > Records.getWallOfFameAddLimit()) {
		// Records.addToWallOfFame(user, score);
		// }
		//
		// } else {// we have only code
		// try {
		// int i = Integer.parseInt(msg);
		// if (i == Messages.MESSAGE_GAME_END_FROM_LOOSER_INT) {
		// backgroundPainted = false;
		// setCurrentInfo("Connection closed");
		// closeConnection();
		// } else if (i == Messages.MESSAGE_PLAY_PUSHED_INT) {
		// opponentPushedPlay = true;
		// } else {
		// opponentData += i;
		// // opponentBrick = i;
		// }
		// } catch (Throwable e) {
		// System.out.println("throwable2" + e.getMessage());
		// }
		// }
	}

	private void setLevel(byte b) {
		this.level = new Level(road, b, 2);
		background.setLevel(b);

	}

	public void handleOpen(ServerConnectionHandler handler) {
		// currentInfo = ("Ready...");
		serverHandler = handler;
		BTReady();

	}

	public void handleOpenError(ServerConnectionHandler handler, String errorMessage) {
		// System.out.println("handleOpenError");
		// exitGame();
		closeConnection();

	}

	public void handleQueuedMessageWasSent(ServerConnectionHandler handler, Integer id) {
		// System.out.println("handleQueuedMessageWasSent");
		// if (id.intValue() == Messages.MESSAGE_CODE_END_GAME.intValue()) {
		// exitGame();
		// }
		// if (id.intValue() == Messages.MESSAGE_CODE_LEVEL.intValue()) {
		// System.out.println("jaj");
		// opponentReceivedLevel = true;
		// }

	}

	// ////////////////////////////////////////////
	protected void paint() {

		if (gamePaused == false && gameDestroyed == false) {
			//

			if (backgroundPainted == false) {
				g.setColor(0, 0, 0);
				g.fillRect(0, 0, width, height);

				backgroundPainted = true;
			}

		}

		paintBackground();
		paintRoad();

		if (evoOpponent != null) {
			if (evo.getY() <= evoOpponent.getY()) {
				paintMovingElements(null);
				paintEvo();
				paintOpponentEvo();
			} else {
				paintMovingElements(evoOpponent);
				paintEvo();
			}
		} else {
			paintMovingElements(null);
			paintEvo();
		}

		paintKilometers();

		if (numberOfPlayers < 2) {
			if (gameStarted)
				paintTimeOfRide();
			else
				paintTimeOfRide0();
		}

		paintFooter();
		paintSpeedMeter();

		if (startingGame == true) {
			paintStartingGame();
		}

		if (endingGame == true) {
			paintEndingGame();
		}

		if (waitingForNewGame == true) {
			paintWaitingForNewGame();
		}

		if (waitingForOpponent == true) {
			paintWaitingForOpponent();
		}

		flushGraphics();

	}

	private void paintWaitingForOpponent() {
		gameCommunicates.setEndGame(GameCommunicates.COMM_WAITING_FOR_OPPONENT);
		gameCommunicates.paint(g);
	}

	private void paintWaitingForNewGame() {
		gameCommunicates.setEndGame(GameCommunicates.COMM_PUSH_BUTTON);
		gameCommunicates.paint(g);
	}

	private void paintEndingGame() {
		gameEnder.paint(g);

	}

	private void paintStartingGame() {
		gameStarter.paint(g);
	}

	private void paintEvo() {
		evo.paint(g);
	}

	private void paintOpponentEvo() {
		if (evoOpponent != null)
			evoOpponent.paintResized(g);

	}

	private void paintMovingElements(EvoResized evoOpponent) {

		level.paint(g, evoOpponent);
	}

	private void paintSpeedMeter() {
		speedMeter.paint(g);
	}

	private void paintKilometers() {
		kilometersToEnd.paint(g);
	}

	private void paintTimeOfRide() {
		timeOfRide.paint(g);
	}

	private void paintTimeOfRide0() {
		timeOfRide.paint00(g);
	}

	private void paintRoad() {
		road.paint(g);
	}

	private void paintFooter() {
		footer.paint(g);

	}

	private void paintBackground() {
		background.paint(g);
	}

	private void paintShakeBackground() {

		for (int i = 0; i < 5; i++) {

			UP_OFFSET = UP_OFFSET + 3;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			backgroundPainted = false;
			paint();
			UP_OFFSET = UP_OFFSET - 3;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			backgroundPainted = false;
			paint();
		}
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setUserName(String userName) {
		this.userName = userName;

	}

	public synchronized void notifyWaitingForGetUserName() {
		notify();
	}

	public void notifyOpponentEndGame() {

		if (numberOfPlayers > 1) {

			if (serverHandler != null) {
				serverHandler.queueMessageForSending(Messages.MESSAGE_CODE_END_GAME,
						Messages.MESSAGE_GAME_END_FROM_LOOSER);
			} else {

				if (clientHandler != null) {
					clientHandler.queueMessageForSending(Messages.MESSAGE_CODE_END_GAME,
							Messages.MESSAGE_GAME_END_FROM_LOOSER);
				} else {
					exitGame();
				}
			}

			// exit is after sending message in handle receive
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		} else {
			exitGame();
		}
	}

	private boolean connectionIsReady() {
		return ((serverHandler != null || clientHandler != null) && gameDestroyed != true);
	}

}

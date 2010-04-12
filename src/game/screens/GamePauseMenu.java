package game.screens;

import game.GameInterface;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class GamePauseMenu extends List implements CommandListener{

	
	/** A list of start menu items */
	private static final String[] elements = { "Powrot", "Koniec gry"};

	/** A menu list instance */
	private final List menu = new List("Evolution Play.", List.IMPLICIT, elements, null);
	
	/* */
	GameInterface game;
	
	public GamePauseMenu(GameInterface game) {
		super("Pauza.", List.IMPLICIT, elements, null);
		this.game = game;
		setCommandListener(this);

	}

	public void commandAction(Command c, Displayable displayable) {

		switch (getSelectedIndex()) {
		case 0:
			game.resumeGame();
			break;
		case 1:			
			//game.notifyOpponentEndGame();
			game.exitGame();
			break;
		default:
			break;
		}
	}

}

package game.screens;

import game.EvoMidlet;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class FormGetUserName extends Form implements CommandListener{
	private final int MAX_USER_NAME_LENGHT = 10;

	private Command acceptCommand = new Command("Dodaj", Command.ITEM, 1);
	EvoMidlet midlet;
	TextField t;
	
	public FormGetUserName(EvoMidlet midlet) {

		super("...Najlepsze wyniki...");
		this.midlet = midlet;
		StringItem str = new StringItem("Gratulacje!","Podaj swoja nazwe:");
		t = new TextField("Uzytkownik :", "", MAX_USER_NAME_LENGHT, TextField.ANY);
		append(str);
		append(t);
		
		addCommand(acceptCommand);
		setCommandListener(this);
		
	}
	
	
	public void commandAction(Command comm, Displayable arg1) {

		if (comm == acceptCommand) {
			midlet.handleUserNameFilled(t.getString());
			midlet.wakeUpWaitingForUserName();
			
		}

	}	

}

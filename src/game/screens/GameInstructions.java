package game.screens;

import game.EvoMidlet;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class GameInstructions extends Form implements CommandListener {

	String instrukcje1 = "fasf" +
			" Gra przechowuje 5 najlepszych wynikow dla kazdej planszy - wybor menu Najlepsze wyniki.";
	String instrukcje2 = "Prawo - skret w prawo, Lewo - skret w lewo, Dol - hamowanie, 7,9,*,# (zaleznie od telefonu) - wyjscie do menu";
	
	String instrukcje3 = "Przed uruchomieniem gry w trybie multiplayer" +
			" nalezy uaktywnic lacze bluetooth w obu telefonach. Aby przeprowadzic gre przez lacze bluetooth," +
			" jeden z graczy musi uruchomic gre w" +
			" trybie 2 Graczy - Serwer, a drugi w trybie 2 Graczy - Klient.  Po uruchomieniu serwera" +
			" przeciwnik - klient powinien wyszukac telefon serwer uruchamiajac szukaj (moze to potrwac do minuty)" +
			" Po wyszukaniu serwera nalezy otworzyc polaczenie wybierajac opcje polaczenie z menu." +
			" "
			;
	
	EvoMidlet midlet;
	StringItem i1 = new StringItem("Instrukcje ",instrukcje1);
	StringItem i2 = new StringItem("Klawisze sterujace ",instrukcje2);
	StringItem i3 = new StringItem("Bluetooth ",instrukcje3);
	

	private Command exitCommand = new Command("Wstecz", Command.EXIT, 0);
	

	public GameInstructions(EvoMidlet midlet) {
		
		super("Instrukcja obsługi");
		this.midlet = midlet;
		this.append(i1);
		this.append(i2);
		this.append(i3);
		this.addCommand(exitCommand);
		setCommandListener(this);

	}

	private void exit() {
		midlet.setCurrentDisplayMainMenu();
	}

	public void commandAction(Command comm, Displayable arg1) {

		if (comm == exitCommand) {
			exit();
		}

	}
}

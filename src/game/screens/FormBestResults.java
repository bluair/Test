package game.screens;

import game.Images;
import game.EvoMidlet;
import game.Records;
import game.WallOfFameRecord;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class FormBestResults extends List implements CommandListener {

	/** A list of start menu items */
	private static final String[] elements = { "1. kala 4002", "2. mamam 1002" };

	private Command cancelCommand = new Command("Wstecz", Command.ITEM, 1);
	EvoMidlet midlet;

	private WallOfFameRecord records[];

	public FormBestResults(EvoMidlet midlet) {
		super("Najlepsze wyniki.", List.IMPLICIT, elements, null);
		this.midlet = midlet;
		deleteAll();
		fillWallOfFame();

		addCommand(cancelCommand);
		setCommandListener(this);
	}
	

	public void fillWallOfFame() {
		
		records = Records.getWallOfFameRecords(0);
		if (records != null) {
			append("Dzienny przejazd:",null);
			for (int i = 0; i < records.length; i++) {
				if (i == 0) {
					append((i + 1) + ". " + records[i].userName + " " + records[i].getData(), null);
				} else {
					append((i + 1) + ". " + records[i].userName + " " + records[i].getData(), null);
				}
			}
		} else {
			append("Dzien - nie ma wynikow", null);
		}
		append("", null);
	
		records = Records.getWallOfFameRecords(1);
		if (records != null) {
			append("Nocny przejazd:",null);
			for (int i = 0; i < records.length; i++) {
				if (i == 0) {
					append((i + 1) + ". " + records[i].userName + " " + records[i].getData(), null);
				} else {
					append((i + 1) + ". " + records[i].userName + " " + records[i].getData(), null);
				}
			}
		} else {
			append("Noc - nie ma wynikow", null);
		}
		
		
	}

	public void commandAction(Command comm, Displayable arg1) {

		if (comm == cancelCommand) {
			midlet.setCurrentDisplayMainMenu();
		}

	}

}

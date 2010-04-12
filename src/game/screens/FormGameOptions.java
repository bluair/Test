package game.screens;

import game.GameOptions;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class FormGameOptions extends Form implements CommandListener {

	Displayable next;
	Display display;
	// private Command exitCommand = new Command("Exit", Command.EXIT, 1);
	private Command saveCommand = new Command("Zapisz", Command.ITEM, 1);
	private Command cancelCommand = new Command("Anuluj", Command.ITEM, 2);

	ChoiceGroup cg;

	private String optionsRSUrl = "Evooptions";
	int numberOfRecords = 1;

	boolean bol[] = new boolean[numberOfRecords];
	byte byt[] = new byte[numberOfRecords];

	public FormGameOptions(Display display, Displayable next) {

		super("Ustawienia");
		this.display = display;

		cg = new ChoiceGroup("Ustaw opcje gry", ChoiceGroup.MULTIPLE);
		cg.append("Muzyka", null);

		this.next = next;
		// addCommand(exitCommand);
		addCommand(saveCommand);
		addCommand(cancelCommand);

		append(cg);
		setDefaultOptions();
		readOptions();
		setGlobalOptions();
		setCommandListener(this);

	}

	private void setDefaultOptions() {
		bol[0] = true; // play music
		byt[0] = (byte) 1;
	}

	private void readOptions() {
		RecordStore rs = null;
		try {
			// RecordStore.deleteRecordStore(optionsRSUrl);
			rs = RecordStore.openRecordStore(optionsRSUrl, true);

			RecordEnumeration re = rs.enumerateRecords(null, null, false);
			while (re.hasNextElement()) {
				byt = re.nextRecord();
			}

			for (int i = 0; i < numberOfRecords; i++) {
				bol[i] = (boolean) (byt[i] == 1 ? true : false);
			}

			cg.setSelectedFlags(bol);
		} catch (Exception e) {
			ErrorScreen.showError("RMS error " + e.getMessage(), display, next);
		} finally {
			if (rs != null)
				try {
					rs.closeRecordStore();
				} catch (Exception e) {
				}
		}
	}

	private void setGlobalOptions() {
		GameOptions.playMusic = bol[0];
	}

	private void saveOptions() {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(optionsRSUrl, true);

			cg.getSelectedFlags(bol);

			for (int i = 0; i < numberOfRecords; i++) {
				byt[i] = (byte) (bol[i] ? 1 : 0);
			}
			if (rs.getNumRecords() == 0) {
				rs.addRecord(byt, 0, numberOfRecords);
			} else {
				rs.setRecord(1, byt, 0, numberOfRecords);
			}
		} catch (Exception e) {
			System.out.println("saveOptions:"+e.getMessage());
		} finally {
			if (rs != null)
				try {
					rs.closeRecordStore();
				} catch (Exception e) {
				}
		}

	}

	public void commandAction(Command comm, Displayable arg1) {

		if (comm == cancelCommand) {
			display.setCurrent(next);
		} else if (comm == saveCommand) {
			saveOptions();
			setGlobalOptions();
			display.setCurrent(next);
		}

	}
}

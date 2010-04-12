package game.screens;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

public class FormProgress extends Form {

	public FormProgress(String progressString) {
		super("Prosze czekac...");
		append(new Gauge("Loading...", false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING));

	}
	
	public void addInfo(String info){
		//append(new TextBox(info,));
		StringItem str = new StringItem(info,"");
		append(str);
		
	}
	
}
   
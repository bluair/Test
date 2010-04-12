package game.btl2cap.server;

import java.util.Vector;

import javax.microedition.lcdui.Displayable;

public interface ServiceDiscoveryListener {

	public void serviceDiscoveryListFatalError(String errorMessage);

	public void serviceDiscoveryListError(String errorMessage);

	public void serviceDiscoveryListOpen(Vector selectedServiceRecords);

	public void serviceDiscoveryListExitRequest();

	public void serviceDiscoveryListBackRequest(Displayable next);

	public void serviceDiscoveryListViewLog(Displayable next);

}

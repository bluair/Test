// Copyright 2004 Nokia Corporation.
//
// THIS SOURCE CODE IS PROVIDED 'AS IS', WITH NO WARRANTIES WHATSOEVER,
// EXPRESS OR IMPLIED, INCLUDING ANY WARRANTY OF MERCHANTABILITY, FITNESS
// FOR ANY PARTICULAR PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE
// OR TRADE PRACTICE, RELATING TO THE SOURCE CODE OR ANY WARRANTY OTHERWISE
// ARISING OUT OF ANY PROPOSAL, SPECIFICATION, OR SAMPLE AND WITH NO
// OBLIGATION OF NOKIA TO PROVIDE THE LICENSEE WITH ANY MAINTENANCE OR
// SUPPORT. FURTHERMORE, NOKIA MAKES NO WARRANTY THAT EXERCISE OF THE
// RIGHTS GRANTED HEREUNDER DOES NOT INFRINGE OR MAY NOT CAUSE INFRINGEMENT
// OF ANY PATENT OR OTHER INTELLECTUAL PROPERTY RIGHTS OWNED OR CONTROLLED
// BY THIRD PARTIES
//
// Furthermore, information provided in this source code is preliminary,
// and may be changed substantially prior to final release. Nokia Corporation
// retains the right to make changes to this source code at
// any time, without notice. This source code is provided for informational
// purposes only.
//
// Nokia and Nokia Connecting People are registered trademarks of Nokia
// Corporation.
// Java and all Java-based marks are trademarks or registered trademarks of
// Sun Microsystems, Inc.
// Other product and company names mentioned herein may be trademarks or
// trade names of their respective owners.
//
// A non-exclusive, non-transferable, worldwide, limited license is hereby
// granted to the Licensee to download, print, reproduce and modify the
// source code. The licensee has the right to market, sell, distribute and
// make available the source code in original or modified form only when
// incorporated into the programs developed by the Licensee. No other
// license, express or implied, by estoppel or otherwise, to any other
// intellectual property rights is granted herein.

package game.btl2cap.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class ServiceDiscoveryList extends List implements CommandListener,
		DiscoveryListener, Runnable {
	private final static String TITLE = "Szukanie przeciwnika";
	private final static int WAIT_MILLIS = 500; // milliseconds
	private final static String[] ACTIVITY = { "", ".", "..", "...", "...." };

	private final UUID uuid;
	private final ServiceDiscoveryListener listener;
	private final Command backCommand;
	private final Command searchCommand;
	private final Command openCommand;
	private final Command stopCommand;
	private final Command logCommand;
	private final Command exitCommand;

	private final int sdTransMax;
	private final int inquiryAccessCode;

	private DiscoveryAgent discoveryAgent;
	private volatile boolean inquiryInProgress = false;
	private volatile int numServiceSearchesInProgress = 0;
	private volatile boolean aborting = false;
	private volatile Thread thread;
	private Displayable backDisplayable;

	private Vector unsearchedRemoteDevices = new Vector();
	private Vector transIds = new Vector();
	private Hashtable matchingServiceRecords = new Hashtable();

	private int numConnectionsAlreadyOpen = 0;

	public ServiceDiscoveryList(ServiceDiscoveryListener listener,
			String uuidString, int inquiryAccessCode) {
		super(TITLE, List.IMPLICIT);
		this.listener = listener;
		uuid = new UUID(uuidString, false);
		this.inquiryAccessCode = inquiryAccessCode;

		openCommand = new Command("Polaczenie", Command.SCREEN, 1);
		searchCommand = new Command("Szukaj", Command.SCREEN, 2);
		logCommand = new Command("View log", Command.SCREEN, 3);
		stopCommand = new Command("Stop", Command.SCREEN, 4);
		backCommand = new Command("Wstecz", Command.BACK, 5);
		exitCommand = new Command("Wyjscie", Command.EXIT, 0);

		String property = LocalDevice.getProperty("bluetooth.sd.trans.max");
		if (property != null) {
			sdTransMax = Integer.parseInt(property);
		} else {
			sdTransMax = 5;
		}

		// create discovery agent
		try {
			discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();

			//addCommand(logCommand);
			addCommand(exitCommand);
			addCommand(searchCommand);
			setCommandListener(this);

			start();
		} catch (BluetoothStateException e) {
			listener
					.serviceDiscoveryListFatalError("Couldn't get a discovery agent: '"
							+ e.getMessage() + "'");
		}
	}

	public void addBackCommand(Displayable backDisplayable) {
		this.backDisplayable = backDisplayable;
		removeCommand(backCommand);
		addCommand(backCommand);
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void abort() {
		thread = null;
	}

	public void init(int numConnectionsAlreadyOpen) {
		this.numConnectionsAlreadyOpen = numConnectionsAlreadyOpen;

		// stop any pending searches
		if (inquiryInProgress || numServiceSearchesInProgress > 0) {
			cancelPendingSearches();
		}

		// remove any old list elements
		while (size() > 0) {
			delete(0);
		}
	}

	private synchronized void setInquiryInProgress(boolean bool) {
		inquiryInProgress = bool;
	}

	public void commandAction(Command command, Displayable d) {
		if (command == logCommand) {
			listener.serviceDiscoveryListViewLog(this);
		} else if (command == searchCommand && !inquiryInProgress
				&& (numServiceSearchesInProgress == 0)) {
			// new inquiry started

			removeCommand(openCommand);

			// remove old device lists
			unsearchedRemoteDevices.removeAllElements();
			for (Enumeration keys = matchingServiceRecords.keys(); keys
					.hasMoreElements();) {
				matchingServiceRecords.remove(keys.nextElement());
			}

			// delete all old List items
			while (size() > 0) {
				delete(0);
			}

			try {
				// disable page scan and inquiry scan
				LocalDevice dev = LocalDevice.getLocalDevice();
				dev.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE);


				// this is non-blocking
				discoveryAgent.startInquiry(inquiryAccessCode, this);

				setInquiryInProgress(true);
				addCommand(stopCommand);
				removeCommand(searchCommand);
			} catch (BluetoothStateException e) {
				addCommand(searchCommand);
				removeCommand(stopCommand);
				listener
						.serviceDiscoveryListError("Error during startInquiry: '"
								+ e.getMessage() + "'");
			}
		} else if (command == stopCommand) {
			// stop searching
			if (cancelPendingSearches()) {
				setInquiryInProgress(false);
				removeCommand(stopCommand);
				addCommand(searchCommand);
			}
		} else if (command == exitCommand) {
			listener.serviceDiscoveryListExitRequest();
		} else if (command == openCommand) { //open
			int size = this.size();
			boolean[] flags = new boolean[size];
			getSelectedFlags(flags);
			Vector selectedServiceRecords = new Vector();
			for (int i = 0; i < size; i++) {
				if (flags[i]) {
					String key = getString(i);
					ServiceRecord rec = (ServiceRecord) matchingServiceRecords
							.get(key);
					selectedServiceRecords.addElement(rec);
				}
			}

			// try to perform an open on selected items
			if (selectedServiceRecords.size() > 0) {
				String value = LocalDevice
						.getProperty("bluetooth.connected.devices.max");
				int maxNum;
				if (value != null) {
					maxNum = Integer.parseInt(value);
				} else {
					maxNum = 1;
				}

				int total = numConnectionsAlreadyOpen
						+ selectedServiceRecords.size();
				if (total > maxNum) {
					listener.serviceDiscoveryListError("Too many selected. "
							+ "This device can connect to at most " + maxNum
							+ " other devices");
				} else {
					listener.serviceDiscoveryListOpen(selectedServiceRecords);
				}
			} else {
				listener
						.serviceDiscoveryListError("Select at least one to open");
			}
		} else if (command == backCommand) {
			listener.serviceDiscoveryListBackRequest(backDisplayable);
		}
	}

	public boolean cancelPendingSearches() {
	
		boolean everythingCancelled = true;

		if (inquiryInProgress) {
			// Note: The BT API (v1.0) isn't completely clear
			// whether cancelInquiry is blocking or non-blocking.

			if (discoveryAgent.cancelInquiry(this)) {
				setInquiryInProgress(false);
			} else {
				everythingCancelled = false;
			}
		}

		for (int i = 0; i < transIds.size(); i++) {
			// Note: The BT API (v1.0) isn't completely clear
			// whether cancelServiceSearch is blocking or
			// non-blocking?

			Integer pendingId = (Integer) transIds.elementAt(i);
			if (discoveryAgent.cancelServiceSearch(pendingId.intValue())) {
				transIds.removeElement(pendingId);
			} else {
				everythingCancelled = false;
			}
		}
		return everythingCancelled;
	}

	// DiscoveryListener callbacks

	public void deviceDiscovered(RemoteDevice remoteDevice,
			DeviceClass deviceClass) {
//		LogScreen.log("deviceDiscovered: " + remoteDevice.getBluetoothAddress()
//				+ " major device class=" + deviceClass.getMajorDeviceClass()
//				+ " minor device class=" + deviceClass.getMinorDeviceClass()
//				+ "\n");

		// Note: The following check has the effect of only
		// performing later service searches on phones.
		// If you intend to run the MIDlet on some other device (e.g.
		// handheld computer, PDA, etc. you have to add a check
		// for them as well.) You might also refine the check using
		// the minor device class check only cellular phones.

		boolean isPhone = (deviceClass.getMajorDeviceClass() == 0x200);

		// Setting the following line to 'true' is a workaround
		// for some early beta SDK device emulators. Set it
		// to false when compiling the MIDlet for download to
		// real MIDP phones!
		boolean isEmulator = true; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		if (isPhone || isEmulator) {
			unsearchedRemoteDevices.addElement(remoteDevice);
		}
	}

	public void inquiryCompleted(int discoveryType) {


		setInquiryInProgress(false);

		if (unsearchedRemoteDevices.size() == 0) {
			setTitle(TITLE);

			addCommand(searchCommand);
			removeCommand(stopCommand);

//			listener
//					.serviceDiscoveryListError("No matching Bluetooth devices were found "
//							+ "that run the desired service");
		}
	}

	public void servicesDiscovered(int transId, ServiceRecord[] serviceRecords) {

		// Remove+Add: ensure there is at most one open command
		removeCommand(openCommand);
		addCommand(openCommand);

		// Use the bluetooth address
		// to identify the matching Device + ServiceRecord

		// there should only be one record
		if (serviceRecords.length == 1) {
			RemoteDevice device = serviceRecords[0].getHostDevice();
			//String name = device.getBluetoothAddress();
			String name;
			try {
				name = device.getFriendlyName(false);
				
			} catch (IOException e) {
				name = device.getBluetoothAddress();								
			}
			if (name == null){
				name = device.getBluetoothAddress();
			}

			// This MIDlet only uses the first matching service
			// record found for a particular device.
			if (!matchingServiceRecords.containsKey(name)) {
				matchingServiceRecords.put(name, serviceRecords[0]);
				append(name, null);

				// The List should have at least one entry,
				// before we add an open command.
				if (size() == 1) {
					addCommand(openCommand);
				}
			}
		} else {
			listener.serviceDiscoveryListError("Error: Unexpected number ("
					+ serviceRecords.length + ") of service records "
					+ "in servicesDiscovered callback, transId=" + transId);
		}
	}

	public void serviceSearchCompleted(int transId, int responseCode) {
		setTitle(TITLE);

//		String responseCodeString = LogScreen.responseCodeString(responseCode);
//		LogScreen.log("serviceSearchCompleted: transId=" + transId + " ("
//				+ responseCodeString + ")\n");

		// For any responseCode, decrement the counter
		numServiceSearchesInProgress--;

		// remove the transaction id from the pending list
		for (int i = 0; i < transIds.size(); i++) {
			Integer pendingId = (Integer) transIds.elementAt(i);
			if (pendingId.intValue() == transId) {
				transIds.removeElement(pendingId);
				break;
			}
		}

		// all the searches have completed
		if (!inquiryInProgress && (transIds.size() == 0)) {
			removeCommand(stopCommand);
			addCommand(searchCommand);

			if (matchingServiceRecords.size() == 0) {
				listener
						.serviceDiscoveryListError("No matching services found");
			}
		}
	}

	// Interface Runnable
	public void run() {
		Thread currentThread = Thread.currentThread();
		int i = 0;

		running: while (thread == currentThread) {
			synchronized (this) {
				if (thread != currentThread) {
					break running;
				} else if (!inquiryInProgress) {
					doServiceSearch();
				}

				if (inquiryInProgress || numServiceSearchesInProgress > 0) {
					setTitle("Wyszukiwanie... " + ACTIVITY[i]);
					if (++i >= ACTIVITY.length) {
						i = 0;
					}
				}

				try {
					wait(WAIT_MILLIS);
				} catch (InterruptedException e) {
					// we can safely ignore this
				}
			}
		}
	}

	public void doServiceSearch() {
		if ((unsearchedRemoteDevices.size() > 0)
				&& (numServiceSearchesInProgress < sdTransMax)) {
			synchronized (this) {
				RemoteDevice device = (RemoteDevice) unsearchedRemoteDevices
						.elementAt(0);

				UUID[] uuids = new UUID[1];
				uuids[0] = uuid;
				try {
					int[] attrSet = null; // default attrSet

					numServiceSearchesInProgress++;

					int transId = discoveryAgent.searchServices(attrSet, uuids,
							device, this);

//					LogScreen.log("starting service search on device="
//							+ device.getBluetoothAddress() + " transId="
//							+ transId + "\n");

					transIds.addElement(new Integer(transId));
					unsearchedRemoteDevices.removeElementAt(0);
				} catch (BluetoothStateException e) {
					numServiceSearchesInProgress--;

					listener
							.serviceDiscoveryListError("Error, could not perform "
									+ "service search: '" + e.getMessage());
				}
			}
		}
	}

	public void remove(Vector alreadyOpen) {
		if (size() > 0) {
			for (int i = 0; i < alreadyOpen.size(); i++) {
				// Bluetooth address of slave device that
				// we already have a connection open to
				String btAddress = (String) alreadyOpen.elementAt(i);

				boolean found = false;
				for (int j = 0; j < size(); j++) {
					if (getString(j).equals(btAddress)) {
						found = true;

						// if the element we are about to
						// remove is selected, select something else
						if (getSelectedIndex() == j) {
							setSelectedIndex(j, false);
						}
						delete(j);
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
	}
}
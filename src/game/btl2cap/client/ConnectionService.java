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

package game.btl2cap.client;

import java.io.IOException;

import javax.bluetooth.DataElement;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;

public class ConnectionService implements Runnable {
	private final ClientConnectionHandlerListener listener;
	private final String url;

	private L2CAPConnectionNotifier connectionNotifier = null;
	private volatile boolean aborting;

	public ConnectionService(String url, ClientConnectionHandlerListener listener) {
		this.url = url;
		this.listener = listener;


		//System.out.println("ConnectionService: waiting to accept connections on '" + url + "'\n");

		// start waiting for a connection
		Thread thread = new Thread(this);
		thread.start();
	}

	public String getClientURL() {
		return url;
	}

	public void close() {
		if (!aborting) {
			synchronized (this) {
				aborting = true;
			}

			// Ideally, one might want to give the run method's
			// loop a chance to abort before calling the
			// subsequent close, but the run loop is anyways
			// likely to be sitting on the acceptAndOpen
			// (i.e. blocked).

			try {
				if (connectionNotifier != null) {
					connectionNotifier.close();
				}
			} catch (IOException e) {
				// There is nothing very useful that
				// we can do for this case.
			}
		}
	}

	public void run() {
		aborting = false;

		try {
			connectionNotifier = (L2CAPConnectionNotifier) Connector.open(url);

			// It might useful in some cases to add a service to the
			// 'Public Browse Group'. For example by doing something
			// approximately as follows:
			// -----------------------------------------------------
			// Retrieve the service record template
			LocalDevice ld = LocalDevice.getLocalDevice();
			ServiceRecord rec = ld.getRecord(connectionNotifier);
			DataElement element = new DataElement(DataElement.DATSEQ);
			//
			// The service class for PublicBrowseGroup (0x1002)
			// is defined in the Bluetooth Assigned Numbers document.
			element.addElement(new DataElement(DataElement.UUID, new UUID(0x1002)));

			// Add to the public browse group:
			rec.setAttributeValue(0x0005, element);
			// -----------------------------------------------------

		} catch (IOException e) {
			String errorMessage = "Error while starting ConnectionService: " + e.getMessage();

			listener.handleErrorClose(null, errorMessage);

			aborting = true;
		}

		catch (SecurityException e) {
			String errorMessage = "SecurityException while starting ConnectionService: " + e.getMessage();

			listener.handleErrorClose(null, errorMessage);

			aborting = true;
		}

		if (aborting == false) {
			listener.handleWaitingForConnections();
		}

		while (!aborting) {
			try {
				// 1. wait to accept & open a new connection
				L2CAPConnection connection = (L2CAPConnection) connectionNotifier.acceptAndOpen();

//				LogScreen.log("ConnectionService: new connection\n");

				// 2. create a handler to take care of
				// the new connection and inform
				// the listener
				if (!aborting) {
					ClientConnectionHandler handler = new ClientConnectionHandler(this, connection, listener);
					listener.handleAcceptAndOpen(handler);
				}
			} catch (IOException e) {
				if (!aborting) {
					String errorMessage = "IOException occurred during accept and open: " + e.getMessage();

					listener.handleError(null, errorMessage);
				}
			}
		}
	}
}
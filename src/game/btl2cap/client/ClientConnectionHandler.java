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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;

public class ClientConnectionHandler implements Runnable {
	private final static int WAIT_MILLIS = 30;

	private final ConnectionService ConnectionService;
	private final ClientConnectionHandlerListener listener;
	private final Hashtable sendMessages = new Hashtable();

	private L2CAPConnection connection;
	int transmitMTU = 0;

	private volatile boolean aborting;

	public ClientConnectionHandler(ConnectionService ConnectionService, L2CAPConnection connection,
			ClientConnectionHandlerListener listener) {
		this.ConnectionService = ConnectionService;
		this.connection = connection;
		this.listener = listener;
		aborting = false;

		// Our caller uses method 'start' to start the reader
		// and writer. (This allows the ConnectionService a
		// chance to add us to its list of handlers before
		// the reader and writer start running.)
	}

	ClientConnectionHandlerListener getListener() {
		return listener;
	}

	public synchronized void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public void close() {
		if (!aborting) {
			synchronized (this) {
				aborting = true;
			}

			synchronized (sendMessages) {
				sendMessages.notify();
			}

			if (connection != null) {
				try {
					connection.close();
					synchronized (this) {
						connection = null;
						transmitMTU = 0;
					}
				} catch (IOException e) {
					// there is nothing we can do: ignore it
				}
			}
		}
	}

	public void queueMessageForSending(Integer id, byte[] data) {
		if (connection != null) {

			if (data.length > transmitMTU) {
				// throw new IllegalArgumentException("Message too long: limit
				// is " + transmitMTU + " bytes");
			} else {

				synchronized (sendMessages) {
					sendMessages.put(id, data);
					sendMessages.notify();
				}
			}
		}
	}

	public void run() {
		// the reader
		try {
			// 1. open the streams, start the writer
			try {
				transmitMTU = connection.getTransmitMTU();

				// start the writer
				Writer writer = new Writer(this);
				Thread writeThread = new Thread(writer);
				writeThread.start();

				listener.handleStreamsOpen(this);
			} catch (IOException e) {
				// open failed: close any connections/streams and
				// inform listener that the open failed

				close(); // also tells listener to delete handler

				listener.handleStreamsOpenError(this, e.getMessage());
				return;
			}

			// 2. wait to receive and read messages
			while (!aborting) {
				boolean ready = false;
				try {
					ready = connection.ready();
				} catch (IOException e) {
					close();
					listener.handleClose(this);
				}

				int length = 0;
				try {
					// something might be ready for reading
					if (ready) {
						int mtuLength = connection.getReceiveMTU();
						if (mtuLength > 0) {
							// Note: In theory, you might need to be
							// a bit cautious about allocating an
							// array of arbitrarily large length.
							byte[] buffer = new byte[mtuLength];

							length = connection.receive(buffer);
							byte[] readData = new byte[length];
							System.arraycopy(buffer, 0, readData, 0, length);

							// handle read
							listener.handleReceivedMessage(this, readData);
						}
					} else {
						// take a short wait before spinning through
						// the loop again
						try {
							synchronized (this) {
								wait(WAIT_MILLIS);
							}
						} catch (InterruptedException e) {
							// can't happen in MIDP, just ignore
						}
					}
				} catch (IOException e) {
					close();

					if (length == 0) {
						listener.handleClose(this);
					} else {
						// we were in the middle of receiving something?
						listener.handleErrorClose(this, e.getMessage());
					}
				}
			}
		} catch (Throwable e) {
			System.out.println("reader1" + e.getMessage());
		}
	}

	private class Writer implements Runnable {
		private final ClientConnectionHandler handler;

		Writer(ClientConnectionHandler handler) {
			this.handler = handler;
		}

		public void run() {
			try {
				while (!aborting) {
					Enumeration e = sendMessages.keys();
					if (e.hasMoreElements()) {
						// send any pending messages
						Integer id = (Integer) e.nextElement();
						byte[] sendData = (byte[]) sendMessages.get(id);
						try {
							// send message
							connection.send(sendData);

							// remove sent message from queue
							sendMessages.remove(id);

							// inform listener that it was sent
							listener.handleQueuedMessageWasSent(handler, id);
						} catch (IOException ex) {
							close(); // stop the networking thread

							// inform that we got an error close
							listener.handleErrorClose(handler, ex.getMessage());
						}
					}

					synchronized (sendMessages) {
						if (sendMessages.isEmpty()) {
							try {
								sendMessages.wait();
							} catch (InterruptedException ex) {
								// can't happen in MIDP: ignore
							}
						}
					}
				}
			} catch (Throwable e) {
				System.out.println("writer1" + e.getMessage());
			}
		}
	}
}

package com.salpakan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.salpakan.ui.views.MainView;

public class Server {
	
	private static int uniqueID;
	
	private ArrayList<ClientThread> clients;

	private boolean keepGoing;
	
	private int port;
	
	private MainView main;
	
	public Server(final int port) {
		this(port, null);
	}

	public Server(final int port, final MainView main) {
		this.port = port;
		this.main = main;
		clients = new ArrayList<ClientThread>();
	}
	
	public void start() {
		keepGoing = true;
		try {
			final ServerSocket serverSocket = new ServerSocket(this.port);
			ClientThread client;
			Socket socket;
			while (keepGoing) {
				socket = serverSocket.accept();
				if (!keepGoing) {
					break;
				}
				client = new ClientThread(socket);
				clients.add(client);
				client.start();
			}
			serverSocket.close();
			for (int i = 0, j = clients.size(); i < j; i++) {
				client = clients.get(i);
				
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void stop() {
		keepGoing = false;
		try {
			new Socket("localhost", port);
		} catch (final UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private synchronized void broadcast(final String message) {
		main.getTabsPanel().getLobby().test(message);
		for(int i = clients.size(); --i >= 0;) {
			ClientThread client = clients.get(i);
			if(!client.writeMsg(message)) {
				clients.remove(i);
			}
		}
	}
	
	private synchronized void remove(final int id) {
		for(int i = 0; i < clients.size(); ++i) {
			ClientThread client = clients.get(i);
			if(client.id == id) {
				clients.remove(i);
				return;
			}
		}
	}
	
	private class ClientThread extends Thread {

		private int id;
		
		private Message message;
		
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;
		
		private Socket socket;
		
		public ClientThread(final Socket socket) {
			id = ++uniqueID;
			this.socket = socket;
			try {
				inputStream = new ObjectInputStream(this.socket.getInputStream());
				outputStream = new ObjectOutputStream(this.socket.getOutputStream());
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return;
			}
		}
		
		public void run() {
			boolean keepGoing = true;
			while (keepGoing) {
				try {
					message = (Message) inputStream.readObject();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					break;
				} catch (ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
					break;
				}
				final String msg = message.getMessage();
				broadcast(message.getType() + "-" + msg);
				/*switch (message.getType()) {
					case Message.MOVE:
						
						break;
					case Message.CHAT:
						
						break;
					case Message.LOGIN:
						
						break;
					case Message.LOGOUT:
						
						break;
					default:
						break;
				}*/
			}
			remove(id);
			close();
		}
		
		private boolean writeMsg(final String msg) {
			if(!socket.isConnected()) {
				close();
				return false;
			}
			try {
				outputStream.writeObject(msg);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return true;
		}
		
		private void close() {
			try {
				outputStream.close();
				inputStream.close();
				socket.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
	}
	
}

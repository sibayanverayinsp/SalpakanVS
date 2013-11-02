package com.salpakan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.salpakan.app.AppServer;

public class Server {
	
	private static int uniqueID;
	
	private ArrayList<ClientThread> clients;
	private int port;
	private AppServer appServer;
	private SimpleDateFormat dateFormat;
	
	private boolean keepGoing;
	
	public Server(final int port) {
		this(port, null);
	}

	public Server(final int port, final AppServer appServer) {
		this.port = port;
		this.appServer = appServer;
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		clients = new ArrayList<ClientThread>();
	}
	
	public void start() {
		display("Server started!");
		keepGoing = true;
		try {
			final ServerSocket serverSocket = new ServerSocket(port);
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
				client.inputStream.close();
				client.outputStream.close();
				client.socket.close();
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void stop() {
		display("Server stopped!");
		keepGoing = false;
		try {
			new Socket("localhost", port);
		} catch (final UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void display(final String message) {
		appServer.appendLog(dateFormat.format(new Date()) + " " + message + "\n");
	}
	
	private synchronized void broadcast(final String message) {
		display(message);
		ClientThread client;
		for (int i = clients.size(); --i >= 0; ) {
			client = clients.get(i);
			if (!client.writeMsg(message)) {
				clients.remove(i);
				display(client.username + " logs out");
			}
		}
	}
	
	private synchronized void remove(final int id) {
		ClientThread client;
		for (int i = 0, j = clients.size(); i < j; i++) {
			client = clients.get(i);
			if (client.id == id) {
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
		
		private final Socket socket;
		
		private String username;
		
		public ClientThread(final Socket socket) {
			id = ++uniqueID;
			this.socket = socket;
			try {
				inputStream = new ObjectInputStream(socket.getInputStream());
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				username = (String) inputStream.readObject();
				
				display(username + " logs in");
			} catch (final IOException ioe) {
				ioe.printStackTrace();
				return;
			} catch (final ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
		
		public void run() {
			boolean keepGoing = true;
			while (keepGoing) {
				try {
					message = (Message) inputStream.readObject();
				} catch (final IOException ioe) {
					ioe.printStackTrace();
					break;
				} catch (final ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
					break;
				}
				
				final String msg = message.getMessage();
				switch (message.getType()) {
				case Message.CHAT:
					broadcast(username + ": " + msg);
					break;
					
				case Message.LOGOUT:
					display(username + "logs out");
					keepGoing = false;
					break;

				default:
					break;
				}
			}
			remove(id);
			close();
		}
		
		private void close() {
			try {
				outputStream.close();
				inputStream.close();
				socket.close();
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		private boolean writeMsg(final String msg) {
			if (!socket.isConnected()) {
				close();
				return false;
			}
			try {
				outputStream.writeObject(msg);
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
			return true;
		}
	}
	
}

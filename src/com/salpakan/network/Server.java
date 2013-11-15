package com.salpakan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.salpakan.app.AppServer;
import com.salpakan.constants.Constants;

public class Server {
	
	private static int uniqueID;
	
	private ArrayList<ClientThread> clients;
	private int port;
	private AppServer appServer;
	
	private boolean keepGoing;
	
	public Server(final int port) {
		this(port, null);
	}

	public Server(final int port, final AppServer appServer) {
		this.port = port;
		this.appServer = appServer;
		clients = new ArrayList<ClientThread>();
	}
	
	public void start() {
		appServer.appendLog(Constants.SERVER_STARTED);
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
		appServer.appendLog(Constants.SERVER_STOPPED);
		keepGoing = false;
		try {
			new Socket(Constants.DEFAULT_HOST, port);
		} catch (final UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void display(final Message message) {
		appServer.appendLog("<" + message.getDate() + "> " + message.getUsername() + ": " + message.getMessage());
	}
	
	private synchronized void broadcast(final Message message) {
		display(message);
		ClientThread client;
		for (int i = clients.size(); --i >= 0; ) {
			client = clients.get(i);
			if (!client.writeMsg(message)) {
				clients.remove(i);
				display(message);
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
	
	private final class ClientThread extends Thread {

		private int id;
		
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;
		
		private final Socket socket;
		
		private String username;
		
		private ClientThread(final Socket socket) {
			id = ++uniqueID;
			this.socket = socket;
			try {
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				inputStream = new ObjectInputStream(socket.getInputStream());
				username = (String) inputStream.readObject();
				
				broadcast(new Message(Message.LOGIN, username, Constants.LOGS_IN.toLowerCase()));
				appServer.appendLog("<" + username + "> " + this.socket.getInetAddress().getHostAddress());
			} catch (final IOException ioe) {
				ioe.printStackTrace();
				return;
			} catch (final ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
		
		public synchronized void run() {
			ClientThread client;
			StringBuffer buffer;
			String msg;
			String user;
			Message message;
			int type;
			
			while (true) {
				try {
					message = (Message) inputStream.readObject();
				} catch (final IOException ioe) {
					break;
				} catch (final ClassNotFoundException cnfe) {
					break;
				}
				
				msg = message.getMessage();
				type = message.getType();
				user = message.getUsername();
				if (type == Message.PLAYERS) {
					buffer = new StringBuffer();
					for(int i = 0, j = clients.size(); i < j; i++) {
						client = clients.get(i);
						if (msg.equals(Constants.LOGOUT_BUTTON)) {
							if (!client.username.equals(user)) {
								buffer.append(client.username + "&");
							}
						} else {
							buffer.append(client.username + "&");
						}
					}
					broadcast(new Message(type, username, buffer.toString()));
				} else if (type == Message.LOGOUT) {
					broadcast(message);
					break;
				} else {
					broadcast(message);
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
		
		private boolean writeMsg(final Message message) {
			if (!socket.isConnected()) {
				close();
				return false;
			}
			try {
				outputStream.writeObject(message);
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
			return true;
		}
	}
	
}

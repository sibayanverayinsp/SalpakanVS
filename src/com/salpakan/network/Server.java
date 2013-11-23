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
	private ArrayList<String> defaultRoomGames;
	private ArrayList<String> showEngagedRoomGames;
	private ArrayList<String> showCapturedRoomGames;
	private ArrayList<String> theBattlefieldRoomGames;
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
		defaultRoomGames = new ArrayList<String>();
		showEngagedRoomGames = new ArrayList<String>();
		showCapturedRoomGames = new ArrayList<String>();
		theBattlefieldRoomGames = new ArrayList<String>();
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
	
	private void broadcastToClient(final Message message) {
		display(message);
		ClientThread client;
		for (int i = clients.size(); --i >= 0; ) {
			client = clients.get(i);
			if (client.username.equals(message.getUsername())) {
				if (!client.writeMsg(message)) {
					clients.remove(i);
					display(message);
				}
				break;
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
				appServer.appendLog("<" + username + "> " + this.socket.getRemoteSocketAddress());
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
			String[] msgArray;
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
				switch (type) {
				case Message.PLAYERS:
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
					break;
					
				case Message.GAME_CREATED:
					msgArray = msg.split("&");
					getRoomArrayList(msgArray[0]).add(msgArray[1]);
					broadcast(new Message(type, username, msg));
					break;
					
				case Message.ROOM_GAMES:
					buffer = new StringBuffer();
					for (int i = 0, j = defaultRoomGames.size(); i < j; i++) {
						buffer.append(defaultRoomGames.get(i) + "&");
					}
					if (defaultRoomGames.size() == 0) {
						buffer.append(" ");
					}
					buffer.append(",");
					for (int i = 0, j = showEngagedRoomGames.size(); i < j; i++) {
						buffer.append(showEngagedRoomGames.get(i) + "&");
					}
					if (showEngagedRoomGames.size() == 0) {
						buffer.append(" ");
					}
					buffer.append(",");
					for (int i = 0, j = showCapturedRoomGames.size(); i < j; i++) {
						buffer.append(showCapturedRoomGames.get(i) + "&");
					}
					if (showCapturedRoomGames.size() == 0) {
						buffer.append(" ");
					}
					buffer.append(",");
					for (int i = 0, j = theBattlefieldRoomGames.size(); i < j; i++) {
						buffer.append(theBattlefieldRoomGames.get(i) + "&");
					}
					if (theBattlefieldRoomGames.size() == 0) {
						buffer.append(" ");
					}
					buffer.append(",");
					broadcastToClient(new Message(type, user, buffer.toString()));
					break;
					
				case Message.GAME_CANCELLED:
					msgArray = msg.split("&");
					getRoomArrayList(msgArray[0]).remove(msgArray[1]);
					broadcast(new Message(type, username, msg));
					break;

				default:
					broadcast(message);
					break;
				}
				
				if (type == Message.LOGOUT) {
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
		
		private ArrayList<String> getRoomArrayList(final String roomName) {
			if (roomName.equals(Constants.DEFAULT_ROOM)) {
				return defaultRoomGames;
			} else if (roomName.equals(Constants.SHOW_ENGAGED)) {
				return showEngagedRoomGames;
			} else if (roomName.equals(Constants.SHOW_CAPTURED)) {
				return showCapturedRoomGames;
			} else {
				return theBattlefieldRoomGames;
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

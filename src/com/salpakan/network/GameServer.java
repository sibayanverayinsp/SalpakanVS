package com.salpakan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer {
	
	private static int uniqueID;
	
	private int port;
	
	private boolean keepGoing;
	
	private ArrayList<PlayerThread> players;
	
	public GameServer(final int port) {
		this.port = port;
		players = new ArrayList<PlayerThread>();
	}
	
	private synchronized void broadcast(final Message message) {
		PlayerThread player;
		for (int i = players.size(); --i >= 0; ) {
			player = players.get(i);
			if (!player.writeMsg(message)) {
				players.remove(i);
			}
		}
	}
	
	public int getPort() {
		return port;
	}
	
	private synchronized void remove(final int id) {
		PlayerThread player;
		for (int i = 0, j = players.size(); i < j; i++) {
			player = players.get(i);
			if (player.id == id) {
				players.remove(i);
				return;
			}
		}
	}
	
	public void start() {
		keepGoing = true;
		try {
			final ServerSocket server = new ServerSocket(port);
			Socket socket = null;
			PlayerThread player;
			
			while (keepGoing) {
				socket = server.accept();
				if (!keepGoing) {
					break;
				}
				player = new PlayerThread(socket);
				players.add(player);
				player.start();
			}
			server.close();
			for (int i = 0, j = players.size(); i < j; i++) {
				player = players.get(i);
				player.inputStream.close();
				player.outputStream.close();
				player.socket.close();
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void stop() {
		keepGoing = false;
	}
	
	private final class PlayerThread extends Thread {
		
		private int id;
		
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;
		
		private final Socket socket;
		
		private String username;
		
		private PlayerThread(final Socket socket) {
			id = ++uniqueID;
			this.socket = socket;
			try {
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				inputStream = new ObjectInputStream(socket.getInputStream());
				username = (String) inputStream.readObject();
				
				broadcast(new Message(Message.JOIN, username, username + " joined!"));
			} catch (final IOException ioe) {
				ioe.printStackTrace();
				return;
			} catch (final ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
		
		public synchronized void run() {
			Message message;
			
			while (true) {
				try {
					message = (Message) inputStream.readObject();
				} catch (final IOException ioe) {
					break;
				} catch (final ClassNotFoundException cnfe) {
					break;
				}
				
				broadcast(message);
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

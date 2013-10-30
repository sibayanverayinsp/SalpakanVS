package com.salpakan.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.Map;

import com.salpakan.constants.Constants;

public class GameServer implements Runnable {
	
	private DatagramSocket serverSocket;

	private GameState game;
	
	private int error;
	private int gameState;
	private int port;
	
	private String playerData;
	
	private Thread t = new Thread(this);
	
	public GameServer(final int port) {
		this.port = port;
		this.gameState = Constants.WAITING_FOR_PLAYERS; 
		this.error = 0;
		try {
			this.serverSocket = new DatagramSocket(this.port);
			this.serverSocket.setSoTimeout(Constants.SOCKET_TIMEOUT);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			this.error = 1;
		}
		
		if (this.error == 0) {
			game = new GameState();
			System.out.println("Game created...");
			t.start();
		}
	}
	
	public int getError() {
		return this.error;
	}
	
	private void broadcast(final String message) {
		String name;
		NetPlayer player;
		Map<String, NetPlayer> players = game.getPlayers();
		for (Iterator<String> i = players.keySet().iterator(); i.hasNext();) {
			name = i.next();
			player = players.get(name);
			this.send(player, message);
		}
	}
	
	private void send(final NetPlayer player, final String message) {
		byte[] buffer = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, player.getAddress(), player.getPort());
		try {
			serverSocket.send(packet);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				serverSocket.receive(packet);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			playerData = new String(buffer);
			playerData = playerData.trim();
			
			switch (this.gameState) {
				case Constants.WAITING_FOR_PLAYERS:
					
					break;
				case Constants.GAME_END:
	
					break;
				case Constants.IN_PROGRESS:
					System.out.println(playerData);
					break;
				case Constants.GAME_START:
					System.out.println("Game started...");
					this.broadcast("START");
					this.gameState = Constants.IN_PROGRESS;
					this.broadcast(game.toString());
					break;
				default:
					break;
			}
		}
	}

}

/**
 * @name Last Tank Standing
 * @version 1.3
 * @class GameServer
 * @author
 * 		Jasper A. Sibayan
 * 		2009-46112
 * @author
 *		Wilbert G. Verayin
 *		2009-60315
 * @date October 17, 2012 1st Semester, AY 2012-2013
 * 		CMSC 137 B-5L
 *		
 *		University of the Philippines Los Banos
 * 		Institute of Computer Science
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameServer implements Runnable, Constants {
	String playerData;
	boolean start = false;
	boolean started = false;
	int playerCount = 0;
	int playerID = 0;
	int gameStage = WAITING_FOR_PLAYERS;
	int numPlayers = MAXPLNUM;
	int error = 0;
	int gsPort = 4500;
    DatagramSocket serverSocket = null;
	GameState game;
	Thread t = new Thread(this);
	//Constructor
	public GameServer(int port, int numPlayers) {
		this.numPlayers = numPlayers;
		this.gsPort = port;
		try {
			error = 0;
			serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		}
		catch(IOException e) {
			System.err.println("Could not listen on port: "+port);
			error = 1;
		}
		catch(Exception e) {
		
		}
		//Create the game state
		game = new GameState();
		System.out.println("Game created...");
		//Start the game thread
		t.start();
	}
	public int getError() {
		return error;
	}
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg) {
		for(Iterator ite = game.getPlayers().keySet().iterator(); ite.hasNext();) {
			String name = (String)ite.next();
			NetPlayer player = (NetPlayer) game.getPlayers().get(name);
			send(player,msg);
		}
	}
	//send data to a client
	public void send(NetPlayer player, String msg) {
		DatagramPacket packet;
		byte buf[] = msg.getBytes();
		packet = new DatagramPacket(buf,buf.length,player.getAddress(),player.getPort());
		try {
			serverSocket.send(packet);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public void run() {
		while(true) {
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			try {
     			serverSocket.receive(packet);
			}
			catch(Exception ioe) {
			
			}
			/**
			 * Convert the array of bytes to string
			 */
			playerData = new String(buf);
			//remove excess bytes
			playerData = playerData.trim();
			// process
			switch(gameStage) {
				case WAITING_FOR_PLAYERS:
					if(playerData.startsWith("CONNECT")) {
						String tokens[] = playerData.split(" ");
						NetPlayer player = new NetPlayer(tokens[1],packet.getAddress(),packet.getPort(),playerID++);
						//initialize state of a player
						System.out.println(playerID+"");
						if(playerID-1 == 0) {
							player.setX(100);
							player.setY(200);
							player.setDirection(RIGHT);
						}
						if(playerID-1 == 1) {
							player.setX(250);
							player.setY(500);
							player.setDirection(UP);
						}
						if(playerID-1 == 2) {
							player.setX(700);
							player.setY(400);
							player.setDirection(LEFT);
						}
						if(playerID-1 == 3) {
							player.setX(500);
							player.setY(100);
							player.setDirection(DOWN);
						}
						if(playerID-1 == 4) {
							player.setX(100);
							player.setY(400);
							player.setDirection(RIGHT);
						}
						if(playerID-1 == 5) {
							player.setX(500);
							player.setY(600);
							player.setDirection(UP);
						}
						if(playerID-1 == 6) {
							player.setX(700);
							player.setY(200);
							player.setDirection(LEFT);
						}
						if(playerID-1 == 7) {
							player.setX(250);
							player.setY(100);
							player.setDirection(DOWN);
						}
						broadcast(game.toString());
						broadcast(game.toString());
						System.out.println("Player connected: "+tokens[1]);
						game.update(tokens[1].trim(),player);
						broadcast("CONNECTED "+tokens[1]);
						playerCount++;
						if(started) {
							//triggers the start of game
							gameStage = GAME_START;
						}
					}
					break;		
				case GAME_START:
					System.out.println("Game State: START");
					broadcast("START");
					gameStage = IN_PROGRESS;
					broadcast(game.toString());
					broadcast(game.toString());
					break;
				case IN_PROGRESS:
					System.out.println(playerData);
					//Player data was received!
					if(playerData.startsWith("PLAYER")) {
						//Tokenize:
						String[] playerInfo = playerData.split(" ");
						String pname = playerInfo[1];
						NetPlayer check = (NetPlayer) game.getPlayers().get(pname);
						if(check.getHP() <= 0)
							continue;
						int x = 0, y = 0, direction = 0, b1x = 0, b1y = 0, b2x = 0, b2y = 0;
						if(!playerData.startsWith("PLAYERBULLET")) {
							x = Integer.parseInt(playerInfo[2].trim());
							y = Integer.parseInt(playerInfo[3].trim());
							direction = Integer.parseInt(playerInfo[8].trim());
						}
						else {
							b1x = Integer.parseInt(playerInfo[4].trim());
							b1y = Integer.parseInt(playerInfo[5].trim());
							b2x = Integer.parseInt(playerInfo[6].trim());
							b2y = Integer.parseInt(playerInfo[7].trim());
						}
						//Get the player from the game state
						NetPlayer player = (NetPlayer) game.getPlayers().get(pname);
						if(!playerData.startsWith("PLAYERBULLET")) {
							player.setX(x);
							player.setY(y);
							player.setDirection(direction);
						}
						else {
							player.setb1X(b1x);
							player.setb1Y(b1y);
							player.setb2X(b2x);
							player.setb2Y(b2y);
						}
						//Update the game state
						game.update(pname, player);
						//Send to all the updated game state
						broadcast(game.toString());
					}
					else if(playerData.startsWith("DAMAGE")) {
						String[] playerInfo = playerData.split(" ");
						String pname = playerInfo[1];
						String qname = playerInfo[2];
						NetPlayer player = (NetPlayer) game.getPlayers().get(pname);
						NetPlayer player2 = (NetPlayer) game.getPlayers().get(qname);
						if(player.getHP() <= 0)
							continue;
						//damage
						player.setHP(player.getHP()-10);
						boolean killed = false;
						if(player.getHP() <= 0) {
							player.setX(-100);
							player.setY(-100);
							player.setDirection(8);
							killed = true;
						}
						//life steal :)
						player2.setScore(player2.getScore()+10);
						if(killed)
							player2.setScore(player2.getScore()+30);
						player2.setHP(player2.getHP()+5);
						//Update the game state
						game.update(pname, player);
						game.update(qname, player2);
						//Send to all the updated game state
						broadcast(game.toString());
						if(killed) {
							send(player,"DEATH");
							playerCount--;
							if(playerCount == 1)
								send(player2,"WIN");
						}
					}
					break;
				default:
					break;
			}
		}
	}
}
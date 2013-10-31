/**
 * @name Last Tank Standing
 * @version 1.3
 * @class ChatServer
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

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class ChatServer implements Runnable {
	static Vector handlers = new Vector();
	//store players data
	String[] kasali = new String[10];
	String[] ipArr = new String[10];
	String[] portArr = new String[10];
	String[] gamesArr = new String[10];
	int index = 0;
	int portipid = 0;
	int gamesID = 0;
	int currentPort = 4500;
	Thread keeper = new Thread(this);
	DataInputStream dis;
	DataOutputStream dos;
	//Constructor
	public ChatServer(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		Socket s = new Socket(InetAddress.getLocalHost(),4000);
		dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
		keeper.start();
		while(true) {
		//wait for clients
			Socket client = server.accept();
			System.out.println("Accepted from "+ client.getInetAddress());
			ChatHandler c = new ChatHandler(client);
			handlers.addElement(c);
			c.start();
		}
	}
	public void run() {
	//handle client data
		while(true) {
			try {
				String line = dis.readUTF();
				if(line.startsWith("JOIN")) {
				//add to player list
					System.out.println(line);
					String bago = (line.toString().split(" ")[1]).trim();
					broadcast("SYSTEM: "+bago +" has entered the game lobby.");
					kasali[index++] = bago;
					String mgaKasali = "MGAKASALI ";
					for(int i = 0; i < index; i++) {
						mgaKasali+=kasali[i]+" ";
					}
					broadcast(mgaKasali);
				}
				else if(line.startsWith("QUIT")) {
				//remove from player list
					System.out.println(line);
					String alis = line.toString().split(" ")[1];
					broadcast("SYSTEM: "+ alis +" has left the game lobby.");
					String[] temp = new String[index-1];
					for(int i = 0; i < index; i++) {
						if(kasali[i].equals(alis)) {
							kasali[i] = "";
							break;
						}
					}
					int j = 0;
					for(int i = 0; i < index; i++) {
						if(!kasali[i].equals(""))
							temp[j++] = kasali[i];
					}
					index--;
					for(int i = 0; i < index; i++) {
							kasali[i] = temp[i];
					}
					String mgaKasali = "MGAKASALI ";
					for(int i = 0; i < index; i++) {
						mgaKasali+=kasali[i]+" ";
					}
					broadcast(mgaKasali);
					//remove created game of quitter
					for(int i = 0; i < gamesID; i++) {
						if(gamesArr[i].startsWith(alis)) {
							gamesArr[i] = "";
							break;
						}
					}
					j = 0;
					for(int i = 0; i < gamesID; i++) {
						if(!gamesArr[i].equals(""))
							temp[j++] = gamesArr[i];
					}
					gamesID--;
					for(int i = 0; i < gamesID; i++) {
							gamesArr[i] = temp[i];
					}
					String gameList = "GAMELIST:";
					for(int i = 0; i < gamesID; i++) {
						gameList+=gamesArr[i]+":";
					}
					System.out.println(gameList);
					broadcast(gameList);
				}				
				else if(line.startsWith("SINONGKASALI")) {
				//give player list to all
					String mgaKasali = "MGAKASALI ";
					for(int i = 0; i < index; i++) {
						mgaKasali+=kasali[i]+" ";
					}
					broadcast(mgaKasali);
				}
				else if(line.startsWith("CREATE")) {
					ipArr[portipid] = line.split(" ")[1];
					portArr[portipid] = (++currentPort) + "";
					System.out.println(ipArr[portipid]+" "+portArr[portipid]);
					broadcast("GIVEPORT "+currentPort);
					portipid++;
				}
				if(line.startsWith("GAMECREATE")) {
					System.out.println(line);
					gamesArr[gamesID++] = line.split(" ")[1]+" "+line.split(" ")[2]+" "+line.split(" ")[3];
					System.out.println(gamesArr[gamesID-1]);
					String gameList = "GAMELIST:";
					for(int i = 0; i < gamesID; i++) {
						gameList+=gamesArr[i]+":";
					}
					System.out.println(gameList);
					broadcast(gameList);
				}				
				else if(line.startsWith("PENGENGLARO")) {
				//give game list
					String gameList = "GAMELIST:";
					for(int i = 0; i < gamesID; i++) {
						gameList+=gamesArr[i]+":";
					}
					System.out.println(gameList);
					broadcast(gameList);
				}
				else if(line.startsWith("REMOVEGAME")) {
				//remove game
					System.out.println(line);
					String alis = line.replace("REMOVEGAME","");
					broadcast("SYSTEM: "+alis.split(" ")[0]+"'s game has started.");
					String[] temp = new String[gamesID-1];
					for(int i = 0; i < gamesID; i++) {
						if(gamesArr[i].equals(alis)) {
							gamesArr[i] = "";
							break;
						}
					}
					int j = 0;
					for(int i = 0; i < gamesID; i++) {
						if(!gamesArr[i].equals(""))
							temp[j++] = gamesArr[i];
					}
					gamesID--;
					for(int i = 0; i < gamesID; i++) {
							gamesArr[i] = temp[i];
					}
					String gameList = "GAMELIST:";
					for(int i = 0; i < gamesID; i++) {
						gameList+=gamesArr[i]+":";
					}
					System.out.println(gameList);
					broadcast(gameList);
				}
				System.out.println(line);
			}
			catch(Exception e) {
			
			}
		}
	}
	protected static void broadcast(String message) {
		synchronized(handlers) {
			Enumeration e = handlers.elements();
			while(e.hasMoreElements()) {
				ChatHandler c = (ChatHandler) e.nextElement();
				try {
					synchronized(c.o) {
						c.o.writeUTF(message);
					}
					c.o.flush();
				}
				catch(IOException ex) {
					c.stop();
				}
			}
		}
	}
	public static void main(String args[]) throws IOException {
		JOptionPane.showMessageDialog(null,"Server at "+InetAddress.getLocalHost().toString().split("/")[1]+" is up and running!");
		new ChatServer(4000);
	}
}
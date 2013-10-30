/**
 * @name Last Tank Standing
 * @version 1.3
 * @class ChatHandler
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

public class ChatHandler extends Thread {
	Socket s;
	DataInputStream i;
	DataOutputStream o;
	ArrayList<String> playerNames = new ArrayList<String>();
	String sendPlayers = "PLAYERS ";
	DatagramSocket serverSocket = null;
	static Vector handlers = new Vector();
	//Constructor
	public ChatHandler(Socket s) throws IOException {
		this.s = s;
		i = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		o = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}	
	public void run() {
		String name = s.getInetAddress().toString();
		try {
			handlers.addElement(this);
			while(true) {
				String msg = i.readUTF();
				broadcast(msg);
			}
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		finally {
			handlers.removeElement(this);
			try {
				s.close();
			}
			catch(IOException ex) {
				ex.printStackTrace();
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
	public void send(InetAddress addr, String msg) {
		DatagramPacket packet;
		byte buf[] = msg.getBytes();
		packet = new DatagramPacket(buf,buf.length,addr,4000);
		try {
			serverSocket.send(packet);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
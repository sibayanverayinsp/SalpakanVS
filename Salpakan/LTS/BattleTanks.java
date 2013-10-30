/**
 * @name Last Tank Standing
 * @version 1.3
 * @class BattleTanks
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

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.*;
import java.util.Random;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//This is the Game Client
public class BattleTanks extends JPanel implements Runnable, Constants, ActionListener {
	JFrame frame = new JFrame();
	JPanel bg = new JPanel();
	int bulletSpeed = 30, x, y, xSpeed = 10, ySpeed = 10, prevX = -1, prevY = -1, b1x = -1, b1y = -1, b2x = -1, b2y = -1, dir = UP, b1dir = UP, b2dir = UP, f1 = 0, f2 = 0, hp = 100, score = 0, id;
	int port;
	//stores enemy data
	int[] tanksX = new int[10];
	int[] tanksY = new int[10];
	int[] tanksID = new int[10];
	String[] tanksName = new String[10];
	String name = "Player";
	String pname;
	String server = "localhost";
	String serverData;
	boolean connected = false;
	BufferedImage offscreen;
	Random pos = new Random();
	Thread t = new Thread(this);
    DatagramSocket socket = new DatagramSocket();
	//bullet movement threads
	Timer t1 = new Timer(30,this);
	Timer t2 = new Timer(30,this);
	//Constructor
	public BattleTanks(String server, String name, int port) throws Exception {
		URL myurl = this.getClass().getResource("images/bg.png");
		Toolkit tk = this.getToolkit();
		ImageIcon imh = new ImageIcon(tk.getImage(myurl));
		ImagePanel panel = new ImagePanel(imh.getImage());
		add(panel);
		this.server = server;
		this.name = name;
		this.port = port;
		frame.setTitle(APP_NAME+": "+name);
		socket.setSoTimeout(100);
		frame.setSize(FIELD_WIDTH-10,FIELD_HEIGHT);
		frame.getContentPane().add(this);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		//create the buffers
		offscreen = (BufferedImage)this.createImage(FIELD_WIDTH,FIELD_HEIGHT);
		panel.setBounds(10,10,FIELD_WIDTH, FIELD_HEIGHT);
		//Add Listener
		frame.addKeyListener(new KeyHandler());
		//time to play
		t.start();
	}
	//Methods
	public void send(String msg) {
	//send data to the GameServer
		try {
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf,buf.length,address,port);
        	socket.send(packet);
        }
		catch(Exception e) {
		
		}
	}
	public void run() {
	//the listener, it accepts data from the server
		while(true) {
			try {
				Thread.sleep(1);
			}
			catch(Exception ioe) {
			
			}
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			try {
     			socket.receive(packet);
			}
			catch(Exception ioe) {
			
			}
			serverData = new String(buf);
			serverData = serverData.trim();
			//Processes the received data
			if(!connected && serverData.startsWith("CONNECTED")) {
				connected = true;
				System.out.println("Connected.");
			}
			else if(!connected) {
				System.out.println("Connecting..");				
				//request connection
				send("CONNECT "+name);
			}
			else if(connected) {
				offscreen.getGraphics().clearRect(0,0,FIELD_WIDTH,FIELD_HEIGHT);
				System.out.println(serverData);
				if(serverData.startsWith("PLAYER")) {
					String[] playersInfo = serverData.split(":");
					//Store data
					for(int i = 0; i < playersInfo.length; i++) {
						String[] playerInfo = playersInfo[i].split(" ");
						String qpname = playerInfo[1];
						tanksName[i] = playerInfo[1];
						tanksX[i] = Integer.parseInt(playerInfo[2]);
						tanksY[i] = Integer.parseInt(playerInfo[3]);
						int qx = Integer.parseInt(playerInfo[2]);
						int qy = Integer.parseInt(playerInfo[3]);
						int qbx1 = Integer.parseInt(playerInfo[4]);
						int qby1 = Integer.parseInt(playerInfo[5]);
						int qbx2 = Integer.parseInt(playerInfo[6]);
						int qby2 = Integer.parseInt(playerInfo[7]);
						int qdir = Integer.parseInt(playerInfo[8]);
						int qhp = Integer.parseInt(playerInfo[9]);
						int qscore = Integer.parseInt(playerInfo[10]);
						int qid = Integer.parseInt(playerInfo[11]);
						System.out.println(tanksName[i]+" = ?"+name);
						Graphics g = offscreen.getGraphics();
						if(tanksName[i].equals(name)) {
							System.out.println("Store details");
							x = qx;
							y = qy;
							dir = qdir;
							hp = qhp;
							score = qscore;
							g.setColor(Color.blue);
						}
						//draw tank upward
						if(qdir == UP) {
							g.fillRect(qx,qy+10,10,20);
							g.fillRect(qx+10,qy,10,20);
							g.fillRect(qx+20,qy+10,10,20);
						}
						//draw tank downward
						if(qdir == DOWN) {
							g.fillRect(qx,qy,10,20);
							g.fillRect(qx+10,qy+10,10,20);
							g.fillRect(qx+20,qy,10,20);
						}
						//draw tank left
						if(qdir == LEFT) {
							g.fillRect(qx+10,qy,20,10);
							g.fillRect(qx,qy+10,20,10);
							g.fillRect(qx+10,qy+20,20,10);
						}
						//draw tank right
						if(qdir == RIGHT) {
							g.fillRect(qx,qy,20,10);
							g.fillRect(qx+10,qy+10,20,10);
							g.fillRect(qx,qy+20,20,10);
						}
						//bullet
						g.fillOval(qbx1,qby1,10,10);
						g.fillOval(qbx2,qby2,10,10);
						g.setColor(Color.black);
						g.drawString(qpname,qx-20,qy+40);
						g.drawString("HP: "+qhp,qx-20,qy+50);
						g.setColor(pickColor(qhp));
						g.fillRect(qx-20,qy+50,qhp/2,10);
						g.setColor(Color.black);
						offscreen.getGraphics().drawRect(20,20,FIELD_WIDTH-60,FIELD_HEIGHT-60);
						g.clearRect(0,0,10,20);
					}
					//show the changes
					frame.repaint();
				}
				//handles death
				if(serverData.startsWith("DEATH")) {
					JOptionPane.showMessageDialog(null,"You have died!","BattleTanks",JOptionPane.ERROR_MESSAGE,null);
				}
				//handles victory
				if(serverData.startsWith("WIN")) {
					JOptionPane.showMessageDialog(null,"You are the Last Tank Standing!","BattleTanks",JOptionPane.INFORMATION_MESSAGE,null);
				}
			}
		}
	}
	public void paintComponent(Graphics g) {
		g.drawImage(offscreen,0,0,null);
	}
	public class KeyHandler extends KeyAdapter {
	//send data to server whenever updating the game
		public void keyPressed(KeyEvent ke) {
			if(hp <= 0)
				return;
			prevX = x;
			prevY = y;
			switch(ke.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					y = (y+ySpeed < FIELD_HEIGHT-60)? y + ySpeed: y;
					dir = DOWN;
					break;
				case KeyEvent.VK_UP:
					y = (y-ySpeed > 10)? y - ySpeed: y;
					dir = UP;
					break;
				case KeyEvent.VK_LEFT:
					x = (x-xSpeed > 10)? x - xSpeed: x;
					dir = LEFT;
					break;
				case KeyEvent.VK_RIGHT:
					x = (x+xSpeed < FIELD_WIDTH-60)? x + xSpeed: x;
					dir = RIGHT;
					break;
				case KeyEvent.VK_SPACE:
					if(f1 == 0) {
						f1 = 1;
						b1y = y + 10;
						b1x = x + 10;
						b1dir = dir;
						t1.start();
					}
					else if(f2 == 0) {
						f2 = 1;
						b2y = y + 10;
						b2x = x + 10;
						b2dir = dir;
						t2.start();
					}
					break;
			}
			//check tank colission
			int i;
			for(i = 0; i < 10; i++) {
				if(tanksName[i] == name && tanksID[i] == id)
					continue;
				if((x+20 == tanksX[i] || x-20 == tanksX[i]) && (y+20 == tanksY[i] || y-20 == tanksY[i]) || (x+10 == tanksX[i] || x-10 == tanksX[i]) && (y+20 == tanksY[i] || y-20 == tanksY[i]) || (x == tanksX[i] || x == tanksX[i]) && (y+20 == tanksY[i] || y-20 == tanksY[i]) || (x+20 == tanksX[i] || x-20 == tanksX[i]) && (y+10 == tanksY[i] || y-10 == tanksY[i] || y == tanksY[i]))
					break;
			}
			if(i < 10) {
				x = prevX;
				y = prevY;
			}
			send("PLAYER "+name+" "+x+" "+y+" "+b1x+" "+b1y+10+" "+b2x+" "+b2y+ " "+dir+" "+hp+" "+score);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == t1) {
			if(b1dir == UP)
				b1y-=bulletSpeed;
			if(b1dir == DOWN)
				b1y+=bulletSpeed;
			if(b1dir == LEFT)
				b1x-=bulletSpeed;
			if(b1dir == RIGHT)
				b1x+=bulletSpeed;
			if(b1y < 0 || b1x < 0 || b1x > FIELD_WIDTH || b1y > FIELD_WIDTH) {
				t1.stop();
				f1 = 0;
			}
			//check bullet-tank collision
			int i;
			for(i = 0; i < 10; i++) {
				if(tanksName[i] == name && tanksID[i] == id)
					continue;
				if((b1x >= tanksX[i] && b1x < tanksX[i]+30) && (b1y >= tanksY[i] && b1y < tanksY[i]+30))
					break;
			}
			if(i < 10) {
				score+=10;
				hp+=5;
				b1x = -1;
				b1y = -1;
				t1.stop();
				f1 = 0;
				send("DAMAGE "+tanksName[i]+ " " + name);
			}
			send("PLAYERBULLET "+name+" "+x+" "+y+" "+b1x+" "+b1y+" "+b2x+" "+b2y+ " "+dir+" "+hp+" "+score);
		}
		if(e.getSource() == t2) {
			if(b2dir == UP)
				b2y-=bulletSpeed;
			if(b2dir == DOWN)
				b2y+=bulletSpeed;
			if(b2dir == LEFT)
				b2x-=bulletSpeed;
			if(b2dir == RIGHT)
				b2x+=bulletSpeed;
			if(b2y < 0 || b2x < 0 || b2x > FIELD_WIDTH || b2y > FIELD_WIDTH) {
				t2.stop();
				f2 = 0;
			}
			if((b1x >= x && b1x < x+30) && (b1y >= y && b1y < y+30))
				score+=10;
			//check bullet-tank collision
			int i;
			for(i = 0; i < 10; i++) {
				if(tanksName[i] == name && tanksID[i] == id)
					continue;
				if((b2x >= tanksX[i] && b2x < tanksX[i]+30) && (b2y >= tanksY[i] && b2y < tanksY[i]+30))
					break;
			}
			if(i < 10) {
				score+=10;
				hp+=5;
				b2x = -1;
				b2y = -1;
				t2.stop();
				f2 = 0;
				send("DAMAGE "+tanksName[i]+ " " + name);
			}
			send("PLAYERBULLET "+name+" "+x+" "+y+" "+b1x+" "+b1y+" "+b2x+" "+b2y+ " "+dir+" "+hp+" "+score);
		}
	}
	public Color pickColor(int hp) {
		//typical shades of hp
		if(hp < 0)
			return new Color(0,0,0);
		if(hp > 0 && hp <= 10)
			return new Color(255,0,0);
		if(hp > 10 && hp <= 20)
			return new Color(255,45,0);
		if(hp > 20 && hp <= 30)
			return new Color(255,165,0);
		if(hp > 30 && hp <= 40)
			return new Color(255,200,0);
		if(hp > 40 && hp <= 50)
			return new Color(255,225,0);
		if(hp > 50 && hp <= 60)
			return new Color(225,255,0);
		if(hp > 60 && hp <= 70)
			return new Color(200,255,0);
		if(hp > 70 && hp <= 80)
			return new Color(165,255,0);
		if(hp > 80 && hp <= 90)
			return new Color(45,255,0);
		if(hp > 90 && hp <= 100)
			return new Color(0,255,0);
		return new Color(0,255,0);
	}
}
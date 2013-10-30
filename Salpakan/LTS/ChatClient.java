/**
 * @name Last Tank Standing
 * @version 1.3
 * @class ChatClient
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.util.*;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClient extends JFrame implements Runnable, Constants {
	DataInputStream i;
	DataOutputStream o;
	JTextArea output;
	JTextArea list;
	JTextArea gameslist;
	JLabel chatLabel = new JLabel("Chat Messages");
	JLabel playersLabel = new JLabel("Online Players");
	JLabel gamesLabel = new JLabel("Available Games");
	JScrollPane oscroll;
	JScrollPane lscroll;
	JScrollPane gscroll;
	TextField input;
	TextField names;
	JButton send;
	JButton start;
	JButton create;
	JButton j1;
	JButton j2;
	JButton j3;
	JButton j4;
	Thread listener;
	protected static Vector handlers = new Vector();
	String pname = "";
	String text = "text";
	String sendPlayers = "PLAYERS ";
	String ipAdd;
	static String findChatServer;
	String[] mgaKasali = new String[10];
	String[] games;
	ArrayList<String> playerNames = new ArrayList <String>();
	int prevPort = 4444;
	int port = 4444;
	int portOfCreator;
	GameServer gs;
	boolean startLabel = false;
	//Constructor
	public ChatClient(String title, InputStream i, OutputStream o, String name) {
		super(title);
		System.out.println(pname);
		this.i = new DataInputStream(new BufferedInputStream(i));
		this.o = new DataOutputStream(new BufferedOutputStream(o));
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		Container c = this.getContentPane();
		c.setBackground(new Color(0,134,139));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		URL myurl = this.getClass().getResource("images/background.png");
		Toolkit tk = this.getToolkit();
		ImageIcon imh = new ImageIcon(tk.getImage(myurl));
		ImagePanel panel = new ImagePanel(imh.getImage());
		setLayout(null);
		add(output = new JTextArea());
		add(list = new JTextArea());
		add(gameslist = new JTextArea());
		add(input = new TextField());
		add(send = new JButton("Send"));
		add(start = new JButton("Start"));
		add(create = new JButton("Create"));
		add(oscroll = new JScrollPane(output));
		add(lscroll = new JScrollPane(list));
		add(gscroll = new JScrollPane(gameslist));
		add(j1 = new JButton("Join"));
		add(j2 = new JButton("Join"));
		add(j3 = new JButton("Join"));
		add(j4 = new JButton("Join"));
		chatLabel.setFont(new Font("Stencil STD",Font.BOLD,24));
		playersLabel.setFont(new Font("Stencil STD",Font.BOLD,24));
		gamesLabel.setFont(new Font("Stencil STD",Font.BOLD,24));
		Color fontColor = new Color(0,0,0);
		chatLabel.setForeground(fontColor);
		playersLabel.setForeground(fontColor);
		gamesLabel.setForeground(fontColor);
		Color buttonColor = new Color(51,51,51);
		send.setBackground(buttonColor);
		create.setBackground(buttonColor);
		start.setBackground(buttonColor);
		j1.setBackground(buttonColor);
		j2.setBackground(buttonColor);
		j3.setBackground(buttonColor);
		j4.setBackground(buttonColor);
		send.setForeground(Color.white);
		create.setForeground(Color.white);
		start.setForeground(Color.white);
		j1.setForeground(Color.white);
		j2.setForeground(Color.white);
		j3.setForeground(Color.white);
		j4.setForeground(Color.white);
		add(chatLabel);
		add(playersLabel);
		add(gamesLabel);
		start.setEnabled(false);
		add(panel);
		send.addActionListener(new ActionListener() {
		//if send button is clicked
			public void actionPerformed(ActionEvent e) {
				text = input.getText().trim();
				if(!text.equals(""))
					buttonClicked();
				input.setText("");
			}
		});
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startLabel = false;
				start.setEnabled(false);
				create.setEnabled(true);
				create.requestFocus();
				//start
				startOwnGame();
			}
		});
		//for joining game
		j1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start
				startGame();
			}
		});
		j2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start
				startGame();
			}
		});
		j3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start
				startGame();
			}
		});
		j4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start
				startGame();
			}
		});
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start
				try {
					sendMessage("CREATE "+InetAddress.getLocalHost().toString().split("/")[1]);
					createGame(8);
					startLabel = true;
					create.setEnabled(false);
					start.setEnabled(true);
					start.requestFocus();
				}
				catch(Exception ex) {
				
				}
			}
		});
		output.setEditable(false);
		output.setLineWrap(true);
		list.setEditable(false);
		list.setLineWrap(true);
		gameslist.setEditable(false);
		gameslist.setLineWrap(true);
		j1.setVisible(false);
		j2.setVisible(false);
		j3.setVisible(false);
		j4.setVisible(false);
		show();
		input.requestFocus();
		panel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
		chatLabel.setBounds(50,WINDOW_HEIGHT-450,300,30);
		playersLabel.setBounds(WINDOW_WIDTH*2/3+30,WINDOW_HEIGHT-450,300,30);
		gamesLabel.setBounds(WINDOW_WIDTH*2/3+30,WINDOW_HEIGHT-285,300,30);
		oscroll.setBounds(50,WINDOW_HEIGHT-420,WINDOW_WIDTH*2/3-60,300);
		lscroll.setBounds(WINDOW_WIDTH*2/3+30,WINDOW_HEIGHT-420,WINDOW_WIDTH/3-60,140);
		gscroll.setBounds(WINDOW_WIDTH*2/3+30,WINDOW_HEIGHT-260,150,140);
		input.setBounds(50,WINDOW_HEIGHT-80,WINDOW_WIDTH*2/3-150,40);
		send.setBounds(WINDOW_WIDTH*2/3-80,WINDOW_HEIGHT-80,70,25);
		start.setBounds(WINDOW_WIDTH*2/3+150,WINDOW_HEIGHT-80,70,25);
		create.setBounds(WINDOW_WIDTH*2/3+60,WINDOW_HEIGHT-80,90,25);
		j1.setBounds(WINDOW_WIDTH*2/3+190,WINDOW_HEIGHT-260,60,22);
		j2.setBounds(WINDOW_WIDTH*2/3+190,WINDOW_HEIGHT-225,60,22);
		j3.setBounds(WINDOW_WIDTH*2/3+190,WINDOW_HEIGHT-190,60,22);
		j4.setBounds(WINDOW_WIDTH*2/3+190,WINDOW_HEIGHT-155,60,22);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("CLOSE");
				quitChat();
			}
		});
		boolean nameAccept = false;
		listener = new Thread(this);
		listener.start();
		//request for past game state in case that there are previous online players
		sendMessage("SINONGKASALI");
		sendMessage("PENGENGLARO");
		while(!nameAccept) {
			sendMessage("SINONGKASALI");
			try {
				pname = JOptionPane.showInputDialog("Your Name: ");
				nameAccept = !pname.trim().equals("") && pname.trim().indexOf(" ") < 0 && !Arrays.asList(mgaKasali).contains(pname.trim());
			}
			catch(Exception e) {
				nameAccept = false;
			}
		}
		//notify joining to the chatroom
		sendMessage("JOIN "+pname);
		setTitle("LTS - Game Lobby: "+pname);
	}
	public void quitChat() {
		//notify quiting from the chatroom
		sendMessage("QUIT "+pname+" "+this.toString());
	}
	public void run() {
	//process data from the server
		try {
			while(true) {
				String line = i.readUTF();
				//data for player list
				if(line.startsWith("MGAKASALI")) {
					list.setText("");
					String[] playerNames = line.split(" ");
					for(int j = 1; j < playerNames.length; j++) {
						list.append(playerNames[j]+"\n");
					}
					mgaKasali = line.split(" ");
				}
				//receive used port for this client not to use anymore.
				else if(line.startsWith("GIVEPORT")) {
					port = Integer.parseInt(line.split(" ")[1]);
				}
				//data for games list
				else if(line.startsWith("GAMELIST")) {
					gameslist.setText("");
					games = line.split(":");
					j1.setVisible(false);
					j2.setVisible(false);
					j3.setVisible(false);
					j4.setVisible(false);
					for(int j = 1; j < games.length; j++) {
						gameslist.append(games[j].split(" ")[0]+"\n\n");
						if(j == 1) {
							if(games[j].split(" ")[0].equals(pname))
								j1.setVisible(false);
							else
								j1.setVisible(true);
							modifyParams(1);
						}
						if(j == 2) {
							if(games[j].split(" ")[0].equals(pname))
								j2.setVisible(false);
							else
								j2.setVisible(true);
							modifyParams(2);
						}
						if(j == 3) {
							if(games[j].split(" ")[0].equals(pname))
								j3.setVisible(false);
							else
								j3.setVisible(true);
							modifyParams(3);
						}
						if(j == 4) {
							if(games[j].split(" ")[0].equals(pname))
								j4.setVisible(false);
							else
								j4.setVisible(true);
							modifyParams(4);
						}
					}
				}
				else if(line.startsWith("QUIT") || line.startsWith("JOIN") || line.startsWith("MGAKASALI") || line.startsWith("SINONGKASALI") || line.startsWith("GAMECREATE") || line.startsWith("REMOVEGAME") || line.startsWith("CREATE") || line.startsWith("PENGENGLARO")) {
					//don't print
				}
				else
					output.append(line.trim()+"\n");
			}
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		finally {
			listener = null;
			input.hide();
			JOptionPane.showMessageDialog(null,"There was a server error","Error",JOptionPane.ERROR_MESSAGE,null);
			System.exit(0);
			validate();
			try {
				o.close();
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	public void sendMessage(String msg) {
	//send message to server
		try {
			o.writeUTF(msg);
			o.flush();
		}
		catch(IOException ex) {
			ex.printStackTrace();
			listener.stop();
		}
	}
	public void startGame() {
		if(!create.isEnabled()) {
			JOptionPane.showMessageDialog(null,"You cannot join a game while you have your own game.","Information",JOptionPane.INFORMATION_MESSAGE,null);
			return;
		}
		try {
			JOptionPane.showMessageDialog(null,"Joining "+ipAdd+" "+pname+" "+portOfCreator, "Information",JOptionPane.INFORMATION_MESSAGE,null);
			BattleTanks bt = new BattleTanks(ipAdd,pname,portOfCreator);
		}
		catch(InterruptedException ex) {
		
		}
		catch(IOException ex) {
		
		}
		catch(Exception ex) {
		
		}
	}
	public void startOwnGame() {
		try {
			JOptionPane.showMessageDialog(null,"The game "+InetAddress.getLocalHost().toString().split("/")[1]+" "+pname+" "+portOfCreator+" has started.","Information",JOptionPane.INFORMATION_MESSAGE,null);
			BattleTanks bt;
			while(true) {
				try {
					bt = new BattleTanks(InetAddress.getLocalHost().toString().split("/")[1],pname,portOfCreator);
					if(gs.getError() == 1)
						continue;
					else
						break;
				}
				catch(InterruptedException ex) {
					continue;
				}
			}
			gs.started = true;
			//remove from available game list
			sendMessage("REMOVEGAME"+pname+" "+InetAddress.getLocalHost().toString().split("/")[1]+" "+portOfCreator);
		}
		catch(InterruptedException ex) {
		
		}
		catch(IOException ex) {
		
		}
		catch(Exception ex) {
		
		}
	}
	public void createGame(int i) {
		while(true) {
			try {
				gs = new GameServer(port,i);
				if(gs.getError() == 1)
					continue;
				else
					break;
			}
			catch(Exception ex) {
				continue;
			}
		}
		try {
			o.writeUTF("SYSTEM: "+pname+" created a game at "+InetAddress.getLocalHost().toString().split("/")[1]+ " at port "+gs.gsPort);
			portOfCreator = gs.gsPort;
			sendMessage("GAMECREATE "+pname+" "+InetAddress.getLocalHost().toString().split("/")[1]+" "+gs.gsPort);
			o.flush();
		}
		catch(IOException ex) {
			ex.printStackTrace();
			listener.stop();
		}
	}
	public void buttonClicked() {
		sendMessage(pname+": "+text);
		input.setText("");
	}
	public boolean handleEvent(Event e) {
		if((e.target == input) && (e.id == Event.ACTION_EVENT)) {
			if(((String) e.arg).trim().equals(""))
				return true;
			try {
				o.writeUTF(pname+": "+((String) e.arg).trim());
				o.flush();
			}
			catch(IOException ex) {
				ex.printStackTrace();
				listener.stop();
			}
			input.setText("");
			return true;
		}
		else if((e.target == this) && (e.id == Event.WINDOW_DESTROY)) {
			if(listener != null)
				listener.stop();
			hide();
			return true;
		}
		return super.handleEvent(e);
	}
	//send data to all.
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
	public void modifyParams(int index) {
		ipAdd = games[index].split(" ")[1];
		portOfCreator = Integer.parseInt(games[index].split(" ")[2]);
	}
	public static void main(String args[]) throws IOException, InterruptedException, Exception {
		Loading l = new Loading();
		l.dispose();
		Socket s = null;
		boolean con = false;
		while(!con) {
			findChatServer = JOptionPane.showInputDialog("Connect to Server: ");
			try {
				s = new Socket(findChatServer,4000);
				con = s.isConnected();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		new ChatClient("LTS - Game Lobby",s.getInputStream(),s.getOutputStream(),"Enjoy! :)");
	}
}
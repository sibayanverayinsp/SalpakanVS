package com.salpakan.constants;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.border.EmptyBorder;

public final class Constants {
	
	/*
	 * 		Screen Stuffs
	 */
	public static final int WINDOW_WIDTH = 1060;
	public static final int WINDOW_HEIGHT = 660;
	
	/*
	 * 		Networking Stuffs
	 */
	public static final int SOCKET_TIMEOUT = 100;
	public static final int DEFAULT_PORT = 1500;
	public static final String DEFAULT_HOST = "localhost";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/*
	 *		Game states
	 */
	public static final int GAME_START = 0;
	public static final int IN_PROGRESS = 1;
	public static final int GAME_END = 2;
	public static final int WAITING_FOR_PLAYERS = 3;
	
	/*
	 * 		App Strings
	 */
	public static final String APP_NAME = "Salpakan";
	public static final String LOBBY = "Game Lobby";
	public static final String BOARD = "Game Board";
	public static final String ABOUT = "About";
	public static final String DEFAULT_ROOM = "Default Room";
	public static final String SHOW_ENGAGED = "Show Engaged";
	public static final String SHOW_CAPTURED = "Show Captured";
	public static final String BATTLEFIELD = "The Battlefield";
	public static final String PLAYERS = "Players";
	public static final String CHAT = "Chat";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String LOGS = "Logs";

	/*
	 * 		Button Strings
	 */
	public static final String LOGIN_BUTTON = "Login";
	public static final String LOGOUT_BUTTON = "Logout";
	public static final String CREATE_BUTTON = "Create";
	public static final String JOIN_BUTTON = "Join";
	public static final String CANCEL_BUTTON = "Cancel";
	public static final String SEND_BUTTON = "Send";
	
	/*
	 * 		Label Strings
	 */
	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	
	/*
	 * 		Component Style Stuffs
	 */
	public static final EmptyBorder PADDING = new EmptyBorder(10, 10, 10, 10);
	public static final EmptyBorder MARGIN = new EmptyBorder(20, 20, 20, 20);
	public static final Dimension BUTTON_DIMENSION = new Dimension(80, 30);
	public static final Dimension TEXTFIELD_DIMENSION = new Dimension(200, 30);
	public static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 15);
	
	/*
	 * 		CardLayout Strings
	 */
	public static final String TABS = "Tabs Panel";
	public static final String LOGIN = "Login Panel";
	
	/*
	 * 		Board Stuffs
	 */
	public static final int ROW = 8;
	public static final int COLUMN = 9;
	public static final Dimension GRID_SIZE = new Dimension(60, 60);
	
	/*
	 * 		Error Strings
	 */
	public static final String ERROR = "Error!";
	public static final String FIELDS_REQUIRED = "All fields are required!";
	public static final String SERVER_CRASHED = "Server crashed!";
	public static final String CONNECT_ERROR = "Error connecting to server!";
	public static final String STREAM_ERROR = "Error creating streams!";
	public static final String WRITE_ERROR = "Error writing to streams!";
	public static final String DISCONNECTED = "Disconnected from the server!";
	
	/*
	 * 		Server Strings
	 */
	public static final String START = "Start";
	public static final String STOP = "Stop";
	public static final String SERVER = "Server";
	public static final String SERVER_STARTED = "Server started!";
	public static final String SERVER_STOPPED = "Server stopped!";
	public static final String LOGS_IN = "Logs in";
	public static final String LOGS_OUT = "Logs out";
	
	/*
	 * 		Timer Strings
	 */
	public static final String TIMER_OPTIONS = "Timer Options";
	public static final String NO_TIMER = "No timer";
	public static final String TWO_MIN = "2 minutes";
	public static final String FIVE_MIN = "5 minutes";
	public static final String EIGHT_MIN = "8 minutes";
	public static final String TEN_MIN = "10 minutes";
	public static final String TWENTY_MIN = "20 minutes";
	
}

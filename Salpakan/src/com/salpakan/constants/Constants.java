package com.salpakan.constants;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.border.EmptyBorder;

public final class Constants {
	
	/*
	 * 		Screen Stuffs
	 */
	public static final Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int SCREEN_WIDTH = (int) SCREEN.getWidth();
	public static final int SCREEN_HEIGHT = (int) SCREEN.getHeight();
	public static final int WINDOW_WIDTH = SCREEN_WIDTH - 300;
	public static final int WINDOW_HEIGHT = SCREEN_HEIGHT - 100;
	
	/*
	 * 		Networking Stuffs
	 */
	public static final int SOCKET_TIMEOUT = 100;
	public static final int PORT = 1500;
	
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

	/*
	 * 		Button Strings
	 */
	public static final String LOGIN_BUTTON = "Login";
	public static final String LOGOUT_BUTTON = "Logout";
	public static final String CREATE_BUTTON = "Create";
	public static final String JOIN_BUTTON = "Join";
	public static final String CANCEL_BUTTON = "Cancel";
	
	/*
	 * 		Label Strings
	 */
	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	
	/*
	 * 		Component Style Stuffs
	 */
	public static final Insets INSET = new Insets(10, 10, 10, 10);
	public static final EmptyBorder PADDING = new EmptyBorder(INSET);
	public static final Dimension BUTTON_DIMENSION = new Dimension(80, 30);
	public static final Dimension TEXTFIELD_DIMENSION = new Dimension(200, 30);
	public static final EmptyBorder MARGIN = new EmptyBorder(20, 20, 20, 20);
	
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
}

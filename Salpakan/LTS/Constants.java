/**
 * @name Last Tank Standing
 * @version 1.3
 * @class Constants
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

public interface Constants {
	//for centralizing frame
	Toolkit usekit  =  Toolkit.getDefaultToolkit();
	Dimension screensize  =  usekit.getScreenSize();
	int screenheight  =  screensize.height;
	int screenwidth  =  screensize.width;
	
	public static final String APP_NAME  =  "Last Tank Standing v1.3";
	/**
	 * Game states.
	 */
	public static final int GAME_START = 0;
	public static final int IN_PROGRESS = 1;
	public static final int GAME_END = 2;
	public static final int WAITING_FOR_PLAYERS = 3;
	/**
	 * Game Field
	 */
	public static final int FIELD_WIDTH = 800;
	public static final int FIELD_HEIGHT = 600;
	/**
	 * Window Size
	 */
	public static final int WINDOW_WIDTH = 850;
	public static final int WINDOW_HEIGHT = 700;
	/**
	 * Directions		
	 */ 
	public static final int DOWN = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 6;
	public static final int UP = 8;
	/**
	 * Game port
	 */
	public static final int PORT = 4444;
	public static final int MAXPLNUM = 8;
}
/**
 * @name Last Tank Standing
 * @version 1.3
 * @class GameState
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameState {
	//Properties
	private Map players = new HashMap();
	//Methods
	public void update(String name, NetPlayer player) {
		players.put(name,player);
	}
	public String toString() {
		String retval = "";
		for(Iterator ite = players.keySet().iterator(); ite.hasNext();) {
			String name = (String) ite.next();
			NetPlayer player = (NetPlayer) players.get(name);
			retval+=player.toString()+":";
		}
		return retval;
	}
	public Map getPlayers() {
		return players;
	}
}
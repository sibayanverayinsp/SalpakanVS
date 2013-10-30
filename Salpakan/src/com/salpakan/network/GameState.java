package com.salpakan.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameState {
	
	private Map<String, NetPlayer> players = new HashMap<String, NetPlayer>();
	
	public void update(final String name, final NetPlayer player) {
		players.put(name, player);
	}
	
	public String toString() {
		String temp = "";
		String name;
		NetPlayer player;
		for (Iterator<String> i = players.keySet().iterator(); i.hasNext();) {
			name = i.next();
			player = players.get(name);
			temp = temp.concat(player.toString() + ":");
		}
		return temp;
	}
	
	public Map<String, NetPlayer> getPlayers() {
		return this.players;
	}
	
}

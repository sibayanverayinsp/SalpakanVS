package com.salpakan.network;

import java.io.Serializable;
import java.util.Date;

import com.salpakan.constants.Constants;

@SuppressWarnings("serial")
public class Message implements Serializable {
	
	public static final int CHAT = 0;
	public static final int LOGOUT = 1;
	public static final int LOGIN = 2;
	public static final int PLAYERS = 3;
	public static final int GAME_CREATED = 4;
	public static final int ROOM_GAMES = 5;
	public static final int JOIN = 6;
	public static final int GAME_CANCELLED = 7;
	
	private String date;
	private String username;
	private int type;
	private String message;

	public Message(final int type, final String username, final String message) {
		this(type, Constants.DATE_FORMAT.format(new Date()), username, message);
	}
	
	public Message(final int type, final String date, final String username, final String message) {
		this.type = type;
		this.date = date;
		this.username = username;
		this.message = message;
	}
	
	public int getType() {
		return type;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getMessage() {
		return message;
	}
	
}

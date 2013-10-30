package com.salpakan.network;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {
	
	public static final int MOVE = 0;
	public static final int CHAT = 1;
	public static final int LOGIN = 2;
	public static final int LOGOUT = 3;
	
	private int type;
	private String message;
	
	public Message(final int type, final String message) {
		this.type = type;
		this.message = message;
	}
	
	public int getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
}

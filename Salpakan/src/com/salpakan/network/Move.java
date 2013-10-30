package com.salpakan.network;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Move implements Serializable {

	private String message;
	
	public Move(final String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}

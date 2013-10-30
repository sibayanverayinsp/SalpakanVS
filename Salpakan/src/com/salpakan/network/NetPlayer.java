package com.salpakan.network;

import java.net.InetAddress;

public class NetPlayer {
	
	private InetAddress address;
	
	private int id;
	private int port;
	
	private String name;
	
	public NetPlayer(final int id, final String name, final InetAddress address, final int port) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public InetAddress getAddress() {
		return this.address;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String toString() {
		return "PLAYER: ".concat(this.name + " ").concat(this.id + "");
	}

}

package com.salpakan.app;

import com.salpakan.ui.views.MainView;

public class App {
	
	private static App me;
	
	private String username;
	private String password;
	
	private final MainView mainView;
	
	public App() {
		me = this;
		mainView = new MainView();
		mainView.setVisible(true);
	}
	
	public static App getInstance() {
		return me;
	}
	
	public void clearCredentials() {
		setUsername("");
		setPassword("");
		getMainview().getLoginPanel().clearFields();
	}
	
	public MainView getMainview() {
		return mainView;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(final String password) {
		this.password = password;
	}
	
	public void setUsername(final String username) {
		this.username = username;
	}
	
	//app main
	public static void main(final String[] args) {
		new App();
	}
	
}

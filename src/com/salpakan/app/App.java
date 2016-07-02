package com.salpakan.app;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.salpakan.constants.Constants;
import com.salpakan.network.Client;
import com.salpakan.ui.views.GameLobbyView;
import com.salpakan.ui.views.LoginView;
import com.salpakan.ui.views.MainView;

public class App {
	
	private static App me;
	
	private String username;
	private String password;
	
	private final MainView mainView;
	
	private Client client;
	
	private boolean isConnected;
	private boolean isGameCreated;
	private boolean joinedAGame;
	
	public App() {
		me = this;
		client = null;
		isConnected = false;
		isGameCreated = false;
		joinedAGame = false;
		mainView = new MainView();
		mainView.setVisible(true);
	}
	
	public static App getInstance() {
		return me;
	}
	
	public void alertError(final String message) {
		JOptionPane.showMessageDialog(mainView, message, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
	}
	
	public void clearCredentials() {
		client = null;
		setUsername("");
		setPassword("");
	}
	
	public Client getClient() {
		return client;
	}
	
	public LoginView getLoginView() {
		return mainView.getLoginView();
	}
	
	public JPanel getMainPanel() {
		return mainView.getMainPanel();
	}
	
	public MainView getMainView() {
		return mainView;
	}
	
	public GameLobbyView getLobby() {
		return mainView.getTabsView().getLobby();
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public boolean isGameCreated() {
		return isGameCreated;
	}
	
	public boolean joinedAGame() {
		return joinedAGame;
	}
	
	public void setClient(final Client client) {
		this.client = client;
	}
	
	public void setIsConnected(final boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public void setIsGameCreated(final boolean isGameCreated) {
		this.isGameCreated = isGameCreated;
	}
	
	public void setJoinedAGame(final boolean joinedAGame) {
		this.joinedAGame = joinedAGame;
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

package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.network.Client;
import com.salpakan.network.Message;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class TabView extends JPanel {

	private final class LogoutButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			final App app = App.getInstance();
			
			if (!app.isConnected()) {
				return;
			}
			
			final JPanel mainPanel = app.getMainPanel();
			final Client client = app.getClient();
			final String username = app.getUsername();
			client.sendMessage(new Message(Message.PLAYERS, username, Constants.LOGOUT_BUTTON));
			client.sendMessage(new Message(Message.ROOM_GAMES_LOGOUT, username, "update rooms on logout"));
			client.sendMessage(new Message(Message.LOGOUT, username, Constants.LOGS_OUT.toLowerCase()));
			app.clearCredentials();
			app.getLoginView().clearFields();
			((CardLayout) mainPanel.getLayout()).show(mainPanel, Constants.LOGIN);
		}
	}
	
	private JButton logoutButton;
	private GameLobbyView lobbyPanel;

	public TabView() {
		setLayout(new BorderLayout());
		initTabView();
	}
	
	private void initTabView() {
		final JTabbedPane tabbedPane = new JTabbedPane();
		final JPanel logoutPanel = new JPanel(new BorderLayout());
		logoutButton = new JButton(Constants.LOGOUT_BUTTON);
		lobbyPanel = new GameLobbyView();
		final GameView gamePanel = new GameView();
		final AboutView aboutPanel = new AboutView();
		
		tabbedPane.addTab(Constants.LOBBY, lobbyPanel);
		tabbedPane.addTab(Constants.BOARD, gamePanel);
		tabbedPane.addTab(Constants.ABOUT, aboutPanel);
		
		ComponentUtils.setCustomButton(logoutButton);
		logoutButton.addActionListener(new LogoutButtonActionListener());
		
		logoutPanel.add(logoutButton, BorderLayout.EAST);
		
		add(logoutPanel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public GameLobbyView getLobby() {
		return lobbyPanel;
	}
	
}

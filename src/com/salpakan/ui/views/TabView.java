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
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class TabView extends JPanel {

	private final class LogoutButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			final App app = App.getInstance();
			final MainView mainView = app.getMainview();
			final JPanel mainPanel = mainView.getMainPanel();
			app.clearCredentials();
			((CardLayout) mainPanel.getLayout()).show(mainPanel, Constants.LOGIN);
		}
	}
	
	private GameLobbyView lobbyPanel;

	public TabView() {
		setLayout(new BorderLayout());
		initTabView();
	}
	
	private void initTabView() {
		final JTabbedPane tabbedPane = new JTabbedPane();
		final JPanel logoutPanel = new JPanel(new BorderLayout());
		final JButton logoutButton = new JButton(Constants.LOGOUT_BUTTON);
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

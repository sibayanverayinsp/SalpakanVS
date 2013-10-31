package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.salpakan.constants.Constants;
import com.salpakan.network.Server;

@SuppressWarnings("serial")
public class MainView extends JFrame implements ActionListener {
	
	private final class ServerRunning extends Thread {
		@Override
		public void run() {
			server.start();
			JOptionPane.showMessageDialog(MainView.this, Constants.SERVER_CRASHED, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
			server = null;
		}
	}
	
	private JPanel mainPanel;
	private LoginView loginPanel;
	private TabView tabsPanel;
	
	private Server server;
	
	private int port;
	
	public MainView(final int port) {
		this.port = port;
		this.setName(Constants.APP_NAME);
		this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.initMainView();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (server != null) {
			server.stop();
			server = null;
			return;
		}
		server = new Server(port, this);
		new ServerRunning().start();
	}
	
	public void clearServer() {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
	
	public LoginView getLoginPanel() {
		return loginPanel;
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public TabView getTabsPanel() {
		return tabsPanel;
	}
	
	private void initMainView() {
		tabsPanel = new TabView();
		mainPanel = new JPanel(new CardLayout());
		loginPanel = new LoginView();

		mainPanel.add(loginPanel, Constants.LOGIN);		
		mainPanel.add(tabsPanel, Constants.TABS);
		
		this.add(mainPanel, BorderLayout.CENTER);
	}

}

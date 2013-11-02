package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.salpakan.constants.Constants;

@SuppressWarnings("serial")
public class MainView extends JFrame {
	
	private JPanel mainPanel;
	private LoginView loginPanel;
	private TabView tabsPanel;
	
	public MainView() {
		this.setName(Constants.APP_NAME);
		this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.initMainView();
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

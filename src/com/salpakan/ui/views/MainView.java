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
		setName(Constants.APP_NAME);
		setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		initMainView();
	}
	
	public LoginView getLoginView() {
		return loginPanel;
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public TabView getTabsView() {
		return tabsPanel;
	}
	
	private void initMainView() {
		tabsPanel = new TabView();
		mainPanel = new JPanel(new CardLayout());
		loginPanel = new LoginView();

		mainPanel.add(loginPanel, Constants.LOGIN);		
		mainPanel.add(tabsPanel, Constants.TABS);
		
		add(mainPanel, BorderLayout.CENTER);
	}

}

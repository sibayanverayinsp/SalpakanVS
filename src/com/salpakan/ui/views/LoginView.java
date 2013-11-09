package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.network.Client;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class LoginView extends JPanel {

	private final class LoginActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final String user = username.getText().trim();
			final char[] passChar = password.getPassword();
			String pass = "";
			
			for (int i = 0; i < passChar.length; i++) {
				pass += passChar[i];
			}
			pass = pass.trim();
			
			if (user.length() == 0 || pass.length() == 0) {
				App.getInstance().alertError(Constants.FIELDS_REQUIRED);
				password.setText("");
				return;
			}
			
			final App app = App.getInstance();
			final JPanel mainPanel = app.getMainPanel();
			
			app.setUsername(user);
			app.setPassword(pass);
			app.setClient(new Client(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT));
			
			if (!app.getClient().start()) {
				return;
			}
			
			app.setIsConnected(true);
			((CardLayout) mainPanel.getLayout()).show(mainPanel, Constants.TABS);
		}
	}

	private JPanel loginPanel;
	private JTextField username;
	private JPasswordField password;
	
	public LoginView() {
		setLayout(new BorderLayout());
		initLoginView();
	}
	
	private void initLoginView() {
		final Box loginBox = new Box(BoxLayout.Y_AXIS);
		
		initLoginPanel();
		
		//init login box
		loginBox.add(Box.createVerticalGlue());
		loginBox.add(loginPanel);
		loginBox.add(Box.createVerticalGlue());

		add(loginBox, BorderLayout.CENTER);
	}
	
	private void initLoginPanel() {
		loginPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();
		final JLabel usernameLabel = new JLabel(Constants.USERNAME);
		final JLabel passwordLabel = new JLabel(Constants.PASSWORD);
		final JButton loginButton = new JButton(Constants.LOGIN_BUTTON);
		username = new JTextField();
		password = new JPasswordField();
		
		ComponentUtils.setCustomTextField(username);
		ComponentUtils.setCustomTextField(password);
		ComponentUtils.setCustomButton(loginButton);
		
		loginButton.addActionListener(new LoginActionListener());
		
		//init login panel
		ComponentUtils.setSize(loginPanel, 350, 250);
		ComponentUtils.setPanelBorder(loginPanel, Constants.LOGIN_BUTTON.toUpperCase(), new Font(Font.SANS_SERIF, Font.BOLD, 15));

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 10, 10);
		loginPanel.add(usernameLabel, constraints);

		constraints.gridx = 1;
		loginPanel.add(username, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		loginPanel.add(passwordLabel, constraints);
		
		constraints.gridx = 1;
		loginPanel.add(password, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		loginPanel.add(loginButton, constraints);
	}
	
	public void clearFields() {
		username.setText("");
		password.setText("");
	}
	
}

package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
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
				JOptionPane.showMessageDialog(LoginView.this, "All fields are required!", "Error!", JOptionPane.ERROR_MESSAGE);
				password.setText("");
				return;
			}
			
			final JPanel mainPanel = App.getInstance().getMainview().getMainPanel();
			final App app = App.getInstance();
			app.setUsername(user);
			app.setPassword(pass);
			((CardLayout) mainPanel.getLayout()).show(mainPanel, Constants.TABS);
		}
	}

	private JPanel loginPanel;
	private JTextField username;
	private JPasswordField password;
	
	public LoginView() {
		this.setLayout(new BorderLayout());
		this.initLoginView();
	}
	
	private void initLoginView() {
		final Box loginBox = new Box(BoxLayout.Y_AXIS);
		
		this.initLoginPanel();
		
		//init login box
		loginBox.add(Box.createVerticalGlue());
		loginBox.add(loginPanel);
		loginBox.add(Box.createVerticalGlue());

		this.add(loginBox, BorderLayout.CENTER);
	}
	
	private void initLoginPanel() {
		loginPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();
		final JLabel usernameLabel = new JLabel(Constants.USERNAME);
		final JLabel passwordLabel = new JLabel(Constants.PASSWORD);
		username = new JTextField();
		password = new JPasswordField();
		final JButton loginButton = new JButton(Constants.LOGIN_BUTTON);
		
		ComponentUtils.setCustomTextfield(username);
		ComponentUtils.setCustomTextfield(password);
		ComponentUtils.setCustomButton(loginButton);
		
		loginButton.addActionListener(new LoginActionListener());
		
		//init login panel
		ComponentUtils.setSize(loginPanel, 350, 250);
		loginPanel.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), Constants.LOGIN_BUTTON.toUpperCase(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(Font.SANS_SERIF, Font.BOLD, 15)), Constants.PADDING));

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = Constants.INSET;
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

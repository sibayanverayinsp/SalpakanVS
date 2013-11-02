package com.salpakan.app;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.salpakan.constants.Constants;
import com.salpakan.network.Server;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class AppServer extends JFrame implements ActionListener {

	private JTextArea logsArea;
	private JTextField portField;
	private JButton startStopButton;
	
	private Server server;
	private final int port;
	
	public AppServer(final int port) {
		server = null;
		this.port = port;
		setName(Constants.APP_NAME + " " + Constants.SERVER);
		setSize(Constants.WINDOW_WIDTH - 600, Constants.WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		initServerFrame();
	}
	
	private void initServerFrame() {
		final JPanel portPanel = new JPanel();
		final JPanel logsPanel = new JPanel();
		portField = new JTextField(20);
		startStopButton = new JButton(Constants.START);
		logsArea = new JTextArea(27, 37);
		
		portField.setText(port + "");
		portField.setCaretPosition(portField.getText().length());
		startStopButton.addActionListener(this);
		portPanel.add(new JLabel(Constants.PORT + ": "));
		portPanel.add(portField);
		portPanel.add(startStopButton);
		
		logsArea.setEditable(false);
		logsArea.setFocusable(false);
		logsArea.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
		
		ComponentUtils.setPanelBorder(logsPanel, Constants.LOGS);
		logsPanel.add(new JScrollPane(logsArea));
		
		add(portPanel, BorderLayout.NORTH);
		add(logsPanel, BorderLayout.CENTER);
	}
	
	public void appendLog(final String log) {
		logsArea.append(log);
		logsArea.setCaretPosition(logsArea.getText().length() - 1);
	}
	
	//server main
	public static void main(final String[] args) {
		new AppServer(Constants.DEFAULT_PORT).setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		if (server != null) {
			server.stop();
			server = null;
			startStopButton.setText(Constants.START);
			portField.setEditable(true);
			portField.requestFocusInWindow();
			return;
		}
		
		int port;
		
		try {
			port = Integer.parseInt(portField.getText().trim());
		} catch (final NumberFormatException nfe) {
			appendLog(Constants.INVALID_PORT.toUpperCase() + "\n");
			return;
		}
		
		server = new Server(port, this);
		new ServerRunning().start();
		startStopButton.setText(Constants.STOP);
		portField.setEditable(false);
	}
	
	private class ServerRunning extends Thread {
		public synchronized void run() {
			server.start();
			server = null;
			if (startStopButton.getText().equals(Constants.STOP)) {
				startStopButton.setText(Constants.START);
				portField.setEditable(true);
				portField.requestFocusInWindow();
				appendLog(Constants.SERVER_CRASHED.toUpperCase() + "\n");
			}
		}
	}
	
}

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
		this.setName(Constants.APP_NAME + " Server");
		this.setSize(Constants.WINDOW_WIDTH - 600, Constants.WINDOW_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.initServerFrame();
	}
	
	private void initServerFrame() {
		final JPanel portPanel = new JPanel();
		final JPanel logsPanel = new JPanel();
		portField = new JTextField(port + "");
		startStopButton = new JButton("Start");
		logsArea = new JTextArea(27, 37);
		
		startStopButton.addActionListener(this);
		portPanel.add(new JLabel("Port: "));
		portPanel.add(portField);
		portPanel.add(startStopButton);
		
		logsArea.setEditable(false);
		logsArea.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
		
		ComponentUtils.setPanelBorder(logsPanel, "Logs");
		logsPanel.add(new JScrollPane(logsArea));
		
		this.add(portPanel, BorderLayout.NORTH);
		this.add(logsPanel, BorderLayout.CENTER);
	}
	
	private void appendLog(final String log) {
		logsArea.append(log);
		logsArea.setCaretPosition(logsArea.getText().length() - 1);
	}
	
	//server main
	public static void main(final String[] args) {
		new AppServer(Constants.PORT).setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		if (server != null) {
			server.stop();
			server = null;
			portField.setEditable(true);
			startStopButton.setText("Start");
			return;
		}
		
		int port;
		
		try {
			port = Integer.parseInt(portField.getText().trim());
		} catch (final NumberFormatException nfe) {
			appendLog("Invalid port!\n");
			return;
		}
		
		server = new Server(port, this);
		new ServerRunning().start();
		startStopButton.setText("Stop");
		portField.setEditable(false);
	}
	
	private class ServerRunning extends Thread {
		public void run() {
			server.start();
			startStopButton.setText("Start");
			portField.setEditable(true);
			appendLog("Server crashed!\n");
			server = null;
		}
	}
	
}

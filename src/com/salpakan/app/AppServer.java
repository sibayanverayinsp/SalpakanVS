package com.salpakan.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.salpakan.constants.Constants;
import com.salpakan.network.Server;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class AppServer extends JFrame implements ActionListener {

	private JTextArea logsArea;
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
		final JPanel logsPanel = new JPanel();
		startStopButton = new JButton(Constants.START);
		logsArea = new JTextArea(27, 37);
		
		startStopButton.addActionListener(this);
		
		logsArea.setEditable(false);
		logsArea.setFocusable(false);
		logsArea.setFont(Constants.FONT);
		logsArea.setLineWrap(true);
		
		ComponentUtils.setPanelBorder(logsPanel, Constants.LOGS);
		logsPanel.add(new JScrollPane(logsArea));
		
		add(startStopButton, BorderLayout.NORTH);
		add(logsPanel, BorderLayout.CENTER);
	}
	
	public void appendLog(final String log) {
		logsArea.append(log + "\n");
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
			return;
		}
		
		server = new Server(port, this);
		new ServerRunning().start();
		startStopButton.setText(Constants.STOP);
	}
	
	private class ServerRunning extends Thread {
		public synchronized void run() {
			server.start();
			server = null;
			if (startStopButton.getText().equals(Constants.STOP)) {
				startStopButton.setText(Constants.START);
				appendLog(Constants.SERVER_CRASHED.toUpperCase());
			}
		}
	}
	
}

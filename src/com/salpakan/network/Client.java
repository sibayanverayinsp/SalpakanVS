package com.salpakan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.ui.views.GameLobbyView;

public class Client {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private final String host;
	private final int port;
	private Socket socket;
	
	public Client(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	public boolean start() {
		try {
			socket = new Socket(host, port);
		} catch (final UnknownHostException uhe) {
			App.getInstance().alertError(Constants.CONNECT_ERROR);
			return false;
		} catch (final IOException ioe) {
			App.getInstance().alertError(Constants.CONNECT_ERROR);
			return false;
		}

		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (final IOException ioe) {
			App.getInstance().alertError(Constants.STREAM_ERROR);
			return false;
		}
		
		new ListenFromServer().start();
		
		try {
			outputStream.writeObject(App.getInstance().getUsername());
		} catch (final IOException ioe) {
			App.getInstance().alertError(Constants.WRITE_ERROR);
			close();
			return false;
		}
		
		return true;
	}
	
	public void sendMessage(final Message message) {
		try {
			outputStream.writeObject(message);
		} catch (final IOException ioe) {
			App.getInstance().alertError(Constants.WRITE_ERROR);
		}
	}
	
	private void close() {
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private final class ListenFromServer extends Thread {
		public synchronized void run() {
			while (true) {
				try {
					App.getInstance().getLobby().appendChat((String) inputStream.readObject());
				} catch (final IOException ioe) {
					disconnected();
					break;
				} catch (final ClassNotFoundException cnfe) {
					disconnected();
					break;
				}
			}
		}
		
		private void disconnected() {
			final App app = App.getInstance();
			final GameLobbyView lobby = app.getLobby();
			
			app.setIsConnected(false);
			if (lobby.isShowing()) {
				lobby.appendLog(Constants.DISCONNECTED);
			} else {
				lobby.clearFields();
			}
		}
	}
	
}

package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.network.Message;
import com.salpakan.ui.components.DataListModel;
import com.salpakan.ui.components.Room;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class GameLobbyView extends JPanel implements ActionListener {

	private JTextField chatField;
	private JTextArea chatArea;
	private JTextArea logsArea;
	
	private Room defaultRoom;
	private Room showCapturedRoom;
	private Room showEngagedRoom;
	private Room theBattlefieldRoom;
	private DataListModel playersListModel;
	
	public GameLobbyView() {
		setLayout(new BorderLayout());
		initRoomPanel();
		initPlayersPanel();
		initChatPanel();
		initHelpPanel();
	}
	
	private void initRoomPanel() {
		final JPanel roomPanel = new JPanel();
		defaultRoom = new Room(Constants.DEFAULT_ROOM);
		showEngagedRoom = new Room(Constants.SHOW_ENGAGED);
		showCapturedRoom = new Room(Constants.SHOW_CAPTURED);
		theBattlefieldRoom = new Room(Constants.BATTLEFIELD);
		
		ComponentUtils.setSize(roomPanel, 0, Constants.WINDOW_HEIGHT / 3);
		roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.X_AXIS));
		roomPanel.add(defaultRoom);
		roomPanel.add(showEngagedRoom);
		roomPanel.add(showCapturedRoom);
		roomPanel.add(theBattlefieldRoom);
		
		add(roomPanel, BorderLayout.NORTH);
	}
	
	private void initPlayersPanel() {
		final JPanel playersPanel = new JPanel();
		final JList playersList = new JList();
		final JScrollPane scrollPane = new JScrollPane();
		playersListModel = new DataListModel();
		
		playersList.setModel(playersListModel);
		
		ComponentUtils.setSize(scrollPane, 180, 310);
		scrollPane.setViewportView(playersList);

		ComponentUtils.setSize(playersPanel, Constants.WINDOW_WIDTH / 5, 0);
		ComponentUtils.setPanelBorder(playersPanel, Constants.PLAYERS);
		playersPanel.add(scrollPane);
		
		add(playersPanel, BorderLayout.WEST);
	}
	
	private void initChatPanel() {
		final Container chatContainer = new Container();
		final JButton sendButton = new JButton(Constants.SEND_BUTTON);
		final JPanel chatPanel = new JPanel();
		chatField = new JTextField(42);
		chatArea = new JTextArea(13, 47);
		
		ComponentUtils.setCustomTextArea(chatArea);
		
		chatField.setFont(Constants.FONT);
		chatField.addActionListener(this);
		sendButton.addActionListener(this);
		
		chatContainer.setLayout(new BorderLayout());
		chatContainer.add(chatField, BorderLayout.WEST);
		chatContainer.add(sendButton, BorderLayout.EAST);
		
		ComponentUtils.setPanelBorder(chatPanel, Constants.CHAT);
		chatPanel.add(new JScrollPane(chatArea));
		chatPanel.add(chatContainer);
		
		add(chatPanel, BorderLayout.CENTER);
	}
	
	private void initHelpPanel() {
		final JPanel helpPanel = new JPanel();
		logsArea = new JTextArea(15, 20);
		
		ComponentUtils.setCustomTextArea(logsArea);
		
		ComponentUtils.setSize(helpPanel, Constants.WINDOW_WIDTH / 4, 0);
		ComponentUtils.setPanelBorder(helpPanel, Constants.INSTRUCTIONS);
		helpPanel.add(new JScrollPane(logsArea));
		
		add(helpPanel, BorderLayout.EAST);
	}
	
	public void appendChat(final String message) {
		chatArea.append(message + "\n");
		chatArea.setCaretPosition(chatArea.getText().length() - 1);
	}
	
	public void addGameToRoom(final String message) {
		final String[] messageArray = message.split("&");
		((DataListModel) getRoom(messageArray[0]).getList().getModel()).add(messageArray[1]);
	}
	
	public void addGamesToAllRooms(final String message) {
		final String[] room = message.split(",");
		String[] roomGames = null;
		roomGames = room[0].split("&");
		for (int i = 0, j = roomGames.length; i < j; i++) {
			if (roomGames[i].equals(" ")) {
				break;
			}
			((DataListModel) defaultRoom.getList().getModel()).add(roomGames[i]);
		}
		roomGames = room[1].split("&");
		for (int i = 0, j = roomGames.length; i < j; i++) {
			if (roomGames[i].equals(" ")) {
				break;
			}
			((DataListModel) showEngagedRoom.getList().getModel()).add(roomGames[i]);
		}
		roomGames = room[2].split("&");
		for (int i = 0, j = roomGames.length; i < j; i++) {
			if (roomGames[i].equals(" ")) {
				break;
			}
			((DataListModel) showCapturedRoom.getList().getModel()).add(roomGames[i]);
		}
		roomGames = room[3].split("&");
		for (int i = 0, j = roomGames.length; i < j; i++) {
			if (roomGames[i].equals(" ")) {
				break;
			}
			((DataListModel) theBattlefieldRoom.getList().getModel()).add(roomGames[i]);
		}
	}
	
	public void appendLog(final String log) {
		logsArea.append(log + "\n");
		logsArea.setCaretPosition(logsArea.getText().length() - 1);
	}
	
	public void clearFields() {
		chatArea.setText("");
		logsArea.setText("");
	}
	
	private Room getRoom(final String roomName) {
		if (roomName.equals(Constants.DEFAULT_ROOM)) {
			return defaultRoom;
		} else if (roomName.equals(Constants.SHOW_ENGAGED)) {
			return showEngagedRoom;
		} else if (roomName.equals(Constants.SHOW_CAPTURED)) {
			return showCapturedRoom;
		} else {
			return theBattlefieldRoom;
		}
	}
	
	public void refreshPlayersList(final String message) {
		final String[] players = message.split("&");
		playersListModel.clear();
		for (int i = 0, j = players.length; i < j; i++) {
			playersListModel.add(players[i]);
		}
	}
	
	public void removeGameFromRoom(final String message) {
		final String[] messageArray = message.split("&");
		((DataListModel) getRoom(messageArray[0]).getList().getModel()).remove(messageArray[1]);
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		final App app = App.getInstance();
		if (!app.isConnected()) {
			return;
		}
		
		app.getClient().sendMessage(new Message(Message.CHAT, app.getUsername(), chatField.getText().trim()));
		chatField.setText("");
		chatField.requestFocusInWindow();
	}
	
}

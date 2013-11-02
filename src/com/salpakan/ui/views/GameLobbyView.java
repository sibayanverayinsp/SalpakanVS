package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.ui.components.RoomListModel;
import com.salpakan.ui.components.TimerOptionsPanel;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class GameLobbyView extends JPanel {
	
	private final class CancelButtonActionListener implements ActionListener {
		
		private final RoomListModel model;
		private final JList list;
		
		private CancelButtonActionListener(final RoomListModel model, final JList list) {
			this.model = model;
			this.list = list;
		}
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final int index = list.getSelectedIndex();
			final String username = model.getElementAt(index).toString();
			if (username.substring(username.indexOf(" ") + 1).equals(App.getInstance().getUsername())) {
				model.remove(index);
				list.clearSelection();
				isGameCreated = false;
			}
		}
	}

	private final class JoinButtonActionListener implements ActionListener {
		
		private final RoomListModel model;
		private final JList list;
		
		private JoinButtonActionListener(final RoomListModel model, final JList list) {
			this.model = model;
			this.list = list;
		}
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final int index = list.getSelectedIndex();
			final String username = model.getElementAt(index).toString();
			if (!username.substring(username.indexOf(" ") + 1).equals(App.getInstance().getUsername())) {
				model.remove(index);
				list.clearSelection();
				isGameCreated = false;
			}
		}
	}

	private final class CreateButtonActionListener implements ActionListener {

		private final RoomListModel model;
		private final JList list;
		
		private CreateButtonActionListener(final RoomListModel model, final JList list) {
			this.model = model;
			this.list = list;
		}
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			if (!isGameCreated) {
				final String user = App.getInstance().getUsername();
				if (!model.contains(user)) {
					final TimerOptionsPanel timerPanel = new TimerOptionsPanel();
					if (JOptionPane.showConfirmDialog(GameLobbyView.this, timerPanel, Constants.TIMER_OPTIONS, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						model.add(timerPanel.getSelectedTime() + " " + user);
						list.clearSelection();
						isGameCreated = true;
					}
				}
			}
		}
	}
	
	private JTextArea chatArea;
	
	private boolean isGameCreated;

	public GameLobbyView() {
		setLayout(new BorderLayout());
		initRoomPanel();
		initPlayersPanel();
		initChatPanel();
		initHelpPanel();
		
		isGameCreated = false;
	}
	
	private void initRoomPanel() {
		final JPanel roomPanel = new JPanel();
		final JPanel defaultRoom = initRoom(Constants.DEFAULT_ROOM);
		final JPanel showEngagedRoom = initRoom(Constants.SHOW_ENGAGED);
		final JPanel showCapturedRoom = initRoom(Constants.SHOW_CAPTURED);
		final JPanel theBattlefieldRoom = initRoom(Constants.BATTLEFIELD);
		
		ComponentUtils.setSize(roomPanel, 0, Constants.WINDOW_HEIGHT / 4);
		roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.X_AXIS));
		roomPanel.add(defaultRoom);
		roomPanel.add(showEngagedRoom);
		roomPanel.add(showCapturedRoom);
		roomPanel.add(theBattlefieldRoom);
		
		add(roomPanel, BorderLayout.NORTH);
	}
	
	private JPanel initRoom(final String roomName) {
		final JPanel room = new JPanel();
		final JList list = new JList();
		final Container buttonContainer = new Container();
		final JButton createButton = new JButton(Constants.CREATE_BUTTON);
		final JButton joinButton = new JButton(Constants.JOIN_BUTTON);
		final JButton cancelButton = new JButton(Constants.CANCEL_BUTTON);
		final JScrollPane scrollPane = new JScrollPane();
		final RoomListModel roomListModel = new RoomListModel();
		
		ComponentUtils.setCustomButton(createButton);
		ComponentUtils.setCustomButton(joinButton);
		ComponentUtils.setCustomButton(cancelButton);

		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
		buttonContainer.add(createButton);
		buttonContainer.add(joinButton);
		buttonContainer.add(cancelButton);
		
		list.setModel(roomListModel);
		
		createButton.addActionListener(new CreateButtonActionListener(roomListModel, list));
		joinButton.addActionListener(new JoinButtonActionListener(roomListModel, list));
		cancelButton.addActionListener(new CancelButtonActionListener(roomListModel, list));
		
		ComponentUtils.setSize(scrollPane, 120, 90);
		scrollPane.setViewportView(list);

		ComponentUtils.setPanelBorder(room, roomName);
		room.add(scrollPane);
		room.add(buttonContainer);
		
		return room;
	}
	
	private void initPlayersPanel() {
		final JPanel playersPanel = new JPanel();

		ComponentUtils.setSize(playersPanel, Constants.WINDOW_WIDTH / 5, 0);
		ComponentUtils.setPanelBorder(playersPanel, Constants.PLAYERS);
		
		add(playersPanel, BorderLayout.WEST);
	}
	
	private void initChatPanel() {
		final Container chatContainer = new Container();
		final JButton sendButton = new JButton(Constants.SEND_BUTTON);
		final JPanel chatPanel = new JPanel();
		final JTextField chatField = new JTextField(42);
		chatArea = new JTextArea(16, 48);
		
		chatArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		chatArea.setEditable(false);
		chatArea.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
		chatField.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
		
		chatContainer.setLayout(new BorderLayout());
		chatContainer.add(chatField, BorderLayout.WEST);
		chatContainer.add(sendButton, BorderLayout.EAST);
		
		ComponentUtils.setPanelBorder(chatPanel, Constants.CHAT);
		chatPanel.add(chatArea);
		chatPanel.add(chatContainer);
		
		add(chatPanel, BorderLayout.CENTER);
	}
	
	private void initHelpPanel() {
		final JPanel helpPanel = new JPanel();
		
		ComponentUtils.setSize(helpPanel, Constants.WINDOW_WIDTH / 4, 0);
		ComponentUtils.setPanelBorder(helpPanel, Constants.INSTRUCTIONS);
		
		add(helpPanel, BorderLayout.EAST);
	}
	
	public void test(final String message) {
		chatArea.append(message);
	}
	
}

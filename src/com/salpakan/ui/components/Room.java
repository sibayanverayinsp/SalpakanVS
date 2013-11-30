package com.salpakan.ui.components;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.network.Message;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class Room extends JPanel {

	private final class CreateButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent evt) {
			final App app = App.getInstance();
			if (!app.isConnected() || app.isGameCreated() || app.joinedAGame()) {
				return;
			}
			
			final String user = app.getUsername();
			final DataListModel model = (DataListModel) list.getModel();
			if (model.contains(user)) {
				return;
			}
			
			final TimerOptionsPanel timerPanel = new TimerOptionsPanel();
			if (JOptionPane.showConfirmDialog(null, timerPanel, Constants.TIMER_OPTIONS, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				final String data = timerPanel.getSelectedTime() + " " + user;
//				game = new GameServer(0);
//				new Thread(new Runnable() {
//					
//					@Override
//					public synchronized void run() {
//						game.start();
//						game = null;
//						model.remove(data);
//						createButton.setEnabled(true);
//					}
//				}).start();
//				final Player player = new Player(Constants.DEFAULT_HOST, game.getPort());
//				if (!player.start()) {
//					return;
//				}
//				player.sendMessage(new Message(Message.JOIN, App.getInstance().getUsername(), "joined!"));
				app.setIsGameCreated(true);
				createButton.setEnabled(false);
				joinButton.setEnabled(true);
				cancelButton.setEnabled(true);
				list.clearSelection();
				app.getClient().sendMessage(new Message(Message.GAME_CREATED, user, roomName + "&" + data));
			}
		}
	}

	private final class JoinButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final App app = App.getInstance();
			final int index = list.getSelectedIndex();
			if (!app.isConnected() || app.isGameCreated() || index < 0) {
				return;
			}
			
			final DataListModel model = (DataListModel) list.getModel();
			final String username = model.getElementAt(index).toString();
			if (!username.substring(username.indexOf(" ") + 1).equals(app.getUsername())) {
				app.setJoinedAGame(true);
				createButton.setEnabled(true);
				joinButton.setEnabled(false);
				cancelButton.setEnabled(false);
				list.clearSelection();
				app.getClient().sendMessage(new Message(Message.JOIN_GAME, app.getUsername(), roomName + "&" + username));
			}
		}
	}
	
	private final class CancelButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final App app = App.getInstance();
			if (!app.isConnected() || !app.isGameCreated()) {
				return;
			}

			final DataListModel model = (DataListModel) list.getModel();
			final String user = app.getUsername();
			String username;
			
			for (int i = 0, j = model.getSize(); i < j; i++) {
				username = model.getElementAt(i).toString();
				if (username.substring(username.indexOf(" ") + 1).equals(user)) {
					app.setIsGameCreated(false);
					createButton.setEnabled(true);
					joinButton.setEnabled(true);
					cancelButton.setEnabled(false);
					app.getClient().sendMessage(new Message(Message.GAME_CANCELLED, user, roomName + "&" + username));
					break;
				}
			}
		}
	}
	
	private JButton createButton;
	private JButton joinButton;
	private JButton cancelButton;
	private JList list;
	private JList listVersus;
	
	private String roomName;
	
//	private GameServer game;
	
	public Room(final String roomName) {
		this.roomName = roomName;
		setLayout(new GridLayout(2, 1));
		ComponentUtils.setPaddedPanelBorder(this, roomName);
		initRoom();
		initRoomVersus();
	}
	
	private void initRoom() {
		final Container roomContainer = new Container();
		final JScrollPane scrollPane = new JScrollPane();
		
		list = new JList(new DataListModel());
		
		scrollPane.setViewportView(list);
		ComponentUtils.setBorderMargin(scrollPane, new EmptyBorder(0, 0, 0, 5));
		
		roomContainer.setLayout(new BoxLayout(roomContainer, BoxLayout.X_AXIS));
		roomContainer.add(scrollPane);
		roomContainer.add(initButtons());

		add(roomContainer);
	}
	
	private Container initButtons() {
		final Container buttonContainer = new Container();
		
		createButton = new JButton(Constants.CREATE_BUTTON);
		joinButton = new JButton(Constants.JOIN_BUTTON);
		cancelButton = new JButton(Constants.CANCEL_BUTTON);
		
		cancelButton.setEnabled(false);
		
		createButton.addActionListener(new CreateButtonActionListener());
		joinButton.addActionListener(new JoinButtonActionListener());
		cancelButton.addActionListener(new CancelButtonActionListener());
		
		ComponentUtils.setCustomButton(createButton);
		ComponentUtils.setCustomButton(joinButton);
		ComponentUtils.setCustomButton(cancelButton);

		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
		buttonContainer.add(createButton);
		buttonContainer.add(joinButton);
		buttonContainer.add(cancelButton);
		
		return buttonContainer;
	}
	
	private void initRoomVersus() {
		final JScrollPane scrollPane = new JScrollPane();
		listVersus = new JList(new DataListModel());
		
		scrollPane.setViewportView(listVersus);
		ComponentUtils.setBorderMargin(scrollPane, new EmptyBorder(5, 0, 0, 0));
		
		add(scrollPane);
	}
	
	public JList getList() {
		return list;
	}
	
	public JList getListVersus() {
		return listVersus;
	}
	
}

package com.salpakan.ui.components;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
			if (!app.isConnected() || app.isGameCreated()) {
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
				cancelButton.setEnabled(true);
				list.clearSelection();
				app.getClient().sendMessage(new Message(Message.GAME_CREATED, user, roomName + "&" + data));
			}
		}
	}
	
	private final class CancelButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final App app = App.getInstance();
			if (!app.isConnected()) {
				return;
			}

			final DataListModel model = (DataListModel) list.getModel();
			final String user = app.getUsername();
			String username;
			
			for (int i = 0, j = model.getSize(); i < j; i++) {
				username = model.getElementAt(i).toString();
				if (username.substring(username.indexOf(" ") + 1).equals(user)) {
					model.remove(i);
					app.setIsGameCreated(false);
					cancelButton.setEnabled(false);
					createButton.setEnabled(true);
					app.getClient().sendMessage(new Message(Message.GAME_CANCELLED, user, roomName + "&" + username));
					break;
				}
			}
		}
	}

	private final class JoinButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final App app = App.getInstance();
			if (!app.isConnected()) {
				return;
			}
			
			final int index = list.getSelectedIndex();
			final DataListModel model = (DataListModel) list.getModel();
			final String username = model.getElementAt(index).toString();
			if (!username.substring(username.indexOf(" ") + 1).equals(app.getUsername())) {
				model.remove(index);
				list.clearSelection();
				app.setIsGameCreated(false);
			}
		}
	}
	
	private JButton createButton;
	private JButton joinButton;
	private JButton cancelButton;
	private JList list;
	
	private String roomName;
	
//	private GameServer game;
	
	public Room(final String roomName) {
		this.roomName = roomName;
		
		final Container buttonContainer = new Container();
		final JScrollPane scrollPane = new JScrollPane();
		final DataListModel roomListModel = new DataListModel();
		list = new JList();
		createButton = new JButton(Constants.CREATE_BUTTON);
		joinButton = new JButton(Constants.JOIN_BUTTON);
		cancelButton = new JButton(Constants.CANCEL_BUTTON);
		
		cancelButton.setEnabled(false);
		
		createButton.addActionListener(new CreateButtonActionListener());
		cancelButton.addActionListener(new CancelButtonActionListener());
		joinButton.addActionListener(new JoinButtonActionListener());
		
		ComponentUtils.setCustomButton(createButton);
		ComponentUtils.setCustomButton(joinButton);
		ComponentUtils.setCustomButton(cancelButton);

		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
		buttonContainer.add(createButton);
		buttonContainer.add(joinButton);
		buttonContainer.add(cancelButton);
		
		list.setModel(roomListModel);
		
		ComponentUtils.setSize(scrollPane, 140, 90);
		scrollPane.setViewportView(list);

		ComponentUtils.setPanelBorder(this, roomName);
		add(scrollPane);
		add(buttonContainer);
	}
	
	public JList getList() {
		return list;
	}
	
}

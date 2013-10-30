package com.salpakan.ui.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.salpakan.app.App;
import com.salpakan.constants.Constants;
import com.salpakan.ui.components.RoomListModel;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class GameLobbyView extends JPanel {
	
	private final class CancelButtonActionListener implements ActionListener {
		
		private RoomListModel model;
		private JList list;
		
		private CancelButtonActionListener(final RoomListModel model, final JList list) {
			this.model = model;
			this.list = list;
		}
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final int index = list.getSelectedIndex();
			if (index != -1 && index < model.getSize() && model.getElementAt(index).equals(App.getInstance().getUsername())) {
				model.remove(index);
			}
		}
	}

	private final class JoinButtonActionListener implements ActionListener {
		
		private RoomListModel model;
		private JList list;
		
		private JoinButtonActionListener(final RoomListModel model, final JList list) {
			this.model = model;
			this.list = list;
		}
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final int index = list.getSelectedIndex();
			if (index != -1 && index < model.getSize() && !model.getElementAt(index).equals(App.getInstance().getUsername())) {
				model.remove(index);
			}
		}
	}

	private final class CreateButtonActionListener implements ActionListener {

		private RoomListModel model;
		
		private CreateButtonActionListener(final RoomListModel model) {
			this.model = model;
		}
		
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final String user = App.getInstance().getUsername();
			if (!model.contains(user)) {
				model.add(user);
			}
		}
	}
	
	private JPanel chatPanel;
	private JTextArea chatArea;

	public GameLobbyView() {
		this.setLayout(new BorderLayout());
		this.initRoomPanel();
		this.initPlayersPanel();
		this.initChatPanel();
		this.initHelpPanel();
	}
	
	private void initRoomPanel() {
		final JPanel roomPanel = new JPanel();
		final JPanel defaultRoom = initRoom(Constants.DEFAULT_ROOM);
		final JPanel showEngagedRoom = initRoom(Constants.SHOW_ENGAGED);
		final JPanel showCapturedRoom = initRoom(Constants.SHOW_CAPTURED);
		final JPanel theBattlefieldRoom = initRoom(Constants.BATTLEFIELD);
		
		roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.X_AXIS));
		roomPanel.setPreferredSize(new Dimension(0, Constants.WINDOW_HEIGHT / 4));
		roomPanel.add(defaultRoom);
		roomPanel.add(showEngagedRoom);
		roomPanel.add(showCapturedRoom);
		roomPanel.add(theBattlefieldRoom);
		
		this.add(roomPanel, BorderLayout.NORTH);
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
		
		ComponentUtils.customButton(createButton);
		ComponentUtils.customButton(joinButton);
		ComponentUtils.customButton(cancelButton);

		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
		buttonContainer.add(createButton);
		buttonContainer.add(joinButton);
		buttonContainer.add(cancelButton);
		
		list.setModel(roomListModel);
		
		createButton.addActionListener(new CreateButtonActionListener(roomListModel));
		joinButton.addActionListener(new JoinButtonActionListener(roomListModel, list));
		cancelButton.addActionListener(new CancelButtonActionListener(roomListModel, list));
		
		scrollPane.setViewportView(list);
		scrollPane.setPreferredSize(new Dimension(120, 90));

		room.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), roomName.toUpperCase(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
		room.add(scrollPane);
		room.add(buttonContainer);
		
		return room;
	}
	
	public void addData(final Vector<String> data) {
		data.add("amp");
	}
	
	private void initPlayersPanel() {
		final JPanel playersPanel = new JPanel();

		playersPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH / 5, 0));
		playersPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Players".toUpperCase(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
		
		this.add(playersPanel, BorderLayout.WEST);
	}
	
	private void initChatPanel() {
		chatPanel = new JPanel();
		chatArea = new JTextArea();

		chatArea.setColumns(50);
		chatArea.setRows(50);
		chatArea.setPreferredSize(new Dimension(50, 50));
		
		chatPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH / 3, 0));
		chatPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Chat".toUpperCase(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
		chatPanel.add(chatArea);
		
		this.add(chatPanel, BorderLayout.CENTER);
	}
	
	private void initHelpPanel() {
		final JPanel helpPanel = new JPanel();
		
		helpPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH / 4, 0));
		helpPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Instructions".toUpperCase(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
		
		this.add(helpPanel, BorderLayout.EAST);
	}
	
	public void test(final String message) {
		chatArea.append(message);
	}
	
}

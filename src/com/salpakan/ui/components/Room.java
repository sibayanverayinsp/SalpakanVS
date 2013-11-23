package com.salpakan.ui.components;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.salpakan.constants.Constants;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class Room extends JPanel {

	private JButton createButton;
	private JButton joinButton;
	private JButton cancelButton;
	
	private JList list;
	
	private String roomName;
	
	public Room(final String roomName) {
		this.roomName = roomName;
		
		final Container buttonContainer = new Container();
		final JScrollPane scrollPane = new JScrollPane();
		final DataListModel roomListModel = new DataListModel();
		list = new JList();
		createButton = new JButton(Constants.CREATE_BUTTON);
		joinButton = new JButton(Constants.JOIN_BUTTON);
		cancelButton = new JButton(Constants.CANCEL_BUTTON);
		
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
	
	public void addCreateAction(final ActionListener action) {
		createButton.addActionListener(action);
	}
	
	public void addJoinAction(final ActionListener action) {
		joinButton.addActionListener(action);
	}
	
	public void addCancelAction(final ActionListener action) {
		cancelButton.addActionListener(action);
	}
	
	public JButton getCreateButton() {
		return createButton;
	}
	
	public JButton getJoinButton() {
		return joinButton;
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}
	
	public JList getList() {
		return list;
	}
	
	public String getName() {
		return roomName;
	}
	
}

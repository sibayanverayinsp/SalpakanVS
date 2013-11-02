package com.salpakan.ui.components;

import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class RoomListModel implements ListModel {

	private final Vector<String> dataList = new Vector<String>();
	private final Vector<ListDataListener> listDataListener = new Vector<ListDataListener>();
	
	public RoomListModel() {
		
	}
	
	public void add(final String data) {
		dataList.add(data);
		notifyDataChanged(ListDataEvent.INTERVAL_ADDED, dataList.size() - 1);
	}
	
	public void remove(final int index) {
		if (index >= 0 && index < getSize()) {
			dataList.remove(index);
			notifyDataChanged(ListDataEvent.INTERVAL_REMOVED, index);
		}
	}
	
	public boolean contains(final String data) {
		return dataList.contains(data);
	}
	
	private void notifyDataChanged(final int type, final int index) {
		final ListDataEvent listDataEvent = new ListDataEvent(this, type, index, index);
		for (int i = 0, j = listDataListener.size(); i < j; i++) {
			((ListDataListener) listDataListener.get(i)).contentsChanged(listDataEvent);
		}
	}
	
	@Override
	public int getSize() {
		return dataList.size();
	}

	@Override
	public Object getElementAt(final int index) {
		return dataList.get(index);
	}

	@Override
	public void addListDataListener(final ListDataListener l) {
		listDataListener.add(l);
	}

	@Override
	public void removeListDataListener(final ListDataListener l) {
		listDataListener.remove(l);
	}

}

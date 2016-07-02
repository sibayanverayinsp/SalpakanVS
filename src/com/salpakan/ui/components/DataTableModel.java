package com.salpakan.ui.components;

import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class DataTableModel implements TableModel {
	
	private final Vector<String> users = new Vector<String>();
	private final Vector<Integer> duration = new Vector<Integer>();
	private final Vector<TableModelListener> tableModelListener = new Vector<TableModelListener>();
	private final String[] columnNames = new String[2];
	
	public DataTableModel() {
		columnNames[0] = "Player";
		columnNames[1] = "Time";
	}

	@Override
	public int getRowCount() {
		return users.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}
	
}

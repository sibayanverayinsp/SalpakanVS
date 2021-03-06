package com.salpakan.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.salpakan.constants.Constants;
import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class Board extends JPanel {
	
	private int row, column;
	
	private JPanel[][] grid;
	
	public Board(int row, int column) {
		this.row = row;
		this.column = column;
		setLayout(new GridLayout(row, column));
		initBoard();
		setBorder(Constants.MARGIN);
	}
	
	private void initBoard() {
		grid = new JPanel[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				grid[i][j] = new JPanel();
				
				ComponentUtils.setSize(grid[i][j], Constants.GRID_SIZE);
				grid[i][j].setLayout(new BorderLayout());
				grid[i][j].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				add(grid[i][j]);
			}
		}
	}
	
	public JPanel getGrid(final int i, final int j) {
		return grid[i][j];
	}
	
}

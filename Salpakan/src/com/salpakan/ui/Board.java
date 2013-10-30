package com.salpakan.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.salpakan.constants.Constants;

@SuppressWarnings("serial")
public class Board extends JPanel {
	
	private int row, column;
	
	private JPanel[][] grid;
	
	public Board(int row, int column) {
		this.row = row;
		this.column = column;
		this.setLayout(new GridLayout(this.row, this.column));
		this.initBoard();
		this.setBorder(Constants.MARGIN);
	}
	
	private void initBoard() {
		grid = new JPanel[this.row][this.column];
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.column; j++) {
				grid[i][j] = new JPanel();
				grid[i][j].setLayout(new BorderLayout());
				grid[i][j].setPreferredSize(Constants.GRID_SIZE);
				grid[i][j].setMaximumSize(Constants.GRID_SIZE);
				grid[i][j].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				this.add(grid[i][j]);
			}
		}
	}
	
	public JPanel getGrid(final int i, final int j) {
		return grid[i][j];
	}
	
}

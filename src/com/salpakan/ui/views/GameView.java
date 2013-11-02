package com.salpakan.ui.views;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.salpakan.constants.Constants;
import com.salpakan.ui.Board;

@SuppressWarnings("serial")
public class GameView extends JPanel {
	
	private JPanel gamePanel;
	private Board board;
	private Board ownBase;
	private Board oppBase;
	
	public GameView() {
		setLayout(new BorderLayout());
		initGameView();
	}
	
	private void initGameView() {
		final Box gameBox = new Box(BoxLayout.Y_AXIS);
		
		initGamePanel();
		
		gameBox.add(Box.createVerticalGlue());
		gameBox.add(gamePanel);
		gameBox.add(Box.createVerticalGlue());
		
		add(gameBox, BorderLayout.CENTER);
	}
	
	private void initGamePanel() {
		gamePanel = new JPanel();
		ownBase = new Board(7, 3);
		board = new Board(Constants.ROW, Constants.COLUMN);
		oppBase = new Board(7, 3);
		
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				//ownBase.getGrid(i, j).add(new Piece(new ImageIcon("res/black" + ((i + 1) * (j + 1)) + ".png")), BorderLayout.CENTER);
			}
		}
		
		//add game components
		gamePanel.add(ownBase);
		gamePanel.add(board);
		gamePanel.add(oppBase);
	}
	
}

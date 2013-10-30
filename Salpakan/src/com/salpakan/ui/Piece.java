package com.salpakan.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Piece extends JPanel {

	private ImageIcon icon;
	
	private JPanel piece;
	
	public Piece(final ImageIcon icon) {
		this.piece = this;
		this.icon = icon;
		final Dimension iconSize = this.getIconSize();
		this.setPreferredSize(iconSize);
		this.setMaximumSize(iconSize);
	}
	
	private Dimension getIconSize() {
		return new Dimension(this.icon.getIconWidth(), this.icon.getIconWidth());
	}
	
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.icon.getImage(), 0, 0, getWidth(), getHeight(), this);
	}

}

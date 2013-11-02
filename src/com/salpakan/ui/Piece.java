package com.salpakan.ui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.salpakan.utils.ComponentUtils;

@SuppressWarnings("serial")
public class Piece extends JPanel {

	private ImageIcon icon;
	
	public Piece(final ImageIcon icon) {
		this.icon = icon;
		ComponentUtils.setSize(this, getIconSize());
	}
	
	private Dimension getIconSize() {
		return new Dimension(icon.getIconWidth(), icon.getIconWidth());
	}
	
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
	}

}

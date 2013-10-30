package com.salpakan.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextFieldWithPlaceholder extends JTextField {

	private String placeholder;
	
	public void setPlaceholder(final String s) {
        placeholder = s;
    }
	
	@Override
	protected void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		
		final Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(getDisabledTextColor());
		graphics2d.drawString(this.placeholder, getInsets().left, graphics.getFontMetrics().getMaxAscent() + getInsets().top);
	}
}

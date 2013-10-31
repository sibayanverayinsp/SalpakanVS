package com.salpakan.utils;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.salpakan.constants.Constants;

public class ComponentUtils {
	
	public static void setCustomTextfield(final JTextField textfield) {
		setSize(textfield, Constants.TEXTFIELD_DIMENSION);
		textfield.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
	}
	
	public static void setCustomButton(final JButton button) {
		setSize(button, Constants.BUTTON_DIMENSION);
	}
	
	public static void setSize(final JComponent component, final int width, final int height) {
		final Dimension dimension = new Dimension(width, height);
		setSize(component, dimension);
	}
	
	public static void setSize(final JComponent component, final Dimension dimension) {
		component.setSize(dimension);
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
		component.setMinimumSize(dimension);
	}
	
}

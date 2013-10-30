package com.salpakan.utils;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.salpakan.constants.Constants;

public class ComponentUtils {
	
	public static void customTextfield(final JTextField textfield) {
		textfield.setPreferredSize(Constants.TEXTFIELD_DIMENSION);
		textfield.setMaximumSize(Constants.TEXTFIELD_DIMENSION);
		textfield.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
	}
	
	public static void customButton(final JButton button) {
		button.setPreferredSize(Constants.BUTTON_DIMENSION);
		button.setMaximumSize(Constants.BUTTON_DIMENSION);
	}
	
}

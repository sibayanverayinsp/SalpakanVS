package com.salpakan.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.salpakan.constants.Constants;

public class ComponentUtils {
	
	public static void setBorderMargin(final JComponent component, final EmptyBorder margin) {
		component.setBorder(BorderFactory.createCompoundBorder(margin, new LineBorder(Color.BLACK)));
	}
	
	public static void setCustomButton(final JButton button) {
		setSize(button, Constants.BUTTON_DIMENSION);
	}
	
	public static void setCustomTextArea(final JTextArea textarea) {
		setPaddedBorder(textarea, Constants.PADDING);
		textarea.setEditable(false);
		textarea.setFocusable(false);
		textarea.setFont(Constants.FONT);
		textarea.setLineWrap(true);
	}
	
	public static void setCustomTextField(final JTextField textfield) {
		setSize(textfield, Constants.TEXTFIELD_DIMENSION);
		textfield.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
	}
	
	public static void setPaddedBorder(final JComponent component, final EmptyBorder padding) {
		component.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GRAY), padding));
	}
	
	public static void setPaddedPanelBorder(final JComponent component, final String title) {
		component.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), title.toUpperCase()) , Constants.PADDING));
	}
	
	public static void setPanelBorder(final JComponent component, final String title) {
		setPanelBorder(component, title, null);
	}
	
	public static void setPanelBorder(final JComponent component, final String title, final Font font) {
		component.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), title.toUpperCase(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font));
	}
	
	public static void setSize(final Component component, final int width, final int height) {
		final Dimension dimension = new Dimension(width, height);
		setSize(component, dimension);
	}
	
	public static void setSize(final Component component, final Dimension dimension) {
		component.setSize(dimension);
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
		component.setMinimumSize(dimension);
	}
	
}

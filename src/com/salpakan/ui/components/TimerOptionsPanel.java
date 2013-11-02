package com.salpakan.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.salpakan.constants.Constants;

@SuppressWarnings("serial")
public class TimerOptionsPanel extends JPanel implements ActionListener {
	
	private HashMap<String, Integer> timeMap;
	private int selectedTime;
	
	public TimerOptionsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.initTimeMap();
		this.initTimerPanel();
	}
	
	@Override
	public void actionPerformed(final ActionEvent evt) {
		selectedTime = timeMap.get(evt.getActionCommand());
	}
	
	private void initTimeMap() {
		timeMap = new HashMap<String, Integer>();
		
		timeMap.put(Constants.NO_TIMER, 0);
		timeMap.put(Constants.TWO_MIN, 2);
		timeMap.put(Constants.FIVE_MIN, 5);
		timeMap.put(Constants.EIGHT_MIN, 8);
		timeMap.put(Constants.TEN_MIN, 10);
		timeMap.put(Constants.TWENTY_MIN, 20);
	}
	
	private void initTimerPanel() {
		final ButtonGroup radioGroup = new ButtonGroup();
		final JRadioButton zero = createRadioButton(Constants.NO_TIMER);
		final JRadioButton two = createRadioButton(Constants.TWO_MIN);
		final JRadioButton five = createRadioButton(Constants.FIVE_MIN);
		final JRadioButton eight = createRadioButton(Constants.EIGHT_MIN);
		final JRadioButton ten = createRadioButton(Constants.TEN_MIN);
		final JRadioButton twenty = createRadioButton(Constants.TWENTY_MIN);
		
		zero.setSelected(true);
		
		addRadioButton(radioGroup, zero);
		addRadioButton(radioGroup, two);
		addRadioButton(radioGroup, five);
		addRadioButton(radioGroup, eight);
		addRadioButton(radioGroup, ten);
		addRadioButton(radioGroup, twenty);
	}
	
	private JRadioButton createRadioButton(final String name) {
		final JRadioButton radioButton = new JRadioButton(name);
		
		radioButton.setActionCommand(name);
		radioButton.addActionListener(this);
		return radioButton;
	}

	private void addRadioButton(final ButtonGroup buttonGroup, final JRadioButton radioButton) {
		buttonGroup.add(radioButton);
		this.add(radioButton);
	}
	
	public int getSelectedTime() {
		return selectedTime;
	}
	
}

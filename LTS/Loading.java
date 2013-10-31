/**
 * @name Last Tank Standing
 * @version 1.3
 * @class Loading
 * @author
 * 		Jasper A. Sibayan
 * 		2009-46112
 * @author
 *		Wilbert G. Verayin
 *		2009-60315
 * @date October 17, 2012 1st Semester, AY 2012-2013
 * 		CMSC 137 B-5L
 *		
 *		University of the Philippines Los Banos
 * 		Institute of Computer Science
 */

import javax.swing.*;
import java.awt.*;
import java.net.*;

public class Loading extends JWindow {
	JLabel loadDot;
	public Loading() {
		URL myurl = this.getClass().getResource("images/splash.png");
		Toolkit tk = this.getToolkit();
		ImageIcon imh = new ImageIcon(tk.getImage(myurl));
		setSize(imh.getIconWidth(), imh.getIconHeight());
		JPanel panelBgImg = new JPanel() {
			public void paintComponent(Graphics g) {
				URL myurl = this.getClass().getResource("images/splash.png");
				Toolkit tk = this.getToolkit();
				g.drawImage(tk.getImage(myurl),0,0,null);
			}
		};
        loadDot = new JLabel();
		loadDot.setBounds(20,imh.getIconHeight()-45,200,40);
		add(panelBgImg);
		panelBgImg.setLayout(null);
		panelBgImg.add(loadDot);
		setLocationRelativeTo(null);
		setVisible(true);
		Font f = new Font("Courier New",Font.BOLD,18);
		loadDot.setFont(f);
		loadDot.setForeground(Color.white);
		for(int i = 0; i < 2; i++) {
			try {
				Thread.sleep(500);
			}
			catch(Exception ex) {
				Thread.currentThread().interrupt();
			}
			loadDot.setText("LOADING");
			try {
				Thread.sleep(500);
			}
			catch(Exception ex) {
				Thread.currentThread().interrupt();
			}
			loadDot.setText("LOADING.");
			try {
				Thread.sleep(500);
			}
			catch(Exception ex) {
				Thread.currentThread().interrupt();
			}
			loadDot.setText("LOADING..");
			try {
				Thread.sleep(500);
			}
			catch(Exception ex) {
				Thread.currentThread().interrupt();
			}
			loadDot.setText("LOADING...");
		}
	}
}
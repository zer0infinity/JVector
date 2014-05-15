/**
 * This file is part of JVector.
 * 
 * JVector is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVector is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JVector.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.hsr.i.jvector.ui.dialogs;


import ibxm.Player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import ch.hsr.i.jvector.ui.main.JVector;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private static String url = "www.i.hsr.ch";
	private static String urlname = "HSR Hochschule fuer Technik Rapperswil";
	private static String email1 = "tobias.binna@hsr.ch";
	private static String name1 = "Tobias Binna";
	private static String email2 = "david.tran@hsr.ch";
	private static String name2 = "David Tran";
	
	private Player player;
	
	public AboutDialog() {
		try {
			player = new Player();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		playTune();
		setJDialog();
		buildGUI();
	}
	
	private void buildGUI() {
		addTextFields();
		
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				ImageIcon img = new ImageIcon(getClass().getResource(JVector.UIIMAGELOCATION + "about.jpg"));
				g.drawImage(img.getImage(), 0, -30, null);
			}
		};
		panel.setOpaque(false);
		panel.setLayout(null);
		
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		};
		// Button: Close
		JButton button_close = new JButton();
		button_close.setText("Close");
		button_close.setBounds(280, 190, 100, 20);
		button_close.addActionListener(cancelListener);
		
		// Escape = Close Window
		getRootPane().registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		add(button_close);
		add(panel);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				stopTune();
			}
		});
	}
	
	/**
	 * textfield of url, name, email of author
	 */
	private void addTextFields() {
		final String[] text = { url, email1, email2 };
		final String[] tooltip = { urlname, name1, name2 };
		final Rectangle[] bounds = { new Rectangle(10, 190, 265, 20), new Rectangle(185, 144, 195, 20), new Rectangle(185, 165, 195, 20) };
		for (int i = 0; i < bounds.length; i++) {
			add(createTextField(bounds[i], text[i], tooltip[i]));
		}
	}
	
	/**
	 * set jdialog
	 */
	private void setJDialog() {
		setTitle(JVector.PROGRAM + " - About");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setSize(400, 250);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - 400) / 2, (dim.height - 350) / 2);
	}
	
	/**
	 * play randomly a chiptunes
	 */
	private void playTune() {
		player.play();
	}
	
	/**
	 * stop playing tune
	 */
	private void stopTune() {
		if (player != null)
			player.stop();
	}
	
	/**
	 * close dialog
	 */
	private void closeDialog() {
		stopTune();
		setVisible(false);
		dispose();
	}
	
	/**
	 * create textfield
	 * 
	 * @param rect setBounds
	 * @param text Label
	 * @param tooltip Tooltip
	 * @return TextField
	 */
	private JTextField createTextField(Rectangle rect, String text, String tooltip) {
		JTextField textField = new JTextField(text);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setBounds(rect);
		textField.setEditable(false);
		textField.setOpaque(false);
		textField.setToolTipText(tooltip);
		
		// PopupMenu on Rightclick
		JPopupMenu popupMenu = popupMenu_textField(textField);
		textField.setComponentPopupMenu(popupMenu);
		return textField;
	}

	/**
	 * PopupMenu TextField.
	 * 
	 * @param textField
	 * @return JPopupMenu
	 */
	private JPopupMenu popupMenu_textField(final JTextField textField) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.copy();
			}
		});
		popupMenu.add(copyMenuItem);
		return popupMenu;
	}
}
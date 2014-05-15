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

package ch.hsr.i.jvector.ui.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import ch.hsr.i.jvector.ui.main.JVector;

public class MessageBox {
	
	private static final long serialVersionUID = 1L;
	
	public static void InfoBox(String title, String message) {
		final JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setAlwaysOnTop(true);
		dialog.setModal(true);
		dialog.setLayout(null);
		dialog.setSize(300, 150);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((dim.width - dialog.getWidth()) / 2, (dim.height - dialog.getHeight()) / 2);
		
		JTextArea textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setText(message);
		textarea.setBounds(75, 20, 200, 50);
		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);
		textarea.setOpaque(false);
		textarea.setFont(new Font("Arial", Font.BOLD, 12));
		
		JLabel icon = new JLabel();
		icon.setIcon(new ImageIcon(MessageBox.class.getResource(JVector.UIIMAGELOCATION + "info.png")));
		icon.setBounds(10, 15, 50, 50);

		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		};
		JButton button = new JButton();
		button.setText("OK");
		button.setBounds(180, 90, 100, 20);
		button.setBorder(LineBorder.createBlackLineBorder());
		button.addActionListener(cancelListener);
		
		JRootPane rootPane = dialog.getRootPane();
		rootPane.registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		rootPane.registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		dialog.add(button);
		dialog.add(textarea);
		dialog.add(icon);
		
		dialog.setVisible(true);
	}
}

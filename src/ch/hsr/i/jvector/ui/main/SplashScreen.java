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

package ch.hsr.i.jvector.ui.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow implements Runnable {
	
	private static final long serialVersionUID = 1L;

	public SplashScreen() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		JLabel label = new JLabel(new ImageIcon(getClass().getResource(JVector.UIIMAGELOCATION + "splash.jpg")));
		add(label);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - label.getWidth()) / 2, (screenSize.height - label.getHeight()) / 2);
		setAlwaysOnTop(true);
		setVisible(true);
		try {
			Thread.sleep(1600);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		setVisible(false);
		dispose();
	}
}

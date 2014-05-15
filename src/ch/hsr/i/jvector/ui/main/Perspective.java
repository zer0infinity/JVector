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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.media.j3d.Canvas3D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.border.DropShadowBorder;

import ch.hsr.i.jvector.ui.universe.Universe;

public class Perspective extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final String PERSPECTIVE = JVector.PERSPECTIVE, TOP = JVector.TOP, LEFT = JVector.LEFT, RIGHT = JVector.RIGHT, RESIZEVIEW = "+/- View";
	private static final String button_labels[] = { PERSPECTIVE, TOP, LEFT, RIGHT, RESIZEVIEW };
	
	/*
	 * put all button created in a hashmap for easier handling and shortend code
	 */
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>(5);
	private Rectangle bounds, old_bounds;
	private String borderLabel;
	private Canvas3D canvas;
	private boolean isMouseEntered = false, enlargePanel = false;
	
	private Universe universe;
	
	public Perspective(String borderLabel, Universe universe) {
		this.universe = universe;
		this.borderLabel = borderLabel;
		canvas = universe.getCanvas3D(borderLabel); 
		
		canvas.setLocation(10, 20);
		canvas.getScreen3D().setPhysicalScreenHeight(0.1);
		canvas.getScreen3D().setPhysicalScreenWidth(0.1);
		canvas.addMouseListener(getMouseListener());
		add(canvas);
		
		setLayout(null);
		
		buildGUI();
		
		if(borderLabel == PERSPECTIVE) {
			enlargePanel = true;
		}
	}
	
	/**
	 * create gui components
	 */
	private void buildGUI() {
		// buttons for changing view
		for (int i = 0; i < button_labels.length; i++) {
			JButton button = createButton(button_labels[i]);
			buttons.put(button_labels[i], button);
		}
	}
	
	/**
	 * update components bounds after frame is resized
	 * 
	 * @param bounds setBounds
	 */
	public void updateBounds(Rectangle bounds) {
		this.bounds = bounds;
		
		if (enlargePanel) {
			old_bounds = bounds;
			this.bounds = new Rectangle(5, 5, 2*bounds.width, 2*bounds.height);
			setBorder(new DropShadowBorder(new Color(0, 0, 0), 7, 0.5f, 12, true, true, true, true));
		} else {
			setBorder(BorderFactory.createTitledBorder(LineBorder.createGrayLineBorder(), borderLabel));
		}
		
		setBounds(this.bounds);
		canvas.setSize(this.bounds.width - 20, this.bounds.height - 30);
		
		updateButtonLocation();
	}
	
	/**
	 * update button location after frame is resized
	 */
	private void updateButtonLocation() {
		for (int i = 0; i < buttons.size(); i++) {
			JButton button = buttons.get(button_labels[i]);
			if (button_labels[i] == PERSPECTIVE) {
				button.setBounds(bounds.width - 280, 0, 50, 17);
			} else if (button_labels[i] == TOP) {
				button.setBounds(bounds.width - 230, 0, 50, 17);
			} else if (button_labels[i] == LEFT) {
				button.setBounds(bounds.width - 180, 0, 50, 17);
			} else if (button_labels[i] == RIGHT) {
				button.setBounds(bounds.width - 130, 0, 50, 17);
			} else {
				button.setBounds(bounds.width - 60, 0, 50, 17);
			}
		}
	}
	
	/**
	 * create button for changing views
	 * 
	 * @param label button label
	 * @return JButton
	 */
	private JButton createButton(String label) {
		JButton button = new JButton();
		button.setText(label);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() != RESIZEVIEW) {
					universe.setView(borderLabel, e.getActionCommand());
				} else {
					enlargePanel = !enlargePanel;
					resizeView();
				}
			}
		});
		button.addMouseListener(getMouseListener());
		add(button);
		return button;
	}
	
	/**
	 * force jpanel to front
	 */
	private void toFront() {
		getParent().add(this, 0);
	}
	
	/**
	 * scale up/down view
	 */
	private void resizeView() {
		if(enlargePanel) {
			toFront();
			updateBounds(bounds);
		} else {
			updateBounds(old_bounds);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isMouseEntered && !enlargePanel) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, bounds.width, bounds.height);
		}
	}
	
	/**
	 * if mousepointer is over the jpanel,
	 * background color changes (see paintComponent).
	 * 
	 * if double clicked on jpanel,
	 * viewsize maximize.
	 * 
	 * @return MouseAdapter
	 */
	private MouseAdapter getMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				isMouseEntered = true;
				repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				isMouseEntered = false;
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					if (!enlargePanel) {
						enlargePanel = true;
						resizeView();
					} else {
						enlargePanel = false;
						resizeView();
					}
				}
			}
		};
	}
}

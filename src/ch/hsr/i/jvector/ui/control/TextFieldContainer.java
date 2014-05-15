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

package ch.hsr.i.jvector.ui.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.ui.util.MessageBox;

public class TextFieldContainer {
	
	private HashMap<String, JTextField> textfieldMap;
	
	private Point3d point1 = new Point3d();
	private Point3d point2 = new Point3d();
	private Point3d point3 = new Point3d();
	private Vector3d vector1 = new Vector3d();
	private Vector3d vector2 = new Vector3d();
	private double lambda = 0.0;
	
	public TextFieldContainer(int size) {
		textfieldMap = new HashMap<String, JTextField>(size);
	}
	
	/**
	 * TextField to enter coordinates
	 * 
	 * @param label JTextFieldLabel
	 */
	public JTextField addTextField(String label) {
		final JTextField textfield = new JTextField();
		textfield.setDragEnabled(true);
		textfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int k = e.getKeyChar();
				/*
				 * 48-57: 0-9
				 * 45: minus
				 * 46: dot
				 */
				if (!(44 < k && k < 58)) {
					e.setKeyChar((char) KeyEvent.VK_CLEAR);
				}
			}
		});
		textfield.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textfield.requestFocusInWindow();
				textfield.selectAll();
			}
		});
		textfield.setComponentPopupMenu(createPopupMenu(textfield));
		textfieldMap.put(label, textfield);
		return textfield;
	}
	
	/**
	 * get textfield from textfieldlist
	 * 
	 * @param key String to get from map
	 * @return JTextField
	 */
	public JTextField get(String key) {
		return textfieldMap.get(key);
	}
	
	public Point3d getPoint() {
		point1.x = Double.valueOf(textfieldMap.get("x1").getText().trim());
		point1.y = Double.valueOf(textfieldMap.get("y1").getText().trim());
		point1.z = Double.valueOf(textfieldMap.get("z1").getText().trim());
		return point1;
	}
	
	public Line toLine() {
		getPoint();
		point2.x = Double.valueOf(textfieldMap.get("x2").getText().trim());
		point2.y = Double.valueOf(textfieldMap.get("y2").getText().trim());
		point2.z = Double.valueOf(textfieldMap.get("z2").getText().trim());
		return new Line(point1, point2);
	}
	
	public Plane toPlaneThreePoints() {
		toLine();
		point3.x = Double.valueOf(textfieldMap.get("x3").getText().trim());
		point3.y = Double.valueOf(textfieldMap.get("y3").getText().trim());
		point3.z = Double.valueOf(textfieldMap.get("z3").getText().trim());
		return new Plane(point1, point2, point3);
	}
	
	public Plane toPlaneVectorConst() {
		vector1.x = Double.valueOf(textfieldMap.get("vx1").getText().trim());
		vector1.y = Double.valueOf(textfieldMap.get("vy1").getText().trim());
		vector1.z = Double.valueOf(textfieldMap.get("vz1").getText().trim());
		lambda = Double.valueOf(textfieldMap.get("const").getText().trim());
		return new Plane(vector1, lambda);
	}
	
	public Plane toPlanePointsTwoVectors() {
		getPoint();
		vector1.x = Double.valueOf(textfieldMap.get("vx1").getText().trim());
		vector1.y = Double.valueOf(textfieldMap.get("vy1").getText().trim());
		vector1.z = Double.valueOf(textfieldMap.get("vz1").getText().trim());
		vector2.x = Double.valueOf(textfieldMap.get("vx2").getText().trim());
		vector2.y = Double.valueOf(textfieldMap.get("vy2").getText().trim());
		vector2.z = Double.valueOf(textfieldMap.get("vz2").getText().trim());
		return new Plane(point1, vector1, vector2);
	}
	
	public int getGridOffset() {
		return Integer.valueOf(textfieldMap.get("grid").getText());
	}
	
	/**
	 * Check if textfields are empty
	 * 
	 * @return boolean
	 */
	public boolean isTextFieldEmpty() {
		Iterator<JTextField> it = textfieldMap.values().iterator();
		while(it.hasNext()) {
			if (it.next().getText().isEmpty()) {
				MessageBox.InfoBox("Empty Fields", "Fill in all necessary fields");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Popupmenu.
	 * 
	 * @param textField
	 * @return popupMenu
	 */
	private JPopupMenu createPopupMenu(final JTextField textField) {
		// PopupMenu on rightclick
		JPopupMenu popupMenu = new JPopupMenu();
		
		// PopupMenu Items
		final String cut = "Cut";
		final String copy = "Copy";
		final String paste = "Paste";
		final String clear = "Clear";
		final String[] menuItems = { cut, copy, paste, clear };
		
		ActionListener mouseListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == cut) {
					textField.cut();
				} else if (event.getActionCommand() == copy) {
					textField.copy();
				} else if (event.getActionCommand() == paste) {
					textField.paste();
				} else if (event.getActionCommand() == clear) {
					textField.setText("");
				}
			}
		};
		for (int i = 0; i < menuItems.length; i++) {
			JMenuItem menuItem = new JMenuItem(menuItems[i]);
			if (i == 3) {
				popupMenu.addSeparator();
			}
			menuItem.addActionListener(mouseListener);
			popupMenu.add(menuItem);
		}
		return popupMenu;
	}
	
	/**
	 * Line Class
	 */
	public class Line {
		
		public Point3d point1 = new Point3d();
		public Point3d point2 = new Point3d();
		
		public Line(Point3d point1, Point3d point2) {
			this.point1 = point1;
			this.point2 = point2;
		}
	}
	
	/**
	 * Plane class 
	 */
	public class Plane {
		
		public Point3d point1 = new Point3d();
		public Point3d point2 = new Point3d();
		public Point3d point3 = new Point3d();
		public Vector3d vector1 = new Vector3d();
		public Vector3d vector2 = new Vector3d();
		public double lambda = 0.0;
		
		public Plane(Point3d point1, Point3d point2, Point3d point3) {
			this.point1 = point1;
			this.point2 = point2;
			this.point3 = point3;
		}
		
		public Plane(Point3d point1, Vector3d vector1, Vector3d vector2) {
			this.point1 = point1;
			this.vector1 = vector1;
			this.vector2 = vector2;
		}
		
		public Plane(Vector3d vector1, double lambda) {
			this.vector1 = vector1;
			this.lambda = lambda;
		}
	}
}

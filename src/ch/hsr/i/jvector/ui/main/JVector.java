/**
 * JVector - Tool zur Eingabe und Visualisierung von Komponenten (Ebenen, Linien, Punkte, Vektoren) aus der Vektorgeometrie in einem 3D-Raum.
 * Copyright (C) 2008
 * HSR Hochschule fuer Technik Rapperswil
 * www.hsr.ch
 * HSR Studiengang Informatik
 * www.i.hsr.ch
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import ch.hsr.i.jvector.logic.ComponentsList;
import ch.hsr.i.jvector.logic.DrawComponents;
import ch.hsr.i.jvector.model.ComponentsManagerModel;
import ch.hsr.i.jvector.ui.control.ControlComponents;
import ch.hsr.i.jvector.ui.dialogs.AboutDialog;
import ch.hsr.i.jvector.ui.universe.Universe;

public class JVector extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public static boolean DEBUG = true;
	public static boolean EXAMPLES = true;
	
	public static final String PROGRAM = "JVector";
	public static final String PROGRAMURL = "http://wiki.hsr.ch/Prog1Java/wiki.cgi?Java3DMiniprojekt";
	public static final String UIIMAGELOCATION = "/ch/hsr/i/jvector/ui/images/";
	public static final String PERSPECTIVE = "Perspective", TOP = "Top", LEFT = "Left", RIGHT = "Right";
	
	DrawComponents drawcomp;
	ComponentsManager compmanager;
	ControlComponents controlcomp;
	
	public JVector(int frame_width, int frame_height) {
		setJFrame(frame_width, frame_height);
		buildGUI();
	}
	
	/**
	 * create gui components
	 */
	private void buildGUI() {
		final String[] label = { PERSPECTIVE, TOP, LEFT, RIGHT };
		Universe universe = new Universe(label);
		final ArrayList<Perspective> perspectiveList = createPerspectives(label, universe);
		
		ComponentsList sourceList = new ComponentsList();
		ComponentsList destinationList = new ComponentsList();
		ComponentsManagerModel sourceModel = new ComponentsManagerModel(sourceList);
		ComponentsManagerModel destinationModel = new ComponentsManagerModel(destinationList);
		
		drawcomp = new DrawComponents(universe.getObjectRoot(), sourceList);
		compmanager = new ComponentsManager(sourceModel, destinationModel, sourceList, destinationList, drawcomp);
		controlcomp = new ControlComponents(drawcomp);
		
		add(controlcomp.createControl(getWidth(), getHeight()));
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateBounds(perspectiveList, label);
			}
		});
	}
	
	/**
	 * update bounds after frame is resized
	 * 
	 * @param perspectiveList List of Perspectives
	 * @param label StringArray of Perspective labels
	 */
	private void updateBounds(ArrayList<Perspective> perspectiveList, String[] label) {
		int x = (getWidth() - 175) /2, y = (getHeight() - 50) /2;
		int width = x, height = y - 5;
		Rectangle bounds = new Rectangle();
		for (int i = 0; i < perspectiveList.size(); i++) {
			if (label[i] == PERSPECTIVE) {
				bounds = new Rectangle(5, 5, width, height);
			} else if (label[i] == TOP) {
				bounds = new Rectangle(x + 5, 5, width, height);
			} else if (label[i] == LEFT) {
				bounds = new Rectangle(5, y, width, height);
			} else {
				bounds = new Rectangle(x + 5, y, width, height);
			}
			perspectiveList.get(i).updateBounds(bounds);
		}
		controlcomp.updateBounds(getWidth(), getHeight());
	}
	
	/**
	 * create perspective for universes
	 * 
	 * @param label StringArray of labels
	 */
	private ArrayList<Perspective> createPerspectives(String[] label, Universe universe) {
		ArrayList<Perspective> perspectiveList = new ArrayList<Perspective>(label.length);
		for (int i = 0; i < label.length; i++) {
			Perspective perspective = new Perspective(label[i], universe);
			perspectiveList.add(perspective);
			add(perspective);
		}
		return perspectiveList;
	}
	
	/**
	 * set jframe
	 */
	private void setJFrame(int frame_width, int frame_height) {
		setLayout(null);
		setMinimumSize(new Dimension(frame_width, frame_height));
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setTitle(PROGRAM);
		setJMenuBar(createMenuBar());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((dim.width - frame_width)/2, (dim.height - frame_height)/2, frame_width, frame_height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(getClass().getResource(UIIMAGELOCATION + "icon.jpg")).getImage());
	}
	
	/**
	 * MenuBar.
	 * 
	 * @return the JMenuBar
	 */
	private JMenuBar createMenuBar() {
		// menu bar
		JMenuBar menuBar = new JMenuBar();
		
		// file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		addFileItems(fileMenu);
		
		// components menu
		JMenu compMenu = new JMenu("Components");
		compMenu.setMnemonic(KeyEvent.VK_C);
		compMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		addComponentItems(compMenu);
		
		// help menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.getPopupMenu().setLightWeightPopupEnabled(false);
		addHelpItems(helpMenu);
		
		JMenuItem[] items = { fileMenu, compMenu, helpMenu };
		for (JMenuItem item: items) {
			menuBar.add(item);
		}
		
		return menuBar;
	}
	
	/**
	 * file menu items
	 * 
	 * @param fileMenu JMenu
	 */
	private void addFileItems(JMenu fileMenu) {
		final String neu = "New", exit = "Exit";
		final String[] label = { neu, exit };
		final KeyStroke[] keyStrokes = { KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), null };
		for (int i = 0; i < label.length; i++) {
			if (i == 1) {
				fileMenu.addSeparator();
			}
			JMenuItem item = new JMenuItem();
			item.setText(label[i]);
			item.setAccelerator(keyStrokes[i]);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == neu) {
						drawcomp.removeAll();
					} else if (e.getActionCommand() == exit) {
						System.exit(0);
					}
				}
			});
			fileMenu.add(item);
		}
	}
	
	/**
	 * help menu items
	 * 
	 * @param helpMenu JMenu
	 */
	private void addHelpItems(JMenu helpMenu) {
		// help menu items
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog about = new AboutDialog();
				about.setVisible(true);
			}
		});
		helpMenu.add(aboutItem);
	}
	
	/**
	 * component menu items
	 * 
	 * @param compMenu JMenu
	 */
	private void addComponentItems(JMenu compMenu) {
		final String examples = "Draw Examples", manager = "Components Manager";
		final String[] labels = { examples, manager };
		final KeyStroke[] keyStrokes = { KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK),
										KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK) };
		
		for (int i = 0; i < labels.length; i++) {
			if (i == 1) {
				compMenu.addSeparator();
			}
			JMenuItem item = new JMenuItem();
			item.setText(labels[i]);
			item.setAccelerator(keyStrokes[i]);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == examples) {
						drawcomp.drawExamples();
					} else if (e.getActionCommand() == manager) {
						compmanager.setVisible(true);
					}
				}
			});
			compMenu.add(item);
		}
	}
	
	public static void main(String[] args) {
		UIManager.put("Button.background", Color.LIGHT_GRAY);
		UIManager.put("Button.select", Color.WHITE);
		UIManager.put("Button.focus", Color.LIGHT_GRAY);
		UIManager.put("Button.border", LineBorder.createBlackLineBorder());
		UIManager.put("TaskPane.titleBackgroundGradientStart", Color.LIGHT_GRAY);
        UIManager.put("TaskPane.titleBackgroundGradientEnd", Color.WHITE);
        UIManager.put("TaskPane.borderColor", Color.GRAY);
        UIManager.put("List.focusCellHighlightBorder", Color.BLACK);
        
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JVector jvector = new JVector(1150, 745);
				if(!DEBUG)
					new SplashScreen();
				jvector.setVisible(true);
			}
		});
	}
}

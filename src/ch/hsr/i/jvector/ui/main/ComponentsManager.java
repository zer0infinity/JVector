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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;

import ch.hsr.i.jvector.components.Component3D.ComponentType;
import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.interfaces.Component;
import ch.hsr.i.jvector.interfaces.Line;
import ch.hsr.i.jvector.interfaces.Plane;
import ch.hsr.i.jvector.interfaces.Point;
import ch.hsr.i.jvector.logic.ComponentsList;
import ch.hsr.i.jvector.logic.DrawComponents;
import ch.hsr.i.jvector.model.ComponentsManagerModel;
import ch.hsr.i.jvector.ui.util.MessageBox;

public class ComponentsManager extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static final String DISTANCE = ">> get Distance >>", INTERSECTION = ">> get Intersection >>", ANGLE = " >> get Angle >>";
	
	/*
	 * put all button in a hashmap for easier handling and shorter code
	 */
	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();
	
	private JXList sourceJList, destinationJList;
	private boolean isDistance = false, isIntersection = false, isAngle = false;
	
	/*
	 * backup old values before hightlighting
	 */
	private AppearanceType appearanceType_old1 = AppearanceType.NORMAL, appearanceType_old2 = AppearanceType.NORMAL;
	private Component value_old1, value_old2;

	private DrawComponents drawcomp;
	private ComponentsManagerModel sourceModel, destinationModel;
	private ComponentsList sourceList, destinationList;

	public ComponentsManager(ComponentsManagerModel sourceModel, ComponentsManagerModel destinationModel,
							ComponentsList sourceList, ComponentsList destinationList, DrawComponents drawcomp) {
		this.sourceModel = sourceModel;
		this.destinationModel = destinationModel;
		this.sourceList = sourceList;
		this.destinationList = destinationList;
		this.drawcomp = drawcomp;
		
		setJFrame();
		buildGUI();
	}
	
	/**
	 * create and set gui components
	 */
	private void buildGUI() {
		addESCListener();
		addCompButtons();
		addButtons();
		addRemoveButtons();
		addLists();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				close();
			}
		});
	}
	
	/**
	 * add jlist to frame
	 */
	private void addLists() {
		// list on the left side
		sourceJList = createJList(sourceModel, new Rectangle(0, 0, 150, 245));
		sourceJList.addListSelectionListener(new ListSelectionListener() {
			@Override
			/**
			 * change appearance
			 * enable/disable buttons if a point is chosen
			 */
			public void valueChanged(ListSelectionEvent e) {
				resetDestination();
				destinationList.clear();
				highlightSource();

				if(isSelected(sourceJList)) {
					Component value = getSourceComponent();
					if (value.getType() == ComponentType.POINT) {
						buttons.get(DISTANCE).setEnabled(true);
						buttons.get(INTERSECTION).setEnabled(false);
						buttons.get(ANGLE).setEnabled(false);
					} else {
						buttons.get(DISTANCE).setEnabled(true);
						buttons.get(INTERSECTION).setEnabled(true);
						buttons.get(ANGLE).setEnabled(true);
					}
				}
			}
		});
		
		// list on the right side
		destinationJList = createJList(destinationModel, new Rectangle(350, 0, 150, 245));
		destinationJList.addListSelectionListener(new ListSelectionListener() {
			@Override
			/**
			 * change appearance
			 */
			public void valueChanged(ListSelectionEvent e) {
				highlightDestination();
			}
		});
		destinationJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					calcEvent();
				}
			}
		});
	}
	
	/**
	 * add remove buttons
	 */
	private void addRemoveButtons() {
		int x = 5;
		int y = 250;
		final String remove = "Remove", removeAll = "Remove All";
		final String[] labels = { remove, removeAll };
		final int[] keyEvent = { KeyEvent.VK_D, KeyEvent.VK_A };
		for(int i = 0; i < labels.length; i++) {
			JButton button = createButton(labels[i], x+i*100, y, keyEvent[i]);
			button.setSize(100, 20);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == remove) {
						if(isSelected(sourceJList)) {
							drawcomp.remove(getSourceIndex());
						}
					} else {
						drawcomp.removeAll();
						
						buttons.get(DISTANCE).setEnabled(false);
						buttons.get(INTERSECTION).setEnabled(false);
						buttons.get(ANGLE).setEnabled(false);
					}
				}
			});
			buttons.put(remove, button);
			add(button);
		}
	}
	
	/**
	 * add components buttons
	 * (get distance, get intersection, get angle)
	 */
	private void addCompButtons() {
		int x = 175;
		int y = 50;
		final String[] buttonLabel = { DISTANCE, INTERSECTION, ANGLE };
		final int[] keyEvent = { KeyEvent.VK_D, KeyEvent.VK_I, KeyEvent.VK_A };
		for (int i = 0; i < buttonLabel.length; i++) {
			JButton button = createButton(buttonLabel[i], x, y+i*50, keyEvent[i]);
			button.setEnabled(false);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getActionCommand() == DISTANCE) {
						isDistance = true;
						isIntersection = false;
						isAngle = false;
						
						if(isSelected(sourceJList)) {
							Component comp = getSourceComponent();
							if (comp.getType() == ComponentType.POINT) {
								destinationList.addAll(sourceList.deepClone());
							} else {
								destinationList.addAll(sourceList.deepCloneFilter(new ComponentType[] { ComponentType.LINE, ComponentType.PLANE }));
							}
						}
					} else if(e.getActionCommand() == INTERSECTION) {
						isDistance = false;
						isIntersection = true;
						isAngle = false;
						
						destinationList.addAll(sourceList.deepCloneFilter(new ComponentType[] { ComponentType.POINT }));
					} else if(e.getActionCommand() == ANGLE) {
						isDistance = false;
						isIntersection = false;
						isAngle = true;
						
						destinationList.addAll(sourceList.deepCloneFilter(new ComponentType[] { ComponentType.POINT }));
					}
				}
			});
			buttons.put(buttonLabel[i], button);
			add(button);
		}
	}
	
	/**
	 * add buttons
	 */
	private void addButtons() {
		int x = 290;
		int y = 250;
		final String calc = "Calculate", close = "Close";
		final String[] buttonLabel = { calc, close };
		int[] keyEvent = new int[] { KeyEvent.VK_O, KeyEvent.VK_C };
		for (int i = 0; i < buttonLabel.length; i++) {
			JButton button = createButton(buttonLabel[i], x+i*100, y, keyEvent[i]);
			button.setSize(100, 20);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand() == calc) {
						calcEvent();
					} else if (e.getActionCommand() == close){
						close();
					}
				}
			});
			buttons.put(buttonLabel[i], button);
			add(button);
		}
	}
	
	/**
	 * get index of selected element
	 * 
	 * @return index
	 */
	private int getSourceIndex() {
		return sourceJList.convertIndexToModel(sourceJList.getSelectedIndex());
	}
	
	/**
	 * get component at the selected index
	 * 
	 * @return Component
	 */
	private Component getSourceComponent() {
		return sourceList.get(getSourceIndex());
	}
	
	/**
	 * get index of selected element
	 * 
	 * @return index
	 */
	private int getDestinationIndex() {
		return destinationJList.convertIndexToModel(destinationJList.getSelectedIndex());
	}
	
	/**
	 * get component at the selected index
	 * 
	 * @return Component
	 */
	private Component getDestinationComponent() {
		return destinationList.get(getDestinationIndex());
	}
	
	/**
	 * check if something is selected in the jlist
	 * 
	 * @param list JXList
	 * @return true if selected else return false;
	 */
	private boolean isSelected(JXList list) {
		if(list.getSelectedIndex() < 0) {
			MessageBox.InfoBox("None Selected", "Select a Component.");
			return false;
		}
		return true;
	}
	
	/**
	 * highlight selected component on the source jlist
	 */
	private void highlightSource() {
		resetSource();
		if (isSelected(sourceJList)) {
			Component value = getSourceComponent();
			appearanceType_old1 = value.getAppearanceType();
			value_old1 = value;
			value.setAppearance(AppearanceType.HIGHLIGHTED1);
		}
	}
	
	/**
	 * highlight selected component on the destination jlist
	 */
	private void highlightDestination() {
		resetDestination();
		if (isSelected(destinationJList)) {
			Component value = getDestinationComponent();
			appearanceType_old2 = value.getAppearanceType();
			value_old2 = value;
			value.setAppearance(AppearanceType.HIGHLIGHTED2);
		}
	}
	
	/**
	 * revert appearance of component on the source jlist
	 */
	private void resetSource() {
		if (value_old1 != null) {
			value_old1.setAppearance(appearanceType_old1);
		}
	}
	
	/**
	 * revert appearance of component on the destination jlist
	 */
	private void resetDestination() {
		if (value_old2 != null) {
			value_old2.setAppearance(appearanceType_old2);
		}
	}
	
	/**
	 * execute if pressed ok or double clicked on the rightlist
	 */
	private void calcEvent() {
		if(!isSelected(sourceJList) || !isSelected(destinationJList)) {
			return;
		}

		Component comp1 = getSourceComponent();
		Component comp2 = getDestinationComponent();
		if(isDistance) {
			calcDistance(comp1, comp2);
		} else if(isIntersection) {
			calcIntersection(comp1, comp2);
		} else if(isAngle) {
			calcAngle(comp1, comp2);
		}
	}
	
	/**
	 * calculate distance of component1 and component2
	 * 
	 * @param comp1 component1
	 * @param comp2 component2
	 */
	private void calcDistance(Component comp1, Component comp2) {
		if(comp1.getType() == ComponentType.POINT && comp2.getType() == ComponentType.POINT) {
			drawcomp.getDistance((Point)comp1, (Point)comp2);
		} else if (comp1.getType() == ComponentType.POINT && comp2.getType() == ComponentType.LINE){
			drawcomp.getDistance((Point)comp1, (Line)comp2);
		} else if (comp1.getType() == ComponentType.LINE && comp2.getType() == ComponentType.POINT){
			drawcomp.getDistance((Point)comp2, (Line)comp1);
		} else if (comp1.getType() == ComponentType.POINT && comp2.getType() == ComponentType.PLANE){
			drawcomp.getDistance((Point)comp1, (Plane)comp2);
		} else if (comp1.getType() == ComponentType.PLANE && comp2.getType() == ComponentType.POINT){
			drawcomp.getDistance((Point)comp2, (Plane)comp1);
		}
	}
	
	/**
	 * calculate and draw intersection
	 * 
	 * @param comp1 component1
	 * @param comp2 component2
	 */
	private void calcIntersection(Component comp1, Component comp2) {
		if(comp1.getType() == ComponentType.LINE && comp2.getType() == ComponentType.LINE) {
			drawcomp.getIntersection((Line)comp1, (Line)comp2);
		} else if (comp1.getType() == ComponentType.LINE && comp2.getType() == ComponentType.PLANE){
			drawcomp.getIntersection((Line)comp1, (Plane)comp2);
		} else if (comp1.getType() == ComponentType.PLANE && comp2.getType() == ComponentType.LINE){
			drawcomp.getIntersection((Line)comp2, (Plane)comp1);
		} else if (comp1.getType() == ComponentType.PLANE && comp2.getType() == ComponentType.PLANE) {
			drawcomp.getIntersection((Plane)comp1, (Plane)comp2);
		}
	}
	
	/**
	 * calculate angle of component1 and component2
	 * 
	 * @param comp1 component1
	 * @param comp2 component2
	 */
	private void calcAngle(Component comp1, Component comp2) {
		if(comp1.getType() == ComponentType.LINE && comp2.getType() == ComponentType.LINE) {
			drawcomp.getAngle((Line)comp1, (Line)comp2);
		} else if (comp1.getType() == ComponentType.LINE && comp2.getType() == ComponentType.PLANE){
			drawcomp.getAngle((Line)comp1, (Plane)comp2);
		} else if (comp1.getType() == ComponentType.PLANE && comp2.getType() == ComponentType.LINE){
			drawcomp.getAngle((Line)comp2, (Plane)comp1);
		} else if (comp1.getType() == ComponentType.PLANE && comp2.getType() == ComponentType.PLANE){
			drawcomp.getAngle((Plane)comp1, (Plane)comp2);
		}
	}
	
	/**
	 * create a jliste
	 * 
	 * @param bounds of jlist
	 * @return JXList
	 */
	private JXList createJList(ComponentsManagerModel model, Rectangle bounds) {
		JXList list = new JXList();
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectionBackground(Color.LIGHT_GRAY);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(bounds);
		add(scrollPane);
		return list;
	}
	
	/**
	 * esc = close window
	 */
	private void addESCListener() {
		ActionListener escListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		};
		getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	/**
	 * close window
	 */
	private void close() {
		resetDestination();
		resetSource();
		
		setVisible(false);
	}
	
	/**
	 * set jframe
	 */
	private void setJFrame() {
		setSize(500, 300);
		setLayout(null);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle(JVector.PROGRAM + " - Component Manager");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width-getWidth())/2 + 200, (dim.height-getHeight())/2 + 200);
		setIconImage(new ImageIcon(getClass().getResource(JVector.UIIMAGELOCATION + "icon.jpg")).getImage());
	}
	
	/**
	 * create buttons
	 * 
	 * @param title buttonlabel
	 * @param x coordinate of button
	 * @param y coordinate of button 
	 * @param mnemonic shortcut of this button
	 * @return JButton
	 */
	private JButton createButton(String title, int x, int y, int mnemonic) {
		JButton button = new JButton(title);
		button.setSize(150, 25);
		button.setLocation(x, y);
		button.setMnemonic(mnemonic);
		return button;
	}
}

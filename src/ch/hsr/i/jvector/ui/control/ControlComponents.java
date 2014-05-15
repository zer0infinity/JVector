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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.logic.DrawComponents;
import ch.hsr.i.jvector.ui.control.TextFieldContainer.Line;
import ch.hsr.i.jvector.ui.control.TextFieldContainer.Plane;
import ch.hsr.i.jvector.ui.main.JVector;
import ch.hsr.i.jvector.util.BrowserControl;

public class ControlComponents {
	
	private static final long serialVersionUID = 1L;
	
	private JXTaskPane pointTaskPane, lineTaskPane, planeTaskPane, gridTaskPane;
	
	JPanel panel;
	DrawComponents drawcomp;
	JXTaskPaneContainer taskpanecontainer;
	
	public ControlComponents(DrawComponents drawcomp) {
		this.drawcomp = drawcomp;
		
		taskpanecontainer = new JXTaskPaneContainer();
		taskpanecontainer.setOpaque(false);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.add(taskpanecontainer);
	}
	
	/**
	 * Swing components
	 * 
	 * @return JPanel
	 */
	public JPanel createControl(int frame_width, int frame_height) {
		updateBounds(frame_width, frame_height);
		
		addLogo();
		addPointComponents();
		addLineComponents();
		addPlaneComponents();
		addGridComponents();
		
		return panel;
	}
	
	/**
	 * Update Location of this Panel after resize
	 * 
	 * @param frame_width
	 */
	public void updateBounds(int frame_width, int frame_height) {
		panel.setBounds(frame_width - 170, 5, 155, frame_height - 61);
		taskpanecontainer.setBounds(-8, 135, 171, frame_height - 61);
	}
	
	/**
	 * Add JVector Logo
	 */
	void addLogo() {
		int x = 2;
		int y = 0;
		int width = 150;
		int height = 145;
		
		JLabel logo = new JLabel();
		logo.setBounds(x, y, width, height);
		logo.setIcon(new ImageIcon(getClass().getResource(JVector.UIIMAGELOCATION + "logo.jpg")));
		logo.setToolTipText("open " + JVector.PROGRAMURL + " in a browser");
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				BrowserControl.displayURL(JVector.PROGRAMURL);
			}
		});
		panel.add(logo);
	}
	
	/**
	 * Swing components for "Add Point"
	 */
	void addPointComponents() {
		pointTaskPane = new JXTaskPane();
		pointTaskPane.setTitle("Draw Point");
		pointTaskPane.setCollapsed(true);
		pointTaskPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pointTaskPane.setCollapsed(false);
				lineTaskPane.setCollapsed(true);
				planeTaskPane.setCollapsed(true);
				gridTaskPane.setCollapsed(true);
			}
		});
		
		final String[] pointLabel = { "x1", "y1", "z1" };
		final TextFieldContainer pointTFC = new TextFieldContainer(3);
		JPanel labelPanel = createPanel();
		JPanel textfieldPanel = createPanel();
		for (int i = 0; i < pointLabel.length; i++) {
			labelPanel.add(createLabel(pointLabel[i]));
			textfieldPanel.add(pointTFC.addTextField(pointLabel[i]));
		}
		pointTaskPane.add(labelPanel);
		pointTaskPane.add(textfieldPanel);
		
		JButton button = createButton("Draw");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!pointTFC.isTextFieldEmpty()) {
					drawcomp.addPoint(pointTFC.getPoint(), AppearanceType.NORMAL);
				}
			}
		});
		pointTaskPane.add(button);
		taskpanecontainer.add(pointTaskPane);
	}
	
	/**
	 * Swing Components for "Add Line"
	 */
	void addLineComponents() {
		lineTaskPane = new JXTaskPane();
		lineTaskPane.setTitle("Draw Line");
		lineTaskPane.setCollapsed(true);
		lineTaskPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pointTaskPane.setCollapsed(true);
				lineTaskPane.setCollapsed(false);
				planeTaskPane.setCollapsed(true);
				gridTaskPane.setCollapsed(true);
			}
		});
		
		addLineTwoPoints();
		addLineTwoVectors();
		
		taskpanecontainer.add(lineTaskPane);
	}
	
	/**
	 * add line with two points
	 */
	private void addLineTwoPoints() {
		final String[][] pointLabel = { { "x1", "y1", "z1" }, { "x2", "y2", "z2" } };
		final TextFieldContainer lineTFC = new TextFieldContainer(6);
		for (int i = 0; i < pointLabel.length; i++) {
			JPanel labelPanel = createPanel();
			JPanel textfieldPanel = createPanel();
			for (int j = 0, k = i*3; j < pointLabel[i].length; j++, k++) {
				labelPanel.add(createLabel(pointLabel[i][j]));
				textfieldPanel.add(lineTFC.addTextField(pointLabel[i][j]));
			}
			lineTaskPane.add(labelPanel);
			lineTaskPane.add(textfieldPanel);
		}
		
		JButton button = createButton("Draw");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!lineTFC.isTextFieldEmpty()) {
					Line line = lineTFC.toLine();
					drawcomp.addLine(line.point1, line.point2, AppearanceType.NORMAL);
				}
			}
		});
		lineTaskPane.add(button);
	}
	
	/**
	 * add line with two Vectors
	 */
	private void addLineTwoVectors() {
		final String[][] vectorLabel = { {"vx1", "vx2"}, {"vy1", "vy2"}, {"vz1", "vz2"} };
		final TextFieldContainer lineTFC = new TextFieldContainer(6);
		for (int i = 0; i < vectorLabel.length; i++) {
			JPanel panel = createPanel();
			for (int j = 0, k = i*2; j < vectorLabel[i].length; j++, k++) {
				panel.add(createLabel(vectorLabel[i][j]));
				panel.add(lineTFC.addTextField(vectorLabel[i][j]));
			}
			lineTaskPane.add(panel);
		}
		
		JButton button = createButton("Draw");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!lineTFC.isTextFieldEmpty()) {
					Line line = lineTFC.toLine();
					drawcomp.addLine(line.point1, line.point2, AppearanceType.NORMAL);
				}
			}
		});
		lineTaskPane.add(button);
	}
	
	/**
	 * Swing Components for "Add Plane"
	 */
	void addPlaneComponents() {
		planeTaskPane = new JXTaskPane();
		planeTaskPane.setTitle("Draw Plane");
		planeTaskPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pointTaskPane.setCollapsed(true);
				lineTaskPane.setCollapsed(true);
				planeTaskPane.setCollapsed(false);
				gridTaskPane.setCollapsed(true);
			}
		});
		
		addPlaneThreePoints();
		addPlaneVectorConstant();
		addPlanePointsTwoVectors();
		
		taskpanecontainer.add(planeTaskPane);
	}
	
	/**
	 * add plane with a point and two vectors
	 */
	private void addPlanePointsTwoVectors() {
		final String[] pointLabel = { "x1", "y1", "z1" };
		final String[][] vectorLabel = { {"vx1", "vx2"}, {"vy1", "vy2"}, {"vz1", "vz2"} };
		final TextFieldContainer planeTFC = new TextFieldContainer(9);
		JPanel labelPanel = createPanel();
		JPanel textfieldPanel = createPanel();
		for (int i = 0; i < pointLabel.length; i++) {
			labelPanel.add(createLabel(pointLabel[i]));
			textfieldPanel.add(planeTFC.addTextField(pointLabel[i]));
		}
		planeTaskPane.add(labelPanel);
		planeTaskPane.add(textfieldPanel);
		for (int i = 0; i < vectorLabel.length; i++) {
			JPanel panel = createPanel();
			for (int j = 0, k = 2*i+3; j < vectorLabel[i].length; j++, k++) {
				panel.add(createLabel(vectorLabel[i][j]));
				panel.add(planeTFC.addTextField(vectorLabel[i][j]));
			}
			planeTaskPane.add(panel);
		}
		
		JButton button = createButton("Draw");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!planeTFC.isTextFieldEmpty()) {
					Plane plane = planeTFC.toPlanePointsTwoVectors();
					drawcomp.addPlane(plane.point1, plane.vector1, plane.vector2, AppearanceType.NORMAL);
				}
			}
		});
		planeTaskPane.add(button);
	}
	
	/**
	 * add plane with a vector and a constant
	 */
	private void addPlaneVectorConstant() {
		final String[] vectorLabel = { "vx1", "vy1", "vz1", "const" };
		final TextFieldContainer planeTFC = new TextFieldContainer(4);
		for (int i = 0; i < vectorLabel.length; i++) {
			JPanel panel = createPanel();
			panel.add(createLabel(""));
			panel.add(createLabel(vectorLabel[i]));
			panel.add(planeTFC.addTextField(vectorLabel[i]));
			panel.add(createLabel(""));
			planeTaskPane.add(panel);
		}
		
		JButton button = createButton("Draw");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!planeTFC.isTextFieldEmpty()) {
					Plane plane = planeTFC.toPlaneVectorConst();
					drawcomp.addPlane(plane.vector1, plane.lambda, AppearanceType.NORMAL);
				}
			}
		});
		planeTaskPane.add(button);
	}
	
	/**
	 * add plane out of three points
	 */
	private void addPlaneThreePoints() {
		final String[][] pointLabel = { {"x1", "y1", "z1"}, { "x2", "y2", "z2" }, {"x3", "y3", "z3"} };
		final TextFieldContainer planeTFC = new TextFieldContainer(9);
		for (int i = 0; i < pointLabel.length; i++) {
			JPanel labelPanel = createPanel();
			JPanel textfieldPanel = createPanel();
			for (int j = 0, k = i*3; j < pointLabel[i].length; j++, k++) {
				labelPanel.add(createLabel(pointLabel[i][j]));
				textfieldPanel.add(planeTFC.addTextField(pointLabel[i][j]));
			}
			planeTaskPane.add(labelPanel);
			planeTaskPane.add(textfieldPanel);
		}
		
		JButton button = createButton("Draw");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!planeTFC.isTextFieldEmpty()) {
					Plane plane = planeTFC.toPlaneThreePoints();
					drawcomp.addPlane(plane.point1, plane.point2, plane.point3, AppearanceType.NORMAL);
				}
			}
		});
		planeTaskPane.add(button);
	}
	
	/**
	 * Swing Components for "Change Grid"
	 */
	void addGridComponents() {
		gridTaskPane = new JXTaskPane();
		gridTaskPane.setTitle("Grid Setting");
		gridTaskPane.setCollapsed(true);
		gridTaskPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pointTaskPane.setCollapsed(true);
				lineTaskPane.setCollapsed(true);
				planeTaskPane.setCollapsed(true);
				gridTaskPane.setCollapsed(false);
			}
		});
		
		final TextFieldContainer gridTFC = new TextFieldContainer(3);
		JPanel panel = createPanel();
		panel.add(createLabel(""));
		panel.add(gridTFC.addTextField("grid"));
		panel.add(createLabel(""));
		
		gridTaskPane.add(panel);
		
		JButton apply_button = createButton("Apply");
		apply_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!gridTFC.isTextFieldEmpty()) {
					drawcomp.removeGrid();
					int offset = gridTFC.getGridOffset();
					drawcomp.addGrid(offset);
				}
			}
		});
		JButton remove_button = createButton("Remove Grid");
		remove_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawcomp.removeGrid();
			}
		});
		gridTaskPane.add(apply_button);
		gridTaskPane.add(remove_button);
		taskpanecontainer.add(gridTaskPane);
	}
	
	/**
	 * create a new button
	 * 
	 * @param text button text
	 * @return JButton
	 */
	private JButton createButton(String text) {
		JButton button = new JButton();
		button.setText(text);
		return button;
	}
	
	/**
	 * create a new label
	 * 
	 * @param text text of label
	 * @return JLabel
	 */
	private JLabel createLabel(String text) {
		JLabel label = new JLabel();
		label.setText(text);
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}
	
	/**
	 * create a new JPanel
	 * 
	 * @return JPanel
	 */
	private JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		return panel;
	}
}

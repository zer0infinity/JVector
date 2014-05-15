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

package ch.hsr.i.jvector.logic;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.components.CoordinateSystem;
import ch.hsr.i.jvector.components.Line3D;
import ch.hsr.i.jvector.components.Plane3D;
import ch.hsr.i.jvector.components.Point3D;
import ch.hsr.i.jvector.components.Component3D.ComponentType;
import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.exceptions.NoIntersectionException;
import ch.hsr.i.jvector.exceptions.NoLineException;
import ch.hsr.i.jvector.exceptions.NoPlaneException;
import ch.hsr.i.jvector.interfaces.Component;
import ch.hsr.i.jvector.interfaces.Line;
import ch.hsr.i.jvector.interfaces.Plane;
import ch.hsr.i.jvector.interfaces.Point;
import ch.hsr.i.jvector.ui.main.JVector;
import ch.hsr.i.jvector.ui.util.MessageBox;

public class DrawComponents {
	
	/**
	 * the radius of the bounding sphere
	 */
	public static int RADIUS = 800;
	
	/**
	 * the current coordinate system object
	 */
	private CoordinateSystem cs = new CoordinateSystem();
	
	private BranchGroup objectRoot, compBranchGroup;
	private ComponentsList compObjectList;
	
	public DrawComponents(BranchGroup objectRoot, ComponentsList compObjectList) {
		this.objectRoot = objectRoot;
		this.compObjectList = compObjectList;
		compBranchGroup = new BranchGroup();
		
		BranchGroup[] branchgroups = { gridBranchGroup, compBranchGroup };
		for (BranchGroup bg: branchgroups) {
			bg.setCapability(BranchGroup.ALLOW_DETACH);
			bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
			bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			
			objectRoot.addChild(bg);
		}
		
		addAxes();
		addGrid(1);
		
		if(JVector.EXAMPLES)
			drawExamples();
	}
	
	/**
	 * draw some example objects
	 */
	public void drawExamples() {
		// line
		Point3d line1p1 = new Point3d(-10, -10, -10);
		Point3d line1p2 = new Point3d(50, 50, 50);
		addLine(line1p1, line1p2, AppearanceType.NORMAL);
		Point3d line2p1 = new Point3d(-10, -10, 0);
		Point3d line2p2 = new Point3d(50, 50, 0);
		addLine(line2p1, line2p2, AppearanceType.NORMAL);
		Point3d line3p1 = new Point3d(-3, -5, -90);
		Point3d line3p2 = new Point3d(10, 3, 66);
		addLine(line3p1, line3p2, AppearanceType.NORMAL);
		
		// point
		Point3d point = new Point3d(5, 2, 5);
		addPoint(point, AppearanceType.NORMAL);
		Point3d point2 = new Point3d(10, 20, 50);
		addPoint(point2, AppearanceType.NORMAL);
		
		// plane
		Point3d plp1 = new Point3d(1, 1, 5);
		Point3d plp2 = new Point3d(50, 1, 5);
		Point3d plp3 = new Point3d(50, 50, 5);
		addPlane(plp1, plp2, plp3, AppearanceType.NORMAL);
		addPlane(new Vector3d(2, 3, 4), 7, AppearanceType.NORMAL);
	}
	
	// ********	
	// * AXES *
	// ********
	
	private BranchGroup axesBranchGroup = new BranchGroup();
	
	/**
	 * Add x, y and z Axis.
	 */
	public void addAxes() {
		axesBranchGroup.removeAllChildren();
		axesBranchGroup.addChild(cs.getXAxisShape());
		axesBranchGroup.addChild(cs.getYAxisShape());
		axesBranchGroup.addChild(cs.getZAxisShape());
		objectRoot.addChild(axesBranchGroup);
	}	
	
	// ********	
	// * GRID *
	// ********
	
	private BranchGroup gridBranchGroup = new BranchGroup();
	
	/**
	 * Add grid with certain offset.
	 * 
	 * @param the desired offset of the raster
	 */
	public void addGrid(int offset) {
		cs = new CoordinateSystem(offset);
		
		BranchGroup grid = new BranchGroup();
		grid.setCapability(BranchGroup.ALLOW_DETACH);
		grid.addChild(cs.getGridShape());
		gridBranchGroup.addChild(grid);
		
	}
	
	/**
	 * remove grid
	 */
	public void removeGrid() {
		gridBranchGroup.removeAllChildren();
	}
	
	// *********	
	// * POINT *
	// *********
	
	/**
	 * Add point to PointList and PointBranchGroup.
	 * 
	 * @param a Point3d object
	 */
	public void addPoint(Point3d p, AppearanceType type) {
		Point3D point = new Point3D(p);
		if(!isInUniverse(point)) {
			compObjectList.add(point);
			BranchGroup pointBG = new BranchGroup();
			pointBG.setCapability(BranchGroup.ALLOW_DETACH);
			pointBG.addChild(point.getShape(type));
			compBranchGroup.addChild(pointBG);
		} else {
			MessageBox.InfoBox("Already drawn", "The Point is already drawn.");
		}
	}
	
	public void getDistance(Point p1, Point p2) {
		if (p1 == p2) {
			MessageBox.InfoBox("", "Same Points.");
		} else {
			MessageBox.InfoBox("", "The distance between the two points is: " +  p1.getDistance(p2));
		}
	}
	
	public void getDistance(Point point, Line line) {		
		MessageBox.InfoBox("", "The shortest distance between the the point and  the line is: " +  point.getDistance(line));
	}
	
	public void getDistance(Point point, Plane plane) {		
		MessageBox.InfoBox("", "The shortest distance between the the point and  the plane is: " +  point.getDistance(plane));
	}
	
	// ********	
	// * LINE *
	// ********
	
	/**
	 * Add line to LineList and LineBranchGroup.
	 * 
	 * @param v1 - the vector to a point on the line
	 * @param v2 - the directon vector of the line
	 */
	public void addLine(Vector3d v1, Vector3d v2, AppearanceType type) {	
		Line3D line;
		try {
			line = new Line3D(v1, v2);
			if(!isInUniverse(line)) {
				compObjectList.add(line);
				BranchGroup lineBG = new BranchGroup();
				lineBG.setCapability(BranchGroup.ALLOW_DETACH);
				lineBG.addChild(line.getShape(type));
				compBranchGroup.addChild(lineBG);
			} else {
				MessageBox.InfoBox("Already drawn", "The Line is already drawn.");
			}
		} catch (NoLineException e) {
			MessageBox.InfoBox("Not a Line", e.getMessage());
		}
	}
	
	/**
	 * Add line to LineList and LineBranchGroup.
	 * 
	 * @param p1 - a point on the line
	 * @param p2 - a point on the line
	 */
	public void addLine(Point3d p1, Point3d p2, AppearanceType type) {	
		Line3D line;
		try {
			line = new Line3D(p1, p2);
			if(!isInUniverse(line)) {
				compObjectList.add(line);
				BranchGroup lineBG = new BranchGroup();
				lineBG.setCapability(BranchGroup.ALLOW_DETACH);
				lineBG.addChild(line.getShape(type));
				compBranchGroup.addChild(lineBG);
			} else {
				MessageBox.InfoBox("Already drawn", "The Line is already drawn.");
			}
		} catch (NoLineException e) {
			MessageBox.InfoBox("Not a Line", e.getMessage());
		}
	}
	
	/**
	 * Calculates the intersection point of the two lines and adds the point into the scene.
	 * 
	 * @param line1
	 * @param line2
	 */
	public void getIntersection(Line line1, Line line2) {
		if (line1 == line2) {
			MessageBox.InfoBox("", "Same line.");
		} else {
			Point point;
			try {
				point = line1.getIntersection(line2);
				addPoint(point.getPoint(), AppearanceType.INTERSECTION);
			} catch (NoIntersectionException e) {
				MessageBox.InfoBox("These lines do not intersect.", e.getMessage());
			}
		}
	}
	
	/**
	 * Calculates the intersection point of the line and the plane and adds the point into the scene.
	 * 
	 * @param line
	 * @param plane
	 */
	public void getIntersection(Line line, Plane plane) {
		Point point;
		try {
			point = line.getIntersection(plane);
			addPoint(point.getPoint(), AppearanceType.INTERSECTION);
		} catch (NoIntersectionException e) {
			MessageBox.InfoBox("These objects do not intersect.", e.getMessage());
		}		
	}
	
	public void getAngle(Line line1, Line line2) {
		if (line1 == line2) {
			MessageBox.InfoBox("", "Same Line.");
		} else {
			MessageBox.InfoBox("", "The angle between the two lines is: " +  line1.getAngle(line2));
		}
	}
	
	public void getAngle(Line line, Plane plane) {		
		MessageBox.InfoBox("", "The angle between the line and the plane is: " +  line.getAngle(plane));
	}
	
	// *********	
	// * PLANE *
	// *********
	
	/**
	 * Add plane to PlaneList and PlaneBranchGroup.
	 * 
	 * @param point1
	 * @param point2
	 * @param point3
	 */
	public void addPlane(Point3d point1, Point3d point2, Point3d point3, AppearanceType type) {
		Plane3D plane;
		try {
			plane = new Plane3D(point1, point2, point3);
			if(!isInUniverse(plane)) {
				compObjectList.add( plane);
				BranchGroup planeBG = new BranchGroup();
				planeBG.setCapability(BranchGroup.ALLOW_DETACH);
				planeBG.addChild(plane.getShape(type));
				compBranchGroup.addChild(planeBG);
			} else {
				MessageBox.InfoBox("Already drawn", "The Plane is already drawn.");
			}
		} catch (NoPlaneException e) {
			MessageBox.InfoBox("Not a Plane", e.getMessage());
		}
	}
	
	/**
	 * Add plane to PlaneList and PlaneBranchGroup.
	 * 
	 * @param point - the point on the plane
	 * @param vector1 - the first direction vector
	 * @param vector2 - the second direction vector
	 */
	public void addPlane(Point3d point, Vector3d vector1, Vector3d vector2, AppearanceType type) {
		Plane3D plane;
		try {
			plane = new Plane3D(point, vector1, vector2);
			if(!isInUniverse(plane)) {
				compObjectList.add(plane);
				BranchGroup planeBG = new BranchGroup();
				planeBG.setCapability(BranchGroup.ALLOW_DETACH);
				planeBG.addChild(plane.getShape(type));
				compBranchGroup.addChild(planeBG);
			} else {
				MessageBox.InfoBox("Already drawn", "The Plane is already drawn.");
			}
		} catch (NoPlaneException e) {
			MessageBox.InfoBox("Not a Plane", e.getMessage());
		}
	}
	
	/**
	 * Add plane to PlaneList and PlaneBranchGroup.
	 * 
	 * @param vector - the normal vector of the plane
	 * @param lambda - the constant which defines the position of the plane
	 */
	public void addPlane(Vector3d vector, double lambda, AppearanceType type) {
		Plane3D plane;
		try {
			plane = new Plane3D(vector, lambda);
			if(!isInUniverse(plane)) {
				compObjectList.add(plane);
				BranchGroup planeBG = new BranchGroup();
				planeBG.setCapability(BranchGroup.ALLOW_DETACH);
				planeBG.addChild(plane.getShape(type));
				compBranchGroup.addChild(planeBG);
			} else {
				MessageBox.InfoBox("Already drawn", "The Plane is already drawn.");
			}
		} catch (NoPlaneException e) {
			MessageBox.InfoBox("Not a Plane", e.getMessage());
		}
	}
	
	/**
	 * Calculates the intersection line of p1 and p2 and adds the line into the scene.
	 * 
	 * @param p1 - plane1
	 * @param p2 - plane2
	 */
	public void getIntersection(Plane p1, Plane p2) {
		if (p1 == p2) {
			MessageBox.InfoBox("", "Same Plane.");
		} else {
			Line line;
			try {
				line = p1.getIntersection(p2);
				addLine(new Vector3d(line.getPointOnLine()), line.getDirection(), AppearanceType.INTERSECTION);
			} catch (NoIntersectionException e) {
				MessageBox.InfoBox("These planes do not intersect.", e.getMessage());
			}
		}
	}
	
	public void getAngle(Plane plane1, Plane plane2) {
		if (plane1 == plane2) {
			MessageBox.InfoBox("", "Same Plane.");
		} else {
			MessageBox.InfoBox("", "The angle between the planes is: " +  plane1.getAngle(plane2));
		}
	}
	
	/**
	 * Removes the component at the specified index.
	 * 
	 * @param the index of the component to remove
	 */
	public void remove(int index) {
		compObjectList.remove(index);
		compBranchGroup.removeChild(index);
	}
	
	/**
	 * Removes all components
	 */
	public void removeAll() {
		compObjectList.removeAll();
		compBranchGroup.removeAllChildren();
	}
	
	private boolean isInUniverse(Component comp) {
		for(int i=0;i<compObjectList.size();i++) {
			if(comp.getType() == ComponentType.POINT && compObjectList.get(i).getType() == ComponentType.POINT) {
				if(((Point)comp).equals((Point)compObjectList.get(i))) {
					return true;
				}
			} else if(comp.getType() == ComponentType.LINE && compObjectList.get(i).getType() == ComponentType.LINE) {
				if(((Line)comp).equals((Line)compObjectList.get(i))) {
					return true;
				}
			} else if(comp.getType() == ComponentType.PLANE && compObjectList.get(i).getType() == ComponentType.PLANE) {
				if(((Plane)comp).equals((Plane)compObjectList.get(i))) {
					return true;
				}
			}
		}
		return false;
	}
}

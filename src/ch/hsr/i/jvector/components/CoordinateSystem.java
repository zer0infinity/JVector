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

package ch.hsr.i.jvector.components;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import ch.hsr.i.jvector.interfaces.CoordSystem;
import ch.hsr.i.jvector.logic.DrawComponents;

public class CoordinateSystem implements CoordSystem {

	private int bound = DrawComponents.RADIUS;

	/**
	 * the offset between two grid lines
	 */
	private double offset = 1.0;

	/**
	 * the number of lines (per half of an axis) which make up the grid
	 */
	private int scaleMax = (int) Math.round((bound / offset) - offset);

	/**
	 * This enum values indicates the plane that is spanned by two axes.
	 */
	private enum GridSection {
		XY, XZ, YZ
	}
	
	private static LineAttributes lineAttrAxis = new LineAttributes(2.0f, LineAttributes.PATTERN_SOLID, true);
	
	/**
	 * Creates a Coordinate System object with default grid offset (1.0).
	 * 
	 * @param the offset for the grid
	 */
	public CoordinateSystem() {
	}
	
	/**
	 * Creates a Coordinate System object with grid offset <code>offset</code>.
	 * 
	 * @param the offset for the grid
	 */
	public CoordinateSystem(double offset) {
		this.offset = offset;
	}
	
	@Override
	/**
	 * Returns a <code>Shape3D</code> object of the x axis. Color is by default red.
	 * 
	 * @return the <code>Shape3D</code> object of the x axis
	 */
	public Shape3D getXAxisShape() {
		
		Point3d point1 = new Point3d(-bound, 0, 0);
		Point3d point2 = new Point3d(bound, 0, 0);
		Point3d[] points = {point1, point2};
		
		LineArray axis = new LineArray(2, LineArray.COORDINATES);
		axis.setCoordinates(0, points);
		
		Color3f red = new Color3f(1f, 0f, 0f);

		Appearance app = new Appearance();
		ColoringAttributes colorAttr = new ColoringAttributes(red,	ColoringAttributes.NICEST);
		app.setLineAttributes(lineAttrAxis);
		app.setColoringAttributes(colorAttr);
		
		return new Shape3D(axis, app);
	}

	@Override
	/**
	 * Returns a <code>Shape3D</code> object of the y axis. Color is by default green.
	 * 
	 * @return the <code>Shape3D</code> object of the y axis
	 */
	public Shape3D getYAxisShape() {
		
		Point3d point1 = new Point3d(0, -bound, 0);
		Point3d point2 = new Point3d(0, bound, 0);
		Point3d[] points = {point1, point2};
		
		LineArray axis = new LineArray(2, LineArray.COORDINATES);
		axis.setCoordinates(0, points);
		
		Color3f green = new Color3f(0f, 1f, 0f);

		Appearance app = new Appearance();
		ColoringAttributes colorAttr = new ColoringAttributes(green,	ColoringAttributes.NICEST);
		app.setLineAttributes(lineAttrAxis);
		app.setColoringAttributes(colorAttr);
		
		return new Shape3D(axis, app);
	}

	@Override
	/**
	 * Returns a <code>Shape3D</code> object of the z axis. Color is by default blue.
	 * 
	 * @return the <code>Shape3D</code> object of the z axis
	 */
	public Shape3D getZAxisShape() {
		
		Point3d point1 = new Point3d(0, 0, -bound);
		Point3d point2 = new Point3d(0, 0, bound);
		Point3d[] points = {point1, point2};
		
		LineArray axis = new LineArray(2, LineArray.COORDINATES);
		axis.setCoordinates(0, points);
		
		Color3f blue = new Color3f(0f, 0f, 1f);

		Appearance app = new Appearance();
		ColoringAttributes colorAttr = new ColoringAttributes(blue,	ColoringAttributes.NICEST);
		app.setLineAttributes(lineAttrAxis);
		app.setColoringAttributes(colorAttr);
		
		return new Shape3D(axis, app);
	}
	
	@Override
	/**
	 * Get Grid with defined Appearance
	 * 
	 * @return the <code>Shape3D</code> object of the grid lines
	 */
	public Shape3D getGridShape() {
		
		Color3f black = new Color3f(0f, 0f, 0f);

		Appearance appGrid = new Appearance();
		ColoringAttributes colorAttrGrid = new ColoringAttributes(black, ColoringAttributes.NICEST);
		LineAttributes lineAttrGrid = new LineAttributes(0.2f, LineAttributes.PATTERN_SOLID, true);
		TransparencyAttributes transpAttrGrid = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.7f);
		appGrid.setLineAttributes(lineAttrGrid);
		appGrid.setColoringAttributes(colorAttrGrid);
		appGrid.setTransparencyAttributes(transpAttrGrid);
		
		return new Shape3D(getGrid(), appGrid);
	}
	
	private LineArray getGrid() {

		LineArray grid = new LineArray(3 * 4 * scaleMax, LineArray.COORDINATES);

		grid.setCoordinates(0, getGridSection(GridSection.XY));
		grid.setCoordinates(4 * scaleMax, getGridSection(GridSection.XZ));
		grid.setCoordinates(8 * scaleMax, getGridSection(GridSection.YZ));

		return grid;
	}
	
	private Point3d[] getGridSection(GridSection gridSection) {

		Point3d[] points = new Point3d[4 * scaleMax];

		switch (gridSection) {
		case XY:
			for (int i = 0, k = 1; i < points.length; i += 4, k++) {
				points[i] = new Point3d(k * offset, -bound, 0.0f);
				points[i + 1] = new Point3d(k * offset, bound, 0.0f);
				points[i + 2] = new Point3d(-bound, k * offset, 0.0f);
				points[i + 3] = new Point3d(bound, k * offset, 0.0f);
			}
			break;
		case XZ:
			for (int i = 0, k = 1; i < points.length; i += 4, k++) {
				points[i] = new Point3d(k * offset, 0.0f, -bound);
				points[i + 1] = new Point3d(k * offset, 0.0f, bound);
				points[i + 2] = new Point3d(-bound, 0.0f, k * offset);
				points[i + 3] = new Point3d(bound, 0.0f, k * offset);
			}
			break;
		case YZ:
			for (int i = 0, k = 1; i < points.length; i += 4, k++) {
				points[i] = new Point3d(0.0f, k * offset, -bound);
				points[i + 1] = new Point3d(0.0f, k * offset, bound);
				points[i + 2] = new Point3d(0.0f, bound, k * offset);
				points[i + 3] = new Point3d(0.0f, -bound, k * offset);
			}
			break;
		}
		return points;
	}
}

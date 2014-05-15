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

import java.math.BigDecimal;

import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.interfaces.Line;
import ch.hsr.i.jvector.interfaces.Plane;
import ch.hsr.i.jvector.interfaces.Point;

public class Point3D extends Component3D implements Point {
	
	private Point3d point = new Point3d();
	private Shape3D shape;
	
	public Point3D(Point3d point) { 
		
		super(ComponentType.POINT);
		
		this.point = point;
		
		PointArray pa = new PointArray(1, PointArray.COORDINATES);
		pa.setCoordinate(0, this.point);
		shape = new Shape3D(pa);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}

	@Override
	public Point3d getPoint() {
		return point;
	}
	
	@Override
	/**
	 * Get <code>Shape3D</code> object with default appearance of this point
	 * 
	 * @return <code>Shape3D</code> object of this point
	 */
	public Shape3D getShape(AppearanceType type) {
		
		shape.setAppearance(ComponentAppearance.getAppearance(ComponentType.POINT, type));
		aType = type;
		
		return shape;
	}
	
	@Override
	public void setAppearance(AppearanceType type) {
		shape.setAppearance(ComponentAppearance.getAppearance(ComponentType.POINT, type));
		aType = type;
	}
	
	@Override
	/**
	 * Returns true if the Object o is of type Point3D and all of its point data members (x, y, z) are
	 * equal to the corresponding data members in this Point3D.
	 * 
	 * @override equals in class <code>java.lang.Object</code>
	 * @param <code>o</code> - the Object with which the comparison is made 
	 * @return true or false
	 */
	public boolean equals(Object o) {
		Point3D p;
		try {
			p = (Point3D) o;
		} catch (ClassCastException e) {
			return false;
		}
		return point.equals(p.getPoint());
	}

	@Override
	public double getDistance(Point point) {
		Point3d p2 = point.getPoint();
		Vector3d d = new Vector3d(p2.x - this.point.x, p2.y - this.point.y, p2.z - this.point.z);
		return round(d.length());
	}

	@Override
	public double getDistance(Line line) {
		return line.getDistance(this);
	}

	@Override
	public double getDistance(Plane plane) {
		return plane.getDistance(this);
	}
	
	private double round(double value) {
		BigDecimal bg = new BigDecimal(value);
		bg = bg.setScale(3, BigDecimal.ROUND_HALF_UP);
		return bg.doubleValue();
	}
}

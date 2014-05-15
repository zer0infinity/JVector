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

package ch.hsr.i.jvector.interfaces;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.exceptions.NoIntersectionException;

public interface Line extends Component {
	
	/**
	 * Returns an Array of the two Point3d objects which define the line
	 * 
	 * @return Point3d Array which define the line
	 */
	public Point3d[] getPoints();
	
	/**
	 * Returns the parameters for this line. The returned array contains of 6 elements, whereas the
	 * first three are a point on the line and second three show the direction of the line.
	 * 
	 * @return the parameters for this line 
	 */
	public double[] getLineParameters();
		
	/**
	 * Returns the Shape3D object of this line. Note that this shape object is implicit
	 * coupled with its appearance.
	 * 
	 * @return the Shape3D object of this line
	 */
	public Shape3D getShape(AppearanceType type);
	
	/**
	 * Returns the vector of the direction of this line.
	 * 
	 * @return the vector pointing in the direction of this line
	 */
	public Vector3d getDirection();
	
	/**
	 * Returns a point on this line.
	 * 
	 * @return the point on this line
	 */
	public Point3d getPointOnLine();
	
	/**
	 * Computes the intersection of this and the given line.
	 * 
	 * @param the line with which to compute the intersection
	 * @throws NoIntersectionException in case the lines have no intersection
	 * @return the intersection point
	 */
	public Point getIntersection(Line line) throws NoIntersectionException;
	
	/**
	 * Computes the intersection of this line and the given plane.
	 * 
	 * @param the plane with which to compute the intersection
	 * @throws NoIntersectionException in case the components have no intersection
	 * @return the intersection point
	 */
	public Point getIntersection(Plane plane) throws NoIntersectionException;
	
	/**
	 * Returns the angle in radians between this line and the line parameter; the return value
	 * is constrained to the range [0,PI].
	 * 
	 * @param the line with which to compute the angle
	 * @return the angle in radians in the range [0,PI]
	 */
	public double getAngle(Line line);
	
	/**
	 * Returns the angle in radians between this line and the plane parameter; the return value
	 * is constrained to the range [0,PI].
	 * 
	 * @param the plane with which to compute the angle
	 * @return the angle in radians in the range [0,PI]
	 */
	public double getAngle(Plane plane);
	
	/**
	 * Returns the distance between this line and the point parameter. 
	 * 
	 * @return the distance between this line and the given point
	 */
	public double getDistance(Point point);
	
	

}

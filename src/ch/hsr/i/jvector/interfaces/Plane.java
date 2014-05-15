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
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.exceptions.NoIntersectionException;

public interface Plane extends Component {
	
	/**
	 * Returns the parameters for this plane. The returned array contains of 4 elements, whereas these
	 * elements are equal to  the parameters of Ax + By + Cz + D = 0 > [A, B, C, D].
	 * 
	 * @return the parameters for this plane 
	 */
	public double[] getPlaneParameters();
	
	/**
	 * Returns the Shape3D object of this plane. Note that this shape object is implicit
	 * coupled with its appearance.
	 * 
	 * @return the Shape3D object of this plane
	 */
	public Shape3D getShape(AppearanceType type);
	
	/**
	 * Returns the normal vector to this plane. 
	 * 
	 * @return the normal vector to this plane
	 */
	public Vector3d getNormalVector();

	/**
	 * Returns the intersection of this plane and the given line.
	 * 
	 * @param the line with which to compute the intersection
	 * @throws NoIntersectionException in case the components have no intersection
	 * @return the intersection point
	 */
	public Point getIntersection(Line line) throws NoIntersectionException;
	
	/**
	 * Returns the line of intersection of this plane and the given plane.
	 * 
	 * @param the plane with which to compute the line of intersection
	 * @throws NoIntersectionException in case the components have no line of intersection
	 * @return the line of intersection
	 */
	public Line getIntersection(Plane plane) throws NoIntersectionException;
	
	/**
	 * Returns the angle in radians between this plane and the plane parameter; the return value
	 * is constrained to the range [0,PI].
	 * 
	 * @param the plane with which to compute the angle
	 * @return the angle in radians in the range [0,PI]
	 */
	public double getAngle(Plane plane);
	
	/**
	 * Returns the angle in radians between this plane and the line parameter; the return value
	 * is constrained to the range [0,PI].
	 * 
	 * @param the line with which to compute the angle
	 * @return the angle in radians in the range [0,PI]
	 */
	public double getAngle(Line line);
	
	/**
	 * Returns the distance between this plane and the point parameter. 
	 * 
	 * @return the distance between this plane and the given point
	 */
	public double getDistance(Point point);
}

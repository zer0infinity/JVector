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

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;

public interface Point extends Component {
	
	
	@Override
	/**
	 * Indicates whether some other object is "equal to" this point.
	 * 
	 * @return true if this point is the same as the point argument; false otherwise
	 */
	public boolean equals(Object point);
	
	/**
	 * Returns the Shape3D object of this point. Note that this shape object is implicit
	 * coupled with its appearance.
	 * 
	 * @return the Shape3D object of this point
	 */
	public Shape3D getShape(AppearanceType type);
	
	/**
	 * Returns the <code>Point3d</code> object of this point.
	 * 
	 * @return the <code>Point3d</code> object of this point
	 */
	public Point3d getPoint();
	
	/**
	 * Returns the distance between this point and the point parameter. 
	 * 
	 * @return the distance between this and the given point
	 */
	public double getDistance(Point point);
	
	/**
	 * Returns the distance between this point and the line parameter. 
	 * 
	 * @return the distance between this point and the given line
	 */
	public double getDistance(Line line);
	
	/**
	 * Returns the distance between this point and the plane parameter. 
	 * 
	 * @return the distance between this point and the given plane
	 */
	public double getDistance(Plane plane);
	

}

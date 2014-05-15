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
import java.util.Arrays;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.exceptions.NoIntersectionException;
import ch.hsr.i.jvector.exceptions.NoLineException;
import ch.hsr.i.jvector.exceptions.NoMatrixException;
import ch.hsr.i.jvector.exceptions.NoPlaneException;
import ch.hsr.i.jvector.exceptions.NoPointOnSphereException;
import ch.hsr.i.jvector.exceptions.NoIntersectionException.StateNoIntersection;
import ch.hsr.i.jvector.exceptions.NoPlaneException.StateNoPlane;
import ch.hsr.i.jvector.interfaces.Line;
import ch.hsr.i.jvector.interfaces.Plane;
import ch.hsr.i.jvector.interfaces.Point;
import ch.hsr.i.jvector.util.Matrix;
import ch.hsr.i.jvector.util.Utils;

public class Plane3D extends Component3D implements Plane {
	
	/**
	 * Stores the parameters for this plane in the form: Ax + Bx + Cz + D = 0 > [A, B, C, D].
	 */
	private double[] plane = new double[4];
	
	/**
	 * the points for this plane on the sphere
	 */
	private Point3d[] points = new Point3d[4];
	
	/**
	 * the shape object of this plane
	 */
	private Shape3D shape;
	
	/**
	 * Creates a plane from a point on the plane and two direction vectors. Notice vector1 and vector2 have to
	 * be linear independent.
	 * 
	 * @param point - the point on the plane
	 * @param vector1 - the first direction vector
	 * @param vector2 - the second direction vector
	 * @throws NoPlaneException in case the plane could not be created
	 */
	public Plane3D(Point3d point, Vector3d vector1, Vector3d vector2) throws NoPlaneException {
		
		super(ComponentType.PLANE);
		
		if( Utils.isLinearDependent(vector1, vector2) ) {
			throw new NoPlaneException(StateNoPlane.NO_LINEAR_INDEP_DIR_VECTORS);
		}
		
		Vector3d crossP = new Vector3d();
		crossP.cross(vector1, vector2);
		crossP.normalize();
		
		plane[0] = crossP.x;
		plane[1] = crossP.y;
		plane[2] = crossP.z;
		plane[3] = -(point.x * crossP.x + point.y * crossP.y + point.z * crossP.z);
		
		Point3d randomPoint1 = new Point3d( 1, 1, -((plane[0] + plane[1] + plane[3]) / plane[2]));
		Point3d randomPoint2 = new Point3d( 2, 2, -((2 * plane[0] + 2 * plane[1] + plane[3]) / plane[2]));
		points = calculate4Points(randomPoint1, randomPoint2, 0.07);
		
		QuadArray plane = new QuadArray(4, QuadArray.COORDINATES);
		plane.setCoordinates(0, points);
		shape = new Shape3D(plane);
		shape.setAppearanceOverrideEnable(true);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}
	
	/**
	 * Creates a plane form a normal vector and a constant value <code>lambda</code>. Note
	 * <code>lambda</code> is thought to be on the left side of the equation. > Ax + By + Cz + D = 0.
	 * 
	 * @param vector - the normal vector of the plane
	 * @param lambda - the constant which defines the position of the plane
	 * @throws NoPlaneException in case the plane could not be created
	 */
	public Plane3D(Vector3d vector, double lambda) throws NoPlaneException {
		
		super(ComponentType.PLANE);
		
		if(vector.length() == 0.0) {
			throw new NoPlaneException(StateNoPlane.NO_NORMAL_VECTOR);
		}
		
		plane[0] = vector.x;
		plane[1] = vector.y;
		plane[2] = vector.z;
		plane[3] = -lambda;
		
		Point3d randomPoint1 = new Point3d( 1, 1, -((plane[0] + plane[1] + plane[3]) / plane[2]));
		Point3d randomPoint2 = new Point3d( 2, 2, -((2 * plane[0] + 2 * plane[1] + plane[3]) / plane[2]));
		points = calculate4Points(randomPoint1, randomPoint2, 0.07);
		
		QuadArray plane = new QuadArray(4, QuadArray.COORDINATES);
		plane.setCoordinates(0, points);
		shape = new Shape3D(plane);
		shape.setAppearanceOverrideEnable(true);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}
	
	/**
	 * Creates a plane form three points which defines the plane.
	 * 
	 * @param point 1
	 * @param point 2
	 * @param point 3
	 * @throws NoPlaneException in case the plane could not be created
	 */
	public Plane3D(Point3d p1, Point3d p2, Point3d p3) throws NoPlaneException {
		
		super(ComponentType.PLANE);
		
		if(p1.equals(p2) || p1.equals(p3) || p2.equals(p3) ) {
			throw new NoPlaneException(StateNoPlane.SAME_POINT_TWICE);
		}
		
		Vector3d vector1 = new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Vector3d vector2 = new Vector3d(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);
		
		if( Utils.isLinearDependent(vector1, vector2) ) {
			throw new NoPlaneException(StateNoPlane.NO_LINEAR_INDEP_DIR_VECTORS);
		}
		
		Vector3d crossP = new Vector3d();
		crossP.cross(vector1, vector2);
		crossP.normalize();
		
		plane[0] = crossP.x;
		plane[1] = crossP.y;
		plane[2] = crossP.z;
		plane[3] = -(p1.x * crossP.x + p1.y * crossP.y + p1.z * crossP.z);
		
		points = calculate4Points(p1, p2, 0.07);
		
		QuadArray plane = new QuadArray(4, QuadArray.COORDINATES);
		plane.setCoordinates(0, points);
		shape = new Shape3D(plane);
		shape.setAppearanceOverrideEnable(true);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		
		//TODO: if method calculate4Points() is ok replace.
		//calculate the points on the sphere
//		try {
//			Point3d[] points1 = Utils.getPointsOnSphere(p1, p2, 0.07);
//			points[0] = points1[0];
//			points[2] = points1[1];
//		} catch (NoPointOnSphereException e) {
//			// TODO: exception handling when points not on sphere
//		}
//		
//		Vector3d crossP2 = new Vector3d();
//		crossP2.cross(vector1, new Vector3d(p2));
//		
//		Point3d tmpPoint = new Point3d(p1.x + crossP2.x, p1.y + crossP2.y, p1.z + crossP2.z);
//		
//		try {
//			Point3d[] points2 = Utils.getPointsOnSphere(p1, tmpPoint, 0.07);
//			points[1] = points2[0];
//			points[3] = points2[1];			
//		} catch (NoPointOnSphereException e) {
//			// TODO exception handling when points not on sphere
//		}		
	}
	
	@Override
	public double[] getPlaneParameters() {
		return plane;
	}
	
	@Override
	/**
	 * Get plane with default appearance.
	 * 
	 * @return a <code>Shape3D</code> object
	 */
	public Shape3D getShape(AppearanceType type) {
		
		shape.setAppearance(ComponentAppearance.getAppearance(ComponentType.PLANE, type));
		aType = type;
		
		return shape;
	}
	
	@Override
	public void setAppearance(AppearanceType type) {
		shape.setAppearance(ComponentAppearance.getAppearance(ComponentType.PLANE, type));
		aType = type;
	}
	
	@Override
	public Vector3d getNormalVector() {
		Vector3d normalVector = new Vector3d(plane[0], plane[1], plane[2]);
		return normalVector;
	}

	@Override
	public double getAngle(Plane plane) {
		
		Vector3d nE1 = getNormalVector();
		Vector3d nE2 = plane.getNormalVector();
		
		nE1.normalize();
		nE2.normalize();
		
		double phi = nE1.angle(nE2);
		
		return round(phi);
	}

	@Override
	public double getAngle(Line line) {
		
		Vector3d nE1 = getNormalVector();
		Vector3d lineDir = line.getDirection();
		
		double phi = (Math.PI / 2) - nE1.angle(lineDir);
		
		return round(phi);
	}

	@Override
	public double getDistance(Point point) {
		
		Point3d p = point.getPoint();
		
		double d = Math.abs( (plane[0] * p.x + plane[1] * p.y + plane[2] * p.z + plane[3]) / Math.sqrt(Math.pow(plane[0], 2) + Math.pow(plane[1],2) + Math.pow(plane[2],2)) ); 
		
		return round(d);
	}

	@Override
	public Point getIntersection(Line line) throws NoIntersectionException {
		
		double[] lineParams = line.getLineParameters();
		double t = 0.0;
		
		double numerator = (- plane[3] - (new Vector3d(lineParams[0], lineParams[1], lineParams[2])).dot(getNormalVector()) );
		double denominator = line.getDirection().dot(getNormalVector());
		
		if(denominator == 0.0 && numerator == 0.0) {
			throw new NoIntersectionException(StateNoIntersection.LINE_IN_PLANE);
		}
		else if(denominator == 0.0) {
			throw new NoIntersectionException(StateNoIntersection.PARALLEL);
		} else {
			t = numerator / denominator;
		}
		
		double x = lineParams[0] + t * lineParams[3];
		double y = lineParams[1] + t * lineParams[4];
		double z = lineParams[2] + t * lineParams[5];
		
		return new Point3D(new Point3d(x, y, z));
	}

	@Override
	public Line getIntersection(Plane plane) throws NoIntersectionException {

		if(Utils.isLinearDependent(getNormalVector(), plane.getNormalVector())) {
			if(this.plane[3] == plane.getPlaneParameters()[3]) {
				throw new NoIntersectionException(StateNoIntersection.CONGRUENT);
			} else {
				throw new NoIntersectionException(StateNoIntersection.PARALLEL);
			}
		}
		
		// direction of the intersection line
		Vector3d direction = new Vector3d();
		direction.cross(getNormalVector(), plane.getNormalVector());
		
		// point on the intersection line
		double[][] system = new double[2][4];
		system[0] = this.plane;
		system[1] = plane.getPlaneParameters();
		
		Matrix m = null;
		
		try {
			m = new Matrix(system);
		} catch (NoMatrixException e) {
			e.printStackTrace();
		}
		
		m = Utils.rref(m);
		
		Vector3d point = new Vector3d(new Point3d(m.getValue(0, 3), m.getValue(1, 3), 0));
		
		Line3D intersection = null;
		
		try {
			intersection = new Line3D(point, direction);
		} catch (NoLineException e) {
			e.printStackTrace();
		}
		
		return intersection;
	}
	
	@Override
	public boolean equals(Object o) {
		Plane3D plane;
		try {
			plane = (Plane3D)o;
		} catch (ClassCastException e) {
			return false;
		}
		return Arrays.equals(plane.getPlaneParameters(), this.plane);
	}
	
	private Point3d[] calculate4Points(Point3d p1, Point3d p2, double scale) {
		
		Point3d[] points = new Point3d[4];
		
		//calculate the points on the sphere
		try {
			Point3d[] points1 = Utils.getPointsOnSphere(p1, p2, scale);
			points[0] = points1[0];
			points[2] = points1[1];
		} catch (NoPointOnSphereException e) {
			// TODO: exception handling when points not on sphere
		}
		
		Vector3d v1 = new Vector3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Vector3d crossP2 = new Vector3d();
		crossP2.cross(v1, new Vector3d(p2));
		
		Point3d tmpPoint = new Point3d(p1.x + crossP2.x, p1.y + crossP2.y, p1.z + crossP2.z);
		
		try {
			Point3d[] points2 = Utils.getPointsOnSphere(p1, tmpPoint, scale);
			points[1] = points2[0];
			points[3] = points2[1];			
		} catch (NoPointOnSphereException e) {
			// TODO exception handling when points not on sphere
		}	
		
		return points;		
	}
	
	private double round(double value) {
		BigDecimal bg = new BigDecimal(value);
		bg = bg.setScale(3, BigDecimal.ROUND_HALF_UP);
		return bg.doubleValue();
	}
}

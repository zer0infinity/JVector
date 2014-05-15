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

import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.exceptions.NoIntersectionException;
import ch.hsr.i.jvector.exceptions.NoLineException;
import ch.hsr.i.jvector.exceptions.NoMatrixException;
import ch.hsr.i.jvector.exceptions.NoPointOnSphereException;
import ch.hsr.i.jvector.exceptions.NoIntersectionException.StateNoIntersection;
import ch.hsr.i.jvector.exceptions.NoLineException.StateNoLine;
import ch.hsr.i.jvector.interfaces.Line;
import ch.hsr.i.jvector.interfaces.Plane;
import ch.hsr.i.jvector.interfaces.Point;
import ch.hsr.i.jvector.util.Matrix;
import ch.hsr.i.jvector.util.Utils;

public class Line3D extends Component3D implements Line {

	/**
	 * Stores the parameters for this line in the form: [x;y;z] + k * [rx;ry;rz] > [x,y,z,rx,ry,rz].
	 */
	private double[] line = new double[6]; 
	
	/**
	 * the points for this line on the sphere
	 */
	private Point3d[] points = new Point3d[2];
	
	/**
	 * the shape object of this line
	 */
	private Shape3D shape;
	
	/**
	 * Creates a Line3D object with the two points.
	 * 
	 * @param p1 - a point on the line
	 * @param p2 - a point on the line
	 * @throws NoLineException in case the line could not be created
	 */
	public Line3D(Point3d p1, Point3d p2) throws NoLineException {
		
		super(ComponentType.LINE);
		
		if(p1.equals(p2)) {
			throw new NoLineException(StateNoLine.SAME_POINT_TWICE);
		}
		
		try {
			points = Utils.getPointsOnSphere(p1, p2, 0);
		} catch (NoPointOnSphereException e) {
			throw new NoLineException(StateNoLine.OTHER_REASON, e.getMessage());
		}
		
		// point on the line
		line[0] = p1.x;
		line[1] = p1.y;
		line[2] = p1.z;
		
		// direction vector
		line[3] = p2.x - p1.x;
		line[4] = p2.y - p1.y;
		line[5] = p2.z - p2.z;
		
		LineArray line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinates(0, points);
		shape = new Shape3D(line);
		shape.setAppearanceOverrideEnable(true);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}
	
	/**
	 * Creates a Line3D object with the two vectors.
	 * 
	 * @param p - the vector to a point on the line
	 * @param d - the direction of the line 
	 * @throws NoLineException in case the line could not be created
	 */
	public Line3D(Vector3d p, Vector3d d) throws NoLineException {
		
		super(ComponentType.LINE);
		
		if(Utils.isLinearDependent(p, d)) {
			throw new NoLineException(StateNoLine.NO_LINEAR_INDEP_DIR_VECTORS);
		}
		
		// two points on the line
		Point3d p1 = new Point3d(p.x, p.y, p.z);
		Point3d p2 = new Point3d(p.x + d.x, p.y + d.y, p.z + d.z);
		
		try {
			points = Utils.getPointsOnSphere(p1, p2, 0);
		} catch (NoPointOnSphereException e) {
			throw new NoLineException(StateNoLine.OTHER_REASON, e.getMessage());
		}
		
		// point on the line
		line[0] = p.x;
		line[1] = p.y;
		line[2] = p.z;
		
		// direction vector
		line[3] = d.x;
		line[4] = d.y;
		line[5] = d.z;
		
		LineArray line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinates(0, points);
		shape = new Shape3D(line);
		shape.setAppearanceOverrideEnable(true);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	}

	@Override
	public Point3d[] getPoints() {
		return points;
	}
	
	@Override
	public double[] getLineParameters() {
		return line;
	}
	
	@Override
	public Shape3D getShape(AppearanceType type) {	
		shape.setAppearance(ComponentAppearance.getAppearance(ComponentType.LINE, type));
		aType = type;
		return shape;
	}
	
	@Override
	public void setAppearance(AppearanceType type) {
		shape.setAppearance(ComponentAppearance.getAppearance(ComponentType.LINE, type));
		aType = type;
	}
	
	@Override
	public Vector3d getDirection() {
		double x = line[3];
		double y = line[4];
		double z = line[5];
		
		return new Vector3d(x, y, z);
	}
	
	@Override
	public Point3d getPointOnLine() {
		double x = line[0];
		double y = line[1];
		double z = line[2];
		
		return new Point3d(x, y, z);
	}
	
	@Override
	/**
	 * Returns the angle in radians between this line and the line parameter; the return value is constrained to the range [0,PI].
	 * 
	 * @param the other line
	 * @return the angle in radians in the range [0,PI]
	 */
	public double getAngle(Line line) {
		
		Vector3d v1 = getDirection();
		Vector3d v2 = line.getDirection();
		
		return round(v1.angle(v2));
	}

	@Override
	public double getAngle(Plane plane) {		
		return plane.getAngle(this);
	}

	@Override
	public double getDistance(Point point) {
		
		// point
		Vector3d q = new Vector3d(point.getPoint());
		
		// line vectors
		Vector3d p = new Vector3d(points[0]);
		Vector3d u = getDirection();
		
		q.sub(p);
		
		// ! note the step above! p is subtracted from q, so q is not the original vector anymore!!
		double lambda = q.dot(u) / u.dot(u);
		
		double x = p.x + lambda * u.x;
		double y = p.y + lambda * u.y;
		double z = p.z + lambda * u.z;
		
		Vector3d r = new Vector3d(x, y, z);
		return round(r.length());
	}
	
	@Override
	/**
	 * Computes the intersection of this and the given line.
	 * 
	 * @param the line with which to compute the intersection
	 * @throws NoIntersectionException in case the lines have no intersection
	 * @return the intersection point
	 */
	public Point getIntersection(Line line) throws NoIntersectionException {
		
		if(Utils.isLinearDependent(getDirection(), line.getDirection())) {
			if(getPointOnLine().equals(line.getPointOnLine())) {
				throw new NoIntersectionException(StateNoIntersection.COLLINEAR);
			} else {
				throw new NoIntersectionException(StateNoIntersection.PARALLEL);
			}
		}
		
		Vector3d v1 = new Vector3d(getPointOnLine());
		v1.sub(new Vector3d(line.getPointOnLine()));
		
		Vector3d v2 = new Vector3d();
		v2.cross(getDirection(), line.getDirection());
		
		if(v1.dot(v2) != 0) {
			throw new NoIntersectionException(StateNoIntersection.SKEW);
		}
		
		// CALCULATE INTERSECTION
		double[] l1 = getLineParameters();
		double[] l2 = line.getLineParameters();
		
		double[][] system = new double[3][3];
		
		// first equation
		system[0][0] = l1[3];
		system[0][1] = -l2[3];
		system[0][2] = l2[0] - l1[0];
		
		// second equation
		system[1][0] = l1[4];
		system[1][1] = -l2[4];
		system[1][2] = l2[1] - l1[1];
		
		// third equation
		system[2][0] = l1[5];
		system[2][1] = -l2[5];
		system[2][2] = l2[2] - l1[2];
		
		Matrix m = null;
		
		try {
			m = new Matrix(system);
		} catch (NoMatrixException e) {
			e.printStackTrace();
		}
		
		m = Utils.rref(m);
		
		double factor = m.getValue(0, 2);
		
		double x = l1[0] + factor * l1[3];
		double y = l1[1] + factor * l1[4];
		double z = l1[2] + factor * l1[5];
		
		return new Point3D(new Point3d(x, y, z));
	}

	@Override
	public Point getIntersection(Plane plane) throws NoIntersectionException {
		return plane.getIntersection(this);
	}
	
	@Override
	public boolean equals(Object o) {
		Line3D line;
		try {
			line = (Line3D)o;
		} catch (ClassCastException e) {
			return false;
		}
		return Arrays.equals(line.getLineParameters(), this.line);
	}
	
	private double round(double value) {
		BigDecimal bg = new BigDecimal(value);
		bg = bg.setScale(3, BigDecimal.ROUND_HALF_UP);
		return bg.doubleValue();
	}
}

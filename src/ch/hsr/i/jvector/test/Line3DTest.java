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

package ch.hsr.i.jvector.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.hsr.i.jvector.components.Line3D;
import ch.hsr.i.jvector.components.Point3D;
import ch.hsr.i.jvector.exceptions.NoIntersectionException;
import ch.hsr.i.jvector.exceptions.NoLineException;
import ch.hsr.i.jvector.exceptions.NoIntersectionException.StateNoIntersection;
import ch.hsr.i.jvector.interfaces.Point;

public class Line3DTest {
	
	// POINT
	private static Point point;
	
	// INTERSECTING LINES
	private static Line3D intersecLine1;
	private static Line3D intersecLine2;
	private static Point3D intersection;
	
	// NOT INTERSECTION LINES
	private static Line3D notIntersecLineSkew1;
	private static Line3D notIntersecLineSkew2;
	
	private static Line3D notIntersecLineCollinear1;
	private static Line3D notIntersecLineCollinear2;
	
	private static Line3D notIntersecLineParallel1;
	private static Line3D notIntersecLineParallel2;
	
	@BeforeClass
	public static void setUp() {
		
		point = new Point3D(new Point3d(2.0, 4.0, 5.0));
		
		try{
			// intersecting lines - intersection: (10/3, 16/3, 0)
			intersecLine1 = new Line3D(new Vector3d(2, 7, 1), new Vector3d(4, -5, -3));
			intersecLine2 = new Line3D(new Vector3d(4, 4, 4), new Vector3d(-1, 2, -6));
			intersection = new Point3D(new Point3d((10.0 / 3.0), (16.0 / 3.0), 0.0));
			
			// not intersecting lines - reason: SKEW
			notIntersecLineSkew1 = new Line3D(new Vector3d(3, 5, 4), new Vector3d(4, 6, 4));
			notIntersecLineSkew2 = new Line3D(new Vector3d(8, 2, 7), new Vector3d(3, 1, 5));
			
			// not intersecting lines - reason: COLLINEAR
			notIntersecLineCollinear1 = new Line3D(new Vector3d(3, 5, 4), new Vector3d(4, 6, 4));
			notIntersecLineCollinear2 = new Line3D(new Vector3d(3, 5, 4), new Vector3d(8, 12, 8));
			
			// not intersecting lines - reason: PARALLEL
			notIntersecLineParallel1 = new Line3D(new Vector3d(3, 5, 4), new Vector3d(4, 6, 4));
			notIntersecLineParallel2 = new Line3D(new Vector3d(8, 2, 7), new Vector3d(4, 6, 4));
			
			//not intersecting
		} catch (NoLineException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDirectionVector() {
		assertEquals(new Vector3d(4.0, -5.0, -3.0), intersecLine1.getDirection());
	}

	@Test
	public void getIntersectionLineLine() {
		try {
			assertEquals(intersection, intersecLine1.getIntersection(intersecLine2));
		} catch (NoIntersectionException e) {
			fail("The method getIntersection(line) threw an exception but expected this test to pass.");
		}
	}
	
	@Test
	public void getIntersectionLineLineSkew() {
		try {
			Point p = notIntersecLineSkew1.getIntersection(notIntersecLineSkew2);
			System.out.println(p);
		} catch (NoIntersectionException e) {
			assertEquals(StateNoIntersection.SKEW, e.getState());
		}
	}
	
	@Test
	public void getIntersectionLineLineCollinear() {
		try {
			Point p = notIntersecLineCollinear1.getIntersection(notIntersecLineCollinear2);
			System.out.println(p);
		} catch (NoIntersectionException e) {
			assertEquals(StateNoIntersection.COLLINEAR, e.getState());
		}
	}
	
	@Test
	public void getIntersectionLineLineParallel() {
		try {
			Point p = notIntersecLineParallel1.getIntersection(notIntersecLineParallel2);
			System.out.println(p);
		} catch (NoIntersectionException e) {
			assertEquals(StateNoIntersection.PARALLEL, e.getState());
		}
	}

	public void testGetAngle() {
		fail("Not yet implemented");
	}
}

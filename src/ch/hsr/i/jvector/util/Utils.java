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

package ch.hsr.i.jvector.util;

import java.math.BigDecimal;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.exceptions.NoPointOnSphereException;
import ch.hsr.i.jvector.exceptions.NoPointOnSphereException.StateNoPointOnSphere;
import ch.hsr.i.jvector.logic.DrawComponents;

public class Utils {
	
	/**
	 * Returns a new <code>Matrix</code> object in the row reduced echelon form.
	 * 
	 * @param the matrix to be reduced
	 * @return the row reduced form of the matrix
	 */
	public static Matrix rref(Matrix matrix) {
		
		Matrix a = matrix;
		
		// matrix dimension
		int m = a.getNumRows();
		int n = a.getNumColumns();
		int pivot = 0;
		
		// LOWER TRIANGULAR FORM
		for(int i = 0, j = 0; i < n && i < m; i++) {
			
			pivot = i;
						
			// pivoting
			j = i;			
			j = a.getIndexMaxValueInColumn(i, i);
			
			// if the max value in column i is 0 then the pivot would be 0 > continue
			if(isZero(j)) {
				continue;
			}
			
            // in case rows should be changed due to pivoting
            if (i != j) {
            	double[] currRowI = a.getRowValues(i);
            	double[] currRowJ = a.getRowValues(j);
            	
            	a.setRowValues(i, currRowJ);
            	a.setRowValues(j, currRowI);    	
            }
            
            // eliminate
			for (j = i + 1; j < m; j++) {
				
				double b = a.getValue(j, i) / a.getValue(i, i);
				
				for (int k = 0; k < n; k++) {
					a.setValue(j, k, a.getValue(j, k) - b * a.getValue(i, k));
				}
			}
		}
			
		// UPPER TRIANGULAR STEP
		for(int i = pivot; i >= 0; i--) {
			
            // eliminate
			for (int j = i - 1; j >= 0; j--) {
				
				// test if pivot position is 0
				if(isZero(a.getValue(i, i))) {
					continue;
				} else { 
					double b = a.getValue(j, i) / a.getValue(i, i);
					
					for (int k = 0; k < n; k++) {
						a.setValue(j, k, a.getValue(j, k) - b * a.getValue(i, k));
					}
				}
			}
		}
		
		// MAKE 1's IN PIVOT POSITIONS
		for(int i = 0; i < m && i < n; i++) {
			
			if(isZero(a.getValue(i, i))) {
				continue;
			} else {
				double[] row = a.getRowValues(i);
				for(int j = 0; j < n; j++) {
					row[j] = row[j] / a.getValue(i, i);
				}
				a.setRowValues(i, row);
			}
		}
		
		return a;
	}
	
	/**
	 * Returns true if the double parameter is 0 or almost zero. This method creates
	 * a <code>BigDeciaml</code> object of the double parameter value and sets its scale
	 * to 15, rounding mode ROUND_HALF_UP. The new double value is then checked for 0.
	 * 
	 * @param the double value to be checked
	 * @return true if the parameter value is 0 else false
	 */
	private static boolean isZero(double d) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(15, BigDecimal.ROUND_HALF_UP);
		
		return bd.doubleValue() == 0.0;
	}

	/**
	 * Solves the given system of linear equations an returns a solution vector.
	 * Note that the matrix must be in augmented form for this reason the number
	 * of rows + 1 must be equal to the number of columns.
	 * 
	 * @param the system to be solved
	 * @return the solution vector or null in case that the system is not in the
	 *         correct form
	 */
	public static double[] solveSystem(double sys[][]) {

		if (sys.length + 1 == sys[0].length) {
			
			int n = sys.length;
			double max, tmp;

			for (int i = 0, j = 0; i < n - 1; i++) {
				
				// pivoting
				j = i;
	            max = Math.abs (sys[j][i]);
	            for (int k = i + 1; k < n; k++) {
	                tmp = Math.abs (sys[k][i]);
	                if (tmp > max) {
	                    j = k;
	                    max = tmp;
	                }
	            }

	            // in case rows should be changed
	            if (i != j) {
	                for (int k = i; k <= n; ++k)
	                {
	                    tmp = sys[i][k];
	                    sys[i] [k] = sys[j][k];
	                    sys[j] [k] = tmp;
	                }
	            }
				
	            // solve
				for (j = i + 1; j < n; j++) {
					double a = sys[j][i] / sys[i][i];
					for (int k = 0; k < n + 1; k++) {
						sys[j][k] = sys[j][k] - a * sys[i][k];
					}
				}
			}

			//printSystem(sys);

			double[] res = new double[sys.length];

			for (int i = n - 1; i >= 0; i--) {
				double b = sys[i][n];
				for (int j = i + 1; j < n; j++) {
					b = b - sys[i][j] * res[j];
				}
				// TODO: Check if this round method is ok!
				res[i] = round(b / sys[i][i], 3);
			}

			return res;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns true if the two vectors are linear dependent otherwise false. This method calculates the
	 * vector product of the two given vectors and checks its length. In case the length is 0.0 the two
	 * given vectors are linear dependent otherwise not.
	 * 
	 * @param vector parameter 1
	 * @param vector parameter 2
	 * @return true if vectors are linear dependent otherwise false 
	 */
	public static boolean isLinearDependent(Vector3d vector1, Vector3d vector2) {
		
		Vector3d crossP = new Vector3d();	
		crossP.cross(vector1, vector2);
		
		return crossP.length() == 0.0; 
	}

	public static double round(double num, int significant) {
		double d = Math.log10(num);
		double power;
		if (num > 0) {
			d = Math.ceil(d);
			power = -(d - significant);
		} else {
			d = Math.floor(d);
			power = -(d - significant);
		}

		return (int) (num * Math.pow(10.0, power) + 0.5)
				* Math.pow(10.0, -power);
	}
	
	/**
	 * Returns an array of two <code>Point3D</code> objects which lie on the bounding sphere. The returned
	 * points are the intersection of the line going trough the two given points and the bounding sphere. 
	 * Note that object refers to a line or a plane in the context of the two point parameters.
	 * 
	 * @param point on the object
	 * @param point on the object
	 * @return array of two <code>Point3D</code> objects
	 * @throws NoPointOnSphereException in case the points do not intersect the sphere or the line going through them is tangential to the sphere
	 */
	public static Point3d[] getPointsOnSphere(Point3d point1, Point3d point2, double scale) throws NoPointOnSphereException {
		
		double boundingShpereRadius = DrawComponents.RADIUS;
		
		Vector3d q = new Vector3d(point1);
		Vector3d v = new Vector3d(point2.x - point1.x, point2.y - point1.y, point2.z - point1.z);
		
		
		double discriminant = Math.pow((2*q.length() * v.length()), 2) - 4 * v.lengthSquared() * (q.lengthSquared() - Math.pow(boundingShpereRadius, 2));
		
		double signum = Math.signum(discriminant);
		
		if(signum == -1.0) {
			throw new NoPointOnSphereException(StateNoPointOnSphere.OBJECT_OUT_OF_BOUNDS);
		}
		else if(signum == 0.0) {
			throw new NoPointOnSphereException(StateNoPointOnSphere.OBJECT_TANGENTIAL);
		}
		
		double lambda1 = (boundingShpereRadius - q.length()) / v.length();
		double lambda2 = -1 * lambda1;
		
		if(scale > 0 && scale < 1) {
			lambda1 *= scale;
			lambda2 *= scale;
		}
		
		double x1 = q.x + lambda1 * v.x;
		double y1 = q.y + lambda1 * v.y;
		double z1 = q.z + lambda1 * v.z;
		
		double x2 = q.x + lambda2 * v.x;
		double y2 = q.y + lambda2 * v.y;
		double z2 = q.z + lambda2 * v.z;
		
		Point3d[] points = {new Point3d(x1,y1,z1), new Point3d(x2,y2,z2)};
		
		return points;
	}

}

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

package ch.hsr.i.jvector.exceptions;


public class NoPlaneException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * defines the reasons why no plane could be created
	 */
	public enum StateNoPlane {
		NO_LINEAR_INDEP_DIR_VECTORS,
		NO_NORMAL_VECTOR,
		SAME_POINT_TWICE
	}
	
	public NoPlaneException (StateNoPlane state) {
		super(getStateMessage(state));
	}
	
	public static String getStateMessage(StateNoPlane state) {
		switch (state) {
		case NO_LINEAR_INDEP_DIR_VECTORS:
			return "No linear indep dir vectors.";
		case NO_NORMAL_VECTOR:
			return "No normal vector.";
		case SAME_POINT_TWICE:
			return "Same point twice.";
		default:
			return "Error.";
		}
	}
}

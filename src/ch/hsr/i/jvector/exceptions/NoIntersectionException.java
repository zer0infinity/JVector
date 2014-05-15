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

/**
 * This Exception is thrown when the calculation of an intersection between two
 * components has no result. The state gives more specific information why the
 * components have no intersection.
 */
public class NoIntersectionException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * defines the reasons why no intersection is found
	 */
	public enum StateNoIntersection {
		PARALLEL, SKEW, COLLINEAR, LINE_IN_PLANE, CONGRUENT
	}
	
	private StateNoIntersection state;

	public NoIntersectionException(StateNoIntersection state) {
		super(getStateMessage(state));
		this.state = state;
	}
	
	public StateNoIntersection getState() {
		return state;
	}
	
	public static String getStateMessage(StateNoIntersection state) {
		switch (state) {
		case PARALLEL:
			return "The objects are parallel.";
		case COLLINEAR:
			return "The lines are collinear.";
		case CONGRUENT:
			return "The planes are congruent. The plane itself is the solution.";
		case SKEW:
			return "The objects are skew.";
		case LINE_IN_PLANE:
			return "The line lies in the plane. The line itself is the solution.";
		default:
			return "Error.";
		}
	}
}

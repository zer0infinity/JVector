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

public class NoPointOnSphereException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * defines the reasons why no plane could be created
	 */
	public enum StateNoPointOnSphere {
		OBJECT_OUT_OF_BOUNDS,
		OBJECT_TANGENTIAL
	}
	
	public NoPointOnSphereException (StateNoPointOnSphere state) {
		super(getStateMessage(state));
	}
	
	public static String getStateMessage(StateNoPointOnSphere state) {
		switch (state) {
		case OBJECT_OUT_OF_BOUNDS:
			return "Object is out of bounds.";
		case OBJECT_TANGENTIAL:
			return "Object is tangential to the bound.";
		default:
			return "Error.";
		}
	}
	
}

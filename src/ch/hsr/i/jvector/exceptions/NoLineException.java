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


public class NoLineException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * defines the reasons why no line could be created
	 */
	public enum StateNoLine {
		SAME_POINT_TWICE,
		NO_LINEAR_INDEP_DIR_VECTORS,
		OTHER_REASON
	}
	
	public NoLineException (StateNoLine state) {
		super(getStateMessage(state));
	}
	
	public NoLineException (StateNoLine state, String message) {
		super(getStateMessage(state) + "\n" + message);
	}
	
	public static String getStateMessage(StateNoLine state) {
		switch (state) {
		case SAME_POINT_TWICE:
			return "Two different points are needed for a line.";
		case NO_LINEAR_INDEP_DIR_VECTORS:
			return "No linear indep dir vectors.";
		default:
			return "Error.";
		}
	}
}

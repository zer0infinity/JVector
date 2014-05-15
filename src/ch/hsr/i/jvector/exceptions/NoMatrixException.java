package ch.hsr.i.jvector.exceptions;

public class NoMatrixException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * defines the reasons why no intersection is found
	 */
	public enum StateNoMatrix {
		NUM_ROW_ELEMENTS_DIFF
	}

	public NoMatrixException(StateNoMatrix state) {
		super(getStateMessage(state));
	}
	
	public NoMatrixException(StateNoMatrix state, String message) {
		super(getStateMessage(state) + " " + message);
	}

	public static String getStateMessage(StateNoMatrix state) {
		switch (state) {
		case NUM_ROW_ELEMENTS_DIFF:
			return "The number of row elements is not always the same.";
		default:
			return "Error.";
		}
	}
}

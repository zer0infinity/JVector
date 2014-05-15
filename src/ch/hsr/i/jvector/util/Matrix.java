package ch.hsr.i.jvector.util;

import ch.hsr.i.jvector.exceptions.NoMatrixException;
import ch.hsr.i.jvector.exceptions.NoMatrixException.StateNoMatrix;

public class Matrix {

	private double[][] matrix;
	
	public Matrix(double[][] matrix) throws NoMatrixException {
		
		for(int i = 0; i < matrix.length; i++) {
			if(matrix[i].length != matrix[0].length) {
				throw new NoMatrixException(StateNoMatrix.NUM_ROW_ELEMENTS_DIFF, "column " + i);
			}
		}
		
		this.matrix = matrix;
	}

	public int getNumRows() {
		return matrix.length;
	}

	public int getNumColumns() {
		return matrix[0].length;
	}

	public double getValue(int row, int column) {
		return matrix[row][column];
	}
	
	public double[] getRowValues(int row) {
		
		double[] rowValues = new double[getNumColumns()];
		
		for(int i = 0; i < getNumColumns(); i++) {
			rowValues[i] = matrix[row][i]; 
		}
		return rowValues;
	}
	
	public double[] getColumnValues(int column) {
		
		double[] colValues = new double[getNumRows()];
		
		for(int i = 0; i < getNumColumns(); i++) {
			colValues[i] = matrix[i][column]; 
		}
		return colValues;
	}
	
	public int getIndexMaxValueInColumn(int column, int fromRow) {
		
		double max = 0;
		int index = 0;
		
		for(int i = fromRow; i < getNumRows(); i++) {
			if(Math.abs(matrix[i][column]) > max) {
				max = Math.abs(matrix[i][column]);
				index = i;
			}
		}
		
		return index;
	}
	
	public void setValue(int row, int column, double value) {
		matrix[row][column] = value;
	}

	public void setRowValues(int row, double[] values) {
		for (int i = 0; i < getNumColumns(); i++) {
			matrix[row][i] = values[i];
		}
	}

	public void setColumnValues(int column, double[] values) {
		for (int i = 0; i < getNumRows(); i++) {
			matrix[i][column] = values[i];
		}
	}

	public boolean isSquare() {
		return getNumRows() == getNumColumns();
	}

	public String toString() {
		
		String s = "";
		boolean first = true;
		
		s = s.concat("\n ");
		for(int i = 0; i < matrix.length; i++) {
			if(first) {
				first = false;
			} else {
				s = s.concat("\n ");
			}
			for(int j = 0; j < matrix[0].length; j++) {
				s = s.concat(matrix[i][j] + "\t");
			}
		}
		s = s.concat("\n");
		return s;
	}
}

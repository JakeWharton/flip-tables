package com.jakewharton.fliptables.format.wrap;

/**
 * Adjusting the columns of the table to the user provided column widths
 * A column is never made narrower than the length of the header
 */
public class CustomColumnWidth extends ColumnWrapFormat {
	
	private int[] desiredColumnWidths;
	
	public static CustomColumnWidth withColumnWidths(int[] desiredColumnWidths) {
		return new CustomColumnWidth(desiredColumnWidths);
	}
	
	private CustomColumnWidth(int[] desiredColumnWidths) {
		this.desiredColumnWidths = desiredColumnWidths;
	}
	
	@Override
	public WrappedTableData adjustData(String[] headers, String[][] data, int[] columnWidths) {
		if (desiredColumnWidths.length != headers.length) {
			throw new IllegalArgumentException("Length of the array of the desired columns does not match the number of columns");
		}
		
		int[] adjustedWidths = new int[columnWidths.length];
		for(int col = 0; col < columnWidths.length; col++) {
			adjustedWidths[col] = Math.max(headers[col].length(), desiredColumnWidths[col]);
		}
		return new WrappedTableData(headers, adjustedData(data, adjustedWidths), adjustedWidths);
	}
}

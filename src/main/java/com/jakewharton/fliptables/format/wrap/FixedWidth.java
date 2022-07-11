package com.jakewharton.fliptables.format.wrap;

import java.util.Arrays;

/**
 * Reduces the individual column widths proportional to the table width.
 * A column is never made narrower than the length of the header
 */
public class FixedWidth extends ColumnWrapFormat {
	
	private int maxWidth;
	
	public static FixedWidth withWidth(int tableWidth) {
		return new FixedWidth(tableWidth);
	}
	
	private FixedWidth(int width) {
		this.maxWidth = width;
	}
	
	@Override
	public WrappedTableData adjustData(String[] headers, String[][] data, int[] columnWidths) {
		int currentLength = Arrays.stream(columnWidths).sum();
		double ratio = maxWidth / (1.0 * currentLength);
		if(ratio > 1.0d) {
			return new WrappedTableData(headers, data, columnWidths);
		}
		int[] adjustedWidths = adjustedColumnWidths(headers, columnWidths, ratio);
		return new WrappedTableData(headers, adjustedData(data, adjustedWidths), adjustedWidths);
	}
	
	private int[] adjustedColumnWidths(String headers[], int[] columnWidths, double ratio) {
		int[] adjustedWidths = headers.length == 1 ?  new int[] { maxWidth }: new int[headers.length];
		int index = 0, sum = 0;
		for(; index < headers.length - 1; index++) {
			adjustedWidths[index] = Math.max((int) (ratio * columnWidths[index]), headers[index].length());
			sum += adjustedWidths[index];
		}
		adjustedWidths[index] = Math.max(maxWidth - sum, headers[index].length());
		return adjustedWidths;
	}

}

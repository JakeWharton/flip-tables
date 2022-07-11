package com.jakewharton.fliptables.format.wrap;

public class DefaultWrapFormat extends ColumnWrapFormat {

	@Override
	public WrappedTableData adjustData(String[] headers, String[][] data, int[] columnWidths) {
		return new WrappedTableData(headers, data, columnWidths);
	}
}

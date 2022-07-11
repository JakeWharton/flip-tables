package com.jakewharton.fliptables.format.wrap;

/**
 * Class to hold the adjusted (wrapped) data out
 */
public class WrappedTableData {
	String[] headers;
	String[][] data;
	int[] columnWidths;
	
	public WrappedTableData() {}
	
	public WrappedTableData(String[] headers, String[][] data, int[] columnWidths) {
		this.headers = headers;
		this.data = data;
		this.columnWidths = columnWidths;
	}
	
	public String[] getHeaders() {
		return this.headers;
	}
	
	public String[][] getData() {
		return this.data;
	}
	
	public int[] getColumnWidths() {
		return this.columnWidths;
	}
}

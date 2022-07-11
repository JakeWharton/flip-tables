package com.jakewharton.fliptables.format.wrap;

/**
 * Provides a mechanism to wrap the text of the columns.
 * Line breaks are added to break long texts without breaking the words
 */
public abstract class ColumnWrapFormat {
	
	private static final String SPLITTER_REGEX = "((?=:|,|\\.|\\s)|(?<=:|,|\\.|\\s))";
	private static final String LINE_BREAK = "\n";
	
	public abstract WrappedTableData adjustData(String[] headers, String[][] data, int[] columnWidths);
	
	protected String[][] adjustedData(String[][] data, int[] adjustedWidths) {
		String[][] adjustedData = new String[data.length][adjustedWidths.length];
		for(int row = 0; row < data.length; row++) {
			for(int col = 0; col < adjustedWidths.length; col++) {
				adjustedData[row][col] = adjustFieldData(data[row][col], adjustedWidths[col]);
			}
		}
		return adjustedData;
	}
	
	protected String adjustFieldData(String field, int desiredWidth) {
		if(null == field || field.isEmpty() || field.length() <= desiredWidth) {
			return field;
		}
		return withLineBreaks(field, desiredWidth);
	}
	
	/**
	 * Adds line breaks on maxLineLength boundaries without breaking the words
	 */
	private String withLineBreaks(String input, int maxLineLength) {
		String[] components = input.split(SPLITTER_REGEX);
		StringBuilder builder = new StringBuilder();
		int lineLength = 0, iterator = 0;
		do {
			String component = components[iterator]; 
			if(lineLength == 0 || (lineLength + component.length() <= maxLineLength)) {
				builder.append(component);
				lineLength += component.length();
				iterator++;
			}
			else {
				builder.append(LINE_BREAK);
				lineLength = 0;
			}
		} while(iterator < components.length);
		
		return builder.toString();
	}
}

/*
 * Copyright 2014 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jakewharton.fliptables;

/**
 * <pre>
 * ╔═════════════╤════════════════════════════╤══════════════╗
 * ║ Name        │ Function                   │ Author       ║
 * ╠═════════════╪════════════════════════════╪══════════════╣
 * ║ Flip Tables │ Pretty-print a text table. │ Jake Wharton ║
 * ╚═════════════╧════════════════════════════╧══════════════╝
 * </pre>
 */
public final class FlipTable {
  private static final String EMPTY = "(empty)";
  private static final String ANSI_COLORS = "\u001B\\[[;\\d]*m";

  /** Create a new table with the specified headers and row data. */
  public static String of(String[] headers, String[][] data) {
    return of(new DefaultTableFormat(), headers, data);
  }
  
  /** Create a new table with the specified TableFormat, headers and row data. */
  public static String of(TableFormat tableFormat, String[] headers, String[][] data) {
    if (headers == null) throw new NullPointerException("headers == null");
    if (headers.length == 0) throw new IllegalArgumentException("Headers must not be empty.");
    if (data == null) throw new NullPointerException("data == null");
    return new FlipTable(tableFormat, headers, data).toString();
  }

  private final String[] headers;
  private final String[][] data;
  private final int columns;
  private final int[] columnWidths;
  private final int emptyWidth;
  private TableFormat tableFormat = null;

  private FlipTable(TableFormat tableFormat, String[] headers, String[][] data) {
	  
	this.tableFormat = tableFormat;
    this.headers = headers;
    this.data = data;

    columns = headers.length;
    columnWidths = new int[columns];
    for (int row = -1; row < data.length; row++) {
      String[] rowData = (row == -1) ? headers : data[row]; // Hack to parse headers too.
      if (rowData.length != columns) {
        throw new IllegalArgumentException(
            String.format("Row %s's %s columns != %s columns", row + 1, rowData.length, columns));
      }
      for (int column = 0; column < columns; column++) {
        for (String rowDataLine : rowData[column].split("\\n")) {
          String rowDataWithoutColor = rowDataLine.replaceAll(ANSI_COLORS, "");
          columnWidths[column] = Math.max(columnWidths[column], rowDataWithoutColor.length());
        }
      }
    }

    int emptyWidth = 3 * (columns - 1); // Account for column dividers and their spacing.
    for (int columnWidth : columnWidths) {
      emptyWidth += columnWidth;
    }
    this.emptyWidth = emptyWidth;

    if (emptyWidth < EMPTY.length()) { // Make sure we're wide enough for the empty text.
      columnWidths[columns - 1] += EMPTY.length() - emptyWidth;
    }
  }

  @Override public String toString() {
    StringBuilder builder = new StringBuilder();
    printDivider(builder, tableFormat.getHeaderRowTopChars());
    printData(builder, headers);
    if (data.length == 0) {
      printDivider(builder, tableFormat.getHeaderRowNoDataBottomChars());
      builder.append(tableFormat.getRowStartChars())
      		 .append(pad(emptyWidth, EMPTY))
      		 .append(tableFormat.getRowEndChars() + tableFormat.getRowEndTerminatorChars());
      printDivider(builder, tableFormat.getFooterRowNoDataBottomChars());
    } else {
      for (int row = 0; row < data.length; row++) {
        printDivider(builder, row == 0 ? tableFormat.getHeaderRowBottomChars() : tableFormat.getDataRowDividerChars());
        printData(builder, data[row]);
      }
      printDivider(builder, tableFormat.getFooterRowBottomChars());
    }
    return builder.toString();
  }

  private void printDivider(StringBuilder out, String format) {
    for (int column = 0; column < columns; column++) {
      out.append(column == 0 ? format.charAt(0) : format.charAt(2));
      out.append(pad(columnWidths[column], "").replace(' ', format.charAt(1)));
    }
    out.append(format.charAt(4)).append(tableFormat.getRowEndTerminatorChars());
  }

  private void printData(StringBuilder out, String[] data) {
    for (int line = 0, lines = 1; line < lines; line++) {
      for (int column = 0; column < columns; column++) {
        out.append(column == 0 ? tableFormat.getRowStartChars() : tableFormat.getColumnSeparatorChars());
        String[] cellLines = data[column].split("\\n");
        lines = Math.max(lines, cellLines.length);
        String cellLine = line < cellLines.length ? cellLines[line] : "";
        out.append(pad(columnWidths[column], cellLine));
      }
      out.append(tableFormat.getRowEndChars() + tableFormat.getRowEndTerminatorChars());
    }
  }

  private static String pad(int width, String data) {
    return String.format(" %1$-" + width + "s ", data);
  }
}

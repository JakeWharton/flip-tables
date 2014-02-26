package com.jakewharton.fliptables;

/**
 * <pre>
 * ╔═══════════════╤════════════════════════════╤══════════════╗
 * ║ Name          │ Function                   │ Author       ║
 * ╠═══════════════╪════════════════════════════╪══════════════╣
 * ║ Flip (Tables) │ Pretty-print a text table. │ Jake Wharton ║
 * ╚═══════════════╧════════════════════════════╧══════════════╝
 * </pre>
 */
public final class FlipTable {
  private static final String EMPTY = "(empty)";

  /** Create a new table with the specified headers and row data. */
  public static String of(String[] headers, String[][] data) {
    if (headers == null) throw new NullPointerException("headers == null");
    if (data == null) throw new NullPointerException("data == null");
    return new FlipTable(headers, data).toString();
  }

  private final String[] headers;
  private final String[][] data;
  private final int columns;
  private final int[] columnWidths;
  private final int emptyWidth;

  private FlipTable(String[] headers, String[][] data) {
    this.headers = headers;
    this.data = data;

    columns = headers.length;
    columnWidths = new int[columns];
    int emptyWidth = 3 * (columns - 1); // Account for column dividers and spacing.
    for (int column = 0; column < columns; column++) {
      for (String headerLine : headers[column].split("\\n")) {
        columnWidths[column] = Math.max(columnWidths[column], headerLine.length());
      }
      for (int row = 0; row < data.length; row++) {
        if (data[row].length != columns) {
          throw new IllegalArgumentException(
              String.format("Row %s %s columns != %s columns", row + 1, data[row].length, columns));
        }
        for (String cellLine : data[row][column].split("\\n")) {
          columnWidths[column] = Math.max(columnWidths[column], cellLine.length());
        }
      }
      emptyWidth += columnWidths[column];
    }
    this.emptyWidth = emptyWidth;

    if (emptyWidth < EMPTY.length()) {
      columnWidths[columns - 1] += EMPTY.length() - emptyWidth;
    }
  }

  @Override public String toString() {
    StringBuilder builder = new StringBuilder();
    printDivider(builder, "╔═╤═╗");
    printData(builder, headers);
    if (data.length == 0) {
      printDivider(builder, "╠═╧═╣");
      builder.append('║').append(pad(emptyWidth, EMPTY)).append("║\n");
      printDivider(builder, "╚═══╝");
    } else {
      for (int row = 0; row < data.length; row++) {
        printDivider(builder, row == 0 ? "╠═╪═╣" : "╟─┼─╢");
        printData(builder, data[row]);
      }
      printDivider(builder, "╚═╧═╝");
    }
    return builder.toString();
  }

  private void printDivider(StringBuilder out, String format) {
    for (int column = 0; column < columns; column++) {
      out.append(column == 0 ? format.charAt(0) : format.charAt(2));
      out.append(pad(columnWidths[column], "").replace(' ', format.charAt(1)));
    }
    out.append(format.charAt(4)).append('\n');
  }

  private void printData(StringBuilder out, String[] data) {
    for (int line = 0, lines = 1; line < lines; line++) {
      for (int column = 0; column < columns; column++) {
        out.append(column == 0 ? '║' : '│');
        String[] cellLines = data[column].split("\\n");
        lines = Math.max(lines, cellLines.length);
        String cellLine = line < cellLines.length ? cellLines[line] : "";
        out.append(pad(columnWidths[column], cellLine));
      }
      out.append("║\n");
    }
  }

  private static String pad(int width, String data) {
    return String.format(" %1$-" + width + "s ", data);
  }
}

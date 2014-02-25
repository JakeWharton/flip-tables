package com.jakewharton.fliptables;

import java.io.IOException;
import java.io.StringWriter;

/**
 * A pretty-printed text table.
 * <p>
 * (╯°□°）╯︵ ┻━┻
 * <pre>
 * ╔══════════╤═══════════════════════════╗
 * ║ Key      │ Value                     ║
 * ╠══════════╪═══════════════════════════╣
 * ║ Name     │ Flip (Tables)             ║
 * ╟──────────┼───────────────────────────╢
 * ║ Function │ Pretty-print text tables. ║
 * ╟──────────┼───────────────────────────╢
 * ║ Author   │ Jake Wharton              ║
 * ╚══════════╧═══════════════════════════╝
 * </pre>
 *
 * @see #of(String[], String[][])
 */
public final class FlipTable {
  public static FlipTable of(String[] headers, String[][] data) {
    if (headers == null) throw new NullPointerException("headers == null");
    if (data == null) throw new NullPointerException("data == null");
    return new FlipTable(headers, data);
  }

  private final String[] headers;
  private final String[][] data;
  private final int columns;
  private final int[] columnWidths;
  private final int emptyWidth;

  FlipTable(String[] headers, String[][] data) {
    this.headers = headers;
    this.data = data;

    columns = headers.length;
    columnWidths = new int[columns];
    int emptyWidth = columns + 1; // Account for dividers.
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
  }

  @Override public String toString() {
    try {
      StringWriter writer = new StringWriter();
      writeTo(writer);
      return writer.toString();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  public void writeTo(Appendable out) throws IOException {
    printDivider(out, "╔═╤═╗");
    printData(out, headers);
    if (data.length == 0) {
      printDivider(out, "╠═╧═╣");
      out.append('║').append(pad(emptyWidth, "(empty)")).append("║\n");
      printDivider(out, "╚═══╝");
    } else {
      for (int row = 0; row < data.length; row++) {
        printDivider(out, row == 0 ? "╠═╪═╣" : "╟─┼─╢");
        printData(out, data[row]);
      }
      printDivider(out, "╚═╧═╝");
    }
  }

  private void printDivider(Appendable out, String format) throws IOException {
    for (int column = 0; column < columns; column++) {
      out.append(column == 0 ? format.charAt(0) : format.charAt(2));
      out.append(pad(columnWidths[column], "").replace(' ', format.charAt(1)));
    }
    out.append(format.charAt(4)).append('\n');
  }

  private void printData(Appendable out, String[] data) throws IOException {
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

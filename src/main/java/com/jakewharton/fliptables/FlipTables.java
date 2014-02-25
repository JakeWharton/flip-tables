package com.jakewharton.fliptables;

import java.io.IOException;
import java.io.StringWriter;

/**
 * (╯°□°）╯︵ ┻━┻
 *
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
 */
public final class FlipTables {
  public static FlipTables makeTable(String[] headers, String[][] data) {
    if (headers == null) throw new NullPointerException("headers == null");
    if (data == null) throw new NullPointerException("data == null");
    return new FlipTables(headers, data);
  }

  private final String[] headers;
  private final String[][] data;
  private final int columns;
  private final int[] columnWidths;
  private final int emptyWidth;

  private FlipTables(String[] headers, String[][] data) {
    this.headers = headers;
    this.data = data;

    columns = headers.length;
    columnWidths = new int[columns];
    for (int column = 0; column < columns; column++) {
      columnWidths[column] = headers[column].length();
    }
    for (int column = 0; column < columns; column++) {
      for (int row = 0; row < data.length; row++) {
        if (data[row].length != columns) {
          throw new IllegalArgumentException(
              String.format("Row %s length %s != %s columns", row + 1, data[row].length, columns));
        }
        columnWidths[column] = Math.max(columnWidths[column], data[row][column].length());
      }
    }

    int emptyWidth = columns + 1; // Account for dividers.
    for (int column = 0; column < columns; column++) {
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
      out.append('║').append(pad(emptyWidth, "No results.")).append("║\n");
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
    for (int column = 0; column < columns; column++) {
      out.append(column == 0 ? '║' : '│');
      out.append(pad(columnWidths[column], data[column]));
    }
    out.append("║\n");
  }

  static String pad(int width, String data) {
    return String.format(" %1$-" + width + "s ", data);
  }
}

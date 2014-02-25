package com.jakewharton.fliptables;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  private static final Pattern METHOD = Pattern.compile("^(?:get|is|has)([A-Z][a-zA-Z0-9]*)+$");

  public static <T> FlipTables makeTable(List<T> rows, Class<T> rowType) {
    if (rows == null) throw new NullPointerException("rows == null");
    if (rowType == null) throw new NullPointerException("rowType == null");

    List<Method> methods = new ArrayList<>();
    List<String> headers = new ArrayList<>();
    for (Method method : rowType.getDeclaredMethods()) {
      if (method.getParameterCount() > 0) continue;
      if (method.getReturnType() == void.class) continue;
      Matcher matcher = METHOD.matcher(method.getName());
      if (!matcher.matches()) continue;
      methods.add(method);
      headers.add(matcher.group(1));
    }

    int columnCount = methods.size();
    int rowCount = rows.size();
    String[][] data = new String[rowCount][columnCount];
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        try {
          data[row][column] = String.valueOf(methods.get(column).invoke(rows.get(row)));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return new FlipTables(headers.toArray(new String[columnCount]), data);
  }

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
    int emptyWidth = columns + 1; // Account for dividers.
    for (int column = 0; column < columns; column++) {
      for (String headerLine : headers[column].split("\\n")) {
        columnWidths[column] = headerLine.length();
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

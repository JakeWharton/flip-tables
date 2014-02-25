package com.jakewharton.fliptables;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FlipTablesTest {
  @Test public void empty() {
    String[] headers = { "Test", "Header" };
    String[][] data = new String[0][0];
    String expected = ""
        + "╔══════╤════════╗\n"
        + "║ Test │ Header ║\n"
        + "╠══════╧════════╣\n"
        + "║ No results.   ║\n"
        + "╚═══════════════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void simple() {
    String[] headers = { "Test", "Header" };
    String[][] data = { //
        { "Foo", "Bar" }, //
        { "Kit", "Kat" }, //
        { "Ping", "Pong" }, //
    };
    String expected = ""
        + "╔══════╤════════╗\n"
        + "║ Test │ Header ║\n"
        + "╠══════╪════════╣\n"
        + "║ Foo  │ Bar    ║\n"
        + "╟──────┼────────╢\n"
        + "║ Kit  │ Kat    ║\n"
        + "╟──────┼────────╢\n"
        + "║ Ping │ Pong   ║\n"
        + "╚══════╧════════╝\n";
    assertTable(headers, data, expected);
  }

  private static void assertTable(String[] headers, String[][] data, String expected) {
    assertThat(FlipTables.makeTable(headers, data).toString()).isEqualTo(expected);
  }
}

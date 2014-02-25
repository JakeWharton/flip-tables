package com.jakewharton.fliptables;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FlipTableTest {
  @Test public void empty() {
    String[] headers = { "Test", "Header" };
    String[][] data = new String[0][0];
    String expected = ""
        + "╔══════╤════════╗\n"
        + "║ Test │ Header ║\n"
        + "╠══════╧════════╣\n"
        + "║ (empty)       ║\n"
        + "╚═══════════════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void simple() {
    String[] headers = { "Hi", "Header" };
    String[][] data = { //
        { "Foo", "Bar" }, //
        { "Kit", "Kat" }, //
        { "Ping", "Pong" }, //
    };
    String expected = ""
        + "╔══════╤════════╗\n"
        + "║ Hi   │ Header ║\n"
        + "╠══════╪════════╣\n"
        + "║ Foo  │ Bar    ║\n"
        + "╟──────┼────────╢\n"
        + "║ Kit  │ Kat    ║\n"
        + "╟──────┼────────╢\n"
        + "║ Ping │ Pong   ║\n"
        + "╚══════╧════════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void dataNewlineFirstLineLong() {
    String[] headers = { "One", "Two" };
    String[][] data = { { "Foo Bar\nBaz", "Two" } };
    String expected = ""
        + "╔═════════╤═════╗\n"
        + "║ One     │ Two ║\n"
        + "╠═════════╪═════╣\n"
        + "║ Foo Bar │ Two ║\n"
        + "║ Baz     │     ║\n"
        + "╚═════════╧═════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void dataNewlineFirstLineShort() {
    String[] headers = { "One", "Two" };
    String[][] data = { { "Foo\nBar Baz", "Two" } };
    String expected = ""
        + "╔═════════╤═════╗\n"
        + "║ One     │ Two ║\n"
        + "╠═════════╪═════╣\n"
        + "║ Foo     │ Two ║\n"
        + "║ Bar Baz │     ║\n"
        + "╚═════════╧═════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void headerNewlineFirstLineLong() {
    String[] headers = { "One Two\nThree", "Four" };
    String[][] data = { { "One", "Four" } };
    String expected = ""
        + "╔═════════╤══════╗\n"
        + "║ One Two │ Four ║\n"
        + "║ Three   │      ║\n"
        + "╠═════════╪══════╣\n"
        + "║ One     │ Four ║\n"
        + "╚═════════╧══════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void headerNewlineFirstLineShort() {
    String[] headers = { "One\nTwo Three", "Four" };
    String[][] data = { { "One", "Four" } };
    String expected = ""
        + "╔═══════════╤══════╗\n"
        + "║ One       │ Four ║\n"
        + "║ Two Three │      ║\n"
        + "╠═══════════╪══════╣\n"
        + "║ One       │ Four ║\n"
        + "╚═══════════╧══════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void nested() {
    String[] innerHeaders = { "One", "Two" };
    String[][] innerData = { { "1", "2" } };
    String nested = FlipTable.of(innerHeaders, innerData).toString();
    String[] outerHeaders = { "Left", "Right" };
    String[][] outerData = { { nested, nested } };
    String expected = ""
        + "╔═══════════════╤═══════════════╗\n"
        + "║ Left          │ Right         ║\n"
        + "╠═══════════════╪═══════════════╣\n"
        + "║ ╔═════╤═════╗ │ ╔═════╤═════╗ ║\n"
        + "║ ║ One │ Two ║ │ ║ One │ Two ║ ║\n"
        + "║ ╠═════╪═════╣ │ ╠═════╪═════╣ ║\n"
        + "║ ║ 1   │ 2   ║ │ ║ 1   │ 2   ║ ║\n"
        + "║ ╚═════╧═════╝ │ ╚═════╧═════╝ ║\n"
        + "╚═══════════════╧═══════════════╝\n";

    assertTable(outerHeaders, outerData, expected);
  }

  @Test public void rowColumnMismatchThrows() {
    String[] headers = { "The", "Headers" };
    try {
      String[][] less = { { "Less" } };
      FlipTable.of(headers, less);
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("Row 1 1 columns != 2 columns");
    }
    try {
      String[][] more = { { "More", "Is", "Not", "Less" } };
      FlipTable.of(headers, more);
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("Row 1 4 columns != 2 columns");
    }
  }

  private static void assertTable(String[] headers, String[][] data, String expected) {
    // Leading new line makes the output and compare view look better.
    assertThat("\n" + FlipTable.of(headers, data).toString()).isEqualTo("\n" + expected);
  }
}

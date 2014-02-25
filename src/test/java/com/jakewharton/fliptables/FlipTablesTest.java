package com.jakewharton.fliptables;

import java.util.Arrays;
import java.util.List;
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
        + "║ (empty)       ║\n"
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

  @Test public void newlines() {
    String[] headers = { "Test\nNew Lines", "Header" };
    String[][] data = { //
        { "Foo\nBar", "Kit\nKat" }, //
        { "Ping", "Pong" }, //
    };
    String expected = ""
        + "╔═══════════╤════════╗\n"
        + "║ Test      │ Header ║\n"
        + "║ New Lines │        ║\n"
        + "╠═══════════╪════════╣\n"
        + "║ Foo       │ Kit    ║\n"
        + "║ Bar       │ Kat    ║\n"
        + "╟───────────┼────────╢\n"
        + "║ Ping      │ Pong   ║\n"
        + "╚═══════════╧════════╝\n";
    assertTable(headers, data, expected);
  }

  @Test public void nested() {
    String[] innerHeaders = { "One", "Two" };
    String[][] innerData = { { "1", "2" } };
    String nested = FlipTables.makeTable(innerHeaders, innerData).toString();
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

  @Test public void simpleReflection() {
    List<Person> people = Arrays.asList( //
        new Person("Big", "Bird", 16, "Big Yellow"), //
        new Person("Joe", "Smith", 42, "Proposition Joe"), //
        new Person("Oscar", "Grouchant", 8, "Oscar The Grouch") //
    );
    String expected = ""
        + "╔═════╤═══════════╤═══════════╤══════════════════╗\n"
        + "║ Age │ FirstName │ LastName  │ NickName         ║\n"
        + "╠═════╪═══════════╪═══════════╪══════════════════╣\n"
        + "║ 16  │ Big       │ Bird      │ Big Yellow       ║\n"
        + "╟─────┼───────────┼───────────┼──────────────────╢\n"
        + "║ 42  │ Joe       │ Smith     │ Proposition Joe  ║\n"
        + "╟─────┼───────────┼───────────┼──────────────────╢\n"
        + "║ 8   │ Oscar     │ Grouchant │ Oscar The Grouch ║\n"
        + "╚═════╧═══════════╧═══════════╧══════════════════╝\n";
    assertTable(people, Person.class, expected);
  }

  @Test public void rowColumnMismatchThrows() {
    String[] headers = { "The", "Headers" };
    try {
      String[][] less = { { "Less" } };
      FlipTables.makeTable(headers, less);
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("Row 1 1 columns != 2 columns");
    }
    try {
      String[][] more = { { "More", "Is", "Not", "Less" } };
      FlipTables.makeTable(headers, more);
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("Row 1 4 columns != 2 columns");
    }
  }

  private static void assertTable(String[] headers, String[][] data, String expected) {
    assertThat("\n" + FlipTables.makeTable(headers, data).toString()).isEqualTo("\n" + expected);
  }

  private static <T> void assertTable(List<T> rows, Class<T> rowType, String expected) {
    assertThat("\n" + FlipTables.makeTable(rows, rowType).toString()).isEqualTo("\n" + expected);
  }
}

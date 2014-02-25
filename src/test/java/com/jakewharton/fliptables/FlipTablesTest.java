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

  private static void assertTable(String[] headers, String[][] data, String expected) {
    assertThat(FlipTables.makeTable(headers, data).toString()).isEqualTo(expected);
  }

  private static <T> void assertTable(List<T> rows, Class<T> rowType, String expected) {
    assertThat(FlipTables.makeTable(rows, rowType).toString()).isEqualTo(expected);
  }
}

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

import com.jakewharton.fliptables.util.FakeResultSet;
import com.jakewharton.fliptables.util.Person;
import com.jakewharton.fliptables.util.PersonType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FlipTableConvertersTest {
  @Test public void simpleIterator() {
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
    String table = FlipTableConverters.fromIterable(people, Person.class);
    assertThat(table).isEqualTo(expected);
  }

  @Test public void emptyIterator() {
    List<Person> people = Collections.emptyList();
    String expected = ""
        + "╔═════╤═══════════╤══════════╤══════════╗\n"
        + "║ Age │ FirstName │ LastName │ NickName ║\n"
        + "╠═════╧═══════════╧══════════╧══════════╣\n"
        + "║ (empty)                               ║\n"
        + "╚═══════════════════════════════════════╝\n";
    String table = FlipTableConverters.fromIterable(people, Person.class);
    assertThat(table).isEqualTo(expected);
  }

  @Test public void simpleResultSet() throws SQLException {
    String[] headers = { "English", "Digit", "Spanish" };
    String[][] data = {
        { "One", "1", "Uno" },
        { "Two", "2", "Dos" },
        { "Three", "3", "Tres" },
    };
    ResultSet resultSet = new FakeResultSet(headers, data);
    String expected = ""
        + "╔═════════╤═══════╤═════════╗\n"
        + "║ English │ Digit │ Spanish ║\n"
        + "╠═════════╪═══════╪═════════╣\n"
        + "║ One     │ 1     │ Uno     ║\n"
        + "╟─────────┼───────┼─────────╢\n"
        + "║ Two     │ 2     │ Dos     ║\n"
        + "╟─────────┼───────┼─────────╢\n"
        + "║ Three   │ 3     │ Tres    ║\n"
        + "╚═════════╧═══════╧═════════╝\n";
    String table = FlipTableConverters.fromResultSet(resultSet);
    assertThat(table).isEqualTo(expected);
  }

  @Test public void emptyResultSet() throws SQLException {
    String[] headers = { "English", "Digit", "Spanish" };
    String[][] data = {};
    ResultSet resultSet = new FakeResultSet(headers, data);
    String expected = ""
        + "╔═════════╤═══════╤═════════╗\n"
        + "║ English │ Digit │ Spanish ║\n"
        + "╠═════════╧═══════╧═════════╣\n"
        + "║ (empty)                   ║\n"
        + "╚═══════════════════════════╝\n";
    String table = FlipTableConverters.fromResultSet(resultSet);
    assertThat(table).isEqualTo(expected);
  }

  @Test public void simpleObjects() {
    String[] headers = { "First Name", "Last Name", "Age", "Type" };
    Object[][] data = { //
        { "Big", "Bird", 16, PersonType.COSTUME }, //
        { "Joe", "Smith", 42, PersonType.HUMAN }, //
        { "Oscar", "Grouchant", 8, PersonType.PUPPET } //
    };
    String expected = ""
        + "╔════════════╤═══════════╤═════╤═════════╗\n"
        + "║ First Name │ Last Name │ Age │ Type    ║\n"
        + "╠════════════╪═══════════╪═════╪═════════╣\n"
        + "║ Big        │ Bird      │ 16  │ COSTUME ║\n"
        + "╟────────────┼───────────┼─────┼─────────╢\n"
        + "║ Joe        │ Smith     │ 42  │ HUMAN   ║\n"
        + "╟────────────┼───────────┼─────┼─────────╢\n"
        + "║ Oscar      │ Grouchant │ 8   │ PUPPET  ║\n"
        + "╚════════════╧═══════════╧═════╧═════════╝\n";
    String table = FlipTableConverters.fromObjects(headers, data);
    assertThat(table).isEqualTo(expected);
  }

  @Test public void emptyObjects() {
    String[] headers = { "First Name", "Last Name", "Age", "Type" };
    Object[][] data = {};
    String expected = ""
        + "╔════════════╤═══════════╤═════╤══════╗\n"
        + "║ First Name │ Last Name │ Age │ Type ║\n"
        + "╠════════════╧═══════════╧═════╧══════╣\n"
        + "║ (empty)                             ║\n"
        + "╚═════════════════════════════════════╝\n";
    String table = FlipTableConverters.fromObjects(headers, data);
    assertThat(table).isEqualTo(expected);
  }
}

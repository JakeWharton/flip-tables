Flip Tables
===========

Because pretty-printing text tables in Java should be easy.

(╯°□°）╯︵ ┻━┻

Development on this project has basically stopped. You can find a more
comprehensive and actively-developed text table library at
https://github.com/JakeWharton/picnic.



Usage
-----

A `FlipTable` requires headers and data in string form:
```java
String[] headers = { "Test", "Header" };
String[][] data = {
    { "Foo", "Bar" },
    { "Kit", "Kat" },
};
System.out.println(FlipTable.of(headers, data));
```
```
╔══════╤════════╗
║ Test │ Header ║
╠══════╪════════╣
║ Foo  │ Bar    ║
╟──────┼────────╢
║ Kit  │ Kat    ║
╚══════╧════════╝
```

They can be empty:
```java
String[] headers = { "Test", "Header" };
String[][] data = {};
System.out.println(FlipTable.of(headers, data));
```
```
╔══════╤════════╗
║ Test │ Header ║
╠══════╧════════╣
║ (empty)       ║
╚═══════════════╝
```

Newlines are supported:
```java
String[] headers = { "One Two\nThree", "Four" };
String[][] data = { { "Five", "Six\nSeven Eight" } };
System.out.println(FlipTable.of(headers, data));
```
```
╔═════════╤═════════════╗
║ One Two │ Four        ║
║ Three   │             ║
╠═════════╪═════════════╣
║ Five    │ Six         ║
║         │ Seven Eight ║
╚═════════╧═════════════╝
```

Which means tables can be nested:
```java
String[] innerHeaders = { "One", "Two" };
String[][] innerData = { { "1", "2" } };
String inner = FlipTable.of(innerHeaders, innerData);
String[] headers = { "Left", "Right" };
String[][] data = { { inner, inner } };
System.out.println(FlipTable.of(headers, data));
```
```
╔═══════════════╤═══════════════╗
║ Left          │ Right         ║
╠═══════════════╪═══════════════╣
║ ╔═════╤═════╗ │ ╔═════╤═════╗ ║
║ ║ One │ Two ║ │ ║ One │ Two ║ ║
║ ╠═════╪═════╣ │ ╠═════╪═════╣ ║
║ ║ 1   │ 2   ║ │ ║ 1   │ 2   ║ ║
║ ╚═════╧═════╝ │ ╚═════╧═════╝ ║
╚═══════════════╧═══════════════╝
```

Helper methods convert from types like lists:
```java
List<Person> people = Arrays.asList(new Person("Foo", "Bar"), new Person("Kit", "Kat"));
System.out.println(FlipTableConverters.fromIterable(people, Person.class));
```
```
╔═══════════╤══════════╗
║ FirstName │ LastName ║
╠═══════════╪══════════╣
║ Foo       │ Bar      ║
╟───────────┼──────────╢
║ Kit       │ Kat      ║
╚═══════════╧══════════╝
```

Or a database result:
```java
ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM users");
System.out.println(FlipTableConverters.fromResultSet(resultSet));
```
```
╔════════════╤═══════════╗
║ first_name │ last_name ║
╠════════════╪═══════════╣
║ Jake       │ Wharton   ║
╟────────────┼───────────╢
║ Edward     │ Snowden   ║
╚════════════╧═══════════╝
```

Arbitrary objects are also supported:
```java
String[] headers = { "First Name", "Last Name", "Age", "Type" };
Object[][] data = {
    { "Big", "Bird", 16, PersonType.COSTUME },
    { "Joe", "Smith", 42, PersonType.HUMAN },
    { "Oscar", "Grouchant", 8, PersonType.PUPPET }
};
System.out.println(FlipTableConverters.fromObjects(headers, data));
```
```
╔════════════╤═══════════╤═════╤═════════╗
║ First Name │ Last Name │ Age │ Type    ║
╠════════════╪═══════════╪═════╪═════════╣
║ Big        │ Bird      │ 16  │ COSTUME ║
╟────────────┼───────────┼─────┼─────────╢
║ Joe        │ Smith     │ 42  │ HUMAN   ║
╟────────────┼───────────┼─────┼─────────╢
║ Oscar      │ Grouchant │ 8   │ PUPPET  ║
╚════════════╧═══════════╧═════╧═════════╝
```

Column wrapping (optional)
--------------------------

**Fixed Table width**
```java
String[] headers = { "First Name", "Last Name", "Details" };
String[][] data = {
    { "One One One One", "Two Two Two:Two", "Three Three.Three,Three" },
    { "Joe", "Smith", "Hello" }
};
System.out.println(FlipTable.of(headers, data, FixedWidth.withWidth(30)));

```
```
╔════════════╤═══════════╤═════════════╗
║ First Name │ Last Name │ Details     ║
╠════════════╪═══════════╪═════════════╣
║ One One    │ Two Two   │ Three Three ║
║ One One    │ Two:Two   │ .Three,     ║
║            │           │ Three       ║
╟────────────┼───────────┼─────────────╢
║ Joe        │ Smith     │ Hello       ║
╚════════════╧═══════════╧═════════════╝
```

**Custom Column widths**
```java
String[] headers = { "First", "Last", "Det" };
String[][] data = {
    { "One One One One", "Two Two Two:Two", "Fifteen four on on on on on five" },
    { "Joe", "Boe", "Hello" }
};
System.out.println(FlipTable.of(headers, data, CustomColumnWidth.withColumnWidths(new int[] {5, 5, 8})));
```
```
╔═══════╤═══════╤══════════╗
║ First │ Last  │ Det      ║
╠═══════╪═══════╪══════════╣
║ One   │ Two   │ Fifteen  ║
║ One   │ Two   │ four on  ║
║ One   │ Two:  │ on on on ║
║ One   │ Two   │  on five ║
╟───────┼───────┼──────────╢
║ Joe   │ Boe   │ Hello    ║
╚═══════╧═══════╧══════════╝
```

Download
--------

Download the [latest jar][1] or reference on Maven central as
`com.jakewharton.fliptables:fliptables:1.1.0`.

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].



License
-------

    Copyright 2014 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: https://search.maven.org/remote_content?g=com.jakewharton.fliptables&a=fliptables&v=LATEST
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/

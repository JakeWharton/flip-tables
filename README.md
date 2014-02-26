Flip (Tables)
=============

Because pretty-printing text tables in Java should be easy.

(╯°□°）╯︵ ┻━┻



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
╔═════════╤══════════════╗
║ One Two │ Four         ║
║ Three   │              ║
╠═════════╪══════════════╣
║ Five    │ Six          ║
║         │ Seven Eight  ║
╚═════════╧══════════════╝
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



To Do
-----

 * Java 8 `Collector`?
 * Java 8 method references
 * Builder



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

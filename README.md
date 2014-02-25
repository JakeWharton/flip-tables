Flip (Tables)
=============

(╯°□°）╯︵ ┻━┻

Because pretty-printing text tables in Java should be easy.



Usage
-----
```java
List<Person> people = Arrays.asList(new Person("Foo", "Bar"), new Person("Kit", "Kat"));
System.out.println(FlipTables.makeTable(people, data));
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

Data can also be specified explicitly:
```java
String[] headers = { "Test", "Header" };
String[][] data = {
    { "Foo", "Bar" },
    { "Kit", "Kat" },
    { "Ping", "Pong" },
};

System.out.println(FlipTables.makeTable(headers, data));
```
```
╔══════╤════════╗
║ Test │ Header ║
╠══════╪════════╣
║ Foo  │ Bar    ║
╟──────┼────────╢
║ Kit  │ Kat    ║
╟──────┼────────╢
║ Ping │ Pong   ║
╚══════╧════════╝
```



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

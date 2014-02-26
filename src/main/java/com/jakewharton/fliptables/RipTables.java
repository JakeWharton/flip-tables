/*bin/mkdir /tmp/riptables 2> /dev/null
javac -d /tmp/riptables $0 `dirname $0`/FlipTable.java
java -cp /tmp/riptables com.jakewharton.fliptables.RipTables "$@"
exit
*/
package com.jakewharton.fliptables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RipTables {
  private final Pattern pattern;
  private final int[] columns;
  private final String[] headers;

  public RipTables(String regex, String columns) {
    pattern = Pattern.compile(regex);

    this.headers = columns.split(",", -1);
    this.columns = new int[headers.length];
    for (int i = 0; i < headers.length; i++) {
      String header = headers[i];
      if (!header.matches("\\d+:.*")) {
        this.columns[i] = i + 1;
      } else {
        int colon = header.indexOf(":");
        this.columns[i] = Integer.parseInt(header.substring(0, colon));
        this.headers[i] = header.substring(colon + 1);
      }
    }
  }

  public String rip(BufferedReader in) throws IOException {
    List<String[]> table = new ArrayList<>();
    for (String line; (line = in.readLine()) != null; ) {
      Matcher matcher = pattern.matcher(line);
      if (!matcher.find()) {
        System.err.println("Rip: Couldn't match \"" + line + "\"");
        continue;
      }

      String[] groups = new String[matcher.groupCount() + 1];
      for (int g = 0; g < groups.length; g++) {
        groups[g] = matcher.group(g);
      }

      String[] row = new String[columns.length];
      for (int i = 0; i < columns.length; i++) {
        row[i] = groups[columns[i]];
      }

      table.add(row);
    }
    return FlipTable.of(headers, table.toArray(new String[table.size()][]));
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      printUsage();
      System.exit(1);
    }

    String regex = args[0];
    String columns = args[1];

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String result = new RipTables(regex, columns).rip(in);
    System.out.println(result);
  }

  public static void printUsage() {
    System.out.println("Usage: RipTables [flags] <regex> <columns>");
    System.out.println();
    System.out.println("  regex: a Java regular expression, with groups");
    System.out.println("  http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html");
    System.out.println("         you can (parenthesize) groups");
    System.out.println("         \\s whitespace");
    System.out.println("         \\S non-whitespace");
    System.out.println("         \\w word characters");
    System.out.println("         \\W non-word");
    System.out.println();
    System.out.println("  columns: a list of columns");
    System.out.println("          'Foo,Bar,Baz'");
    System.out.println();
    System.out.println("  Use 'single quotes' to prevent bash from interfering");
  }
}

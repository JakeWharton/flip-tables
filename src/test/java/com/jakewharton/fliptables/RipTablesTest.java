package com.jakewharton.fliptables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class RipTablesTest {
  @Test public void rip() throws IOException {
    String input = ""
        + "-rw-r--r--   1 jwilson  jwilson  11358 Feb 25 22:05 LICENSE.txt\n"
        + "-rw-r--r--   1 jwilson  jwilson   4513 Feb 25 22:05 README.md\n"
        + "drwxr-xr-x   7 jwilson  jwilson    238 Feb 25 22:15 build\n"
        + "-rw-r--r--   1 jwilson  jwilson    265 Feb 25 22:05 build.gradle\n";

    RipTables ripTables = new RipTables(" (\\d+) .{12} (.+)", "2:File,1:Size");
    BufferedReader in = new BufferedReader(new StringReader(input));
    String expected = ""
        + "╔══════════════╤═══════╗\n"
        + "║ File         │ Size  ║\n"
        + "╠══════════════╪═══════╣\n"
        + "║ LICENSE.txt  │ 11358 ║\n"
        + "╟──────────────┼───────╢\n"
        + "║ README.md    │ 4513  ║\n"
        + "╟──────────────┼───────╢\n"
        + "║ build        │ 238   ║\n"
        + "╟──────────────┼───────╢\n"
        + "║ build.gradle │ 265   ║\n"
        + "╚══════════════╧═══════╝\n"
        + "";
    assertThat(ripTables.rip(in)).isEqualTo(expected);
  }
}

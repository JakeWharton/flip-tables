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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Helper methods for creating {@link FlipTable tables} from various different data sets. */
public final class FlipTableConverters {
  private static final Pattern METHOD = Pattern.compile("^(?:get|is|has)([A-Z][a-zA-Z0-9]*)+$");
  private static final Comparator<Method> METHOD_COMPARATOR = new Comparator<Method>() {
    @Override public int compare(Method o1, Method o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };

  /** Create a table from an array of objects using {@link String#valueOf}. */
  public static String fromObjects(String[] headers, Object[][] data) {
    if (headers == null) throw new NullPointerException("headers == null");
    if (data == null) throw new NullPointerException("data == null");

    List<String[]> stringData = new ArrayList<>();
    for (Object[] row : data) {
      String[] stringRow = new String[row.length];
      for (int column = 0; column < row.length; column++) {
        stringRow[column] = String.valueOf(row[column]);
      }
      stringData.add(stringRow);
    }

    String[][] dataArray = stringData.toArray(new String[stringData.size()][]);
    return FlipTable.of(headers, dataArray);
  }

  /**
   * Create a table from a group of objects. Public accessor methods on the class type with no
   * arguments will be used as the columns.
   */
  public static <T> String fromIterable(Iterable<T> rows, Class<T> rowType) {
    if (rows == null) throw new NullPointerException("rows == null");
    if (rowType == null) throw new NullPointerException("rowType == null");

    Method[] declaredMethods = rowType.getDeclaredMethods();
    Arrays.sort(declaredMethods, METHOD_COMPARATOR);

    List<Method> methods = new ArrayList<>();
    List<String> headers = new ArrayList<>();
    for (Method declaratedMethod : declaredMethods) {
      if (declaratedMethod.getParameterTypes().length > 0) continue;
      if (declaratedMethod.getReturnType() == void.class) continue;
      Matcher matcher = METHOD.matcher(declaratedMethod.getName());
      if (!matcher.matches()) continue;

      declaratedMethod.setAccessible(true);
      methods.add(declaratedMethod);
      headers.add(matcher.group(1));
    }

    int columnCount = methods.size();
    List<String[]> data = new ArrayList<>();
    for (T row : rows) {
      String[] rowData = new String[columnCount];
      for (int column = 0; column < columnCount; column++) {
        try {
          rowData[column] = String.valueOf(methods.get(column).invoke(row));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      data.add(rowData);
    }

    String[] headerArray = headers.toArray(new String[headers.size()]);
    String[][] dataArray = data.toArray(new String[data.size()][]);
    return FlipTable.of(headerArray, dataArray);
  }

  /** Create a table from a {@link ResultSet}. */
  public static String fromResultSet(ResultSet resultSet) throws SQLException {
    if (resultSet == null) throw new NullPointerException("resultSet == null");
    if (!resultSet.isBeforeFirst()) throw new IllegalStateException("Result set not at first.");

    List<String> headers = new ArrayList<>();
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    int columnCount = resultSetMetaData.getColumnCount();
    for (int column = 0; column < columnCount; column++) {
      headers.add(resultSetMetaData.getColumnName(column + 1));
    }

    List<String[]> data = new ArrayList<>();
    while (resultSet.next()) {
      String[] rowData = new String[columnCount];
      for (int column = 0; column < columnCount; column++) {
        rowData[column] = resultSet.getString(column + 1);
      }
      data.add(rowData);
    }

    String[] headerArray = headers.toArray(new String[headers.size()]);
    String[][] dataArray = data.toArray(new String[data.size()][]);
    return FlipTable.of(headerArray, dataArray);
  }

  private FlipTableConverters() {
    throw new AssertionError("No instances.");
  }
}

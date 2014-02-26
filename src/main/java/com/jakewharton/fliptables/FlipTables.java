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

/** Helper methods which convert various types into those required for {@link FlipTable}. */
public final class FlipTables {
  private static final Pattern METHOD = Pattern.compile("^(?:get|is|has)([A-Z][a-zA-Z0-9]*)+$");
  private static final Comparator<Method> METHOD_COMPARATOR = new Comparator<Method>() {
    @Override public int compare(Method o1, Method o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };

  /**
   * Create a {@link FlipTable} from a group of objects. The public accessor methods on the class
   * type will be used as columns.
   */
  public static <T> FlipTable fromIterable(Iterable<T> rows, Class<T> rowType) {
    if (rows == null) throw new NullPointerException("rows == null");
    if (rowType == null) throw new NullPointerException("rowType == null");

    Method[] declaredMethods = rowType.getDeclaredMethods();
    Arrays.sort(declaredMethods, METHOD_COMPARATOR);

    List<Method> methods = new ArrayList<>();
    List<String> headers = new ArrayList<>();
    for (Method declaratedMethod : declaredMethods) {
      if (declaratedMethod.getParameterCount() > 0) continue;
      if (declaratedMethod.getReturnType() == void.class) continue;
      Matcher matcher = METHOD.matcher(declaratedMethod.getName());
      if (!matcher.matches()) continue;
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
    String[][] dataArray = data.toArray(new String[data.size()][0]);
    return new FlipTable(headerArray, dataArray);
  }

  /** Create a {@link FlipTable} from a {@link ResultSet}. */
  public static FlipTable fromResultSet(ResultSet resultSet) throws SQLException {
    if (resultSet == null) throw new NullPointerException("resultSet == null");
    if (!resultSet.isBeforeFirst()) throw new IllegalStateException("Result set not at first.");

    List<String> headers = new ArrayList<>();
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    int columnCount = resultSetMetaData.getColumnCount();
    for (int column = 0; column < columnCount; column++) {
      headers.add(resultSetMetaData.getColumnName(column));
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
    String[][] dataArray = data.toArray(new String[data.size()][0]);
    return new FlipTable(headerArray, dataArray);
  }

  private FlipTables() {
    throw new AssertionError("No instances.");
  }
}

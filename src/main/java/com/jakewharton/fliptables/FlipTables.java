package com.jakewharton.fliptables;

import java.lang.reflect.Method;
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

  public static <T> FlipTable fromList(List<T> rows, Class<T> rowType) {
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
    int rowCount = rows.size();
    String[][] data = new String[rowCount][columnCount];
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        try {
          data[row][column] = String.valueOf(methods.get(column).invoke(rows.get(row)));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return new FlipTable(headers.toArray(new String[headers.size()]), data);
  }

  private FlipTables() {
    throw new AssertionError("No instances.");
  }
}

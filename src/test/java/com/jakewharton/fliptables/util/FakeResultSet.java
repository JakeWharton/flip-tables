package com.jakewharton.fliptables.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public final class FakeResultSet extends AbstractResultSet {
  private final String[] headers;
  private final String[][] data;
  private int row = -1;

  public FakeResultSet(String[] headers, String[][] data) {
    this.headers = headers;
    this.data = data;
  }

  @Override public ResultSetMetaData getMetaData() throws SQLException {
    return new FakeResultSetMetaData();
  }

  @Override public boolean isBeforeFirst() throws SQLException {
    return row == -1;
  }

  @Override public boolean next() throws SQLException {
    return ++row < data.length;
  }

  @Override public String getString(int columnIndex) throws SQLException {
    return data[row][columnIndex - 1];
  }

  private class FakeResultSetMetaData extends AbstractResultSetMetaData {
    @Override public int getColumnCount() throws SQLException {
      return headers.length;
    }

    @Override public String getColumnName(int column) throws SQLException {
      return headers[column];
    }
  }
}

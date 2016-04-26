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
      return headers[column - 1];
    }
  }
}

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
 */package com.jakewharton.fliptables;

public class AsciiTableFormat implements TableFormat {

	@Override
	public String getHeaderRowTopChars() {
		return "+=+=+";
	}

	@Override
	public String getHeaderRowBottomChars() {
		return "|=|=|";
	}

	@Override
	public String getHeaderRowNoDataBottomChars() {
		return "|=|=|";
	}

	@Override
	public String getFooterRowBottomChars() {
		return "+-+-+";
	}

	@Override
	public String getFooterRowNoDataBottomChars() {
		return "+-+-+";
	}

	@Override
	public String getDataRowDividerChars() {
		return "|-|-|";
	}

	@Override
	public String getRowStartChars() {
		return "|";
	}

	@Override
	public String getRowEndChars() {
		return "|";
	}

	@Override
	public String getColumnSeparatorChars() {
		return "|";
	}

	@Override
	public String getRowEndTerminatorChars() {
		return "\n";
	}

}

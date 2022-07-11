package com.jakewharton.fliptables.format.wrap;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.jakewharton.fliptables.FlipTable;
import com.jakewharton.fliptables.util.ResourceReader;

public class ColumnWrapTest {
	
	private String[] headers = new String[] {"Feather", "Weather", "Fizz", "Buzz"};
	private String[][] data = new String[3][headers.length];
	
	public ColumnWrapTest() {
		for(int idx = 0; idx < 3; idx++) {
			data[idx][0] = columnData("a", 6,  10);
			data[idx][1] = columnData("b", 6,  5);
			data[idx][2] = columnData("c", 6,  15);
			data[idx][3] = columnData("d", 6,  25);
		}
		data[1][2] = "some long words somelongwords somelongerwords someevenlongerwords someevenmorelongerwords";
	}
	
	private static String columnData(String base, int wordSize, int words) {
		return IntStream.range(0, words)
			.mapToObj(wordIndex -> IntStream.range(0, wordSize).mapToObj(charIndex -> base).collect(Collectors.joining()))
			.collect(Collectors.joining(" "));
	}
	
	@Test
	public void testFixedWidthWrapping() throws IOException {
		String read = ResourceReader.readFromResourceFile("fixed-width-wrapping.txt");
		String flipTable = FlipTable.of(headers, data, FixedWidth.withWidth(120));
		assertThat(flipTable).isEqualTo(read);
	}
	
	@Test
	public void testCustomWidthWrapping() throws IOException {
		String read = ResourceReader.readFromResourceFile("custom-width-wrapping.txt");
		String flipTable = FlipTable.of(headers, data, CustomColumnWidth.withColumnWidths(new int[] {30, 30, 30, 30}));
		assertThat(flipTable).isEqualTo(read);
	}
	
	public static void main(String args[]) {
		String[] headers = { "First", "Last", "Det" };
		String[][] data = {
		    { "One One One One", "Two Two Two:Two", "Fifteen four on on on on on five" },
		    { "Joe", "Boe", "Hello" }
		};
		System.out.println(FlipTable.of(headers, data, CustomColumnWidth.withColumnWidths(new int[] {5, 5, 8})));
	}

}

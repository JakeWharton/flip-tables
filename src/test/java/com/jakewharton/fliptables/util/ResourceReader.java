package com.jakewharton.fliptables.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceReader {
	
	public static String readFromResourceFile(String filePath) throws IOException {
		
		try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)) {
		    StringBuilder textBuilder = new StringBuilder();
		    try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
		        int c = 0;
		        while ((c = reader.read()) != -1) {
		            textBuilder.append((char) c);
		        }
		    }
		    return textBuilder.toString();
		}
	}
}

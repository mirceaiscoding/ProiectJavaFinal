package app.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import app.entities.HasToCSV;

public class CSVWriterServiceSingleton {

	private static CSVWriterServiceSingleton instance = null;
	
	private CSVWriterServiceSingleton() {}
	
	/**
	 * @return the single instance of this service
	 */
	public static CSVWriterServiceSingleton getInstance() {
		if (instance == null) {
			instance = new CSVWriterServiceSingleton();
		}
		return instance;
	}
	
	/**
	 * Writes the collection of objects in CSV format
	 * 
	 * @param <T> type of object to write
	 * @param objectsToWrite
	 * @param pathToCSV where to write (will write over the current text)
	 * @throws IOException
	 */
	public <T extends HasToCSV> void write (List<T> objectsToWrite, Path pathToCSV) throws IOException {
    	try (BufferedWriter writer = Files.newBufferedWriter(pathToCSV)) {
    	    List<String> dataToCSV = objectsToWrite.stream().map(b -> b.toCSV()).toList();
    	    writer.write(String.join("\n", dataToCSV));
    	}
	}
	
	/**
	 * Writes the data that is already in CSV format
	 * 
	 * @param dataToCSV
	 * @param pathToCSV
	 * @throws IOException
	 */
	public void writeCSV (List<String> dataToCSV, Path pathToCSV) throws IOException {
    	try (BufferedWriter writer = Files.newBufferedWriter(pathToCSV)) {
        	writer.write(String.join("\n", dataToCSV));
    	}
	}

}

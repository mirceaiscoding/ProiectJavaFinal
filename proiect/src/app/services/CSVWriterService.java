package app.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import app.entities.HasToCSV;

public class CSVWriterService {

	/**
	 * Writes the collection of objects in CSV format
	 * 
	 * @param <T> type of object to write
	 * @param objectsToWrite
	 * @param pathToCSV where to write (will write over the current text)
	 * @throws IOException
	 */
	public static <T extends HasToCSV> void write (Collection<T> objectsToWrite, Path pathToCSV) throws IOException {
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
	public static void writeCSV (List<String> dataToCSV, Path pathToCSV) throws IOException {
    	try (BufferedWriter writer = Files.newBufferedWriter(pathToCSV)) {
        	writer.write(String.join("\n", dataToCSV));
    	}
	}

}

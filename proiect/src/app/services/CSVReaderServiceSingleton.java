package app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class CSVReaderServiceSingleton {
	
	private static CSVReaderServiceSingleton instance = null;
	
	private CSVReaderServiceSingleton() {}
	
	/**
	 * @return the single instance of this service
	 */
	public static CSVReaderServiceSingleton getInstance() {
		if (instance == null) {
			instance = new CSVReaderServiceSingleton();
		}
		return instance;
	}

	private static final String COMMA_DELIMITER = ",";

	/**
	 * @param pathToCSV
	 * @param numberOfColumns
	 * @return a list of records (string values of a line)
	 * @throws CSVBadColumnLengthException
	 * @throws IOException
	 */
	public List<String[]> readCSV(Path pathToCSV, int numberOfColumns) throws CSVBadColumnLengthException, IOException{
		List<String[]> data = new ArrayList<>();
    	try (BufferedReader reader = Files.newBufferedReader(pathToCSV)) {
    	    String line;
    	    while ((line = reader.readLine()) != null) {
    	        String[] values = line.split(COMMA_DELIMITER);
    	        if (values.length != numberOfColumns) {
    	        	throw new CSVBadColumnLengthException(String.format("Expected %d collumns, but %d were found", numberOfColumns, values.length));
    	        }
    	        data.add(values);
    	    }
    	}
		return data;
	}
	
}

package app.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class follows the singleton design pattern
 */
public class AuditService {

	private static AuditService instance = null;
	
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
	private static final Path PATH_TO_AUDIT_FILE = Paths.get("data/audit.txt");
		
	private AuditService(){}
	
	public static AuditService getInstance() {
		if (instance == null) {
			instance = new AuditService();
		}
		return instance;
	}
	
	/**
	 * Appends the message with a time-stamp to the audit file
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void logMessage(String message) throws IOException {
		File file = PATH_TO_AUDIT_FILE.toFile();
		if(!file.exists()) {
			file.createNewFile();
		}
				
		// true is to append the content to file
    	FileWriter fw = new FileWriter(file, true);
    	
    	try(BufferedWriter writer = new BufferedWriter(fw)){    		
    		writer.write(String.format("%s: %s%n", message, LocalDateTime.now().format(DATE_FORMATTER)));
    	}
		
	}
	
	

}

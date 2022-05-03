package app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.entities.driver.Driver;

/**
 * This class follows the singleton design pattern
 */
public class DriverCSVDatabaseService implements IDriverDatabaseService {

	/**
	 * Path to file where data is stored
	 */
	private static final Path PATH_TO_CSV = Paths.get("data/drivers.csv");
	
	private static final String COMMA_DELIMITER = ",";
	
	private static final int COLLUMS_NUMBER = 4;

    private static DriverCSVDatabaseService instance = null;
	
    private List<Driver> drivers = new ArrayList<>();
    
    /**
     * Private constructor
     */
    private DriverCSVDatabaseService() {}
    
    /**
     * @return the single instance of this service
     */
    public static DriverCSVDatabaseService getInstance() {
        if (instance == null) {        	
        	instance = new DriverCSVDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void loadData() throws IOException, CSVBadColumnLengthException {
    	try (BufferedReader reader = Files.newBufferedReader(PATH_TO_CSV)) {
    	    String line;
    	    while ((line = reader.readLine()) != null) {
    	        String[] values = line.split(COMMA_DELIMITER);
    	        if (values.length != COLLUMS_NUMBER) {
    	        	throw new CSVBadColumnLengthException(String.format("Expected %d collumns, but %d were found", COLLUMS_NUMBER, values.length));
    	        }
    	        UUID id = UUID.fromString(values[0]);
    	        String name = values[1];
    	        String email = values[2];
    	        String phoneNumber = values[3];
    	        
    	        drivers.add(new Driver(name, email, phoneNumber, id));
    	    }
    	}
    }
    
    @Override
    public void saveData() throws IOException {
    	CSVDatabaseWriteService.write(drivers, PATH_TO_CSV);
    }

	@Override
	public List<Driver> getDrivers() {
		return drivers;
	}
	
	@Override
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	@Override
	public void addDriver(Driver driver) {
		drivers.add(driver);
	}

	@Override
	public List<Driver> getFreeDrivers() {
		List<Driver> freeDrivers = new ArrayList<>();
		for (Driver driver : drivers) {
			if (driver.isFree()) {
				freeDrivers.add(driver);
			}
		}
		return freeDrivers;
	}

	@Override
	public Driver getDriverById(UUID id) {
		try {			
			Driver driver = (Driver) drivers.stream().filter(d -> d.getId().equals(id)).toArray()[0];
			return driver;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

}

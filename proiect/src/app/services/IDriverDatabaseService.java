package app.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import app.entities.driver.Driver;

public interface IDriverDatabaseService {
	
    /**
     * Loads data from CSV file
     * 
     * @throws IOException if a path is wrong
     * @throws CSVBadColumnLengthException if a line doesn't have the right number of collums
     */
    void loadData() throws IOException, CSVBadColumnLengthException;
    
    /**
     * Save data to CSV file
     * 
     * @throws IOException if a path is wrong
     */
    public void saveData() throws IOException;

	/**
	 * @return the drivers
	 */
	public List<Driver> getDrivers();

	/**
	 * @return the free drivers
	 */
	public List<Driver> getFreeDrivers();
	
	/**
	 * @param drivers the drivers to set
	 */
	public void setDrivers(List<Driver> drivers);
	
	/**
	 * @param driver the driver to add
	 */
	public void addDriver(Driver driver);

	/**
	 * @param id
	 * @return the driver with the specified id of null if none is found
	 */
	Driver getDriverById(UUID id);
}

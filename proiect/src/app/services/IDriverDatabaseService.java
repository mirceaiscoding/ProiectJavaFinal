package app.services;

import java.io.IOException;
import java.util.List;

import app.entities.driver.Driver;

public interface IDriverDatabaseService {
	
    /**
     * Loads data from CSV file
     * 
     * @throws IOException if a path is wrong
     * @throws CSVBadCollumnLengthException if a line doesn't have the right number of collums
     */
    void loadData() throws IOException, CSVBadCollumnLengthException;
    
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
	 * @param users the users to set
	 */
	public void setDrivers(List<Driver> drivers);
	
	/**
	 * @param user the user to add
	 */
	public void addDriver(Driver driver);
}

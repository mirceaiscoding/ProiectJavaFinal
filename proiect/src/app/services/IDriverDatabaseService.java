package app.services;

import java.util.List;

import app.entities.driver.Driver;

public interface IDriverDatabaseService extends IGenericDatabaseService<Driver> {
	
	/**
	 * @return the free drivers
	 */
	public List<Driver> getFreeDrivers();
	
}

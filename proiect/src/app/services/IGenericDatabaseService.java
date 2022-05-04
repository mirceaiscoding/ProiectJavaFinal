package app.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IGenericDatabaseService <T> {
	
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
	 * @return all the objects of type T
	 */
	public List<T> getAll();
	
	/**
	 * @param drivers the drivers to set
	 */
	public void setAll(List<T> objects);
	
	/**
	 * @param driver the driver to add
	 */
	public void add(T object);

	/**
	 * @param id
	 * @return the object with the specified id of null if none is found
	 */
	T getById(UUID id);

}

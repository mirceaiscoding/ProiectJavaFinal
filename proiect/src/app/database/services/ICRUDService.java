package app.database.services;

import java.sql.SQLException;
import java.util.List;

public interface ICRUDService <T> {
	
	/**
	 * Add a new object to the database
	 * 
	 * @param object
	 * @return if the action was successful
	 * @throws SQLException
	 */
	boolean create(T object) throws SQLException;
	
	/**
	 * @return a list of all objects currently in the database
	 * @throws SQLException 
	 */
	List<T> read() throws SQLException;
	
	/**
	 * @return the object with the specified id or null
	 * @throws SQLException 
	 */
	T getById(String id) throws SQLException;
	
	/**
	 * Update an object in the database
	 * 
	 * @param object 
	 * @return if the action was successful
	 * @throws SQLException 
	 */
	boolean update(T object) throws SQLException;
	
	/**
	 * Delete an object from the database
	 * 
	 * @param object
	 * @return if the action was successful
	 * @throws SQLException 
	 */
	boolean delete(T object) throws SQLException;
	
}

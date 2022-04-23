package app.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import app.entities.user.User;

public interface IUserDatabaseService {
	
    /**
     * Loads data from CSV file
     * 
     * @throws IOException if a path is wrong
     * @throws CSVBadColumnLengthException if a line doesn't have the right number of columns
     */
    void loadData() throws IOException, CSVBadColumnLengthException;
    
    /**
     * Save data to CSV file
     * 
     * @throws IOException if a path is wrong
     */
    public void saveData() throws IOException;


	/**
	 * @return the users
	 */
	public List<User> getUsers();

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users);
	
	/**
	 * @param user the user to add
	 */
	public void addUser(User user);

	/**
	 * @param id
	 * @return the user with the specified id of null if none is found
	 */
	User getUserById(UUID id);

}

package app.services;

import java.io.IOException;
import java.util.List;

import app.entities.user.User;

public interface IUserDatabaseService {
	
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

}

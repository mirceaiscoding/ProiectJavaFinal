package app.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.entities.user.City;
import app.entities.user.Country;
import app.entities.user.User;
import app.entities.user.UserAddress;

/**
 * This class follows the singleton design pattern
 * There can only exist one instance of this class
 */
public class UserCSVDatabaseService implements IUserDatabaseService {

	/**
	 * Path to file where data is stored
	 */
	private static final Path PATH_TO_CSV = Paths.get("data/users.csv");
	
	private static final String COMMA_DELIMITER = ",";
	
	private static final int COLLUMS_NUMBER = 8;

    private static UserCSVDatabaseService instance = null;
	
    private List<User> users = new ArrayList<>();
    
    /**
     * Private constructor
     */
    private UserCSVDatabaseService() {}
    
    /**
     * @return the single instance of this service
     */
    public static UserCSVDatabaseService getInstance() {
        if (instance == null) {        	
        	instance = new UserCSVDatabaseService();
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
    	        Country country = Country.valueOf(values[4]);
    	        City city = City.valueOf(values[5]);
    	        String addressLine1 = values[6];
    	        String addressLine2 = values[7];
    	        
    	        users.add(new User(name, email, phoneNumber, new UserAddress(country, city, addressLine1, addressLine2), id));
    	    }
    	}
    }
    
    @Override
    public void saveData() throws IOException {
    	try (BufferedWriter writer = Files.newBufferedWriter(PATH_TO_CSV)) {
    	    List<String> dataToCSV = users.stream().map(b -> b.toCSV()).toList();
    	    writer.write(String.join("\n", dataToCSV));
    	}
    }

	@Override
	public List<User> getUsers() {
		return users;
	}
	
	@Override
	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public void addUser(User user) {
		users.add(user);
	}

	@Override
	public User getUserById(UUID id) {
		try {			
			User user = (User) users.stream().filter(u -> u.getId().equals(id)).toArray()[0];
			return user;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

}

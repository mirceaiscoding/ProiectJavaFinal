package app.services;

import java.io.IOException;
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
 * This class follows the singleton design pattern There can only exist one
 * instance of this class
 */
public class UserCSVDatabaseService implements IGenericDatabaseService<User> {

	/**
	 * Path to file where data is stored
	 */
	private static final Path PATH_TO_CSV = Paths.get("data/users.csv");

	private static final int COLLUMS_NUMBER = 9;

	private static UserCSVDatabaseService instance = null;

	private List<User> users = new ArrayList<>();

	/**
	 * Private constructor
	 */
	private UserCSVDatabaseService() {
	}

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
		List<String[]> data = CSVReaderServiceSingleton.getInstance().readCSV(PATH_TO_CSV, COLLUMS_NUMBER);
		for (String[] values : data) {
			UUID id = UUID.fromString(values[0]);
			String name = values[1];
			String email = values[2];
			String phoneNumber = values[3];
			Country country = Country.valueOf(values[4]);
			City city = City.valueOf(values[5]);
			String addressLine1 = values[6];
			String addressLine2 = values[7];
			double accountBalance = Double.valueOf(values[8]);

			users.add(new User(name, email, phoneNumber, new UserAddress(country, city, addressLine1, addressLine2), id,
					accountBalance));
		}
	}

	@Override
	public void saveData() throws IOException {
		CSVWriterServiceSingleton.getInstance().write(users, PATH_TO_CSV);
	}

	@Override
	public List<User> getAll() {
		return users;
	}

	@Override
	public void setAll(List<User> users) {
		this.users = users;
	}

	@Override
	public void add(User user) {
		users.add(user);
	}

	@Override
	public User getById(UUID id) {
		try {
			User user = (User) users.stream().filter(u -> u.getId().equals(id)).toArray()[0];
			return user;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

}

package app.database.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.database.DatabaseConfiguration;
import app.entities.user.City;
import app.entities.user.Country;
import app.entities.user.User;
import app.entities.user.UserAddress;

public class UserDatabaseService implements ICRUDService<User> {

	private static UserDatabaseService instance;
	
	private static final Connection connection = DatabaseConfiguration.getDatabaseConnection();
	
	private UserDatabaseService() {}

	/**
	 * @return the single instance of this service
	 */
	public static UserDatabaseService getInstance() {
		if (instance == null) {
			instance = new UserDatabaseService();
		}
		return instance;
	}

	@Override
	public boolean create(User object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		statement.setString(1, object.getId().toString());
		statement.setString(2, object.getName());
		statement.setString(3, object.getEmail());
		statement.setString(4, object.getPhoneNumber());
		statement.setString(5, object.getAddress().getCountry().toString());
		statement.setString(6, object.getAddress().getCity().toString());
		statement.setString(7, object.getAddress().getAdressLine1());
		statement.setString(8, object.getAddress().getAdressLine2());
		statement.setDouble(9, object.getAccountBalance());

		int rowsAffected = statement.executeUpdate();
		return rowsAffected > 0;
	}

	@Override
	public List<User> read() throws SQLException {
		List<User> users = new ArrayList<>();
		Statement statement = connection.createStatement();
		ResultSet cursor = statement.executeQuery("SELECT * FROM Users");
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			UUID id = UUID.fromString(cursor.getString(1));
			String name = cursor.getString(2);
			String email = cursor.getString(3);
			String phoneNumber = cursor.getString(4);
			Country country = Country.valueOf(cursor.getString(5));
			City city = City.valueOf(cursor.getString(6));
			String addressLine1 = cursor.getString(7);
			String addressLine2 = cursor.getString(8);
			double accountBalance = cursor.getDouble(9);
			
			users.add(new User(name, email, phoneNumber, new UserAddress(country, city, addressLine1, addressLine2), id, accountBalance));
		}
		return users;
	}

	@Override
	public boolean update(User object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"UPDATE Users SET name=?, email=?, phone=?, country=?, city=?, addressline1=?, addressline2=?, balance=? WHERE id=?");

		statement.setString(1, object.getName());
		statement.setString(2, object.getEmail());
		statement.setString(3, object.getPhoneNumber());
		statement.setString(4, object.getAddress().getCountry().toString());
		statement.setString(5, object.getAddress().getCity().toString());
		statement.setString(6, object.getAddress().getAdressLine1());
		statement.setString(7, object.getAddress().getAdressLine2());
		statement.setDouble(8, object.getAccountBalance());
		statement.setString(9, object.getId().toString());

		int rowsAffected = statement.executeUpdate();
		return rowsAffected > 0;
	}

	@Override
	public boolean delete(User object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"DELETE FROM Users WHERE id=?");

			statement.setString(1, object.getId().toString());

			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;		
	}

	@Override
	public User getById(String id) throws SQLException {
		User user = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users where id=?");
		statement.setString(1, id);
		ResultSet cursor = statement.executeQuery();
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			String name = cursor.getString(2);
			String email = cursor.getString(3);
			String phoneNumber = cursor.getString(4);
			Country country = Country.valueOf(cursor.getString(5));
			City city = City.valueOf(cursor.getString(6));
			String addressLine1 = cursor.getString(7);
			String addressLine2 = cursor.getString(8);
			double accountBalance = cursor.getDouble(9);
			
			user = new User(name, email, phoneNumber, new UserAddress(country, city, addressLine1, addressLine2), UUID.fromString(id), accountBalance);
			return user;
		}
		return null;
	}

}

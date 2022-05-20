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
import app.entities.driver.Driver;
import app.entities.order.Order;
import app.entities.order.OrderStatus;

public class DriverDatabaseService implements ICRUDService<Driver> {
	
	private static DriverDatabaseService instance;
	
	private static final Connection connection = DatabaseConfiguration.getDatabaseConnection();
	
	private DriverDatabaseService() {}

	/**
	 * @return the single instance of this service
	 */
	public static DriverDatabaseService getInstance() {
		if (instance == null) {
			instance = new DriverDatabaseService();
		}
		return instance;
	}

	@Override
	public boolean create(Driver object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO Drivers VALUES (?, ?, ?, ?, ?)");

		statement.setString(1, object.getId().toString());
		statement.setString(2, object.getName());
		statement.setString(3, object.getEmail());
		statement.setString(4, object.getPhoneNumber());
		statement.setDouble(5, object.getAccountBalance());

		int rowsAffected = statement.executeUpdate();
		return rowsAffected > 0;
	}

	@Override
	public List<Driver> read() throws SQLException {
		List<Driver> drivers = new ArrayList<>();
		Statement statement = connection.createStatement();
		ResultSet cursor = statement.executeQuery("SELECT * FROM Drivers");
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			UUID id = UUID.fromString(cursor.getString(1));
			String name = cursor.getString(2);
			String email = cursor.getString(3);
			String phoneNumber = cursor.getString(4);
			double accountBalance = cursor.getDouble(5);
			
			drivers.add(new Driver(name, email, phoneNumber, id, accountBalance));
		}
		return drivers;
	}

	@Override
	public boolean update(Driver object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"UPDATE Drivers SET name=?, email=?, phone=?, balance=? WHERE id=?");

		statement.setString(1, object.getName());
		statement.setString(2, object.getEmail());
		statement.setString(3, object.getPhoneNumber());
		statement.setDouble(4, object.getAccountBalance());
		statement.setString(5, object.getId().toString());

		int rowsAffected = statement.executeUpdate();
		return rowsAffected > 0;
	}

	@Override
	public boolean delete(Driver object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"DELETE FROM Drivers WHERE id=?");

			statement.setString(1, object.getId().toString());

			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;		
	}

	@Override
	public Driver getById(String id) throws SQLException {
		Driver driver = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM Drivers where id=?");
		statement.setString(1, id);
		ResultSet cursor = statement.executeQuery();
		
		while (cursor.next()) {
			String name = cursor.getString(2);
			String email = cursor.getString(3);
			String phoneNumber = cursor.getString(4);
			double accountBalance = cursor.getDouble(5);
			
			driver = new Driver(name, email, phoneNumber, UUID.fromString(id), accountBalance);
			return driver;
		}
		return null;
	}

	public boolean isFree(Driver driver) {
		List<Order> orders = new ArrayList<>();
		try {
			orders = OrderDatabaseService.getInstance().read();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders.stream()
				.filter(o -> o.getDriver() != null && o.getDriver().getId().equals(driver.getId()))
				.filter(o -> o.getStatus() == OrderStatus.DELIVERING || o.getStatus() == OrderStatus.ARRIVED)
				.count() == 0;
	}


}

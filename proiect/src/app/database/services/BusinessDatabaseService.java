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
import app.entities.business.Business;
import app.entities.business.BusinessFactory;
import app.entities.business.BusinessType;
import app.entities.business.GroceryShop;
import app.entities.business.Product;
import app.entities.business.Restaurant;

public class BusinessDatabaseService implements ICRUDService<Business> {
	
	private static BusinessDatabaseService instance;
	
	private static final Connection connection = DatabaseConfiguration.getDatabaseConnection();
	
	private BusinessDatabaseService() {}

	/**
	 * @return the single instance of this service
	 */
	public static BusinessDatabaseService getInstance() {
		if (instance == null) {
			instance = new BusinessDatabaseService();
		}
		return instance;
	}
	
	@Override
	public boolean create(Business object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO Businesses VALUES (?, ?, ?)");

		statement.setString(1, object.getId().toString());
		String type = "OTHER";
		if (object instanceof GroceryShop) {
			type = "GROCERY_STORE";
		}
		if (object instanceof Restaurant) {
			type = "RESTAURANT";
		}
		statement.setString(2, type);
		statement.setString(3, object.getName());
		
		int rowsAffected = statement.executeUpdate();
		return rowsAffected > 0;
	}

	@Override
	public List<Business> read() throws SQLException {
		List<Business> businesses = new ArrayList<>();
		Statement statement = connection.createStatement();
		ResultSet cursor = statement.executeQuery("SELECT * FROM Businesses");
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			UUID id = UUID.fromString(cursor.getString(1));
			BusinessType type = BusinessType.valueOf(cursor.getString(2));
			String name = cursor.getString(3);
			
			businesses.add(BusinessFactory.makeBusiness(id, type, name));
		}
		
		statement = connection.createStatement();
		cursor = statement.executeQuery("SELECT * FROM BusinessProducts");
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			UUID business_id = UUID.fromString(cursor.getString(1));
			String name = cursor.getString(2);
			double price = cursor.getDouble(3);
			
			Business business = (Business) businesses.stream().filter(b -> b.getId().equals(business_id)).toArray()[0];
			business.addProduct(new Product(name, price));
		}
		return businesses;
	}

	@Override
	public boolean update(Business object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"UPDATE Businesses SET name=? WHERE id=?");

		statement.setString(1, object.getName());
		statement.setString(2, object.getId().toString());

		int rowsAffected = statement.executeUpdate();
		if (rowsAffected == 0) {
			return false;
		}
		
		PreparedStatement deleteStatement = connection.prepareStatement(
			"DELETE FROM BusinessProducts WHERE business_id=?");

		deleteStatement.setString(1, object.getId().toString());
		deleteStatement.executeUpdate();
		
		for (Product product : object.getProducts()) {
			PreparedStatement createStatement = connection.prepareStatement(
				"INSERT INTO BusinessProducts VALUES (?, ?, ?)");

			createStatement.setString(1, object.getId().toString());
			createStatement.setString(2, product.getName());
			createStatement.setDouble(3, product.getPrice());
			
			rowsAffected = createStatement.executeUpdate();
			if (rowsAffected == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean delete(Business object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"DELETE FROM Businesses WHERE id=?");

		statement.setString(1, object.getId().toString());

		int rowsAffected = statement.executeUpdate();
		if (rowsAffected > 0) {
			return false;
		}
		
		PreparedStatement deleteStatement = connection.prepareStatement(
			"DELETE FROM BusinessProducts WHERE business_id=?");

		deleteStatement.setString(1, object.getId().toString());
		deleteStatement.executeUpdate();
		return true;
	}

	@Override
	public Business getById(String id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM Businesses WHERE id=?");
		statement.setString(1, id);
		ResultSet cursor = statement.executeQuery();
		Business business = null;
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			BusinessType type = BusinessType.valueOf(cursor.getString(2));
			String name = cursor.getString(3);
			
			business = BusinessFactory.makeBusiness(UUID.fromString(id), type, name);
			break;
		}
		
		if (business == null) {
			return null;
		}
		
		statement = connection.prepareStatement("SELECT * FROM BusinessProducts WHERE business_id=?");
		statement.setString(1, id);
		cursor = statement.executeQuery();
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			String name = cursor.getString(2);
			double price = cursor.getDouble(3);
			business.addProduct(new Product(name, price));
		}
		return business;
	}
	
}

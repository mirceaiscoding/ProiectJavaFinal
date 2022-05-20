package app.database.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.database.DatabaseConfiguration;
import app.entities.business.Business;
import app.entities.business.Product;
import app.entities.driver.Driver;
import app.entities.order.Order;
import app.entities.order.OrderData;
import app.entities.order.OrderItem;
import app.entities.order.OrderStatus;
import app.entities.user.User;

public class OrderDatabaseService implements ICRUDService<Order> {

	private static OrderDatabaseService instance;
	
	private static final Connection connection = DatabaseConfiguration.getDatabaseConnection();
	
	private OrderDatabaseService() {}

	/**
	 * @return the single instance of this service
	 */
	public static OrderDatabaseService getInstance() {
		if (instance == null) {
			instance = new OrderDatabaseService();
		}
		return instance;
	}
	
	@Override
	public boolean create(Order object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?)");

		statement.setString(1, object.getId().toString());
		
		if (object.getClient() != null) {
			statement.setString(2, object.getClient().getId().toString());
		} else {
			statement.setString(2, "NULL");
		}
		if (object.getBusiness() != null) {
			statement.setString(3, object.getBusiness().getId().toString());
		} else {
			statement.setString(3, "NULL");
		}
		if (object.getDriver() != null) {
			statement.setString(4, object.getDriver().getId().toString());
		} else {
			statement.setString(4, "NULL");
		}
		
	    ZonedDateTime zdt = ZonedDateTime.of(object.getTimePlacedOrder(), ZoneId.systemDefault());
	    long date = zdt.toInstant().toEpochMilli();
		statement.setDate(5, new Date(date));
		
		statement.setString(6, object.getStatus().toString());
		
		int rowsAffected = statement.executeUpdate();
		if (rowsAffected == 0) {
			return false;
		}
		
		for (OrderItem orderItem : object.getData().getOrderItems()) {
			PreparedStatement insertStatement = connection.prepareStatement(
				"INSERT INTO OrderItems VALUES (?, ?, ?, ?)");

			insertStatement.setString(1, object.getId().toString());
			insertStatement.setString(2, orderItem.getProduct().getName());
			insertStatement.setDouble(3, orderItem.getProduct().getPrice());
			insertStatement.setInt(4, orderItem.getQuantity());
			
			rowsAffected = insertStatement.executeUpdate();
			if (rowsAffected == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Order> read() throws SQLException {
		List<Order> orders = new ArrayList<>();
		Statement statement = connection.createStatement();
		ResultSet cursor = statement.executeQuery("SELECT * FROM Orders");
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			UUID id = UUID.fromString(cursor.getString(1));
			User client = UserDatabaseService.getInstance().getById(cursor.getString(2));
			Business business = BusinessDatabaseService.getInstance().getById(cursor.getString(3));
			Driver driver = DriverDatabaseService.getInstance().getById(cursor.getString(4));
			LocalDateTime timePlacedOrder = Instant.ofEpochMilli(cursor.getDate(5).getTime())
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			OrderStatus status = OrderStatus.valueOf(cursor.getString(6));
			
			Order order = new Order(id, client, business, driver, timePlacedOrder, status, new OrderData(new ArrayList<>()));
			orders.add(order);
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM OrderItems WHERE order_id=?");
			preparedStatement.setString(1, id.toString());
			ResultSet orderItemsCursor = preparedStatement.executeQuery();
			
			// Move cursor to next line. Exit when there are no more lines
			while (orderItemsCursor.next()) {
				String name = orderItemsCursor.getString(2);
				double price = orderItemsCursor.getDouble(3);
				int quantity = orderItemsCursor.getInt(4);
				
				order.addOrderItem(new OrderItem(new Product(name, price), quantity));
			}
		}
		
		return orders;
	}

	@Override
	public boolean update(Order object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"UPDATE Orders SET client_id=?, business_id=?, driver_id=?, status=? WHERE id=?");
		
		if (object.getClient() != null) {
			statement.setString(1, object.getClient().getId().toString());
		} else {
			statement.setString(1, "NULL");
		}
		if (object.getBusiness() != null) {
			statement.setString(2, object.getBusiness().getId().toString());
		} else {
			statement.setString(2, "NULL");
		}
		if (object.getDriver() != null) {
			statement.setString(3, object.getDriver().getId().toString());
		} else {
			statement.setString(3, "NULL");
		}

		statement.setString(4, object.getStatus().toString());
		statement.setString(5, object.getId().toString());

		int rowsAffected = statement.executeUpdate();
		return rowsAffected > 0;

	}

	@Override
	public boolean delete(Order object) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
			"DELETE FROM Orders WHERE id=?");

		statement.setString(1, object.getId().toString());

		int rowsAffected = statement.executeUpdate();
		if (rowsAffected > 0) {
			return false;
		}
		
		PreparedStatement deleteStatement = connection.prepareStatement(
			"DELETE FROM OrderItems WHERE order_id=?");

		deleteStatement.setString(1, object.getId().toString());
		deleteStatement.executeUpdate();
		return true;
	}

	@Override
	public Order getById(String id) throws SQLException {
		Order order = null;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders where id=?");
		statement.setString(1, id);
		ResultSet cursor = statement.executeQuery();
		
		// Move cursor to next line. Exit when there are no more lines
		while (cursor.next()) {
			User client = UserDatabaseService.getInstance().getById(cursor.getString(2));
			Business business = BusinessDatabaseService.getInstance().getById(cursor.getString(3));
			Driver driver = DriverDatabaseService.getInstance().getById(cursor.getString(4));
			LocalDateTime timePlacedOrder = Instant.ofEpochMilli(cursor.getDate(5).getTime())
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			OrderStatus status = OrderStatus.valueOf(cursor.getString(6));
			
			order = new Order(UUID.fromString(id), client, business, driver, timePlacedOrder, status, new OrderData(new ArrayList<>()));
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM OrderItems WHERE order_id=?");
			preparedStatement.setString(1, id.toString());
			ResultSet orderItemsCursor = preparedStatement.executeQuery();
			
			// Move cursor to next line. Exit when there are no more lines
			while (orderItemsCursor.next()) {
				String name = orderItemsCursor.getString(2);
				double price = orderItemsCursor.getDouble(3);
				int quantity = orderItemsCursor.getInt(4);
				
				order.addOrderItem(new OrderItem(new Product(name, price), quantity));
			}
			return order;
		}
		return null;
	}
}

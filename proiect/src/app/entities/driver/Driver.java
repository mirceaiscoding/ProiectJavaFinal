package app.entities.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import app.database.services.DriverDatabaseService;
import app.entities.Person;
import app.entities.Rating;
import app.entities.order.Order;
import app.entities.order.OrderStatus;
 

public class Driver extends Person{

	private Rating rating;
		
	private List<Order> deliveredOrders;
	
	private final UUID id;
	
	public Driver(String name, String email, String phoneNumber, Rating rating) {
		super(name, email, phoneNumber);
		this.id = UUID.randomUUID();
		this.rating = rating;
		deliveredOrders = new ArrayList<>();
	}
	
	public Driver(String name, String email, String phoneNumber) {
		super(name, email, phoneNumber);
		this.id = UUID.randomUUID();
		this.rating = new Rating();
		deliveredOrders = new ArrayList<>();
	}
	
	public Driver(String name, String email, String phoneNumber, UUID id) {
		super(name, email, phoneNumber);
		this.id = id;
		this.rating = new Rating();
		deliveredOrders = new ArrayList<>();
	}
	
	public Driver(String name, String email, String phoneNumber, UUID id, double accountBalance) {
		this(name, email, phoneNumber, id);
		this.setAccountBalance(accountBalance);
	}
	
	public Driver(Scanner scanner) {
		super(scanner);
		this.id = UUID.randomUUID();
		this.rating = new Rating();
		deliveredOrders = new ArrayList<>();
	}


	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	/**
	 * @return if the driver has no currentOrder to fulfill
	 */
	public boolean isFree() {
		return DriverDatabaseService.getInstance().isFree(this);
	}

	/**
	 * @return the deliveredOrders
	 */
	public List<Order> getDeliveredOrders() {
		return deliveredOrders;
	}

	/**
	 * @param deliveredOrders the deliveredOrders to set
	 */
	public void setDeliveredOrders(List<Order> deliveredOrders) {
		this.deliveredOrders = deliveredOrders;
	}
	
	/**
	 * @param order the order to deliver
	 * @throws IllegalStateException if driver already has an order to deliver
	 * @throws IllegalArgumentException if order already has a driver assigned or the status is not AwaitingPickUp
	 */
	public void acceptOrder(Order order) {
		if (!isFree()) {
			throw new IllegalStateException("Driver already has an order to fulfill");
		}
		if (order.getDriver() != null) {
			throw new IllegalArgumentException("Order is already being delivered by another driver");
		}
		if (order.getStatus() != OrderStatus.AWAITING_PICKUP) {
			throw new IllegalArgumentException("Order is not awaiting pick up");
		}
		order.setDriver(this);
		order.setStatus(OrderStatus.DELIVERING);
	}
	
	
	/**
	 * @return the object in CSV format
	 */
	public String toCSV() {
		return String.format("%s,%s,%s,%s", id.toString(), super.name, super.email, super.phoneNumber);
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}
	
}
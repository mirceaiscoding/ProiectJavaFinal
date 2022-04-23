package app.entities.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import app.entities.business.Business;
import app.entities.driver.Driver;
import app.entities.user.User;


public class Order implements Comparable<Order> {
	
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private UUID id;
	
	private User client;
	
	private Business business;
	
	private Driver driver;
		
	private LocalDateTime timePlacedOrder;
	
	private OrderStatus status;
	
	private OrderData data;
	
	/**
	 * @param id
	 * @param client
	 * @param business
	 * @param driver
	 * @param timePlacedOrder
	 * @param status
	 * @param data
	 */
	public Order(UUID id, User client, Business business, Driver driver, LocalDateTime timePlacedOrder, OrderStatus status,
			OrderData data) {
		super();
		this.id = id;
		this.client = client;
		this.business = business;
		this.driver = driver;
		this.timePlacedOrder = timePlacedOrder;
		this.status = status;
		this.data = data;
	}
	
	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	
	/**
	 * @return the client
	 */
	public User getClient() {
		return client;
	}


	/**
	 * @param client the client to set
	 */
	public void setClient(User client) {
		this.client = client;
	}


	/**
	 * @return the business
	 */
	public Business getBusiness() {
		return business;
	}


	/**
	 * @param business the business to set
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}


	/**
	 * @return the driver
	 */
	public Driver getDriver() {
		return driver;
	}


	/**
	 * @param driver the driver to set
	 */
	public void setDriver(Driver driver) {
		this.driver = driver;
	}


	/**
	 * @return the timePlacedOrder
	 */
	public LocalDateTime getTimePlacedOrder() {
		return timePlacedOrder;
	}


	/**
	 * @param timePlacedOrder the timePlacedOrder to set
	 */
	public void setTimePlacedOrder(LocalDateTime timePlacedOrder) {
		this.timePlacedOrder = timePlacedOrder;
	}


	/**
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	/**
	 * @return the data
	 */
	public OrderData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(OrderData data) {
		this.data = data;
	}
	
	/**
	 * Add an orderItem to orderData
	 * 
	 * @param item
	 */
	public void addOrderItem(OrderItem item) {
		data.addItem(item);
	}
	
	/**
	 * @return the price of the order
	 */
	public double getPrice() {
		return data.getPrice();
	}
	
	/**
	 * Place the order. Used to recreate the order process only
	 */
	public void placeOrder() {
		client.addOrder(this);
		business.addOrder(this);
	}
	
	/**
	 * Prepare the order. Used to recreate the order process only
	 */
	public void prepareOrder() {
		business.completeOrder(this);
	}
	
	/**
	 * Pick up the order. Used to recreate the order process only
	 */
	public void pickUp() {
		// Driver can only accept orders that have no driver
		Driver finalDriver = driver;
		this.setDriver(null);
		finalDriver.acceptOrder(this);
	}
	
	/**
	 * Deliver the order. Used to recreate the order process only
	 */
	public void deliver() {
		driver.finishDelivery();
	}
	
	@Override
	public int compareTo(Order o) {
		return -this.timePlacedOrder.compareTo(o.timePlacedOrder); // latest first
	}
	
	@Override
	public String toString() {
		return String.format("Order %s: [business: %s], [user: %s], [price: %f], [status: %s]", id.toString(), business.getName(), client.getName(), getPrice(), status.name());
	}
	
	public String toCSV() {
		String clientId = "NULL";
		if (client != null) {
			clientId = client.getId().toString();
		}
		String businessId = "NULL";
		if (business != null) {
			businessId = business.getId().toString();
		}
		String driverId = "NULL";
		if (driver != null) {
			driverId = driver.getId().toString();
		}
		return String.format("%s,%s,%s,%s,%s,%s", 
				id.toString(),
				clientId,
				businessId,
				driverId,
				timePlacedOrder.format(DATE_FORMATTER),
				status.toString());
	}
	
	public String getOrderDataToCSV() {
		return data.toCSV(id);
	}

}
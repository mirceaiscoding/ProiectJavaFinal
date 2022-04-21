package app.entities.order;

import java.time.LocalDateTime;

import app.entities.business.Business;
import app.entities.driver.Driver;
import app.entities.user.User;


public class Order implements Comparable<Order> {
	
	private int id;
	
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
	public Order(int id, User client, Business business, Driver driver, LocalDateTime timePlacedOrder, OrderStatus status,
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
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	
	public double getPrice() {
		return data.getPrice();
	}
	
	public void prepareOrder(Order order) {
		business.completeOrder(order);
	}
	
	@Override
	public int compareTo(Order o) {
		if (this.timePlacedOrder.equals(o.timePlacedOrder)) {
			return this.id - o.id;
		}
		return -this.timePlacedOrder.compareTo(o.timePlacedOrder); // latest first
	}
	
	@Override
	public String toString() {
		return String.format("Order #%d: [business: %s], [user: %s], [price: %f], [status: %s]", id, business.getName(), client.getName(), getPrice(), status.name());
	}
	
//	int id, User client, Business business, Driver driver, LocalDateTime timePlacedOrder, OrderStatus status,
//	OrderData data

//	public String toCSV() {
//		return String.format("%d,", null)
//	}

}
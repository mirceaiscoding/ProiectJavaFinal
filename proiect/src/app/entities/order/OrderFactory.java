package app.entities.order;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.entities.AccountBalanceHolder;
import app.entities.business.Business;
import app.entities.user.User;


public class OrderFactory {
	
	private User client;
	
	private Business business;
	
	private List<OrderItem> orderItems;

	/**
	 * @param client
	 * @param business
	 */
	public OrderFactory(User client, Business business) {
		super();
		this.client = client;
		this.business = business;
		orderItems = new ArrayList<>();
	}
	
	/**
	 * @param item
	 */
	public void addOrderItem(OrderItem item) {
		if (!business.getProducts().contains(item.getProduct())) {
			throw new IllegalArgumentException("Product is not from this restaurant");
		}
		orderItems.add(item);
	}
	
	public void removeOrderItem(OrderItem item) {
		orderItems.remove(item);
	}
	
	/**
	 * Creates a new order for this factory with a unique id
	 * 
	 * @return the order that was placed or null if failed
	 */
	public Order placeNewOrder() {
		OrderData orderData = new OrderData(orderItems); 
		// try to pay for order
		try {			
			double ammount = orderData.getPrice();
			System.out.println("Transfering money from client to business. Ammount: " + ammount);
			AccountBalanceHolder.exchangeMoney(client, business, ammount);
			Order order = new Order(UUID.randomUUID(), client, business, null, LocalDateTime.now(), OrderStatus.SENT, orderData);
			client.addOrder(order);
			business.addOrder(order);
			return order;
		} catch (Exception e) {
			System.out.println("Money exchange failed");
		}
		return null;
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
	 * @return the orderItems
	 */
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	
	
}
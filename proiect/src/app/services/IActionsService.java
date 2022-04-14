package app.services;

import java.util.List;

import app.entities.business.Business;
import app.entities.business.Product;
import app.entities.driver.Driver;
import app.entities.order.Order;
import app.entities.order.OrderFactory;
import app.entities.order.OrderStatus;
import app.entities.user.User;

public interface IActionsService {
	
	public List<User> getUsers();	

	public List<Business> getBusinesses();
	
	public List<Driver> getFreeDrivers();

	public void addNewUser(User user);

	public double addFoundsToUser(User user, double ammount);

	public void addNewDriver(Driver driver);

	public void addProductToBusiness(Product product, Business business);

	public void addNewBusiness(Business restaurant1);

	public Order getOrderById(int id, List<Order> orders);

	public List<Order> getOrdersByStatus(OrderStatus status);
	
	
	// Order process

	/**
	 * Creates an order factory for the user and business
	 * 
	 * @param userThatOrders
	 * @param businessToOrderFrom
	 * @return the order factory
	 */
	public OrderFactory createNewOrderFactory(User userThatOrders, Business businessToOrderFrom);
	
	/**
	 * Place an order created using the order factory.
	 * Order status is now Sent.
	 * 
	 * @param orderFactory
	 * @return the order
	 */
	public Order placeOrder(OrderFactory orderFactory);
	
	/**
	 * Order must have status Sent.
	 * Prepare the order.
	 * Order status is now AwaitingDelivery.
	 * 
	 * @param orderToPrepare
	 */
	public void prepareOrder(Order orderToPrepare);

	/**
	 * Order must have status AwaitingDelivery.
	 * The driver will pickup the order and the delivery will start.
	 * Order status is now Delivering.
	 * 
	 * @param orderToPickup
	 * @param freeDriver the driver that picks up the order
	 */
	public void pickupOrder(Order orderToPickup, Driver freeDriver);

	/**
	 * Order must have status Delivering.
	 * Complete the delivery of an order. It is now waiting for the client to get it
	 * Order status is now Arrived.
	 * 
	 * @param orderToDeliver
	 */
	public void deliverOrder(Order orderToDeliver);
	
	/**
	 * Order must have status Delivering.
	 * Get the order and tip the driver
	 * Order status is now Complete.
	 * 
	 * @param orderToGet
	 * @param tip amount to tip
	 */
	public void getOrderAndTip(Order orderToGet, double tip);
	
}
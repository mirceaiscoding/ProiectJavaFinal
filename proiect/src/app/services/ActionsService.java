package app.services;


import java.util.ArrayList;
import java.util.List;

import app.entities.business.Business;
import app.entities.business.Product;
import app.entities.driver.Driver;
import app.entities.order.Order;
import app.entities.order.OrderFactory;
import app.entities.order.OrderStatus;
import app.entities.user.User;

/**
 * This class follows the singleton design pattern
 */
public class ActionsService implements IActionsService{
	
    private static ActionsService instance = null;
    
    private List<Business> businesses;
    
    private List<User> users;
    
    private List<Driver> drivers;
    
    private List<Order> orders;
    
    /**
     * Private constructor
     */
    private ActionsService() {
		businesses = new ArrayList<>();
		users = new ArrayList<>();
		drivers = new ArrayList<>();
		orders = new ArrayList<>();
	}
    
    /**
     * @return get the single instance of this service
     */
    public static ActionsService getInstance()
    {
        if (instance == null) {        	
        	instance = new ActionsService();
        }
        return instance;
    }

	/**
	 * @return the businesses
	 */
	public List<Business> getBusinesses() {
		return businesses;
	}

	/**
	 * @param businesses the businesses to set
	 */
	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the drivers
	 */
	public List<Driver> getDrivers() {
		return drivers;
	}

	/**
	 * @param drivers the drivers to set
	 */
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	
	/**
	 * @param user the user to add
	 */
	public void addNewUser(User user) {
		users.add(user);
	}
	
	/**
	 * @param user the user to which to add founds
	 * @param ammount value added
	 * @return the new balance of the user
	 */
	public double addFoundsToUser(User user, double ammount) {
		user.addFounds(ammount);
		return user.getAccountBalance();
	}
	
	/**
	 * @param business the business to add
	 */
	public void addNewBusiness(Business business) {
		businesses.add(business);
	}
	
	/**
	 * @param driver the driver to add
	 */
	public void addNewDriver(Driver driver) {
		drivers.add(driver);
	}

	/**
	 * @param userThatOrders
	 * @param businessToOrderFrom
	 * @return order factory
	 */
	public OrderFactory createNewOrderFactory(User userThatOrders, Business businessToOrderFrom) {
		return new OrderFactory(userThatOrders, businessToOrderFrom);
	}

	public void addProductToBusiness(Product product, Business business) {
		business.addProduct(product);
	}

	public Order placeOrder(OrderFactory orderFactory) { 
		Order order = orderFactory.placeNewOrder();
		if (order != null) {			
			orders.add(order);
		}
		return order;
	}

	public void prepareOrder(Order orderToPrepare) {
		orderToPrepare.getBusiness().completeOrder(orderToPrepare);
	}
	
	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}
	
	/**
	 * @param status
	 * @return the orders with this status
	 */
	public List<Order> getOrdersByStatus(OrderStatus status) {
		List<Order> filteredOrders = new ArrayList<Order>();
		for (Order order : orders) {
			if (order.getStatus().equals(status)) {
				filteredOrders.add(order);
			}
		}
		return filteredOrders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 
	 * @param id
	 * @param ordersToPickFrom
	 * @return
	 */
	public Order getOrderById(int id, List<Order> ordersToPickFrom) {
		for (Order order : ordersToPickFrom) {
			if (order.getId() == id) {
				return order;
			}
		}
		return null;
	}

	/**
	 * @return free drivers
	 */
	public List<Driver> getFreeDrivers() {
		List<Driver> freeDrivers = new ArrayList<>();
		for (Driver driver : drivers) {
			if (driver.isFree()) {
				freeDrivers.add(driver);
			}
		}
		return freeDrivers;
	}

	/**
	 * @param orderToPickup
	 * @param freeDriver
	 */
	public void pickupOrder(Order orderToPickup, Driver freeDriver) {
		freeDriver.acceptOrder(orderToPickup);
	}

	/**
	 * @param orderToDeliver
	 */
	public void deliverOrder(Order orderToDeliver) {
		orderToDeliver.getDriver().finishDelivery();
	}

	public void getOrderAndTip(Order orderToGet, double tip) {
		orderToGet.getClient().pickUp(orderToGet, tip);
		
	}
	
}
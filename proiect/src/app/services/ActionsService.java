package app.services;


import app.entities.user.User;

/**
 * This class follows the singleton design pattern
 */
public class ActionsService implements IActionsService{
	
    private static ActionsService instance = null;
    
    /**
     * Private constructor
     */
    private ActionsService() {}
    
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
	 * @param user the user to which to add founds
	 * @param ammount value added
	 * @return the new balance of the user
	 */
	public double addFoundsToUser(User user, double ammount) {
		user.addFounds(ammount);
		return user.getAccountBalance();
	}

//	/**
//	 * @param userThatOrders
//	 * @param businessToOrderFrom
//	 * @return order factory
//	 */
//	public OrderFactory createNewOrderFactory(User userThatOrders, Business businessToOrderFrom) {
//		return new OrderFactory(userThatOrders, businessToOrderFrom);
//	}
//
//	public void addProductToBusiness(Product product, Business business) {
//		business.addProduct(product);
//	}

//	public Order placeOrder(OrderFactory orderFactory) { 
//		Order order = orderFactory.placeNewOrder();
//		if (order != null) {			
//			orders.add(order);
//		}
//		return order;
//	}

//	public void prepareOrder(Order orderToPrepare) {
//		orderToPrepare.getBusiness().completeOrder(orderToPrepare);
//	}
	
	
//	/**
//	 * @param status
//	 * @return the orders with this status
//	 */
//	public List<Order> getOrdersByStatus(OrderStatus status) {
//		List<Order> filteredOrders = new ArrayList<>();
//		for (Order order : orders) {
//			if (order.getStatus().equals(status)) {
//				filteredOrders.add(order);
//			}
//		}
//		return filteredOrders;
//	}


//	/**
//	 * 
//	 * @param id
//	 * @param ordersToPickFrom
//	 * @return
//	 */
//	public Order getOrderById(int id, List<Order> ordersToPickFrom) {
//		for (Order order : ordersToPickFrom) {
//			if (order.getId() == id) {
//				return order;
//			}
//		}
//		return null;
//	}


//	/**
//	 * @param orderToPickup
//	 * @param freeDriver
//	 */
//	public void pickupOrder(Order orderToPickup, Driver freeDriver) {
//		freeDriver.acceptOrder(orderToPickup);
//	}
//
//	/**
//	 * @param orderToDeliver
//	 */
//	public void deliverOrder(Order orderToDeliver) {
//		orderToDeliver.getDriver().finishDelivery();
//	}
//
//	public void getOrderAndTip(Order orderToGet, double tip) {
//		orderToGet.getClient().pickUp(orderToGet, tip);
//		
//	}
	
}
package app.services;

import app.entities.user.User;

public interface IActionsService {
	

	double addFoundsToUser(User user, double ammount);

	
	// Order process

//	/**
//	 * Creates an order factory for the user and business
//	 * 
//	 * @param userThatOrders
//	 * @param businessToOrderFrom
//	 * @return the order factory
//	 */
//	OrderFactory createNewOrderFactory(User userThatOrders, Business businessToOrderFrom);
//	
//	/**
//	 * Place an order created using the order factory.
//	 * Order status is now Sent.
//	 * 
//	 * @param orderFactory
//	 * @return the order
//	 */
//	Order placeOrder(OrderFactory orderFactory);
//	
//	/**
//	 * Order must have status Sent.
//	 * Prepare the order.
//	 * Order status is now AwaitingDelivery.
//	 * 
//	 * @param orderToPrepare
//	 */
//	void prepareOrder(Order orderToPrepare);
//
//	/**
//	 * Order must have status AwaitingDelivery.
//	 * The driver will pickup the order and the delivery will start.
//	 * Order status is now Delivering.
//	 * 
//	 * @param orderToPickup
//	 * @param freeDriver the driver that picks up the order
//	 */
//	void pickupOrder(Order orderToPickup, Driver freeDriver);
//
//	/**
//	 * Order must have status Delivering.
//	 * Complete the delivery of an order. It is now waiting for the client to get it
//	 * Order status is now Arrived.
//	 * 
//	 * @param orderToDeliver
//	 */
//	void deliverOrder(Order orderToDeliver);
//	
//	/**
//	 * Order must have status Delivering.
//	 * Get the order and tip the driver
//	 * Order status is now Complete.
//	 * 
//	 * @param orderToGet
//	 * @param tip amount to tip
//	 */
//	void getOrderAndTip(Order orderToGet, double tip);
	
}
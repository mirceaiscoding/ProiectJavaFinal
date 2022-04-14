package app.entities.order;


import java.util.List;

public class OrderData {

	private List<OrderItem> orderItems;

	/**
	 * @param orderItems
	 */
	public OrderData(List<OrderItem> orderItems) {
		super();
		this.orderItems = orderItems;
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

	/**
	 * @return the price of the order
	 */
	double getPrice() {
		double price = 0;
		for (OrderItem item : orderItems) {
			price += item.getPrice();
		}
		return price;
	}

}
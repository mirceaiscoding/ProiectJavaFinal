package app.entities.order;


import java.util.List;
import java.util.UUID;

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
	
	public String toCSV(UUID orderId) {
		return String.join("\n", orderItems.stream().map(o -> o.toCSV(orderId)).toList());
	}

	/**
	 * @param item the item to add
	 */
	public void addItem(OrderItem item) {
		orderItems.add(item);
	}

}
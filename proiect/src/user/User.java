package user;

import java.util.Scanner;
import java.util.TreeSet;

import order.Order;
import order.OrderStatus;
import shared.AccountBalanceHolder;
import shared.Person;

public class User extends Person {

	private UserAddress address;
	
	public User(String name, String email, String phoneNumber, UserAddress adress) {
		super(name, email, phoneNumber);
		this.address = adress;
	}
	
	/**
	 * Construct user from scanner
	 * 
	 * @param scanner
	 */
	public User(Scanner scanner) {
		super(scanner);
		address = new UserAddress(scanner);
	}

	/**
	 * Keeps orders sorted
	 * Tree set can be used because there can not exist duplicate orders
	 * https://stackoverflow.com/questions/8725387/why-is-there-no-sortedlist-in-java
	 */
	private TreeSet<Order> orders = new TreeSet<Order>();

	/**
	 * @return the address
	 */
	public UserAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAdress(UserAddress address) {
		this.address = address;
	}

	/**
	 * @return the orders
	 */
	public TreeSet<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(TreeSet<Order> orders) {
		this.orders = orders;
	}
	
	/**
	 * @param order the order to add
	 */
	public void addOrder(Order order) {
		orders.add(order);
	}

	/**
	 * @param order
	 * @param tip
	 */
	public void pickUp(Order order, double tip) {
		if (order.getClient() != this) {
			throw new IllegalArgumentException("The order is not for this client");
		}
		if (order.getStatus() != OrderStatus.Arrived) {
			throw new IllegalArgumentException("The order has not arrived yet");
		}
		order.setStatus(OrderStatus.Done);
		
		// Give tip to driver
		AccountBalanceHolder.exchangeMoney(this, order.getDriver(), tip);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "\n" + address;
	}
}
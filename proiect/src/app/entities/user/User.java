package app.entities.user;

import java.util.Scanner;
import java.util.TreeSet;
import java.util.UUID;

import app.entities.AccountBalanceHolder;
import app.entities.Person;
import app.entities.order.Order;
import app.entities.order.OrderStatus;

public class User extends Person {

	private UserAddress address;
	
	private final UUID id;
	
	public User(String name, String email, String phoneNumber, UserAddress adress, UUID id, double accountBalance) {
		super(name, email, phoneNumber);
		this.id = id;
		this.address = adress;
		this.setAccountBalance(accountBalance);
	}
	
	/**
	 * @param name
	 * @param email
	 * @param phoneNumber
	 * @param adress
	 */
	public User(String name, String email, String phoneNumber, UserAddress adress) {
		super(name, email, phoneNumber);
		this.id = UUID.randomUUID();
		this.address = adress;
	}
	
	/**
	 * Construct user from scanner
	 * 
	 * @param scanner
	 */
	public User(Scanner scanner) {
		super(scanner);
		this.id = UUID.randomUUID();
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
		if (order.getStatus() != OrderStatus.ARRIVED) {
			throw new IllegalArgumentException("The order has not arrived yet");
		}
		order.setStatus(OrderStatus.DONE);
		
		// Give tip to driver
		AccountBalanceHolder.exchangeMoney(this, order.getDriver(), tip);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "\n" + address;
	}
	
	public String toCSV() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%f", 
				id.toString(),
				super.name, 
				super.email, 
				super.phoneNumber,
				address.getCountry().toString(),
				address.getCity().toString(),
				address.getAdressLine1(),
				address.getAdressLine2(),
				getAccountBalance());
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}
}
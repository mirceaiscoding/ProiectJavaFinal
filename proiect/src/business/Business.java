package business;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import order.Order;
import order.OrderStatus;
import shared.AccountBalanceHolder;
import shared.Rating;


public class Business extends AccountBalanceHolder{
	
	private String name;
	
	private Rating rating;
	
	private List<Product> products;
	
	private List<Order> futureOrders;
	
	private List<Order> completedOrders;
	
	/**
	 * @param name
	 * @param rating
	 */
	public Business(String name, Rating rating) {
		super();
		this.name = name;
		this.rating = rating;
		products = new ArrayList<>();
		futureOrders = new ArrayList<>();
		completedOrders = new ArrayList<>();
	}
	
	public Business(Scanner scanner) {
		super();
		System.out.println("Business name:\n");
		name = scanner.nextLine();
		rating = new Rating();
		products = new ArrayList<>();
		futureOrders = new ArrayList<>();
		completedOrders = new ArrayList<>();
	}

	public List<Order> getFutureOrders() {
		return futureOrders;
	}

	public void setFutureOrders(List<Order> futureOrders) {
		this.futureOrders = futureOrders;
	}

	public List<Order> getCompletedOrders() {
		return completedOrders;
	}

	public void setCompletedOrders(List<Order> completedOrders) {
		this.completedOrders = completedOrders;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	/**
	 * @param order the order to add to futureOrders
	 * @throws IllegalArgumentException if order is not for this business or the status is not "Sent"
	 */
	public void addOrder(Order order) {
		if (order.getBusiness() != this || order.getStatus() != OrderStatus.Sent) {
			throw new IllegalArgumentException("This order can not be added");
		}
		this.futureOrders.add(order);
		order.setStatus(OrderStatus.Preparing);
	}
	
	/**
	 * @param order the order to complete and move to completedOrders
	 * @throws IllegalArgumentException if order is not in futureOrders or status is not "Preparing"
	 */
	public void completeOrder(Order order) {
		if (!futureOrders.contains(order) || order.getStatus() != OrderStatus.Preparing) {
			throw new IllegalArgumentException("This order can not be completed");
		}
		this.completedOrders.add(order);
		this.futureOrders.remove(order);
		order.setStatus(OrderStatus.AwaitingPickUp);
	}

	/**
	 * @return the products
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("Business: [name: %s]", name) + "\n" + super.toString();
	}

	public void addProduct(Product product) {
		products.add(product);
	}

	public void printProducts() {
		for (int i = 0; i < products.size(); i++) {
			System.out.printf("#%d: %s%n", i, products.get(i));
		}
		
	}

}
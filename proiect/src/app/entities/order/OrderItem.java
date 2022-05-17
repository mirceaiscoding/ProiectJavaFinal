package app.entities.order;


import java.util.Scanner;
import java.util.UUID;

import app.entities.business.Product;

public class OrderItem {

	private Product product;
	
	private int quantity;
	
	/**
	 * Construct a new OrderItem
	 * 
	 * @param product
	 * @param quantity
	 */
	public OrderItem(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}
	
	/**
	 * Construct a new OrderItem with data from the scanner
	 * 
	 * @param scanner
	 */
	public OrderItem(Scanner scanner) {
		product = new Product(scanner);
		System.out.println("Enter quntity:");
		while (!scanner.hasNextInt()) {
			scanner.next();
		}
		quantity = scanner.nextInt();
	}
	

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @return the price of the item
	 */
	public double getPrice() {
		return product.getPrice() * quantity;
	}
	
	public String toCSV(UUID orderId) {
		return String.format("%s,%s,%f,%d", orderId.toString(), product.getName(), product.getPrice(), quantity);
	}
}
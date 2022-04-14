package business;

import java.util.Scanner;

public class Product {

	private String name;
	
	private double price;

	/**
	 * @param name
	 * @param price
	 */
	public Product(String name, double price) {
		this.name = name;
		this.price = price;
	}
	
	public Product(Scanner scanner) {
		System.out.println("Enter product name:");
		name = scanner.nextLine();
		System.out.println("Enter product price:");
		while (!scanner.hasNextDouble()) {
			scanner.next();
		}
		price = scanner.nextDouble();
		scanner.nextLine();
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
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return String.format("Product: [name: %s], [price: %f]", name, price);
	}
	
}
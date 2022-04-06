package app;

import java.util.List;
import java.util.Scanner;

import business.*;
import driver.*;
import order.*;
import user.*;
import shared.*;

public class Main {
	
	private static final String[] COMMANDS = { 
			"Add user", "Add Business", "Add driver",
			"Get user account balance", "Add founds", "Add product to business",
			"Place order", "Prepare order", "Find driver to pickup order", "Deliver order", "Get order and tip",
			"Exit" };
	
	private static void printCommands() {
		for (int i = 0; i < COMMANDS.length; i++) {
			System.out.printf("#%d: %s%n", i, COMMANDS[i]);
		}
	}
	
	private static void printUsers(List<User> users) {
		for (int i = 0; i < users.size(); i++) {
			System.out.printf("#%d: %s%n", i, users.get(i).getName());
		}
	}
	
	private static void printBusinesses(List<Business> businesses) {
		for (int i = 0; i < businesses.size(); i++) {
			System.out.printf("#%d: %s%n", i, businesses.get(i).getName());
		}
	}

	private static void printOrders(List<Order> orders) {
		for (int i = 0; i < orders.size(); i++) {
			System.out.println(orders.get(i));
		}
	}
	
	private static void printDrivers(List<Driver> drivers) {
		for (int i = 0; i < drivers.size(); i++) {
			System.out.printf("#%d: %s%n", i, drivers.get(i).getName());
		}
	}
	
	/**
	 * Prints a delimiter line
	 */
	private static void printDelimiter() {
		System.out.println("-------------------------------------------------------------------");
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		IActionsService service = ActionsService.getInstance();
		
		// Initial data
		User user1 = new User("Bina Mircea", "bina.mircea@yahoo.com", "0777123123", new UserAddress(Country.Romania , City.Craiova, "str Balcescu", "12"));
		service.addNewUser(user1);
		service.addFoundsToUser(user1, 100);
		
		Driver driver1 = new Driver("Popoescu Andrei", "andrei@yahoo.com", "0766666666");
		service.addNewDriver(driver1);
		
		Business restaurant1 = new Restaurant("Starbucks", new Rating());
		service.addProductToBusiness(new Product("Espresso", 10), restaurant1);
		service.addProductToBusiness(new Product("Latte", 13), restaurant1);
		service.addProductToBusiness(new Product("Frapuccinno", 15), restaurant1);
		service.addProductToBusiness(new Product("Pancakes", 10), restaurant1);
		service.addNewBusiness(restaurant1);
		
		// Actions
		try {
			while (true) {
				printCommands();
				System.out.println("Enter the command number");
				while(!scanner.hasNextInt()) {
					scanner.next();
				}
				int i = scanner.nextInt();
				scanner.nextLine(); // consume \n
				System.out.println(COMMANDS[i]);
				switch (i) {
				case 0: {
					// Add user
					User user = new User(scanner);
					service.addNewUser(user);
					printDelimiter();
					System.out.println("Added user:\n" + user);
					printDelimiter();
					break;
				}
				case 1: {
					// Add business
					System.out.println("Business type: [Restaurant], [GroceryShop], [other]");
					String type = scanner.nextLine();
					System.out.println("Business name:");
					String name = scanner.nextLine();
					Business business = BusinessFactory.makeBusiness(type, name);
					service.addNewBusiness(business);
					printDelimiter();
					System.out.println("Added business:\n" + business);
					printDelimiter();
					break;
				}
				case 2: {
					// Add driver
					Driver driver = new Driver(scanner);
					service.addNewDriver(driver);
					printDelimiter();
					System.out.println("Added driver:\n" + driver);
					printDelimiter();
					break;
				}
				case 3:
					// Get user account balance
					User user = pickUser(scanner, service);
					printDelimiter();
					System.out.println("User balance: " + user.getAccountBalance());
					printDelimiter();
					break;
				case 4:
					// Add founds
					User userToAddFounds = pickUser(scanner, service);
					printDelimiter();
					System.out.println("Current user balance: " + userToAddFounds.getAccountBalance());
					printDelimiter();
					System.out.println("Enter the ammount to add");
					while(!scanner.hasNextDouble()) {
						scanner.next();
					}
					double ammount = scanner.nextDouble();
					service.addFoundsToUser(userToAddFounds, ammount);
					printDelimiter();
					System.out.println("New user balance: " + userToAddFounds.getAccountBalance());
					printDelimiter();
					break;
				case 5:
					// Add product to business
					Business business = pickBusiness(scanner, service);
					Product product = new Product(scanner);
					service.addProductToBusiness(product, business);
					printDelimiter();
					System.out.println("New business products: " + business.getProducts());
					printDelimiter();
					break;
				case 6:
					// Place order
					User userThatOrders = pickUser(scanner, service);
					Business businessToOrderFrom = pickBusiness(scanner, service);
					OrderFactory newOrderFactory = service.createNewOrderFactory(userThatOrders, businessToOrderFrom);
					
					boolean finish = false;
					while (!finish) {
						System.out.println("Order maker options: [Add], [Finish]");
						String response = scanner.nextLine();
						switch (response) {
						case "Add":
							System.out.println("Enter product number:");
							businessToOrderFrom.printProducts();
							while(!scanner.hasNextInt()) {
								scanner.next();
							}
							int productIndex = scanner.nextInt();
							scanner.nextLine();
							System.out.println("Quantity:");
							Product productToAdd = businessToOrderFrom.getProducts().get(productIndex); 
							while(!scanner.hasNextInt()) {
								scanner.next();
							}
							int quantity = scanner.nextInt();
							scanner.nextLine();
							newOrderFactory.addOrderItem(new OrderItem(productToAdd, quantity));
							break;
						case "Finish":
							if (newOrderFactory.getOrderItems().isEmpty()) {
								System.out.println("Can't place an empty order");
							} else {								
								finish = true;
							}
							break;
						default:
							System.out.println("No action selected");
							break;
						}
					}
					Order order = service.placeOrder(newOrderFactory);
					if (order != null) {
						printDelimiter();
						System.out.println("Placed order: " + order);
						printDelimiter();
					}
					break;
				case 7:
					// Prepare order
					System.out.println("Enter order number:");
					List<Order> ordersToPrepare = service.getOrdersByStatus(OrderStatus.Preparing);
					if (ordersToPrepare.isEmpty()) {
						System.out.println("No orders to prepare. Place an order first");
						break;
					}
					printOrders(ordersToPrepare);
					while(!scanner.hasNextInt()) {
						scanner.next();
					}
					Order orderToPrepare = service.getOrderById(scanner.nextInt(), ordersToPrepare);		
					if (orderToPrepare == null) {
						System.out.println("No order with this id");
						break;
					}
					service.prepareOrder(orderToPrepare);
					printDelimiter();
					System.out.println("Prepared order: " + orderToPrepare);
					printDelimiter();
					break;
				case 8:
					// Find driver to pickup order
					List<Order> ordersAwaitingPickup = service.getOrdersByStatus(OrderStatus.AwaitingPickUp);
					if (ordersAwaitingPickup.isEmpty()) {
						System.out.println("No orders awaiting pickup. Prepare an order first");
						break;
					}
					printOrders(ordersAwaitingPickup);
					System.out.println("Enter order number:");
					while(!scanner.hasNextInt()) {
						scanner.next();
					}
					Order orderToPickup = service.getOrderById(scanner.nextInt(), ordersAwaitingPickup);	
					List<Driver> freeDrivers = service.getFreeDrivers();
					if (freeDrivers.isEmpty()) {
						System.out.println("No free drivers at the moment. Try again later");
						break;
					}
					printDrivers(freeDrivers);
					while(!scanner.hasNextInt()) {
						scanner.next();
					}
					Driver freeDriver = freeDrivers.get(scanner.nextInt());
					service.pickupOrder(orderToPickup, freeDriver);
					printDelimiter();
					System.out.println("Pickup order: " + orderToPickup);
					printDelimiter();
					break;
				case 9:
					// Deliver order
					List<Order> ordersToDeliver = service.getOrdersByStatus(OrderStatus.Delivering);
					if (ordersToDeliver.isEmpty()) {
						System.out.println("No orders to deliver. Pickup an order first");
						break;
					}
					printOrders(ordersToDeliver);
					while(!scanner.hasNextInt()) {
						scanner.next();
					}
					Order orderToDeliver = service.getOrderById(scanner.nextInt(), ordersToDeliver);	
					service.deliverOrder(orderToDeliver);
					printDelimiter();
					System.out.println("Delivered order: " + orderToDeliver);
					printDelimiter();
					break;
				case 10:
					// Get order and tip
					List<Order> ordersToGet = service.getOrdersByStatus(OrderStatus.Arrived);
					if (ordersToGet.isEmpty()) {
						System.out.println("No orders to get. Deliver an order first");
						break;
					}
					printOrders(ordersToGet);
					System.out.println("Enter order number:");
					while(!scanner.hasNextInt()) {
						scanner.next();
					}
					Order orderToGet = service.getOrderById(scanner.nextInt(), ordersToGet);
					System.out.println("Enter tip amount:");
					while(!scanner.hasNextDouble()) {
						scanner.next();
					}
					double tip = scanner.nextDouble();
					try {
						service.getOrderAndTip(orderToGet, tip);
						printDelimiter();
						System.out.println("Completed order: " + orderToGet);
						printDelimiter();
					} catch (Exception e) {
						System.out.println("Couldn't complete order");
					}
					break;
				case 11:
					// Exit
					System.out.println("Exit app");
					return;
				default:
					System.out.println("Unexpected value: " + i); 
				}
			}
		} finally {
			// Always called
			scanner.close();
		}
	}


	/**
	 * @param scanner
	 * @param service
	 * @return the picked business
	 */
	private static Business pickBusiness(Scanner scanner, IActionsService service) {
		List<Business> businesses = service.getBusinesses();
		printBusinesses(businesses);
		System.out.println("Enter the business number");
		while(!scanner.hasNextInt()) {
			scanner.next();
		}
		int userNumber = scanner.nextInt();
		scanner.nextLine(); // consume \n
		
		return businesses.get(userNumber);
	}

	/**
	 * @param scanner
	 * @param service
	 * @return the picked user
	 */
	public static User pickUser(Scanner scanner, IActionsService service) {
		List<User> users = service.getUsers();
		printUsers(users);
		System.out.println("Enter the user number");
		while(!scanner.hasNextInt()) {
			scanner.next();
		}
		int userNumber = scanner.nextInt();
		scanner.nextLine(); // consume \n
		
		return users.get(userNumber);
	}

}
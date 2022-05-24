package app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import app.database.DatabaseInitializer;
import app.database.services.BusinessDatabaseService;
import app.database.services.DriverDatabaseService;
import app.database.services.ICRUDService;
import app.database.services.OrderDatabaseService;
import app.database.services.UserDatabaseService;
import app.entities.business.Business;
import app.entities.business.BusinessFactory;
import app.entities.business.BusinessType;
import app.entities.business.Product;
import app.entities.driver.Driver;
import app.entities.order.Order;
import app.entities.order.OrderFactory;
import app.entities.order.OrderItem;
import app.entities.order.OrderStatus;
import app.entities.user.User;
import app.services.ActionsService;
import app.services.AuditService;
import app.services.IActionsService;


public class Main {
	
	private static final String[] COMMANDS = { 
			"Add user", "Add Business", "Add driver",
			"Get user account balance", "Add founds", "Add product to business",
			"Place order", "Prepare order", "Find driver to pickup order", "Deliver order", "Get order and tip",
			"Delete order", "Exit" };
	
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
			System.out.printf("#%d: %s%n", i, orders.get(i).toString());
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
		
		try {
			
			Scanner scanner = new Scanner(System.in);
			
			IActionsService actionsService = ActionsService.getInstance();
			
			DatabaseInitializer.createTables();
			
			ICRUDService<Business> businessDatabaseService = BusinessDatabaseService.getInstance();
			ICRUDService<User> userDatabaseService = UserDatabaseService.getInstance();
			ICRUDService<Driver> driverDatabaseService = DriverDatabaseService.getInstance();
			ICRUDService<Order> orderDatabaseService = OrderDatabaseService.getInstance();
			
			AuditService auditService = AuditService.getInstance();
			
			System.out.println("LOADED BUSINESS FROM CSV: " + businessDatabaseService.read());
			
			System.out.println("LOADED USERS FROM CSV: " + userDatabaseService.read());
			
			System.out.println("LOADED DRIVERS FROM CSV: " + driverDatabaseService.read());
			
			System.out.println("LOADED ORDERS FROM CSV: " + orderDatabaseService.read());
			
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
					if (i >= 0 && i < 12) {					
						System.out.println(COMMANDS[i]);
					}
					switch (i) {
					case 0: {
						// Add user
						User user = new User(scanner);
						try {
							userDatabaseService.create(user);
							printDelimiter();
							System.out.println("Added user:\n" + user);
							 auditService.logMessage(String.format("Added user: id: %s name: %s", user.getId().toString(), user.getName()));
							printDelimiter();
						} catch (IOException e) {
							System.out.println("Error while adding user");
							e.printStackTrace();
						}
						break;
					}
					case 1: {
						// Add business
						System.out.println("Business type: [RESTAURANT], [GROCERY_STORE], [other]");
						String type = scanner.nextLine();
						System.out.println("Business name:");
						String name = scanner.nextLine();
						Business business = BusinessFactory.makeBusiness(BusinessType.valueOf(type), name);
						try {
							businessDatabaseService.create(business);
							printDelimiter();
							System.out.println("Added business:\n" + business);
							auditService.logMessage(String.format("Added business: id: %s name: %s", business.getId().toString(), business.getName()));
							printDelimiter();
						} catch (IOException e1) {
							System.out.println("Error while adding business");
							e1.printStackTrace();
						}
						break;
					}
					case 2: {
						// Add driver
						Driver driver = new Driver(scanner);
						try {
							driverDatabaseService.create(driver);
							printDelimiter();
							System.out.println("Added driver:\n" + driver);
							auditService.logMessage(String.format("Added driver: id: %s name: %s", driver.getId().toString(), driver.getName()));
							printDelimiter();
						} catch (IOException e) {
							System.out.println("Error while adding driver");
							e.printStackTrace();
						}
						break;
					}
					case 3:
						// Get user account balance
						try {
							User user = pickUser(scanner, userDatabaseService);
							printDelimiter();
							System.out.println("User balance: " + user.getAccountBalance());
							auditService.logMessage(String.format("Get user account balance: id: %s balance: %f", user.getId().toString(), user.getAccountBalance()));
							printDelimiter();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						break;
					case 4:
						// Add founds
						User userToAddFounds = pickUser(scanner, userDatabaseService);
						printDelimiter();
						System.out.println("Current user balance: " + userToAddFounds.getAccountBalance());
						printDelimiter();
						System.out.println("Enter the ammount to add");
						while(!scanner.hasNextDouble()) {
							scanner.next();
						}
						double ammount = scanner.nextDouble();
						try {
							actionsService.addFoundsToUser(userToAddFounds, ammount);
							userDatabaseService.update(userToAddFounds);
							printDelimiter();
							System.out.println("New user balance: " + userToAddFounds.getAccountBalance());
							auditService.logMessage(String.format("Added to user account balance: id: %s new balance: %f", userToAddFounds.getId().toString(), userToAddFounds.getAccountBalance()));
							printDelimiter();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						break;
					case 5:
						// Add product to business
						Business business = pickBusiness(scanner, actionsService, businessDatabaseService);
						Product product = new Product(scanner);
						try {
							business.addProduct(product);
							businessDatabaseService.update(business);
							printDelimiter();
							System.out.println("New business products: " + business.getProducts());
							auditService.logMessage(String.format("Added product to business: business id: %s product: %s", business.getId().toString(), product.getName()));
							printDelimiter();
						} catch (IOException e1) {
							System.out.println("Error while adding product to business");
							e1.printStackTrace();
						}
						break;
					case 6:
						// Place order
						User userThatOrders = pickUser(scanner, userDatabaseService);
						Business businessToOrderFrom = pickBusiness(scanner, actionsService, businessDatabaseService);
						OrderFactory newOrderFactory = new OrderFactory(userThatOrders, businessToOrderFrom);
						
						boolean finish = false;
						while (!finish) {
							System.out.println("Order maker options: [Add], [Finish]");
							String response = scanner.nextLine();
							switch (response) {
							case "Add":
								Product productToAdd = pickProductFromBusiness(scanner, actionsService, businessToOrderFrom);
								System.out.println("Quantity:");
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
						try {
							Order order = newOrderFactory.placeNewOrder();
							if (order != null && order.getPrice() != 0) {
								printDelimiter();
								System.out.println("Placed order: " + order);
								auditService.logMessage(String.format("Placed order: id: business: %s client: %s", order.getId().toString(), businessToOrderFrom.getName(), userThatOrders.getName()));
								orderDatabaseService.create(order);
								userDatabaseService.update(userThatOrders);
								businessDatabaseService.update(businessToOrderFrom);
								printDelimiter();
							} else {
								System.out.println("Order can't be empty");
							}
						} catch (IOException e) {
							System.out.println("Error placing the order");
						}
						break;
					case 7:
						// Prepare order
						System.out.println("Enter order number:");
						List<Order> ordersToPrepare = getOrdersByStatus(orderDatabaseService, OrderStatus.PREPERING);
						if (ordersToPrepare.isEmpty()) {
							System.out.println("No orders to prepare. Place an order first");
							break;
						}
						Order orderToPrepare = pickOrder(scanner, ordersToPrepare);
						try {
							orderToPrepare.prepareOrder();
							orderDatabaseService.update(orderToPrepare);
							printDelimiter();
							System.out.println("Prepared order: " + orderToPrepare);
							auditService.logMessage(String.format("Prepared order: id: %s", orderToPrepare.getId().toString()));
							printDelimiter();
						} catch (IOException e) {
							System.out.println("Error preparing order");
						}
						break;
					case 8:
						// Find driver to pickup order
						List<Order> ordersAwaitingPickup = getOrdersByStatus(orderDatabaseService, OrderStatus.AWAITING_PICKUP);
						if (ordersAwaitingPickup.isEmpty()) {
							System.out.println("No orders awaiting pickup. Prepare an order first");
							break;
						}
						Order orderToPickup = pickOrder(scanner, ordersAwaitingPickup);
						Driver freeDriver = pickFreeDriver(scanner, driverDatabaseService);
						if (freeDriver == null) {
							break;
						}
						try {
							orderToPickup.setDriver(freeDriver);
							orderToPickup.pickUp();
							orderDatabaseService.update(orderToPickup);
							driverDatabaseService.update(freeDriver);
							printDelimiter();
							System.out.println("Pickup order: " + orderToPickup);
							auditService.logMessage(String.format("Picked up order: id: %s driver: %s", orderToPickup.getId().toString(), freeDriver.getName()));
							printDelimiter();
						} catch (IOException e) {
							System.out.println("Error picking up order");
						}
						break;
					case 9:
						// Deliver order
						List<Order> ordersToDeliver = getOrdersByStatus(orderDatabaseService, OrderStatus.DELIVERING);
						Order orderToDeliver = pickOrder(scanner, ordersToDeliver);
						orderToDeliver.deliver();
						orderDatabaseService.update(orderToDeliver);
						printDelimiter();
						System.out.println("Delivered order: " + orderToDeliver);
						printDelimiter();
						break;
					case 10:
						// Get order and tip
						List<Order> ordersToGet = getOrdersByStatus(orderDatabaseService, OrderStatus.ARRIVED);
						Order orderToGet = pickOrder(scanner, ordersToGet);
						System.out.println("Enter tip amount:");
						while(!scanner.hasNextDouble()) {
							scanner.next();
						}
						double tip = scanner.nextDouble();
						try {
							orderToGet.getClient().pickUp(orderToGet, tip);
							orderDatabaseService.update(orderToGet);
							driverDatabaseService.update(orderToGet.getDriver());
							userDatabaseService.update(orderToGet.getClient());
							orderDatabaseService.update(orderToGet);
							printDelimiter();
							System.out.println("Completed order: " + orderToGet);
							auditService.logMessage(String.format("Completed order: id: %s", orderToGet.getId().toString()));
							printDelimiter();
						} catch (Exception e) {
							System.out.println("Couldn't complete order");
						}
						break;
					case 11:
						// Delete order
						List<Order> orders = orderDatabaseService.read();
						Order orderToDelete = pickOrder(scanner, orders);
						try {
							orderDatabaseService.delete(orderToDelete);
							printDelimiter();
							System.out.println("Deleted order: " + orderToDelete);
							auditService.logMessage(String.format("Deleted order: id: %s", orderToDelete.getId().toString()));
							printDelimiter();
						} catch (Exception e) {
							System.out.println("Couldn't delete order");
						}
						break;
					case 12:
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
		} catch (SQLException e4) {
			e4.printStackTrace();
			return;
		}
	}


	private static List<Order> getOrdersByStatus(ICRUDService<Order> ordersDatabaseService, OrderStatus status) {
		List<Order> filteredOrders = new ArrayList<>();
		try {
			for (Order order : ordersDatabaseService.read()) {
				if (order.getStatus().equals(status)) {
					filteredOrders.add(order);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return filteredOrders;
	}

	/**
	 * @param scanner
	 * @param service
	 * @return the picked business
	 */
	private static Business pickBusiness(Scanner scanner, IActionsService service, ICRUDService<Business> businessDatabaseService) {
		List<Business> businesses;
		try {
			businesses = businessDatabaseService.read();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if (businesses.isEmpty()) {
			System.out.println("No businesses!");
			return null;
		}
		printBusinesses(businesses);
		int businessNumber;
		while(true) {
			System.out.println("Enter the business number");
			while(!scanner.hasNextInt()) {
				scanner.next();
			}
			businessNumber = scanner.nextInt();
			scanner.nextLine(); // consume \n
			if (businessNumber < 0 || businessNumber >= businesses.size()) {
				System.out.println("Out of bounds number. Try again");
				continue;
			} else {
				break;
			}
		}
		
		return businesses.get(businessNumber);
	}

	/**
	 * @param scanner
	 * @param service
	 * @param business
	 * @return the picked product
	 */
	private static Product pickProductFromBusiness(Scanner scanner, IActionsService service, Business business) {
		List<Product> products = business.getProducts();
		if (products.isEmpty()) {
			System.out.println("No products!");
			return null;
		}
		business.printProducts();
		int productNumber;
		while(true) {
			System.out.println("Enter the product number");
			while(!scanner.hasNextInt()) {
				scanner.next();
			}
			productNumber = scanner.nextInt();
			scanner.nextLine(); // consume \n
			if (productNumber < 0 || productNumber >= products.size()) {
				System.out.println("Out of bounds number. Try again");
				continue;
			} else {
				break;
			}
		}
		
		return products.get(productNumber);
	}
	
	/**
	 * @param scanner
	 * @param orders
	 * @return the picked order
	 */
	private static Order pickOrder(Scanner scanner, List<Order> orders) {
		if (orders.isEmpty()) {
			System.out.println("No orders!");
			return null;
		}
		printOrders(orders);
		int orderNumber;
		while(true) {
			System.out.println("Enter the order number");
			while(!scanner.hasNextInt()) {
				scanner.next();
			}
			orderNumber = scanner.nextInt();
			scanner.nextLine(); // consume \n
			if (orderNumber < 0 || orderNumber >= orders.size()) {
				System.out.println("Out of bounds number. Try again");
				continue;
			} else {
				break;
			}
		}
		
		return orders.get(orderNumber);
	}
	
	/**
	 * @param scanner
	 * @param service
	 * @return the picked user
	 */
	private static User pickUser(Scanner scanner, ICRUDService<User> userDatabaseService) {
		List<User> users;
		try {
			users = userDatabaseService.read();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if (users.isEmpty()) {
			System.out.println("No users!");
			return null;
		}
		printUsers(users);
		int userNumber;
		while(true) {			
			System.out.println("Enter the user number");
			while(!scanner.hasNextInt()) {
				scanner.next();
			}
			userNumber = scanner.nextInt();
			scanner.nextLine(); // consume \n
			if (userNumber < 0 || userNumber >= users.size()) {
				System.out.println("Out of bounds number. Try again");
				continue;
			} else {
				break;
			}
		}
		
		return users.get(userNumber);
	}
	
	private static Driver pickFreeDriver(Scanner scanner, ICRUDService<Driver> driverDatabaseService) {
		List<Driver> drivers;
		try {
			drivers = driverDatabaseService.read().stream()
				.filter(d -> d.isFree()).toList();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if (drivers.isEmpty()) {
			System.out.println("No free drivers!");
			return null;
		}
		printDrivers(drivers);
		int driverNumber;
		while(true) {			
			System.out.println("Enter the driver number");
			while(!scanner.hasNextInt()) {
				scanner.next();
			}
			driverNumber = scanner.nextInt();
			scanner.nextLine(); // consume \n
			if (driverNumber < 0 || driverNumber >= drivers.size()) {
				System.out.println("Out of bounds number. Try again");
				continue;
			} else {
				break;
			}
		}
		
		return drivers.get(driverNumber);
	}

}
package app;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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
import app.services.BusinessCSVDatabaseService;
import app.services.CSVBadColumnLengthException;
import app.services.DriverCSVDatabaseService;
import app.services.IActionsService;
import app.services.IBusinessDatabaseService;
import app.services.IDriverDatabaseService;
import app.services.IGenericDatabaseService;
import app.services.IOrdersDatabaseService;
import app.services.OrdersCSVDatabaseService;
import app.services.UserCSVDatabaseService;


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
		Scanner scanner = new Scanner(System.in);
		
		IActionsService actionsService = ActionsService.getInstance();
		
		IBusinessDatabaseService businessDatabaseService = BusinessCSVDatabaseService.getInstance();
		IGenericDatabaseService<User> userDatabaseService = UserCSVDatabaseService.getInstance();
		IDriverDatabaseService driverDatabaseService = DriverCSVDatabaseService.getInstance();
		IOrdersDatabaseService ordersDatabaseService = OrdersCSVDatabaseService.getInstance();
		
		AuditService auditService = AuditService.getInstance();
		
		try {
			businessDatabaseService.loadData();
			System.out.println("LOADED BUSINESS FROM CSV: " + businessDatabaseService.getAll());
			
			userDatabaseService.loadData();
			System.out.println("LOADED USERS FROM CSV: " + userDatabaseService.getAll());
			
			driverDatabaseService.loadData();
			System.out.println("LOADED DRIVERS FROM CSV: " + driverDatabaseService.getAll());
			
			ordersDatabaseService.loadData();
			System.out.println("LOADED ORDERS FROM CSV: " + ordersDatabaseService.getAll());

		} catch (IOException | CSVBadColumnLengthException e1) {
			System.out.println("Exception while loading data");
			e1.printStackTrace();
		}
		
		// Actions
		try {
			while (true) {
				
				// Save data from previous command
				try {
					businessDatabaseService.saveData();
					userDatabaseService.saveData();
					driverDatabaseService.saveData();
					businessDatabaseService.saveData();
				} catch (IOException e3) {
					e3.printStackTrace();
				}
				
				
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
					try {
						userDatabaseService.add(user);
						userDatabaseService.saveData();
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
						businessDatabaseService.add(business);
						businessDatabaseService.saveData();
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
						driverDatabaseService.add(driver);
						driverDatabaseService.saveData();
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
						userDatabaseService.saveData();
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
						businessDatabaseService.addProductToBusiness(business, product);
						businessDatabaseService.saveData();
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
					OrderFactory newOrderFactory = ordersDatabaseService.createNewOrderFactory(userThatOrders, businessToOrderFrom);
					
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
						Order order = ordersDatabaseService.placeOrder(newOrderFactory);
						if (order != null && order.getPrice() != 0) {
							ordersDatabaseService.saveData();
							printDelimiter();
							System.out.println("Placed order: " + order);
							auditService.logMessage(String.format("Placed order: id: business: %s client: %s", order.getId().toString(), businessToOrderFrom.getName(), userThatOrders.getName()));
							userDatabaseService.saveData();
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
					List<Order> ordersToPrepare = ordersDatabaseService.getOrdersByStatus(OrderStatus.PREPERING);
					if (ordersToPrepare.isEmpty()) {
						System.out.println("No orders to prepare. Place an order first");
						break;
					}
					Order orderToPrepare = pickOrder(scanner, ordersToPrepare);
					try {
						ordersDatabaseService.prepareOrder(orderToPrepare);
						ordersDatabaseService.saveData();
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
					List<Order> ordersAwaitingPickup = ordersDatabaseService.getOrdersByStatus(OrderStatus.AWAITING_PICKUP);
					if (ordersAwaitingPickup.isEmpty()) {
						System.out.println("No orders awaiting pickup. Prepare an order first");
						break;
					}
					Order orderToPickup = pickOrder(scanner, ordersAwaitingPickup);
					Driver freeDriver = pickFreeDriver(scanner, driverDatabaseService);
					try {
						ordersDatabaseService.pickupOrder(orderToPickup, freeDriver);
						ordersDatabaseService.saveData();
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
					List<Order> ordersToDeliver = ordersDatabaseService.getOrdersByStatus(OrderStatus.DELIVERING);
					Order orderToDeliver = pickOrder(scanner, ordersToDeliver);
					try {
						ordersDatabaseService.deliverOrder(orderToDeliver);
						ordersDatabaseService.saveData();
						printDelimiter();
						System.out.println("Delivered order: " + orderToDeliver);
						printDelimiter();
					} catch (IOException e) {
						System.out.println("Error delivering order");
					}
					break;
				case 10:
					// Get order and tip
					List<Order> ordersToGet = ordersDatabaseService.getOrdersByStatus(OrderStatus.ARRIVED);
					Order orderToGet = pickOrder(scanner, ordersToGet);
					System.out.println("Enter tip amount:");
					while(!scanner.hasNextDouble()) {
						scanner.next();
					}
					double tip = scanner.nextDouble();
					try {
						ordersDatabaseService.getOrderAndTip(orderToGet, tip);
						ordersDatabaseService.saveData();
						printDelimiter();
						System.out.println("Completed order: " + orderToGet);
						auditService.logMessage(String.format("Completed order: id: %s", orderToGet.getId().toString()));
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
	private static Business pickBusiness(Scanner scanner, IActionsService service, IBusinessDatabaseService businessDatabaseService) {
		List<Business> businesses = businessDatabaseService.getAll();
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
	private static User pickUser(Scanner scanner, IGenericDatabaseService<User> userDatabaseService) {
		List<User> users = userDatabaseService.getAll();
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
	
	private static Driver pickFreeDriver(Scanner scanner, IDriverDatabaseService driverDatabaseService) {
		List<Driver> drivers = driverDatabaseService.getFreeDrivers();
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
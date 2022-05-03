package app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.entities.business.Business;
import app.entities.business.Product;
import app.entities.driver.Driver;
import app.entities.order.Order;
import app.entities.order.OrderData;
import app.entities.order.OrderFactory;
import app.entities.order.OrderItem;
import app.entities.order.OrderStatus;
import app.entities.user.User;

/**
 * This class follows the singleton design pattern
 */
public class OrdersCSVDatabaseService implements IOrdersDatabaseService {

	/**
	 * Path to file where data is stored
	 */
	private static final Path PATH_TO_CSV = Paths.get("data/orders.csv");
	
	private static final Path PATH_TO_ORDER_DATA_CSV = Paths.get("data/orderData.csv");
	
	private static final String COMMA_DELIMITER = ",";
	
	private static final int COLLUMS_NUMBER = 6;
	
	private static final int ORDER_DATA_COLLUMS_NUMBER = 4;

    private static OrdersCSVDatabaseService instance = null;
	
    private List<Order> orders = new ArrayList<>();
    
    private IUserDatabaseService userDatabaseService = UserCSVDatabaseService.getInstance();
    private IDriverDatabaseService driverDatabaseService = DriverCSVDatabaseService.getInstance();
    private IBusinessDatabaseService businessDatabaseService = BusinessCSVDatabaseService.getInstance();
    
    /**
     * Private constructor
     */
    private OrdersCSVDatabaseService() {}
    
    /**
     * @return the single instance of this service
     */
    public static OrdersCSVDatabaseService getInstance() {
        if (instance == null) {        	
        	instance = new OrdersCSVDatabaseService();
        }
        return instance;
    }
    
    @Override
    public void loadData() throws IOException, CSVBadColumnLengthException {
    	try (BufferedReader reader = Files.newBufferedReader(PATH_TO_CSV)) {
    	    String line;
    	    while ((line = reader.readLine()) != null) {
    	        String[] values = line.split(COMMA_DELIMITER);
    	        if (values.length != COLLUMS_NUMBER) {
    	        	throw new CSVBadColumnLengthException(String.format("Expected %d collumns, but %d were found", COLLUMS_NUMBER, values.length));
    	        }
    	        UUID id = UUID.fromString(values[0]);
    	        
    	        String clientId = values[1];
    	        User client = null;
    	        if (!clientId.equals("NULL")) {
    	        	client = userDatabaseService.getUserById(UUID.fromString(clientId));
    	        }
    	        
    	        String businessId = values[2];
    	        Business business = null;
    	        if (!businessId.equals("NULL")) {
    	        	business = businessDatabaseService.getBusinessById(UUID.fromString(businessId));
    	        }
    	        
    	        String driverId = values[3];
    	        Driver driver = null;
    	        if (!driverId.equals("NULL")) {
    	        	driver = driverDatabaseService.getDriverById(UUID.fromString(driverId));
    	        }
    	            	            	        
    	        LocalDateTime timePlacedOrder = LocalDateTime.parse(values[4], Order.DATE_FORMATTER);
    	        OrderStatus status = OrderStatus.valueOf(values[5]);
    	        
    	        List<OrderItem> emptyOrderData = new ArrayList<>();
				orders.add(new Order(id, client, business, driver, timePlacedOrder, status, new OrderData(emptyOrderData)));
    	    }
    	}
    	
    	try (BufferedReader reader = Files.newBufferedReader(PATH_TO_ORDER_DATA_CSV)) {
    	    String line;
    	    while ((line = reader.readLine()) != null) {
    	        String[] values = line.split(COMMA_DELIMITER);
    	        if (values.length != ORDER_DATA_COLLUMS_NUMBER) {
    	        	throw new CSVBadColumnLengthException(String.format("Expected %d collumns, but %d were found", ORDER_DATA_COLLUMS_NUMBER, values.length));
    	        }
    	        UUID orderId = UUID.fromString(values[0]);
    	        String name = values[1];
    	        double price = Double.parseDouble(values[2]);
    	        int quantity = Integer.parseInt(values[3]);
    	        
    	        Order order = getOrderById(orderId);
    	        order.addOrderItem(new OrderItem(new Product(name, price), quantity));
    	    }
    	}
    	
    	// Must retrace the order process for each order
    	for (Order order : orders) {
    		OrderStatus finalStatus = order.getStatus();
    		if (finalStatus == OrderStatus.DONE || finalStatus == OrderStatus.CANCELLED) {
    			continue;
    		}
    		
    		order.setStatus(OrderStatus.SENT);
    		order.placeOrder();
    		if (finalStatus == OrderStatus.SENT || finalStatus == OrderStatus.PREPERING) {
    			continue;
    		}
    		
    		order.prepareOrder();
    		if (finalStatus == OrderStatus.AWAITING_PICKUP) {
    			continue;
    		}
    		
    		order.pickUp();
    		if (finalStatus == OrderStatus.DELIVERING) {
    			continue;
    		}
    		
    		order.deliver();
    		if (finalStatus == OrderStatus.ARRIVED) {
    			continue;
    		}
    	}
    }
    
    public Order getOrderById(UUID id) {
		try {			
			Order order = (Order) orders.stream().filter(o -> o.getId().equals(id)).toArray()[0];
			return order;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
    public void saveData() throws IOException {
    	CSVDatabaseWriteService.write(orders, PATH_TO_CSV);
    	
    	List<String> dataToCSV = orders.stream().map(o -> o.getOrderDataToCSV()).toList();
    	CSVDatabaseWriteService.writeCSV(dataToCSV, PATH_TO_ORDER_DATA_CSV);
    }
	
	@Override
	public OrderFactory createNewOrderFactory(User userThatOrders, Business businessToOrderFrom) {
		return new OrderFactory(userThatOrders, businessToOrderFrom);
	}

	@Override
	public Order placeOrder(OrderFactory orderFactory) { 
		Order order = orderFactory.placeNewOrder();
		if (order != null) {			
			orders.add(order);
		}
		return order;
	}

	@Override
	public void prepareOrder(Order orderToPrepare) {
		orderToPrepare.getBusiness().completeOrder(orderToPrepare);
	}
	
	
	@Override
	public List<Order> getOrdersByStatus(OrderStatus status) {
		List<Order> filteredOrders = new ArrayList<>();
		for (Order order : orders) {
			if (order.getStatus().equals(status)) {
				filteredOrders.add(order);
			}
		}
		return filteredOrders;
	}


	@Override
	public void pickupOrder(Order orderToPickup, Driver freeDriver) {
		freeDriver.acceptOrder(orderToPickup);
	}

	@Override
	public void deliverOrder(Order orderToDeliver) {
		orderToDeliver.getDriver().finishDelivery();
	}

	@Override
	public void getOrderAndTip(Order orderToGet, double tip) {
		orderToGet.getClient().pickUp(orderToGet, tip);
		
	}

	@Override
	public List<Order> getOrders() {
		return orders;
	}

	@Override
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public void addOrder(Order order) {
		orders.add(order);
	}

}

package app.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.entities.business.Business;
import app.entities.business.BusinessFactory;
import app.entities.business.BusinessType;
import app.entities.business.Product;

/**
 * This class follows the singleton design pattern There can only exist one
 * instance of this class
 */
public class BusinessCSVDatabaseService implements IBusinessDatabaseService {

	/**
	 * Path to file where data is stored
	 */
	private static final Path PATH_TO_CSV = Paths.get("data/businesses.csv");

	private static final Path PATH_TO_PRODUCTS_CSV = Paths.get("data/businessProducts.csv");

	private static final int COLLUMS_NUMBER = 3;

	private static final int PRODUCTS_COLLUMS_NUMBER = 3;

	private static BusinessCSVDatabaseService instance = null;

	private List<Business> businesses = new ArrayList<>();

	/**
	 * Private constructor
	 */
	private BusinessCSVDatabaseService() {
	}

	/**
	 * @return the single instance of this service
	 */
	public static BusinessCSVDatabaseService getInstance() {
		if (instance == null) {
			instance = new BusinessCSVDatabaseService();
		}
		return instance;
	}

	@Override
	public void loadData() throws IOException, CSVBadColumnLengthException {

		List<String[]> data = CSVReaderService.readCSV(PATH_TO_CSV, COLLUMS_NUMBER);
		for (String[] values : data) {
			UUID id = UUID.fromString(values[0]);
			BusinessType type = BusinessType.valueOf(values[1]);
			String name = values[2];
			businesses.add(BusinessFactory.makeBusiness(id, type, name));
		}

		data = CSVReaderService.readCSV(PATH_TO_PRODUCTS_CSV, PRODUCTS_COLLUMS_NUMBER);
		for (String[] values : data) {
			UUID id = UUID.fromString(values[0]);
			String name = values[1];
			double price = Double.parseDouble(values[2]);
			Business business = (Business) businesses.stream().filter(b -> b.getId().equals(id)).toArray()[0];
			business.addProduct(new Product(name, price));
		}
	}

	@Override
	public void saveData() throws IOException {
		CSVWriterService.write(businesses, PATH_TO_CSV);

		List<String> dataToCSV = businesses.stream().map(b -> b.productsToCSV()).toList();
		CSVWriterService.writeCSV(dataToCSV, PATH_TO_PRODUCTS_CSV);
	}

	@Override
	public List<Business> getBusinesses() {
		return businesses;
	}

	@Override
	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}

	@Override
	public void addBusiness(Business business) {
		this.businesses.add(business);
	}

	@Override
	public void addProductToBusiness(Business business, Product product) {
		business.addProduct(product);
	}

	@Override
	public Business getBusinessById(UUID id) {
		try {
			Business business = (Business) businesses.stream().filter(b -> b.getId().equals(id)).toArray()[0];
			return business;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

}

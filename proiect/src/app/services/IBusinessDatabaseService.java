package app.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import app.entities.business.Business;
import app.entities.business.Product;

public interface IBusinessDatabaseService {

    /**
     * Loads data from CSV file
     * 
     * @throws IOException if a path is wrong
     * @throws CSVBadColumnLengthException if a line doesn't have the right number of collums
     */
    void loadData() throws IOException, CSVBadColumnLengthException;
    
    /**
     * Save data to CSV file
     * 
     * @throws IOException if a path is wrong
     */
    public void saveData() throws IOException;


	/**
	 * @return the businesses
	 */
	public List<Business> getBusinesses();

	/**
	 * @param businesses the businesses to set
	 */
	public void setBusinesses(List<Business> businesses);
	
	/**
	 * @param business the business to add
	 */
	public void addBusiness(Business business);

	/**
	 * Add a product to a business
	 * 
	 * @param business
	 * @param product
	 */
	public void addProductToBusiness(Business business, Product product);

	/**
	 * @param id
	 * @return the business with the specified id of null if none is found
	 */
	Business getBusinessById(UUID id);
}

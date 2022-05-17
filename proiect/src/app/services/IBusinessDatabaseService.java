package app.services;

import app.entities.business.Business;
import app.entities.business.Product;

public interface IBusinessDatabaseService extends IGenericDatabaseService<Business> {

	/**
	 * Add a product to a business
	 * 
	 * @param business
	 * @param product
	 */
	public void addProductToBusiness(Business business, Product product);

}

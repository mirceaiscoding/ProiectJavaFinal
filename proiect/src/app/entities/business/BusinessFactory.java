package app.entities.business;

import java.util.UUID;

import app.entities.Rating;

public class BusinessFactory {
	
	public static Business makeBusiness(BusinessType type, String name) {
		switch (type) {
		case RESTAURANT:
			return new Restaurant(name, new Rating());
		case GROCERY_STORE:
			return new GroceryShop(name, new Rating());
		default:
			return new Business(name, new Rating());
		}
	}
	
	public static Business makeBusiness(UUID id, BusinessType type, String name) {
		switch (type) {
		case RESTAURANT:
			return new Restaurant(id, name, new Rating());
		case GROCERY_STORE:
			return new GroceryShop(id, name, new Rating());
		default:
			return new Business(id, name, new Rating());
		}
	}
}
package app.entities.business;

import java.util.UUID;

import app.entities.Rating;

public class GroceryShop extends Business {

	public GroceryShop(String name, Rating rating) {
		super(name, rating);
	}
	
	public GroceryShop(UUID id, String name, Rating rating) {
		super(id, name, rating);
	}
	
	@Override
	public String toString() {
		return "Grocery shop: " + super.toString();
	}
	
	@Override
	public String toCSV() {
		return String.format("%s,GROCERY_STORE,%s", super.id, super.name);
	}

}
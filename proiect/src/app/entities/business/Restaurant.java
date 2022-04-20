package app.entities.business;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import app.entities.Rating;


public class Restaurant extends Business{
	
	private Set<RestaurantType> restaurantTypes;
	
	/**
	 * @param name
	 * @param rating
	 */
	public Restaurant(String name, Rating rating) {
		super(name, rating);
		restaurantTypes = new HashSet<>();
	}
	
	public Restaurant(UUID id, String name, Rating rating) {
		super(id, name, rating);
		restaurantTypes = new HashSet<>();
	}

	/**
	 * @return the restaurantTypes
	 */
	public Set<RestaurantType> getRestaurantTypes() {
		return restaurantTypes;
	}

	/**
	 * @param restaurantTypes the restaurantTypes to set
	 */
	public void setRestaurantTypes(Set<RestaurantType> restaurantTypes) {
		this.restaurantTypes = restaurantTypes;
	}
	
	public void addRestaurantType(RestaurantType type) {
		restaurantTypes.add(type);
	}
	
	@Override
	public String toString() {
		return "Restaurant " + super.toString();
	}

	@Override
	public String toCSV() {
		return String.format("%s,RESTAURANT,%s", super.id, super.name);
	}
}
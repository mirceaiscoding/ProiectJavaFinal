package user;

import java.util.Scanner;

public class UserAddress {

	private Country country;
	
	private City city;
	
	/**
	 * Prints available countries
	 */
	private void printCountries() {
		for (Country c : Country.values()) {
			System.out.printf("[%s] ", c.name());
		}
		System.out.println();
	}
	
	/**
	 * Prints available cities
	 */
	private void printCities() {
		for (City c : City.values()) {
			System.out.printf("[%s] ", c.name());
		}
		System.out.println();
	}
	
	/**
	 * @param country
	 * @param city
	 * @param adressLine1
	 * @param adressLine2
	 */
	public UserAddress(Country country, City city, String adressLine1, String adressLine2) {
		super();
		this.country = country;
		this.city = city;
		this.adressLine1 = adressLine1;
		this.adressLine2 = adressLine2;
	}

	/**
	 * Construct user address from scanner
	 * 
	 * @param scanner
	 */
	public UserAddress(Scanner scanner) {
		System.out.print("Country options: ");
		printCountries();
		country = Country.valueOf(scanner.nextLine());
		
		System.out.print("City options: ");
		printCities();
		city = City.valueOf(scanner.nextLine());
		
		System.out.println("Adress line 1:");
		adressLine1 = scanner.nextLine();
		
		System.out.println("Adress line 2:");
		adressLine2 = scanner.nextLine();
	}

	// Street, number
	private String adressLine1;

	// floor, apartment, etc
	private String adressLine2;

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return the city
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * @return the adressLine1
	 */
	public String getAdressLine1() {
		return adressLine1;
	}

	/**
	 * @param adressLine1 the adressLine1 to set
	 */
	public void setAdressLine1(String adressLine1) {
		this.adressLine1 = adressLine1;
	}

	/**
	 * @return the adressLine2
	 */
	public String getAdressLine2() {
		return adressLine2;
	}

	/**
	 * @param adressLine2 the adressLine2 to set
	 */
	public void setAdressLine2(String adressLine2) {
		this.adressLine2 = adressLine2;
	}
	
	@Override
	public String toString() {
		return String.format("Adress: %s, %s, %s, %s", country, city, adressLine1, adressLine2);
	}

}
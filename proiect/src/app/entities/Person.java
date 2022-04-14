package app.entities;

import java.util.Scanner;

public class Person extends AccountBalanceHolder{
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	/**
	 * @param name
	 * @param email
	 * @param phoneNumber
	 */
	public Person(String name, String email, String phoneNumber) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Construct person from scanner
	 * 
	 * @param scanner
	 */
	public Person(Scanner scanner) {
		System.out.println("Name:");
		name = scanner.nextLine();
		
		System.out.println("Email:");
		email = scanner.nextLine();

		System.out.println("Phone number:");
		phoneNumber = scanner.nextLine();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return String.format("Person: [name: %s], [email: %s], [phone number: %s]", name, email, phoneNumber)
				+ "\n" + super.toString();
	}

}
package app.entities;

public class AccountBalanceHolder {

	private double accountBalance;
	
	public AccountBalanceHolder() {
		accountBalance = 0;
	}

	/**
	 * @return the accountBalance
	 */
	public double getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @param accountBalance2 the accountBalance to set
	 */
	public void setAccountBalance(double accountBalance2) {
		this.accountBalance = accountBalance2;
	}
	
	/**
	 * @param amount the amount to add to add to balance
	 */
	public void addFounds(double amount) {
		accountBalance += amount;
	}
	
	/**
	 * @param amount the amount to withdraw from balance
	 * @throws IllegalArgumentException if there are not enough founds 
	 */
	public void withdrawFounds(double amount) {
		if (accountBalance < amount) {
			throw new IllegalArgumentException("Insufficiet founds");
		}
		accountBalance -= amount;
	}
	

	/**
	 * 
	 * @param from giving account
	 * @param to receiving account
	 * @param amount the sum being exchanged
	 */
	public static void exchangeMoney(AccountBalanceHolder from, AccountBalanceHolder to, double amount) {
		from.withdrawFounds(amount);
		to.addFounds(amount);
	}
	
	@Override
	public String toString() {
		return "Account balance: " + accountBalance + " RON";
	}

}
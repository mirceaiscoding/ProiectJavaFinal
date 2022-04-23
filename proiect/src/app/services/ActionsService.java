package app.services;


import app.entities.user.User;

/**
 * This class follows the singleton design pattern
 */
public class ActionsService implements IActionsService{
	
    private static ActionsService instance = null;
    
    /**
     * Private constructor
     */
    private ActionsService() {}
    
    /**
     * @return get the single instance of this service
     */
    public static ActionsService getInstance()
    {
        if (instance == null) {        	
        	instance = new ActionsService();
        }
        return instance;
    }
	
	/**
	 * @param user the user to which to add founds
	 * @param ammount value added
	 * @return the new balance of the user
	 */
	public double addFoundsToUser(User user, double ammount) {
		user.addFounds(ammount);
		return user.getAccountBalance();
	}
	
}
package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {

	private static final String dbName = "foodDelivery";
	private static final String dbUserName = "root";
	private static final String dbPassword = "Parola123!";
	private static final String connectionString = 
			"jdbc:mysql://localhost/" + dbName +
			"?user=" + dbUserName +
			"&password=" + dbPassword + 
			"&useUnicode=true&characterEncoding=UTF-8";
	

    private static Connection databaseConnection;

    /**
     * Private constructor
     */
    private DatabaseConfiguration() {}

    /**
     * Opens a connection is one is not already open
     * 
     * @return the database connection
     */
    public static Connection getDatabaseConnection() {

        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                databaseConnection = DriverManager.getConnection(connectionString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return databaseConnection;
    }

    /**
     * Closes the connection
     */
    public static void closeDatabaseConfiguration() {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
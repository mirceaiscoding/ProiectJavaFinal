package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {

	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Parola123!";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/foodDelivery";
	

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
                databaseConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
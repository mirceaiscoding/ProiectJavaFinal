package app.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

	/**
	 * Used to create the tables in the database
	 */
	public static void createTables() {
		Connection connection = DatabaseConfiguration.getDatabaseConnection();
		try {
			Statement statement = connection.createStatement();
			
			statement.addBatch("CREATE TABLE IF NOT EXISTS `businesses` (\n"
					+ "  `id` varchar(50) COLLATE utf8_bin NOT NULL,\n"
					+ "  `type` varchar(45) COLLATE utf8_bin NOT NULL DEFAULT 'OTHER',\n"
					+ "  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  PRIMARY KEY (`id`)\n"
					+ ")");
			
			statement.addBatch("CREATE TABLE IF NOT EXISTS `businessProducts` (\n"
					+ "  `business_id` varchar(50) COLLATE utf8_bin NOT NULL,\n"
					+ "  `name` varchar(45) COLLATE utf8_bin NOT NULL,\n"
					+ "  `price` decimal(10,2) DEFAULT '0.00',\n"
					+ "  PRIMARY KEY (`business_id`,`name`)\n"
					+ ")");
			
			statement.addBatch("CREATE TABLE IF NOT EXISTS `drivers` (\n"
					+ "  `id` varchar(50) COLLATE utf8_bin NOT NULL,\n"
					+ "  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `email` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `phone` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `balance` decimal(10,2) DEFAULT '0.00',\n"
					+ "  PRIMARY KEY (`id`)\n"
					+ ")");
			
			statement.addBatch("CREATE TABLE IF NOT EXISTS `orderItems` (\n"
					+ "  `order_id` varchar(50) COLLATE utf8_bin NOT NULL,\n"
					+ "  `product_name` varchar(45) COLLATE utf8_bin NOT NULL,\n"
					+ "  `product_price` decimal(10,2) DEFAULT NULL,\n"
					+ "  `quantity` int DEFAULT NULL,\n"
					+ "  PRIMARY KEY (`order_id`,`product_name`)\n"
					+ ")");
			
			statement.addBatch("CREATE TABLE IF NOT EXISTS `orders` (\n"
					+ "  `id` varchar(50) COLLATE utf8_bin NOT NULL,\n"
					+ "  `client_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `business_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `driver_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `time_placed_order` datetime DEFAULT NULL,\n"
					+ "  `status` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  PRIMARY KEY (`id`)\n"
					+ ")");
			
			statement.addBatch("CREATE TABLE IF NOT EXISTS `users` (\n"
					+ "  `id` varchar(50) COLLATE utf8_bin NOT NULL,\n"
					+ "  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `email` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `phone` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `country` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `city` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `addressline1` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `addressline2` varchar(45) COLLATE utf8_bin DEFAULT NULL,\n"
					+ "  `balance` decimal(10,2) DEFAULT '0.00',\n"
					+ "  PRIMARY KEY (`id`),\n"
					+ "  UNIQUE KEY `id_UNIQUE` (`id`)\n"
					+ ")");
			
			statement.executeBatch();
			
			
		} catch (SQLException e) {
			System.out.println("Exception creating tables");
			e.printStackTrace();
		}
	}

}

package bmwTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import service.Service;

public class Main {
	
	public static void main(String[] args) {
		Logger log = (Logger) LogManager.getLogger();
		String jdbcURL = "jdbc:postgresql://localhost:5432/bmw";
		String username = "postgres";
		String password = "admin";
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			log.info("Connected to PostgreSQL server");
			
			Service.insertUsers(connection);
			
			connection.close();
			
		} catch (SQLException e) {
			log.info("Error in connecting to PostgreSQL server");
			e.printStackTrace();
		}
	}

}

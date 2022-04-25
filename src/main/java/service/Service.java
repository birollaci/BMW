package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.postgresql.util.PGobject;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import validate.Validator;

public class Service {
	
	private static void create(Statement statement) throws SQLException {
		String sql = "CREATE TABLE users(id int primary key, name text, username text, email text, address json, phone text, website text, company json)";
		statement.executeUpdate(sql);
	}
	
	private static void drop(Statement statement) throws SQLException {
		String sql = "DROP TABLE IF EXISTS users";
		statement.executeUpdate(sql);
	}
	
	private static void delete(Statement statement) throws SQLException {
		String sql = "DELETE FROM users WHERE 1=1";
		statement.executeUpdate(sql);
	}

	public static void insertUsers(Connection connection) throws SQLException {
		Logger log = (Logger) LogManager.getLogger();
		
		HttpResponse<JsonNode> res = Unirest.get("https://jsonplaceholder.typicode.com/users").header("accept", "application/json").asJson();
		JSONArray data = res.getBody().getArray();
		
		Validator.validateStatus(log, res.getStatus());
		
		Statement statement = null;
		statement = connection.createStatement();
		
		drop(statement);
		create(statement);
		
		String sql = "";
		statement.executeUpdate(sql);
		
		for (int jsonIter = 0; jsonIter < data.length(); jsonIter++) {
			JSONObject object = data.getJSONObject(jsonIter);
			
			int id = object.getInt("id");
			String name = object.getString("name");
			String usrname = object.getString("username");
			String email = object.getString("email");
			
			if(!Validator.validateEmail(email)) {
				log.warn("Email of " + usrname + " is invalid!");
				continue;
			}
			
			JSONObject address = object.getJSONObject("address");
			
			String phone = object.getString("phone");
			String website = object.getString("website");
			
			JSONObject company = object.getJSONObject("company");
			
			String insertSQL = "INSERT INTO users (id, name, username, email, address, phone, website, company)"
					+ " VALUES (?, ?, ?, ?, ?::JSON, ?, ?, ?::JSON)";
			
			PreparedStatement insertStatement;
			insertStatement = connection.prepareStatement(insertSQL);
			
			PGobject jsonAddress = new PGobject();
			jsonAddress.setType("json");
			jsonAddress.setValue(address.toString());
			PGobject jsonCompany = new PGobject();
			jsonCompany.setType("json");
			jsonCompany.setValue(company.toString());
			
			insertStatement.setInt(1, id);
			insertStatement.setString(2, name);
			insertStatement.setString(3, usrname);
			insertStatement.setString(4, email);
			insertStatement.setObject(5, jsonAddress);
			insertStatement.setString(6, phone);
			insertStatement.setString(7, website);
			insertStatement.setObject(8, jsonCompany);
			
			int rows = insertStatement.executeUpdate();
			if (rows > 0) {
				log.info("A new user has been inserted.");
			}
			
		}
	}
	
}

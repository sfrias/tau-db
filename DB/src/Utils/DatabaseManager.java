package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.TreeMap;

public class DatabaseManager {
	
	private final static String USERNAME = "root";
	private final static String PASSWORD = "mapo00";
	private final static String URL = "jdbc:mysql://localhost:3306/testdb"; 

	private static JDCConnectionDriver connectionDriver;

	private static Properties connProperties;
	
	
	public DatabaseManager(){
		try {
			if (connectionDriver== null){
				connectionDriver = new JDCConnectionDriver("com.mysql.jdbc.Driver", URL, USERNAME, PASSWORD);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("Error initializing JDCConnectionDriver - class " + e.getMessage() + " not found!");
		} catch (InstantiationException e) {
			System.err.println("Error initializing JDCConnectionDriver - class " + e.getMessage() + " cannot be instanced!");
		} catch (IllegalAccessException e) {
			System.err.println("Error initializing JDCConnectionDriver - IllegalAccessException");
		} catch (SQLException e) {
			System.err.println("Error initializing JDCConnectionDriver - an SQLException was thrown");
		}
		
		connProperties = new Properties();
		
	}
	
	public JDCConnectionDriver getConnectionDriver() {
		return connectionDriver;
	}
	
	public JDCConnection getConnection() {
		try {
			return (JDCConnection) connectionDriver.connect(URL, connProperties);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean executeStatement(String statement) {
		
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();
			boolean ok = stmt.execute(statement);
			stmt.close();
			conn.close();
			return ok;
			
		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeStatement("+statement+")");
			return false;
		}
	}
	
	public ResultSet executeQuery(String query) {
		
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(query);

			stmt.close();
			conn.close(); 
			return resultSet;
			
		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeQuery("+query+")");
			return null;
		}
	}
	
	public TreeMap<String, Integer> generateHashMapFromQuery(String query,  int intCol, int stringCol) {

		
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(query);
			
			TreeMap<String, Integer> hashMap = new TreeMap<String, Integer>();

			try {
				while (resultSet.next()) {
					int id = resultSet.getInt(intCol);
					System.out.println("id=" + id);
					String name = resultSet.getString(stringCol);
					hashMap.put(name, id);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			resultSet.close();
			stmt.close();
			conn.close(); 
			return hashMap;
			
		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at generateHashMapFromQuery("+query+")");
			return null;
		}
	}
	
//	
//	public static void main (String agrs[]){
//		DatabaseManager dbManager = new DatabaseManager();
//		dbManager.executeStatement("INSERT INTO locations (location_name, location_universe_id) values('Coconino County', '648');");
//		ResultSet rs = dbManager.executeQuery("Select * FROM locations");
//		//System.out.println(rs.get);
//	}
}

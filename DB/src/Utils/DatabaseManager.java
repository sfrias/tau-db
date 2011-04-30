package Utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import GUI.commons.Pair;

public class DatabaseManager {

	private final static String USERNAME = "root";
	private final static String PASSWORD = "mapo00";
	private final static String URL = "jdbc:mysql://localhost:3306/testdb"; 

	private static DatabaseManager instance = null;

	private static JDCConnectionDriver connectionDriver;

	private static Properties connProperties;


	private DatabaseManager(){
		try {
			connectionDriver = new JDCConnectionDriver("com.mysql.jdbc.Driver", URL, USERNAME, PASSWORD);
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

	public static DatabaseManager getInstance(){

		if (instance == null){
			instance = new DatabaseManager();
		}
		
		return instance;
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

	public void executeBatchFile(String batchFileLocation) {

		try {
			System.out.println("Starting executing batch file " + batchFileLocation);

			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();

			FileInputStream fileInputStream = new FileInputStream(batchFileLocation);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str;
			while ((str = bufferedReader.readLine()) != null) {
				if (!str.equals("")){
					stmt.addBatch(str);
				}
			}

			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();

			stmt.executeBatch();
			stmt.close();
			conn.close();

			System.out.println("Finished executing batch file " + batchFileLocation);

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeBatchFile("+batchFileLocation+")");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public Pair[] executeQueryAndGetValues(Tables table, int interestingCol) {

		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + table.toString());

			List<Pair> valuesList = new ArrayList<Pair>();

			while (resultSet.next()) {
				valuesList.add(new Pair(resultSet.getString(interestingCol), resultSet.getInt(1)));
			}

			resultSet.close();
			stmt.close();
			conn.close(); 
			return valuesList.toArray(new Pair[]{});

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeQueryAndGetValues("+ table.toString() + ")");
			return null;
		}
	}

	public TreeMap<String, Integer> generateHashMapFromQuery(String query,  int intCol, int stringCol) throws UnsupportedEncodingException {

		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(query);

			TreeMap<String, Integer> hashMap = new TreeMap<String, Integer>();

			try {
				while (resultSet.next()) {
					int id = resultSet.getInt(intCol);
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

	public String[] getCurrentValues(Tables table, String idFieldName, int recordId) {
		
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table.toString() + " WHERE " + idFieldName + " = ?");
			stmt.setInt(1, recordId);
			ResultSet resultSet = stmt.executeQuery();
			
			//TODO: this is ugly, fix that.
			String[] valuesArr = new String[1];
			
			resultSet.next();
			valuesArr[0] = resultSet.getString(3);
			
			resultSet.close();
			stmt.close();
			conn.close(); 
			return valuesArr;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at getCurrentValues("+ table.toString() + ")");
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

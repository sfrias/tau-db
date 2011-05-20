package db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import Connection.JDCConnection;
import Connection.JDCConnectionDriver;
import Enums.ExecutionResult;
import Enums.Tables;
import GUI.commons.Pair;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class DatabaseManager {

	//TODO add finally statements to all methods and check null!!!
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

		String tableName = table.toString();
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			String statementString;
			if (table.equals(Tables.characters)){
				statementString = "SELECT * FROM characters ORDER BY character_name ASC";
			}
			else{
				statementString = "SELECT * FROM " + tableName + " ORDER BY " + tableName + "_name ASC";
			}
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(statementString);

			List<Pair> valuesList = new ArrayList<Pair>();

			while (resultSet.next()) {
				valuesList.add(new Pair(resultSet.getString(interestingCol), resultSet.getInt(1)));
			}

			resultSet.close();
			stmt.close();
			conn.close(); 
			return valuesList.toArray(new Pair[]{});
		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeQueryAndGetValues("+ tableName + ")");
			return null;
		}
	}

	public Pair[] executeLimetedQueryAndGetValues(Tables table, int interestingCol, String queryName) {

		String tableName = table.toString();
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			String statementString;
			if (table.equals(Tables.characters)){
				statementString = "SELECT * FROM characters WHERE character_name REGEXP '^" + queryName + "' ORDER BY character_name ASC LIMIT 5000";
			}
			else{
				statementString = "SELECT * FROM " + tableName + " WHERE " + tableName + "_name REGEXP '^" +
				queryName + "' ORDER BY " + tableName + "_name ASC LIMIT 5000";
			}
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(statementString);

			List<Pair> valuesList = new ArrayList<Pair>();

			while (resultSet.next()) {
				valuesList.add(new Pair(resultSet.getString(interestingCol), resultSet.getInt(1)));
			}

			resultSet.close();
			stmt.close();
			conn.close(); 
			return valuesList.toArray(new Pair[]{});
		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeQueryAndGetValues("+ tableName + ")");
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

	public Pair[] getCharacters(String recordName) {

		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT character_id, character_name FROM characters WHERE character_name REGEXP '^" + recordName + "\' ORDER BY character_name");
			List<Pair> valuesList = new ArrayList<Pair>() ;

			while(resultSet.next()){
				Pair pair = new Pair(resultSet.getString(2), resultSet.getInt(1));
				valuesList.add(pair);
			}
			return valuesList.toArray(new Pair[valuesList.size()]);

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at getCurrentValues("+ recordName + ")");
			return null;
		}
		finally{
			try {
				if (resultSet != null){
					resultSet.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

	}

	public Pair[][] getCharacterAttributes(int recordId, String [] tables){

		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			Pair [][] values = new Pair[tables.length][];
			                                         
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			for (int i=0; i < tables.length; i++){
				List<Pair> currentAttr = new ArrayList<Pair>();
				String currentTable = tables[i];
				resultSet = stmt.executeQuery("SELECT " + currentTable+"_id, " + currentTable + "_name FROM characters_and_" 
						+ currentTable + ", " + currentTable + " WHERE characters_and_" + currentTable + "_" + currentTable 
						+ "_id = " + currentTable + "_id AND characters_and_" + currentTable + "_character_id = " + recordId);
				while (resultSet.next()){
					int id = resultSet.getInt(1);
					String name = resultSet.getString(2);
					Pair pair = new Pair(name, id);
					currentAttr.add(pair);
				}
				
				resultSet.close();
				values[i] = currentAttr.toArray(new Pair[currentAttr.size()]);				
			}
			return values;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at getCharacterAttributes("+ recordId+ ")");
			return null;
		}
		finally{
			try {
				if (resultSet != null){
					resultSet.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

	}

	public ExecutionResult executeSimpleInsert(Tables table, String fieldName, String value) {

		String tableName = table.toString();
		try {

			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt1 = conn.createStatement();
			ResultSet resultSet = stmt1.executeQuery("SELECT " + tableName+"_id FROM " + tableName + " WHERE " + tableName+ "_name LIKE \'" + value + "\'");
			boolean alreadyAdded = resultSet.next();
			resultSet.close();
			stmt1.close();
			conn.close();

			if (alreadyAdded){
				return ExecutionResult.Success_Simple_Add_Edit_Delete;	
			}

			Statement stmt2 = conn.createStatement();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT IGNORE INTO " + tableName + " (" + fieldName + ") values(\'" + value + "\')");
			stmt2.executeUpdate(stringBuilder.toString());

			stmt2.close();
			conn.close(); 

			return ExecutionResult.Success_Simple_Add_Edit_Delete;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeUpdate("+ tableName + ")");
			return ExecutionResult.Exception;
		}
	}

	public ExecutionResult executeInsertCharacter(String[] tables, Pair[][] values) {

		JDCConnection conn = null;
		Statement stmt = null;		
		Statement stmt2 = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			Pair pair = values[0][0];
			stmt.execute("INSERT IGNORE INTO characters (character_name) values (\'"+pair.getName()+"\')", Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKey = stmt.getGeneratedKeys();
			int key = 0;
			while(generatedKey.next()){
				key = generatedKey.getInt(1);
			}
			generatedKey.close();
			stmt2 = conn.createStatement();
			for (int i=1; i < tables.length; i++){

				for (int j=0; j < values[i].length; j++){
					String tableName = "characters_and_"+tables[i];
					Pair currentPair = values[i][j];
					int currentAttrId = currentPair.getId();
					stmt2.addBatch("INSERT IGNORE INTO " + tableName + " values(" + key + ", " + currentAttrId + ")");				}
			}
			stmt2.executeBatch();

			conn.commit();

			return ExecutionResult.Success_Add_Character;
		}
		catch (SQLException e ) {
			try {
				conn.rollback();
			} 
			catch(SQLException excep) {
			}
			return ExecutionResult.Exception;
		}
		finally {
			try {

				if (stmt != null) { 
					stmt.close(); 
				}
				if (stmt2 != null) { 
					stmt2.close(); 
				}
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}


	public ExecutionResult executeUpdate(Tables table, String[] fieldNames, String[] values, int id) {

		String tableName = table.toString();
		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("UPDATE " + tableName + " SET ");
			int length = fieldNames.length;
			for (int i=0; i < length - 1; i++){
				stringBuilder.append(fieldNames[i] + " = \'" + values[i] + "\', ");
			}
			stringBuilder.append(fieldNames[length-1] + " = \'" + values[length -1] + "\'");
			stringBuilder.append(" WHERE " + tableName + "_id = " + id);

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(stringBuilder.toString());

			stmt.close();
			conn.close(); 

			return ExecutionResult.Success_Simple_Add_Edit_Delete;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeUpdate("+ tableName + ")");
			return ExecutionResult.Exception;
		}
	}

	public ExecutionResult executeDelete(Tables table, int id) {

		String tableName = table.toString();

		try {
			JDCConnection conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			Statement stmt = conn.createStatement();
			if (table.equals(Tables.characters)){
				stmt.executeUpdate("DELETE FROM characters WHERE character_id = " + id);		
			}
			else{
				stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + tableName + "_id = " + id);		
			}
			stmt.close();
			conn.close(); 
			return ExecutionResult.Success_Simple_Add_Edit_Delete;
		} 
		catch (MySQLIntegrityConstraintViolationException e){
			return ExecutionResult.IntegrityConstraintViolationException;
		}
		catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeDelete("+ tableName + ")");
			return ExecutionResult.Exception;
		}		
	}
}

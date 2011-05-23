package database;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.commons.Pair;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import connections.JDCConnection;
import connections.JDCConnectionDriver;

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
		JDCConnection conn = null;
		Statement stmt = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			boolean ok = stmt.execute(statement);
			return ok;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeStatement("+statement+")");
			return false;
		}
		finally{
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ResultSet executeQuery(String query) {
		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(query);
			return resultSet;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeQuery("+query+")");
			return null;
		}
		finally{
			if (resultSet!= null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//TODO see who calls this method and add appropriate handling for returned null value
	public Pair[] executeQueryAndGetValues(Tables table) {

		String tableName;
		if (table.equals(Tables.place_of_birth)){
			tableName = Tables.place_of_birth.name();
		}
		else{
			tableName = table.toString();
		}

		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			String statementString;
			if (table.equals(Tables.characters)){
				statementString = "SELECT character_id, character_name FROM characters ORDER BY character_name ASC";
			}
			else{
				statementString = "SELECT " + tableName + "_id, " + tableName + "_name FROM " + tableName +" ORDER BY " + tableName + "_name ASC";
			}
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(statementString);

			List<Pair> valuesList = new ArrayList<Pair>();

			while (resultSet.next()) {
				valuesList.add(new Pair(resultSet.getString(2), resultSet.getInt(1)));
			}

			resultSet.close();
			stmt.close();
			conn.close(); 
			return valuesList.toArray(new Pair[]{});
		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeQueryAndGetValues("+ tableName + ")");
			return null;
		}
		finally{
			if (resultSet!= null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//TODO see who calls this method and add appropriate handling for returned null value
	public TreeMap<String, Integer> generateHashMapFromQuery(String query,  int intCol, int stringCol) throws UnsupportedEncodingException {

		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(query);

			TreeMap<String, Integer> hashMap = new TreeMap<String, Integer>();

			while (resultSet.next()) {
				int id = resultSet.getInt(intCol);
				String name = resultSet.getString(stringCol);
				hashMap.put(name, id);
			}

			return hashMap;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at generateHashMapFromQuery("+query+")");
			return null;
		}
		finally{
			if (resultSet!= null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
			if (resultSet!= null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
				if (currentTable.equals(Tables.place_of_birth.name())){
					resultSet = stmt.executeQuery("SELECT place_of_birth_id, place_of_birth_name FROM place_of_birth, characters "
							+ "WHERE character_place_of_birth_id = place_of_birth_id AND character_id = " + recordId);
				}
				else{
					resultSet = stmt.executeQuery("SELECT " + currentTable+"_id, " + currentTable + "_name FROM characters_and_" 
							+ currentTable + ", " + currentTable + " WHERE characters_and_" + currentTable + "_" + currentTable 
							+ "_id = " + currentTable + "_id AND characters_and_" + currentTable + "_character_id = " + recordId);	
				}
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
			if (resultSet!= null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}			 
	}

	public ExecutionResult executeSimpleInsert(Tables table, String fieldName, String value) {

		String tableName = table.toString();
		JDCConnection conn = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet resultSet = null;

		try {

			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt1 = conn.createStatement();
			resultSet = stmt1.executeQuery("SELECT " + tableName+"_id FROM " + tableName + " WHERE " + tableName+ "_name LIKE \'" + value + "\'");
			boolean alreadyAdded = resultSet.next();

			if (alreadyAdded){
				return ExecutionResult.Success_Simple_Add_Edit_Delete;	
			}

			stmt2 = conn.createStatement();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("INSERT IGNORE INTO " + tableName + " (" + fieldName + ") values(\'" + value + "\')");
			stmt2.executeUpdate(stringBuilder.toString());

			return ExecutionResult.Success_Simple_Add_Edit_Delete;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeUpdate("+ tableName + ")");
			return ExecutionResult.Exception;
		}
		finally{
			if (resultSet!= null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null){
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null){
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ExecutionResult executeInsertCharacter(String[] tables, Pair[][] values) {

		JDCConnection conn = null;
		Statement stmt = null;		
		Statement stmt2 = null;
		ResultSet generatedKey = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			Pair name = values[0][0];
			Pair placeOfBirth = values[Tables.place_of_birth.getIndex() + 1][0];
			stmt.execute("INSERT IGNORE INTO characters (character_name, character_place_of_birth_id) values (\'"+name.getName()+"\', " + placeOfBirth.getId() + ")", Statement.RETURN_GENERATED_KEYS);
			generatedKey = stmt.getGeneratedKeys();
			int key = 0;
			while(generatedKey.next()){
				key = generatedKey.getInt(1);
			}

			stmt2 = conn.createStatement();
			for (int i=1; i < tables.length; i++){
				for (int j=0; j < values[i].length; j++){
					Pair currentPair = values[i][j];
					int currentAttrId = currentPair.getId();
					if (tables[i].equals(Tables.place_of_birth.name())){
						continue;
					}
					String tableName = "characters_and_"+tables[i];
					stmt2.addBatch("INSERT IGNORE INTO " + tableName + " values(" + key + ", " + currentAttrId + ")");	
				}
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
			if (generatedKey!= null){
				try {
					generatedKey.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null){
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}	

	public ExecutionResult executeUpdate(Tables table, String[] fieldNames, String[] values, int id) {

		String tableName = table.toString();
		JDCConnection conn = null;
		Statement stmt = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("UPDATE " + tableName + " SET ");
			int length = fieldNames.length;
			for (int i=0; i < length - 1; i++){
				stringBuilder.append(fieldNames[i] + " = \'" + values[i] + "\', ");
			}
			stringBuilder.append(fieldNames[length-1] + " = \'" + values[length -1] + "\'");
			stringBuilder.append(" WHERE " + tableName + "_id = " + id);

			stmt = conn.createStatement();
			stmt.executeUpdate(stringBuilder.toString());

			return ExecutionResult.Success_Simple_Add_Edit_Delete;

		} catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeUpdate("+ tableName + ")");
			return ExecutionResult.Exception;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ExecutionResult executeEditCharacters(String[] tables, Pair[][] addedValues, Pair[][] removedValues) {

		return ExecutionResult.Success_Edit_Character;
	}
	
	public ExecutionResult executeDelete(Tables table, int id) {

		String tableName = table.toString();
		JDCConnection conn = null;
		Statement stmt = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			if (table.equals(Tables.characters)){
				stmt.executeUpdate("DELETE FROM characters WHERE character_id = " + id);		
			}
			else{
				stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + tableName + "_id = " + id);		
			}

			return ExecutionResult.Success_Simple_Add_Edit_Delete;
		} 
		catch (MySQLIntegrityConstraintViolationException e){
			return ExecutionResult.IntegrityConstraintViolationException;
		}
		catch (SQLException e) {
			System.err.println("An SQLException was thrown at executeDelete("+ tableName + ")");
			return ExecutionResult.Exception;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn!= null){
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

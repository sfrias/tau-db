package database;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import connection.JDCConnection;
import connection.JDCConnectionDriver;
import core.Algorithm;
import core.AlgorithmUtilities;
import dataTypes.Pair;
import dataTypes.SearchResultObject;
import dataTypes.SuccessRateObject;
import dataTypes.connectionElement;
import enums.ConnectionResult;
import enums.ExecutionResult;
import enums.Tables;

public class DatabaseManager {

	//TODO add finally statements to all methods and check null!!!
	private final static String USERNAME = "root";

	private final static String PASSWORD = "root";

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

	public JDCConnection getConnection() {
		try {
			return (JDCConnection) connectionDriver.connect(URL, connProperties);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void executeUpdate(String stmtToExecute) {
		JDCConnection conn = getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(stmtToExecute);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int executeInsertAndReturnGeneratedKey(String table, String fieldName, String value){
		JDCConnection conn = getConnection();
		Statement addNullIdStatement = null;
		ResultSet generatedKeys = null;
		try{
			conn.setAutoCommit(false);
			addNullIdStatement = conn.createStatement();
			addNullIdStatement.execute("INSERT IGNORE into " + table + "(" + fieldName+ ") values (\'" + value +"\')", Statement.RETURN_GENERATED_KEYS);						
			generatedKeys = addNullIdStatement.getGeneratedKeys();
			generatedKeys.first();
			int currentId = generatedKeys.getInt(1);
			addNullIdStatement.executeUpdate("UPDATE " + table + " SET " + table + "_fb_id = \'" + currentId + "\' WHERE " + table + "_id = " + currentId);
			conn.commit();
			
			return currentId;
		}
		catch (SQLException e){
			e.printStackTrace();
			return -1;
		}
		finally{
			if (generatedKeys != null){
				try {
					generatedKeys.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (addNullIdStatement != null){
				try {
					addNullIdStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null){
				try {
					conn.setAutoCommit(true);
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
			tableName = table.name();
		}

		Statement stmt = null;
		ResultSet resultSet = null;
		JDCConnection conn = getConnection();

		try {
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

		Statement stmt = null;
		ResultSet resultSet = null;
		JDCConnection conn = getConnection();

		try {
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

		Statement stmt = null;
		ResultSet resultSet = null;
		JDCConnection conn = getConnection();

		try {
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

		Statement stmt = null;
		ResultSet resultSet = null;
		JDCConnection conn = getConnection();

		try {
			Pair [][] values = new Pair[tables.length][];

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

		String tableName = table.name();
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet generatedKeys = null;
		JDCConnection conn = getConnection();

		try {
			conn.setAutoCommit(false);

			stmt1 = conn.createStatement();
			resultSet = stmt1.executeQuery("SELECT " + tableName+"_id FROM " + tableName + " WHERE " + tableName+ "_name LIKE \'" + value + "\'");
			boolean alreadyAdded = resultSet.next();

			if (alreadyAdded){
				return ExecutionResult.Success_Simple_Add_Edit_Delete;	
			}

			stmt2 = conn.createStatement();
			stmt2.executeUpdate("INSERT IGNORE INTO " + tableName + " (" + fieldName + ") values(\'" + value + "\')", Statement.RETURN_GENERATED_KEYS);

			generatedKeys = stmt2.getGeneratedKeys();
			generatedKeys.first();
			int key = generatedKeys.getInt(1);

			if (!table.equals(Tables.place_of_birth)){
				stmt2.executeUpdate("UPDATE " + tableName + " SET " + tableName + "_fb_id = \'" + key + "\' WHERE " + tableName + "_id = " + key);
			}
			conn.commit();
			return ExecutionResult.Success_Simple_Add_Edit_Delete;

		} 
		catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.err.println("An SQLException was thrown at executeSimpleInsert("+ tableName + ")");
			return ExecutionResult.Exception;
		}
		finally{
			if (generatedKeys!= null){
				try {
					generatedKeys.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ExecutionResult executeInsertCharacter(String[] tables, Pair[][] values) {

		Statement stmt = null;		
		Statement stmt2 = null;
		ResultSet generatedKeys = null;
		JDCConnection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			Pair name = values[0][0];
			Pair[] placeOfBirthArray = values[Tables.place_of_birth.getIndex() + 1];
			int placeOfBirthId;
			if (placeOfBirthArray != null && placeOfBirthArray.length > 0){
				placeOfBirthId = placeOfBirthArray[0].getId();
			}
			else{

				ResultSet rs = stmt.executeQuery("SELECT place_of_birth_id FROM place_of_birth WHERE place_of_birth_name LIKE \'Unspecified\'");
				rs.first();
				placeOfBirthId = rs.getInt(1);
			}

			stmt.execute("INSERT IGNORE INTO characters (character_name, character_place_of_birth_id) values (\'"+name.getName()+"\', " + placeOfBirthId + ")", Statement.RETURN_GENERATED_KEYS);
			generatedKeys = stmt.getGeneratedKeys();
			generatedKeys.first();
			int key = generatedKeys.getInt(1);
			stmt.executeUpdate("UPDATE characters SET character_fb_id  = \'" + key + "\' WHERE character_id = " + key);

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
			if (generatedKeys != null){
				try {
					generatedKeys.close();
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

		String tableName = table.name();
		Statement stmt = null;
		JDCConnection conn = getConnection();

		try {
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



	public ExecutionResult executeUpdateInSuccesRate(boolean matchfound) {

		String tableName = "success_rate";
		String success = tableName + "_successful_searches";
		String fail = tableName + "_unsuccessful_searches";
		String total = tableName + "_searches";

		Statement stmt = null;
		JDCConnection conn = getConnection();

		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("UPDATE " + tableName + " SET ");
			if (matchfound){
				stringBuilder.append(total + "= " + total + "+1, " + success + " = "  + success + "+1");				
			}
			else {
				stringBuilder.append(total + "= " + total + "+1, " + fail + " = "  + fail + "+1");				
			}

			stmt = conn.createStatement();
			stmt.executeUpdate(stringBuilder.toString());

			return ExecutionResult.Success_Update_Success_Rate;

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




	public ExecutionResult executeEditCharacters(String[] tables, Pair[][] addedValues, Pair[][] removedValues, Pair character, int place_of_birth_id) {

		Statement stmt = null;		
		PreparedStatement preparedStmt = null;		

		int characterId = character.getId();
		String name = character.getName();
		
		JDCConnection conn = getConnection();

		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE characters SET character_name = \'" + name + "\', character_place_of_birth_id = " + place_of_birth_id + " WHERE character_id = " + characterId);

			for (int i=0; i < tables.length; i++){

				String tableName = "characters_and_"+tables[i];
				String field = tableName + "_" + tables[i] + "_id";
				preparedStmt = conn.prepareStatement("INSERT INTO " + tableName + " values(" + characterId + " , ?)");
				for (int j=0; j< addedValues[i].length; j++){
					preparedStmt.setInt(1, addedValues[i][j].getId());
					preparedStmt.execute();
				}

				preparedStmt = conn.prepareStatement("DELETE FROM " +  tableName + " WHERE " + tableName + "_character_id = " + characterId + " AND " + field + " = ?");
				for (int j=0; j< removedValues[i].length; j++){
					preparedStmt.setInt(1, removedValues[i][j].getId());
					preparedStmt.execute();
				}
			}

			conn.commit();
			return ExecutionResult.Success_Edit_Character;
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
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStmt != null){
				try {
					preparedStmt.close();
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

	public ExecutionResult executeDelete(Tables table, int id) {

		String tableName = table.name();
		Statement stmt = null;
		JDCConnection conn = getConnection();

		try {
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

	/* 
	 * gets the character's name by his/her id 
	 */
	public String getNameFromId(int id){

		Statement stmt = null;
		ResultSet rs = null;
		String Name = null;
		Algorithm noEnd = Algorithm.getInstance();
		JDCConnection conn = getConnection();

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT character_name FROM characters WHERE character_id=" +id +"");
			if (rs.first()){
				Name = rs.getString("character_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
			return null;
		} 
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}

		}
		return Name;	
	}




	/* 
	 * gets the attribute's name by its id
	 */

	public String getAttributeNameFromID(String table, int id){

		Statement stmt = null;
		ResultSet rs = null;
		String Name = null;
		Algorithm noEnd = Algorithm.getInstance();
		if (table.equals(Tables.romantic_involvement.name()) ||
				table.equals(Tables.parent.name()) ||
				table.equals("child")) {

			return "none";
		}
		
		JDCConnection conn = getConnection();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " +table+"_name FROM " + table+ " WHERE " + table+"_id=" + id);
			if (rs.first()){
				Name = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
			return null;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
		}
		return Name;
	}


	//for top 5 searches use field 'count'
	//for top 5 recent matches use field 'date'
	public SearchResultObject[] topSearches (String field) {

		Statement stmt = null;
		ResultSet rs = null;
		String startName=null, endName=null;
		Date date = null;
		int count = -1;
		SearchResultObject[] searchResult = new SearchResultObject[5];
		int index=0;

		JDCConnection conn = getConnection();
		try {			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM history ORDER BY "+ field +" DESC LIMIT 5");
			while (rs.next()) {
				startName = getNameFromId(rs.getInt(1));
				endName = getNameFromId(rs.getInt(2));
				if (field.equals("count")){
					count = rs.getInt(5);
					if (count!=0){
						searchResult[index] = new SearchResultObject(startName, endName, count);
					}
					else{
						break;
					}
				} else if (field.equals("date")){
					date = rs.getDate(3);
					searchResult[index] = new SearchResultObject(startName, endName, date);
				} else {
					return null; // not a valid input
				}
				index++;
			}
			
			return searchResult;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs!= null){
				try {
					rs.close();
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


	/*
	 * gets unspecifiedId of a specific attribute
	 */

	public int getUnspecifiedId(String table) {
		
		Statement stmt = null;
		ResultSet rs = null;
		int unspecifiedID = 0;
		String field = "";
		Algorithm noEnd = Algorithm.getInstance();
		if (table.equals(Tables.characters.name())){
			field = "character";
		}
		else {
			field = table;
		}

		JDCConnection conn = getConnection();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + field + "_id" + " FROM " + table + " WHERE " +  field + "_name = 'Unspecified'");
			rs.first();
			unspecifiedID= rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
		}

		return unspecifiedID;
	}



	/*
	 * inserts connection found
	 */

	public void insertIntoHistory (String connections) {
		
		Statement stmt = null;
		String[] connectionsSplit = connections.split("\t");
		int length = connectionsSplit.length;
		boolean execute = false;
		Algorithm noEnd = Algorithm.getInstance();
		JDCConnection conn = getConnection();

		try {			
			stmt = conn.createStatement();
			String date = AlgorithmUtilities.getCurrentDate();
			String information = "";

			for (int i=0; i<length; i++) {
				String[] arr = connectionsSplit[i].split(","); 
				int first = Integer.parseInt(arr[0]);

				for (int j=i; j<length; j++) {
					String[] values = connectionsSplit[j].split(","); 
					int second = Integer.parseInt(values[1]);
					information = "";
					for (int k=i; k<j; k++) { 
						if (connectionsSplit[k]!=null){
							if (connectionsSplit[k+1]!=null){
								information += connectionsSplit[k]+ "\t";
							}
							else {
								information += connectionsSplit[k];
								break;
							}
						}
						else
							break;
					}
					String toQuery = null;

					if (connectionsSplit[j]!= null){
						information += connectionsSplit[j];
					}
					if (i==0 && j==length-1){
						toQuery = "INSERT INTO history (character_id1, character_id2, date, information,count) values (" + first + "," + second + ",'" + date + "', '" + information + "',1);";
						execute = true;
					}
					else {
						if (!lookForConnectionInHistory(first, second, new connectionElement[Algorithm.getMaxNumOfConnection()])){
							toQuery = "INSERT INTO history (character_id1, character_id2, date, information) values (" + first + "," + second + ",'" + date + "', '" + information + "');";
							execute = true;
						}
					}

					if (execute){
						stmt.executeUpdate(toQuery);
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
			return;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
		}
	}


	public void insertIntoFailedSearchesTable (int start_id, int end_id) {
		Statement stmt = null;
		String toQuery;
		String date;
		Algorithm noEnd = Algorithm.getInstance();
		JDCConnection conn = getConnection();

		try {
			stmt = conn.createStatement();
			date = AlgorithmUtilities.getCurrentDate();

			toQuery = "INSERT IGNORE INTO failed_searches (character_id1, character_id2, date) values (" + start_id + "," + end_id + ",'" + date + "');";
			stmt.executeUpdate(toQuery);

		} catch (SQLException e) {
			e.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
			return;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
		}
	}



	/*
	 * Searching for the couple in the failed_searches table. 
	 */
	public  boolean lookForConnectionInFailedSearchesTable (int start_id, int end_id){
		Statement stmt = null;
		ResultSet rs = null;
		boolean result = false;
		Algorithm noEnd = Algorithm.getInstance();
		JDCConnection conn = getConnection();;

		// checks if the couple is in failed_searches table
		try {
			stmt = conn.createStatement();

			rs = stmt.executeQuery("SELECT * FROM failed_searches WHERE character_id1 = " + start_id + " AND character_id2 = " + end_id);
			if (rs.next()) {
				result = true;	
			}
			else {
				rs = stmt.executeQuery("SELECT * FROM failed_searches WHERE character_id1 = " + end_id + " AND character_id2 = " + start_id);
				if (rs.next()){
					result = true;
				}
			}
			//if (result) { //found the couple in the failed_searches table
			//System.out.println("couldn't find a connection between "+ start_name +" and "+ end_name);
			//}

		} catch (SQLException e1) {
			e1.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
			return false;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}

			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
		}

		return result;
	}


	/*
	 * Searching for the connection in the history table. 
	 * If exists - prints the connection to console.
	 */
	public boolean lookForConnectionInHistory(int start_id, int end_id, connectionElement[] conenctionArray){
		
		JDCConnection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		boolean result = false;
		int count;
		Algorithm noEnd = Algorithm.getInstance();
		// checks if the connection between these 2 characters already in history table
		try {
			stmt = conn.createStatement();

			rs = stmt.executeQuery("SELECT * FROM history WHERE character_id1 = " + start_id + " AND character_id2 = " + end_id);
			if (rs.next()) {
				result = true;	
			}
			else {
				rs = stmt.executeQuery("SELECT * FROM history WHERE character_id1 = " + end_id + " AND character_id2 = " + start_id);
				if (rs.next()){
					result = true;
				}
			}

			if (result) { //found the connection in the history table
				count = rs.getInt("count");
				count++;
				int id1 = rs.getInt(1);
				int id2 = rs.getInt(2);
				String date = AlgorithmUtilities.getCurrentDate();
				//TODO PRINT THIS TO THE SCREEN
				System.out.println("this connection was found in " + rs.getDate(3));
				String getConnectionOfCharacters = rs.getString(4);
				//TODO PRINT THIS TO THE SCREEN
				//System.out.println("Match found between "+ start_name +" and "+ end_name);
				stmt.executeUpdate("UPDATE history SET count = " + count + ", date = '" + date + "' WHERE character_id1 = " + id1 + " AND character_id2 = " + id2);

				//if (opposite) {
				//start_name = end_name;
				//}

				AlgorithmUtilities.prepareConnectionsFromHistory(getConnectionOfCharacters, conenctionArray);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			noEnd.setR(ConnectionResult.Exception);
			return false;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					noEnd.setR(ConnectionResult.Close_Exception);
				}
			}
		}

		return result;

	}

	public SuccessRateObject getSuccessRate(){

		Statement stmt = null;
		ResultSet rs = null;
		Algorithm noEnd = Algorithm.getInstance();
		int total = 0;
		int success = 0;
		SuccessRateObject successRate;
		
		JDCConnection conn = getConnection();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM success_rate");
			rs.first();
			total = rs.getInt(1);
			success = rs.getInt(2);
			successRate = new SuccessRateObject(total, success);
		} catch (SQLException e) {
			e.printStackTrace();
			//noEnd.setR(ConnectionResult.Exception_In_Success_Rate);
			return null;
		}
		finally{
			try{
				if (stmt != null){
					stmt.close();
				} 
				if (rs!= null){
					rs.close();
				} 
				if (conn!= null){
					conn.close();
				}
			}catch (SQLException e) {
				e.printStackTrace();
				//noEnd.setR(ConnectionResult.Close_Exception);
				return null;
			}
		}

		return successRate;

	}
	
	public void executeDeleteTableContent (String tableName){  
		JDCConnection conn = getConnection(); 
		Statement stmt = null; 
		try { 
			stmt = conn.createStatement(); 
			stmt.executeUpdate("TRUNCATE TABLE " + tableName); 
		} catch (SQLException e) {
				e.printStackTrace(); 
		} 
		finally { 
			
			if (stmt != null){ 
				try { stmt.close();
				} 
				catch (SQLException e) { 
					e.printStackTrace();
					} 
			} 
			
			if (conn != null){
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

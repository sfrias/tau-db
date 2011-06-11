package database;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import connection.JDCConnection;
import connection.JDCConnectionDriver;
import core.AlgorithmUtilities;
import dataTypes.Pair;
import dataTypes.SearchResultObject;
import dataTypes.SuccessRateObject;
import dataTypes.connectionElement;
import enums.ConnectionResult;
import enums.ExecutionResult;
import enums.Tables;

public class DatabaseManager {

	private final static String USERNAME = System.getProperty("username");
	private final static String PASSWORD = System.getProperty("password");
	private final static String HOST = System.getProperty("host");
	private final static String PORT = System.getProperty("port");
	private final static String SID = System.getProperty("sid");
	private final static String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + SID; 

	private static JDCConnectionDriver connectionDriver;
	private static Properties connProperties;
	private TreeMap<Tables, Integer> unspecifiedIdOfTables = new TreeMap<Tables,Integer>();
	private boolean init = false;


	public DatabaseManager() throws Exception{
		connectionDriver = new JDCConnectionDriver("com.mysql.jdbc.Driver", URL, USERNAME, PASSWORD);
		connProperties = new Properties(); 
	}

	public void initialize() throws Exception{
		if (!init){
			buildUnspecifiedMapPerTable();
			init = true;
		}
	}

	public JDCConnection getConnection() {
		try {
			return (JDCConnection) connectionDriver.connect(URL, connProperties);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean getLock(){
		return executeLockCommands("SELECT GET_LOCK('lock1',10)");
	}

	public boolean releaseLock(){
		return executeLockCommands("SELECT RELEASE_LOCK('lock1');");
	}

	public void executeUpdate(String stmtToExecute) {
		JDCConnection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
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
		JDCConnection conn =null;
		Statement addNullIdStatement = null;
		ResultSet generatedKeys = null;
		try{
			conn = getConnection();
			conn.setAutoCommit(false);
			addNullIdStatement = conn.createStatement();
			addNullIdStatement.execute("INSERT IGNORE into " + table + " (" + fieldName+ ") values (\'" + value +"\')", Statement.RETURN_GENERATED_KEYS);						
			generatedKeys = addNullIdStatement.getGeneratedKeys();
			generatedKeys.first();
			int currentId = generatedKeys.getInt(1);
			addNullIdStatement.executeUpdate("UPDATE " + table + " SET " + table + "_fb_id = '" + currentId + "' WHERE " + table + "_id = " + currentId);
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
		JDCConnection conn = null;

		try {
			String statementString;
			conn = getConnection();
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

			int unspecified = getUnspecifiedId(table);

			Pair currentPair = null;
			Iterator<Pair> iter = valuesList.iterator();
			while (iter.hasNext()){
				currentPair = iter.next();
				if (currentPair.getId() == unspecified){
					valuesList.remove(currentPair);
					break;
				}
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
		JDCConnection conn =null;

		try {
			conn = getConnection();
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
		JDCConnection conn = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			recordName = recordName.replace("\'", "\\'");
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
		JDCConnection conn = null;

		try {
			conn = getConnection();
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
		JDCConnection conn = null;

		try {
			conn = getConnection();
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
		JDCConnection conn = null;
		try {
			conn = getConnection();
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

			stmt.execute("INSERT IGNORE INTO characters (character_name, character_place_of_birth_id) values (\'"+name.getName().replace("\'" , "\\'") +"\', " + placeOfBirthId + ")", Statement.RETURN_GENERATED_KEYS);
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
		JDCConnection conn =null;

		try {
			conn = getConnection();
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
		JDCConnection conn = null;

		try {
			conn = getConnection();
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
					return ExecutionResult.Exception;		
				}
			}
			if (conn!= null){
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Exception;
				}
			}
		}
	}




	public ExecutionResult executeEditCharacters(Tables[] tables, Pair[][] addedValues, Pair[][] removedValues, Pair character, int place_of_birth_id) {

		Statement stmt = null;		
		PreparedStatement preparedStmt = null;		

		int characterId = character.getId();
		String name = character.getName();
		name = name.replace("\'", "\\'");
		int unspecified = -1;
		JDCConnection conn = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			stmt.executeUpdate("UPDATE characters SET character_name = \'" + name + "\', character_place_of_birth_id = " + place_of_birth_id + " WHERE character_id = " + characterId);

			for (int i=0; i < tables.length; i++){

				String tableName = "characters_and_"+tables[i].name();
				String field = tableName + "_" + tables[i].name() + "_id";
				preparedStmt = conn.prepareStatement("INSERT INTO " + tableName + " values(" + characterId + " , ?)");
				//TODO CHANGE PARAMETER TABLES TO BE TABLES AND NOT STRINGS!!!!!!!! WE NEED TO BE CONSISTENT AND GENERIC
				unspecified = getUnspecifiedId(tables[i]);
				stmt = conn.createStatement();
				for (int j=0; j< addedValues[i].length; j++){
					preparedStmt.setInt(1, addedValues[i][j].getId());
					preparedStmt.execute();
					stmt.executeUpdate("DELETE FROM "+tableName + " WHERE " + tableName + "_character_id = " + characterId + " AND " + field + " = " + unspecified);
				}


				preparedStmt = conn.prepareStatement("DELETE FROM " +  tableName + " WHERE " + tableName + "_character_id = " + characterId + " AND " + field + " = ?");
				for (int j=0; j< removedValues[i].length; j++){
					preparedStmt.setInt(1, removedValues[i][j].getId());
					preparedStmt.execute();
				}

				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE "+ tableName + "_character_id = " + characterId);
				if (!rs.first()){
					stmt = conn.createStatement();
					stmt.executeUpdate("INSERT INTO " + tableName +" values(" + characterId + ", "+ unspecified + ")");
				}

			}

			conn.commit();
			return ExecutionResult.Success_Edit_Character;
		}
		catch (SQLException e ) {
			e.printStackTrace();
			try {
				conn.rollback();
			} 
			catch(SQLException excep) {
				e.printStackTrace();
			}
			return ExecutionResult.Exception;
		}
		finally {
			if (rs !=null){
				try{
					rs.close();
				}catch(SQLException excep) {
					return ExecutionResult.Exception;
				}

			}
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
		JDCConnection conn = null;

		try {
			conn = getConnection();
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
		JDCConnection conn = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT character_name FROM characters WHERE character_id=" +id +"");
			if (rs.first()){
				Name = rs.getString("character_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
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

		if (table.equals(Tables.romantic_involvement.name()) ||
				table.equals(Tables.parent.name()) ||
				table.equals("child")) {

			return "none";
		}

		JDCConnection conn = null;
		try {

			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " +table+"_name FROM " + table+ " WHERE " + table+"_id=" + id);
			if (rs.first()){
				Name = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
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

		JDCConnection conn = null;
		try {			
			conn = getConnection();
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

	public int getUnspecifiedId(Tables table){
		if (unspecifiedIdOfTables.get(table) != null){
			return unspecifiedIdOfTables.get(table);
		}
		else{
			return -1;
		}
	}

	public ConnectionResult checkForExistance(int id1, int id2){

		PreparedStatement stmt = null;
		ResultSet rs = null;
		JDCConnection conn = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.prepareStatement("SELECT * FROM characters WHERE character_id = ?");
			stmt.setInt(1,id1);
			rs = stmt.executeQuery();
			if (rs.first()){
				stmt.setInt(1,id2);
				rs = stmt.executeQuery();
				if (rs.first()){
					return ConnectionResult.Ok;
				}
			}

			return ConnectionResult.Character_not_found;

		} catch (SQLException e) {
			e.printStackTrace();
			return ConnectionResult.Exception;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ConnectionResult.Close_Exception;
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ConnectionResult.Close_Exception;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ConnectionResult.Close_Exception;
				}
			}
		}

	}



	/*
	 * inserts connection found
	 */
	public ExecutionResult insertIntoHistory (String connections) {

		Statement stmt = null;
		String[] connectionsSplit = connections.split("\t");
		int length = connectionsSplit.length;
		boolean execute = false;
		String toQuery = null;
		JDCConnection conn = null;

		try {			
			conn = getConnection();
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

					toQuery = null;

					if (connectionsSplit[j]!= null){
						information += connectionsSplit[j];
					}

					information = information.replace("\'", "\\'");
					if (i==0 && j==length-1){
						toQuery = "INSERT INTO history (character_id1, character_id2, date, information,count) values (" + first + "," + second + ",'" + date + "', '" + information + "',1);";
						execute = true;
					}
					else {
						if (lookForConnectionInHistory(first, second, new connectionElement[AlgorithmUtilities.getMaxNumber()])== ExecutionResult.Did_Not_Find_Connection_In_History){
							toQuery = "INSERT INTO history (character_id1, character_id2, date, information) values (" + first + "," + second + ",'" + date + "', '" + information + "');";
							execute = true;
						}
					}

					if (execute){
						stmt.executeUpdate(toQuery);
					}
					execute = false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return ExecutionResult.Exception;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
		}
		return ExecutionResult.Success_Insert_To_History;
	}


	public ExecutionResult insertIntoFailedSearchesTable (int start_id, int end_id) {
		Statement stmt = null;
		String toQuery;
		String date;
		JDCConnection conn = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			date = AlgorithmUtilities.getCurrentDate();

			toQuery = "INSERT IGNORE INTO failed_searches (character_id1, character_id2, date) values (" + start_id + "," + end_id + ",'" + date + "');";
			stmt.executeUpdate(toQuery);

		} catch (SQLException e) {
			e.printStackTrace();
			return ExecutionResult.Exception;

		}finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
		}
		return ExecutionResult.Success_Insert_To_History;
	}



	/*
	 * Searching for the couple in the failed_searches table. 
	 */
	public ExecutionResult lookForConnectionInFailedSearchesTable (int start_id, int end_id){
		Statement stmt = null;
		ResultSet rs = null;

		JDCConnection conn = null;

		// checks if the couple is in failed_searches table
		try {
			conn = getConnection();
			stmt = conn.createStatement();

			rs = stmt.executeQuery("SELECT * FROM failed_searches WHERE character_id1 = " + start_id + " AND character_id2 = " + end_id);
			if (rs.next()) {
				return ExecutionResult.Found_Connection_In_Failed_Searches;
			}
			else {
				rs = stmt.executeQuery("SELECT * FROM failed_searches WHERE character_id1 = " + end_id + " AND character_id2 = " + start_id);
				if (rs.next()){
					return ExecutionResult.Found_Connection_In_Failed_Searches;
				}
			}


		} catch (SQLException e1) {
			e1.printStackTrace();
			return ExecutionResult.Exception;

		}	finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}

			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
		}

		return ExecutionResult.Did_Not_Find_Connection_In_Failed_Searches;
	}


	/*
	 * Searching for the connection in the history table. 
	 * If exists - prints the connection to console.
	 */
	public ExecutionResult lookForConnectionInHistory(int start_id, int end_id, connectionElement[] conenctionArray){

		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean result = false;
		int count;

		// checks if the connection between these 2 characters already in history table
		try {
			conn = getConnection();

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
				String getConnectionOfCharacters = rs.getString(4);
				stmt.executeUpdate("UPDATE history SET count = " + count + ", date = '" + date + "' WHERE character_id1 = " + id1 + " AND character_id2 = " + id2);
				AlgorithmUtilities.prepareConnectionsFromHistory(getConnectionOfCharacters, conenctionArray);
				return ExecutionResult.Found_Connection_In_History;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return ExecutionResult.Exception;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return ExecutionResult.Close_Exception;
				}
			}
		}

		return ExecutionResult.Did_Not_Find_Connection_In_History;

	}

	public SuccessRateObject getSuccessRate(){

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		int success = 0;
		SuccessRateObject successRate;

		JDCConnection conn = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM success_rate");
			rs.first();
			total = rs.getInt(2);
			success = rs.getInt(3);
			successRate = new SuccessRateObject(total, success);
		} catch (SQLException e) {
			e.printStackTrace();
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
				return null;
			}
		}

		return successRate;

	}

	public ExecutionResult executeDeleteTableContent (String tableName){  
		JDCConnection conn =null;
		Statement stmt = null; 
		try { 
			conn = getConnection();
			stmt = conn.createStatement(); 
			stmt.executeUpdate("TRUNCATE TABLE " + tableName); 
		} catch (SQLException e) {
			return ExecutionResult.Exception;
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
		return ExecutionResult.Succees_Delete_Table_Content;
	} 

	private void buildUnspecifiedMapPerTable() throws Exception{
		Tables[] tablesArray = Tables.values();
		Tables currentTable;
		int unspecified;
		for (int i=0; i< tablesArray.length; i++){
			currentTable = tablesArray[i];
			if ((currentTable == Tables.characters ||
					!currentTable.name().contains("and")) && 
					currentTable != Tables.parent &&
					currentTable != Tables.romantic_involvement) {

				unspecified = getUnspecifiedId(currentTable.name());

				if (unspecified==-1){
					throw new Exception();
				}

				else {
					unspecifiedIdOfTables.put(currentTable, unspecified);
				}
			}
		}

	}

	private boolean executeLockCommands(String command){

		JDCConnection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(command);

			if (rs == null){
				return false;
			}

			rs.first();
			int success = rs.getInt(1);

			if (success == 1){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			if (rs != null){
				try {
					rs.close();
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
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int getUnspecifiedId(String table) {

		Statement stmt = null;
		ResultSet rs = null;
		int unspecifiedID = 0;
		String field = "";
		if (table.equals(Tables.characters.name())){
			field = "character";
		}
		else {
			field = table;
		}

		JDCConnection conn = null;
		try {
			conn = (JDCConnection) connectionDriver.connect(URL, connProperties);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + field + "_id" + " FROM " + table + " WHERE " +  field + "_name = 'Unspecified'");
			rs.first();
			unspecifiedID= rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return -1;
				}
			}
			if (rs!= null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return -1;
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return -1;
				}
			}
		}

		return unspecifiedID;
	}

}

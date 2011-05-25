package core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;


import connection.JDCConnection;
import database.DatabaseManager;
import enums.Tables;


public class algorithmUtils {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";

	/* 
	 * gets the character's name by his/her id 
	 */
	public static String getNameFromId(int id){
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String Name = null;
		try {
			stmt = conn.createStatement();

			rs = stmt.executeQuery("SELECT character_name FROM characters WHERE character_id=" +id +"");
			rs.first();
			Name = rs.getString("character_name");
			rs.close(); 
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		return Name;	
	}


	/* 
	 * gets the attribute's name by its id
	 */

	public static String getAttributeNameFromID(String table, int id){

		Statement stmt = null;
		ResultSet rs = null;
		String Name = null;
		
		if (table.equals(Tables.romantic_involvement.name()) ||
				table.equals(Tables.parent.name()) ||
				table.equals("child")) {
			
			return "none";
		}
		
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		try {
			stmt = conn.createStatement();

			rs = stmt.executeQuery("SELECT " +table+"_name FROM " + table+ " WHERE " + table+"_id=" + id);
			rs.first();
			Name = rs.getString(1);
			rs.close(); 
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		return Name;
	}


	/*
	 *prints connections in case found in history 	
	 */
	static void prepareConnectionsFromHistory(String connArr, connectionElement[] connectionArray){
		String startName="", endName="";
		String[] valueArr = new String[4];
		//String toPrint;
		String connections[] = connArr.split("\t");
		String atrName = null;
		for (int i=0; i<connections.length; i++){
			if (connections[i] != ""){ 
				valueArr = connections[i].split(",");
				startName =getNameFromId(Integer.parseInt(valueArr[0]));
				endName = getNameFromId(Integer.parseInt(valueArr[1]));
				int temp = Integer.parseInt(valueArr[3]);
				atrName = getAttributeNameFromID(valueArr[2], temp);
				connectionArray[i] = new connectionElement(startName, endName, valueArr[2], atrName);
				atrName = null;
			}
		}
	}



	public static String getCurrentDate() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd");
		String dateNow = formatter.format(currentDate.getTime());
		String [] d = dateNow.split("/");
		String s = d[0]+"."+d[1]+"."+d[2];
		return s;
	}

	/* 
	 * this function presents the 5 popular 
	 */
	public static void topSerches () {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		connectionElement[] connectionArray = new connectionElement[3];

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM history ORDER BY count DESC LIMIT 5");
			while (rs.next()) {
				String startName = getNameFromId(rs.getInt(1));
				String endName = getNameFromId(rs.getInt(2));
				System.out.println("this is a connection between " + startName + " and " + endName);
				System.out.println("this connection was found in " + rs.getDate(3));
				prepareConnectionsFromHistory(rs.getString(4),connectionArray);
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
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

	public static int getUnspecifiedId(String table) {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
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

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + field + "_id" + " FROM " + table + " WHERE " +  field + "_name = 'Unspecified'");
			rs.first();
			unspecifiedID= rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
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

		return unspecifiedID;


	}

	/*
	 * inserts connection found
	 */

	public static void insertIntoHistory (String connections, int start_id, int end_id) {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		String toQuery;
		String date;
		String[] connectionsSplit = connections.split("\t");
		String[] values;
		String[] arr;
		int length = connectionsSplit.length;
		int first, second;

		try {
			stmt = conn.createStatement();
			date = getCurrentDate();
			String information = "";

			for (int i=0; i<length; i++) {
				arr = connectionsSplit[i].split(","); 
				first = Integer.parseInt(arr[0]);

				for (int j=i; j<length; j++) {
					values = connectionsSplit[j].split(","); 
					second = Integer.parseInt(values[1]);
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

					if (connectionsSplit[j]!= null){
						information += connectionsSplit[j];
					}
					toQuery = "INSERT IGNORE INTO history (character_id1, character_id2, date, information) values (" + first + "," + second + ",'" + date + "', '" + information + "');";
					stmt.executeUpdate(toQuery);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
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
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void insertIntoFailedSearchesTable (int start_id, int end_id) {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		String toQuery;
		String date;

		try {
			stmt = conn.createStatement();
			date = getCurrentDate();

			toQuery = "INSERT IGNORE INTO failed_searches (character_id1, character_id2, date) values (" + start_id + "," + end_id + ",'" + date + "');";
			stmt.executeUpdate(toQuery);

		} catch (SQLException e) {
			e.printStackTrace();
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
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void insertIntoStatisticTable (int start_id, int end_id, int num , long time) {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		String toQuery;

		try {
			stmt = conn.createStatement();

			toQuery = "INSERT IGNORE INTO statistic (character_id1, character_id2, num_of_connections, time) values (" + start_id + "," + end_id + "," + num + "," + (int)time+ ");";
			stmt.executeUpdate(toQuery);

		} catch (SQLException e) {
			e.printStackTrace();
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
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Searching for the couple in the failed_searches table. 
	 */
	public static boolean lookForConnectionInFailedSearchesTable (String start_name, String end_name, int start_id, int end_id){
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		boolean result = false;

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
			if (result) { //found the couple in the failed_searches table
				System.out.println("couldn't find a connection between "+ start_name +" and "+ end_name);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
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
	
		return result;
	}


	/*
	 * Searching for the connection in the history table. 
	 * If exists - prints the connection to console.
	 */
	public static boolean lookForConnectionInHistory(String start_name, String end_name, int start_id, int end_id, connectionElement[] conenctionArray){
		DatabaseManager dbManager = DatabaseManager.getInstance();
		JDCConnection conn = dbManager.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		boolean result = false, opposite = false;
		int count;

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
					opposite = true;
				}
			}

			if (result) { //found the connection in the history table
				count = rs.getInt("count");
				count++;
				int id1 = rs.getInt(1);
				int id2 = rs.getInt(2);
				System.out.println("this connection was found in " + rs.getDate(3));
				String getConnectionOfCharacters = rs.getString(4);
				System.out.println("Match found between "+ start_name +" and "+ end_name);
				stmt.executeUpdate("UPDATE history SET count = " + count + " WHERE character_id1 = " + id1 + " AND character_id2 = " + id2);

				if (opposite) {
					start_name = end_name;
				}
			
				algorithmUtils.prepareConnectionsFromHistory(getConnectionOfCharacters, conenctionArray);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
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
		
		return result;

	}
	/*
	 * builds a query for finding all values of a specific attribute of the character.
	 * For example, if the attribute is 'power' and the character has 2 power - fly and jump - 
	 * the execution of this query will result the resultset of these 2 powers.
	 */

	public static String specificAttributeValuesQuery(String joinedAtr, String currentAtr, int charid){
		return 	
		"SELECT " + joinedAtr + "_" + currentAtr+ "_id"  + 
		" FROM " + joinedAtr + 
		" WHERE " +joinedAtr + "_character_id = " + charid;
	}

	public static String specificAttributeValuesQueryAny(String joinedAtr, String currentAtr, int charid, int unspecifiedidofCharacter, int unspecifiedIdOfAtr){
		return 	
		"SELECT DISTINCT x." + joinedAtr + "_character_id , x." + joinedAtr + "_" + currentAtr + "_id " +
		" FROM " + joinedAtr + " AS x, " + joinedAtr + " AS y " +
		" WHERE x." +joinedAtr + "_" + currentAtr+ "_id = y."+joinedAtr + "_" + currentAtr+ "_id" +
		" AND y."  + joinedAtr +  "_character_id =" + charid +
		" AND x." + joinedAtr +  "_character_id !=" + unspecifiedidofCharacter +
		" AND y."+joinedAtr + "_" + currentAtr+ "_id !=" +unspecifiedIdOfAtr;
	}
	
	public static String specificAttributeValuesQueryEnd(String joinedAtr, String currentAtr, int charid, int end_id, int unSpecAtr){
		return 	
		"SELECT DISTINCT x." + joinedAtr + "_character_id , x." + joinedAtr + "_" + currentAtr + "_id " +
		" FROM " + joinedAtr + " AS x, " + joinedAtr + " AS y " +
		" WHERE x." +joinedAtr + "_" + currentAtr+ "_id = y."+joinedAtr + "_" + currentAtr+"_id" +
		" AND y."  + joinedAtr +  "_character_id =" + charid +
		" AND x." + joinedAtr +  "_character_id =" + end_id +
		" AND y."+joinedAtr + "_" + currentAtr+"_id !=" +unSpecAtr;
	}

	public static String placeOfBirthQueryAny(String currentAtr, int charid, int unSpecChar, int unSpecPlace){
		return 	
		"SELECT DISTINCT x.character_id, x.character_" + currentAtr+ "_id "+  
		" FROM characters AS x, characters AS y " +
		" WHERE x.character_" + currentAtr+ "_id  = y.character_" + currentAtr+ "_id " +
		" AND y.character_id = " + charid + 
		" AND y.character_" + currentAtr+ "_id != " + unSpecChar +
		" AND y.character_" + currentAtr+ "_id !=" + unSpecPlace;
	}
	
	public static String placeOfBirthQueryEnd(String currentAtr, int charid, int end_id, int unspecifiedIdPlaceOfBirth){
		return 	
		"SELECT DISTINCT x.character_" + currentAtr+ "_id "+  
		" FROM characters AS x, characters AS y " +
		" WHERE x.character_" + currentAtr+ "_id  = y.character_" + currentAtr+ "_id " +
		" AND y.character_id = " + charid + 
		" AND x.character_id = " + end_id +
		" AND y.character_" + currentAtr+ "_id !=" + unspecifiedIdPlaceOfBirth;
	}
	
	

	/*
	 * builds a query for finding all characters and attribute values that have a common attribute value with 'charid'
	 */
	public static String joinedAttributeValuesQueryString(int charid){
		String allAtr = "SELECT characters_and_attributes_attribute_name, characters_and_attributes_attribute_id" +
		" FROM characters_and_attributes" +
		" WHERE characters_and_attributes_character_id =" + charid;
		return allAtr;
	}

	/*
	 * build a simple query - select, from, where 
	 */
	static String simpleQuery(String select, String from, String where){
		return 	
		"SELECT " + select +
		" FROM " + from + 
		" WHERE " +where;
	}



	public static String allCharactersWithTheSameAttributeQuery(String select, String from,String where1, String where2,String where3, boolean firstRun){
		String query;
		query = "SELECT "+select +" FROM " + from +" WHERE " + where1 + " AND "; 

		if (!firstRun){
			query +=   where2 +   " AND " 
			+ where3 ;
		}
		else{
			query += where2 ;

		}
		return query;
	}


	
	public static String relationsQuery(String field1,String field2,String from,String compare,int start_id, int unspecified){

		String query = "SELECT " +from + "_" + field1+  ","  +from + "_" + field2 +
		" FROM " + from 
		+" WHERE (" + from + "_" + field1  + "=" + start_id + " AND " + from + "_" + field2 + compare + unspecified
		+ ") OR " + "(" + from + "_" + field2  + "=" + start_id + " AND " + from + "_" + field1 + compare + unspecified +")";

		return query;
	}


	public static String getAllValuesOfASpecificAttributes(String joinedAtr, String currentAtr, int start_id, int unspecifiedIdOfCharacter){
		String charactersWithAtr = "SELECT " +  joinedAtr + "_character_id ," + joinedAtr + "_" + currentAtr + "_id " +
		" FROM " + joinedAtr + 
		" WHERE " + joinedAtr +  "_character_id !=" + unspecifiedIdOfCharacter + " AND " +
		joinedAtr +  "_character_id !=" + start_id + " AND (";
		return charactersWithAtr;
	}


	public static String getDirectConnectionString(String joinedAtr, String currentAtr, int end_id){
		String charactersWithAtr = "SELECT " +  joinedAtr + "_character_id ," + joinedAtr + "_" + currentAtr + "_id " +
		" FROM " + joinedAtr + 
		" WHERE " + joinedAtr +  "_character_id =" + end_id + " AND (" ;
		return charactersWithAtr;
	}


	public static void prepareTablesAndHashMaps(noEndAlg alg){
		TreeMap<String, String> joinedAttributesMap = new TreeMap<String,String>();
		int numOfTables = alg.tbs.length;
		int unspec=0;
		String atrTable, currentTable, putCouples;
		String[] attributes = new String[numOfTables-1]; 
		String[] result = new String[numOfTables-1];

		int indexOfAttr=0, indexOfResult=0;
		for (int i=0; i< numOfTables; i++){
			currentTable = alg.tbs[i].name();
			if (currentTable.equals(Tables.characters.name())){
				unspec = algorithmUtils.getUnspecifiedId(currentTable);
				alg.unspecifiedIdOfTables.put(Tables.characters.name(), unspec);
			}
			else if (!currentTable.contains("and")){
				attributes[indexOfAttr]=currentTable;
				indexOfAttr++;
			}
			else {
				atrTable = currentTable.substring(15);
				joinedAttributesMap.put(atrTable, currentTable);
			}
		}

		for (int i=0; i<indexOfAttr;i++){ 		//first loop- looking for joinedTables
			putCouples = joinedAttributesMap.get(attributes[i]);
			if (!attributes[i].equals(Tables.parent.name()) &&
					!attributes[i].equals(Tables.romantic_involvement.name())){
				unspec = algorithmUtils.getUnspecifiedId(attributes[i]);
				alg.unspecifiedIdOfTables.put(attributes[i], unspec);
			}

			if (putCouples != null){
				result[indexOfResult]=attributes[i];
				result[indexOfResult+1]=putCouples;
				indexOfResult = indexOfResult + 2;
				attributes[i]="ok";
			}
		}

		alg.indexOfJumps=indexOfResult; //this index is used in connectionFinder

		//adding all other tables;
		for (int i=0; i<indexOfAttr;i++){
			if(!attributes[i].equals("ok")){
				result[indexOfResult]=attributes[i];
				indexOfResult++;
			}
		}

		joinedAttributesMap.clear();
		alg.tablesArr = result;

		//adding all tables and their internal identifier to hash maps
		for (short i=0; i<alg.tablesArr.length;i++){
			noEndAlg.tablesMap.put(alg.tablesArr[i],i);
			noEndAlg.reverseTablesMap.put((short)i, alg.tablesArr[i]);
		}

		noEndAlg.tablesMap.put("child", (short)alg.tablesArr.length);
		noEndAlg.reverseTablesMap.put((short)alg.tablesArr.length, "child");
	}
	
	
	public static String helperForGUI(connectionElement connElement){
		String theConnection;
		String attribute = connElement.getAttribute();
		String startName = connElement.getStartName();
		String endName = connElement.getEndName();
		if (attribute.equals(Tables.romantic_involvement.name()) ) {
			theConnection = startName + " has a " + attribute + " relationship with " + endName;
		}
		else if( attribute.equals("child")) {
			theConnection = startName + " is " + endName +"'s child";
		}
		
		else if (attribute.equals(Tables.parent.name())) {
			theConnection = startName + " is " + endName +"'s parent";
		}
		else {
			theConnection = startName + " has the same "+ attribute + " as " + endName + " - " + connElement.getAttributeValue();
		}
		return theConnection;
			
	}
		
	public static String prepareConnectionsForGUI(charElement[] connection, connectionElement[] connectionArray){
		
		String startName="", endName="";
		String toHisory="";
		String atrName = null;
		short attribute; 
		int attributeVal;
		String attributeString;
		charElement conLast = connection[0], conPrev;
		int i=0;
		while (conLast.prevElement != null){
			conPrev = conLast.prevElement;
			attribute = conLast.connectedAttribute;
			attributeVal = conLast.attributeValue;
			attributeString = noEndAlg.reverseTablesMap.get(attribute);

			startName =algorithmUtils.getNameFromId(conLast.characterId);
			endName = algorithmUtils.getNameFromId(conPrev.characterId);

			atrName = algorithmUtils.getAttributeNameFromID(attributeString, attributeVal);

			toHisory+= conLast.characterId +","+conPrev.characterId +"," +attributeString + "," + atrName;
			if (conPrev.prevElement != null){
				toHisory+= "\t";
			}
			connectionArray[i] = new connectionElement(startName, endName, attributeString, atrName);
			i++;
			conLast = conPrev;
		}
		return toHisory;
		
	}
}

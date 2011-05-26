package core;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import connection.JDCConnection;
import database.DatabaseManager;
import enums.ConnectionResult;
import enums.Tables;


public class algorithmUtils {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	static int maxNumberOfConnection=0;


	public static void setMaxNumber(int number){
		maxNumberOfConnection = number;
	}

	/*
	 *prints connections in case found in history 	
	 */
	public static void prepareConnectionsFromHistory(String connArr, connectionElement[] connectionArray){
		DatabaseManager dbManager = DatabaseManager.getInstance();
		String startName="", endName="";
		String[] valueArr = new String[4];
		String connections[] = connArr.split("\t");
		String atrName = null;
		for (int i=0; i<connections.length; i++){
			if (connections[i] != ""){ 
				valueArr = connections[i].split(",");
				startName =dbManager.getNameFromId(Integer.parseInt(valueArr[0]));
				endName = dbManager.getNameFromId(Integer.parseInt(valueArr[1]));
				if (noEndAlg.getR()!= ConnectionResult.Ok){
					break;
				}
				int temp = Integer.parseInt(valueArr[3]);
				atrName = dbManager.getAttributeNameFromID(valueArr[2], temp);
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


// to be deleted	

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



	public static String allCharactersWithTheSameAttributeQuery(String select, String from,String where1, String where2,String where3, boolean two_parameters){
		String query;
		query = "SELECT "+select +" FROM " + from +" WHERE " + where1 + " AND "; 

		if (!two_parameters){
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


	public static String getAllValuesOfASpecificAttribute(String joinedAtr, String currentAtr, int start_id, int unspecifiedIdOfCharacter){
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
		DatabaseManager dbManager = DatabaseManager.getInstance();
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
				unspec = dbManager.getUnspecifiedId(currentTable);
				if (noEndAlg.getR()!= ConnectionResult.Ok){
					return;
				}
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
				unspec = dbManager.getUnspecifiedId(attributes[i]);
				if (noEndAlg.getR()!= ConnectionResult.Ok){
					return;
				}
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
		return;
	}
	
	
	
	/*
	 * get the connection from connection element
	 */
	public static String readOneConnectionElement(connectionElement connElement){
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
	
	
	
	/*
	 * Helper function for the GUI for reading the entire connection chain
	 */
	public static String[] readConnectionChain(connectionElement[] connElementArray){
		String[] connectionInStrings = new String[3];
		for (int i=0; i<maxNumberOfConnection; i++){
			if (connElementArray[i] != null){
				connectionInStrings[i] = readOneConnectionElement(connElementArray[i]);
			}
			else {
				break;
			}

		}
		return connectionInStrings;
	
	}
	
	/*
	 * prepare the connections' array from the charElement
	 */
		
	public static String prepareConnectionsForGUI(charElement[] connection, connectionElement[] connectionArray){
		DatabaseManager dbManager = DatabaseManager.getInstance();
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

			startName =dbManager.getNameFromId(conLast.characterId);
			endName = dbManager.getNameFromId(conPrev.characterId);
			atrName = dbManager.getAttributeNameFromID(attributeString, attributeVal);

			if (noEndAlg.getR()!= ConnectionResult.Ok){
				return null;
			}
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
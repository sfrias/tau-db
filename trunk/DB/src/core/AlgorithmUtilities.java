package core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import GUI.GuiHandler;
import dataTypes.charElement;
import dataTypes.connectionElement;
import database.DatabaseManager;
import enums.ExecutionResult;
import enums.Tables;


public class AlgorithmUtilities {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	static int maxNumberOfConnection=0;

	public static void setMaxNumber(int number){
		maxNumberOfConnection = number;
	}
	public static int getMaxNumber(){
		return maxNumberOfConnection;
	}

	/*
	 *Prepares a connection array for GUI in case found in history 	
	 */
	public static ExecutionResult prepareConnectionsFromHistory(String connArr, connectionElement[] connectionArray){
		DatabaseManager dbManager = GuiHandler.getDatabaseManager();
		
		String startName=null, endName=null;
		String[] valueArr = new String[4];
		String connections[] = connArr.split("\t");
		String atrName = null;
		for (int i=0; i<connections.length; i++){
			if (!connections[i].equals("")){ 
				valueArr = connections[i].split(",");
				startName =dbManager.getNameFromId(Integer.parseInt(valueArr[0]));
				endName = dbManager.getNameFromId(Integer.parseInt(valueArr[1]));
				
				if (startName == null || endName == null){
					return ExecutionResult.Exception;
				}
				atrName = valueArr[3];
				connectionArray[i] = new connectionElement(startName, endName, valueArr[2], atrName);
				atrName = null;
			}
		}
		return ExecutionResult.General_Success;
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
		String charactersWithAtr = "SELECT " + joinedAtr + "_" + currentAtr + "_id " +
		" FROM " + joinedAtr + 
		" WHERE " + joinedAtr +  "_character_id =" + end_id + " AND (" ;
		return charactersWithAtr;
	}
	
	
	static String directConnectionRealtions(String currentAtr, int start_id,int unspecifiedIdOfCharacter, int end_id, boolean directToEnd){
		String first = "character_id1";
		String second = "character_id2";
		String parent = "parent_character_id";
		String child = "child_character_id";
		String charactersWithAtr;

		if (currentAtr.equals(Tables.parent.name()) && directToEnd) { //only table that has meaning to each column
			charactersWithAtr = AlgorithmUtilities.relationsQuery(parent, child, currentAtr,"=", start_id, end_id);
		}
		else if (currentAtr.equals(Tables.parent.name())){
			charactersWithAtr = AlgorithmUtilities.relationsQuery(parent, child, currentAtr,"!=", start_id, unspecifiedIdOfCharacter);
		}
		else if (directToEnd){
			charactersWithAtr = AlgorithmUtilities.relationsQuery(first, second, currentAtr,"=", start_id, end_id);			
		}
		else {
			charactersWithAtr = AlgorithmUtilities.relationsQuery(first, second, currentAtr,"!=", start_id, unspecifiedIdOfCharacter);
		}
		return charactersWithAtr;
	}



	public static ExecutionResult prepareTablesAndHashMaps(){
		
		Algorithm alg = Algorithm.getInstance();
		TreeMap<String, Tables> joinedAttributesMap = new TreeMap<String,Tables>();
		int numOfTables = alg.getTables().length;
		String atrTable; 
		Tables currentTable,putCouples;
		Tables[] attributes = new Tables[numOfTables-1]; 
		Tables[] result = new Tables[numOfTables-1];

		int indexOfAttr=0, indexOfResult=0;
		for (int i=0; i< numOfTables; i++){
			currentTable = alg.getTables()[i];
			if (currentTable.equals(Tables.characters.name())){
				continue;
			}
			else if (!currentTable.name().contains("and")){
				attributes[indexOfAttr]=currentTable;
				indexOfAttr++;
				alg.putInPrintRepresentation(currentTable.name(),  alg.getTables()[i].toString());
			}
			else {
				atrTable = currentTable.name().substring(15);
				joinedAttributesMap.put(atrTable, currentTable);
			}
		}

		for (int i=0; i<indexOfAttr;i++){ 		//first loop- looking for joinedTables
			putCouples = joinedAttributesMap.get(attributes[i].name());
			if (putCouples != null){
				result[indexOfResult]=attributes[i];
				result[indexOfResult+1]=putCouples;
				indexOfResult = indexOfResult + 2;
				attributes[i]=Tables.characters;
			}
		}
		
		alg.setIndexOfJumps(indexOfResult);

		//adding all other tables;
		for (int i=0; i<indexOfAttr;i++){
			if(attributes[i] != Tables.characters){
				result[indexOfResult]=attributes[i];
				indexOfResult++;
			}
		}

		joinedAttributesMap.clear();
		alg.setTablesArr(result);

		//adding all tables and their internal identifier to hash maps
		for (short i=0; i<result.length;i++){
			alg.putInTabelsMap(result[i].name(),i);
			alg.putInReversedTabelsMap((short)i, result[i].name());
		}

		alg.putInTabelsMap("child",(short) result.length);
		alg.putInReversedTabelsMap((short) result.length, "child");

		return ExecutionResult.General_Success;
	}
	
	
	
	/*
	 * get the connection from connection element
	 */
	public static String readOneConnectionElement(connectionElement connElement){
		String theConnection;
		String attribute = connElement.getAttribute();
		String startName = connElement.getStartName();
		String endName = connElement.getEndName();
		if (attribute.equals(Tables.romantic_involvement.toString()) ) {
			theConnection = startName + " has a " + attribute + " relationship with " + endName;
		}
		else if( attribute.equals("child")) {
			theConnection = startName + " is " + endName +"'s child";
		}
		
		else if (attribute.equals(Tables.parent.toString())) {
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
		String[] connectionInStrings = new String[maxNumberOfConnection];
		for (int i=0; i<maxNumberOfConnection; i++){
			if (connElementArray[i] != null){
				connectionInStrings[i] = readOneConnectionElement(connElementArray[i]);
			}
			else {
				connectionInStrings[i]=null;
			}

		}
		return connectionInStrings;
	
	}
	
	/*
	 * prepare the connections' array from the charElement
	 */
		
	
	public static String prepareConnectionsForGUI(charElement[] connection, connectionElement[] connectionArray){
		
		DatabaseManager dbManager = GuiHandler.getDatabaseManager();
		Algorithm alg = Algorithm.getInstance();
		
		String startName="", endName="";
		String toHisory="";
		String atrName = null;
		short attribute; 
		int attributeVal;
		String attributeString;
		charElement conLast = connection[0], conPrev;
		int i=0;
		while (conLast.getPrevElement() != null){
			conPrev = conLast.getPrevElement();
			attribute = conLast.getConnectedAttribute();
			attributeVal = conLast.getAttributeValue();
			attributeString = alg.getValueFromReversedTableMap(attribute);

			startName =dbManager.getNameFromId(conLast.getCharacterId());
			endName = dbManager.getNameFromId(conPrev.getCharacterId());
			atrName = dbManager.getAttributeNameFromID(attributeString, attributeVal);

			if (startName == null || endName == null || atrName == null || attributeString == null){ //an error has occurred
				return null;
			}
			
			toHisory+= conLast.getCharacterId() +","+conPrev.getCharacterId() +"," + alg.getFromPrintRepresentation(attributeString) + "," + atrName;
			if (conPrev.getPrevElement() != null){
				toHisory+= "\t";
			}
			connectionArray[i] = new connectionElement(startName, endName, alg.getFromPrintRepresentation(attributeString), atrName);
			i++;
			conLast = conPrev;
		}
		return toHisory;
		
	}
}

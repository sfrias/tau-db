package core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import GUI.GuiHandler;
import connection.JDCConnection;
import dataTypes.Character;
import dataTypes.ReturnElement;
import dataTypes.charElement;
import dataTypes.connectionElement;
import database.DatabaseManager;
import enums.ConnectionResult;
import enums.ExecutionResult;
import enums.Tables;

public class Algorithm{

	private static Algorithm instance = null;
	private boolean init = false;

	private Tables[] tbs;
	private Tables[] tablesArr;
	private int indexOfJumps;
	private int maxConnection;

	private TreeMap<String, Short> tablesMap = new TreeMap<String, Short>();
	private TreeMap<Short, String> reverseTablesMap = new TreeMap<Short, String>();
	private TreeMap<String, String> printRepresentation = new TreeMap<String,String>();
	private HashSet<Integer> foundCharactersIDs = new HashSet<Integer>();
	private List<charElement> currentPhase = new ArrayList<charElement>();
	private List<charElement> previousPhase = new ArrayList<charElement>();
	
	private DatabaseManager dbManager = GuiHandler.getDatabaseManager();
	private int end_id;
	private Character start_character = null;
	private Character end_character = null;
	private ConnectionResult status = ConnectionResult.Ok;
	private int globalNumOfConnections;	
	
	private Algorithm() {
		tbs = Tables.values();
	}
	
	public static Algorithm getInstance() {
		if (instance == null){
			instance = new Algorithm();	
		}
		return instance;
	}
	
	public void initialization(){
		if (!init){
			if (AlgorithmUtilities.prepareTablesAndHashMaps()==ExecutionResult.General_Success){
				init = true;				
			}
			else{
				init = false;			
			}
		}
		else{
			setR(ConnectionResult.Ok);
		}
	}
	
	public Tables[] getTables(){
		return tbs;
	}
	
	public ConnectionResult getR (){
		return status;
	}
	
	public  void setR(ConnectionResult re){
		if (status != ConnectionResult.Ok && re != ConnectionResult.Ok){ //we want to hold the first error that occurred
			return;
		}
		status = re;
	}
	
	public void setIndexOfJumps(int idx){
		indexOfJumps = idx;
	}
	
	public void setMaxNumOfConnection(int maxNum){
		maxConnection = maxNum;
	}
	
	public  int getMaxNumOfConnection(){
		return maxConnection;
	}
	
	public void putInTabelsMap(String key, Short value){
		tablesMap.put(key, value);
	}
	
	public void putInReversedTabelsMap( Short key, String value){
		reverseTablesMap.put(key, value);
	}
	
	
	public void putInPrintRepresentation( String key, String value){
		printRepresentation.put(key, value);
	}
	
	public String getFromPrintRepresentation( String key){
		return printRepresentation.get(key);
	}
	
	public void setTablesArr(Tables[] arr){
		tablesArr = arr;
	}
	
	public String getValueFromReversedTableMap(short key){
		return reverseTablesMap.get(key);
	}
	
	public int getGlobalNumOfConnections(){
		return globalNumOfConnections;
	}


	/*
	 * While the phase is smaller than the maximum number of connections, we are running over all characters found in the last phase
	 * and saving all new connections found.
	 * If this is the last phase, we just check a connection from all of the characters found in the last phase to the end character.
	 */
	private boolean findConnection(charElement[] result){
		Iterator<charElement> iterator = previousPhase.iterator();

		boolean resultFlag = false;
		charElement currentElement;
		if (globalNumOfConnections==maxConnection){ //last phase, only needs to check direct connection to end character
			while (iterator.hasNext() ){
				currentElement = iterator.next();
				resultFlag = DirectConnectionToEnd(currentElement, result);
				if (getR() != ConnectionResult.Ok || resultFlag){
					break;
				}
			}
			return resultFlag;
		}

		while (iterator.hasNext()){
			currentElement = iterator.next();
			resultFlag = directConnectionToAny(currentElement, result);
			if (getR() != ConnectionResult.Ok || resultFlag){
				break;
			}
		}

		previousPhase = currentPhase;
		currentPhase = new ArrayList<charElement>();
		return resultFlag;
	}



	/*
	 * Place of birth is a special attribute since a character could have only one value.
	 */

	
	
	private boolean helperForDirectConnectionToAnyPlaceOfBirth(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, int atrID, charElement[] result){

		int currentid=0;
		boolean foundMatch = false;

		try {
			while (charsWithAtrRS.next()) {
				currentid = charsWithAtrRS.getInt(1);

				if (foundCharactersIDs.contains(currentid)){ //already found a connection in this phase.
					continue;	
				}
				foundMatch = addNewConnection(currentid, start_element, currentAtr, atrID, result);
				if (foundMatch){
					return true;
				}
			}


		}catch (SQLException e) {
			System.out.println(e.toString());
			setR (ConnectionResult.Exception);
			return false;
		}
		return foundMatch;

	}

	

	/*
	 * For each character that share a mutual attribute with the character represented by start element, we check whether this character
	 * was already found. If not, we add the connection found, using 'addNewConnection' function.
	 */

	private boolean helperForDirectConnectionToAll	(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, charElement[] result){

		int currentid=0, atrID;
		boolean foundMatch = false;

		try {
			while (charsWithAtrRS.next()) {
				currentid = charsWithAtrRS.getInt(1);
				atrID = charsWithAtrRS.getInt(2);

				if (foundCharactersIDs.contains(currentid)){ //already found a this connection 
					continue;	
				}

				foundMatch= addNewConnection(currentid, start_element, currentAtr, atrID, result);
				if (foundMatch){
					return true;
				}
			}			
		}catch (SQLException e) {
			System.out.println(e.toString());
			setR (ConnectionResult.Exception);
			return false;
		}
		return foundMatch;
	}


	/*
	 * For each character that share a mutual attribute with the character represented by start element, we check whether this character
	 * was already found in the current phase (this function is called only if the character wasn't found in previous phases).
	 * If not, we add the connection found and add this character to the found character both in the current phase and in general.
	 */


	private boolean addNewConnection(int currentid, charElement start_element, String currentAtr, int atrID, charElement[] result){
		charElement connection = new charElement(currentid, start_element);
		short attribute = tablesMap.get(currentAtr);
		connection.setConnectedAttribute(attribute);;
		connection.setAttributeValue(atrID);

		if (currentid == end_id){
			result[0] = connection;
			return true;
		}
		foundCharactersIDs.add(currentid);
		currentPhase.add(connection);
		return false;
	}

/*
 * For an attribute of relation (parent-child / romantic involvement), we need to check the exact relation.
 * In case of a parent-child attribute it matters (while in the romantic involvement the order doesn't matter).
 */

	private boolean helperForDirectConnectionToAnyInRealtions(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, charElement[] result){

		int currentidField1, currentidField2=0, currentid;
		String atr = currentAtr;
		boolean foundMatch = false;

		try {
			while (charsWithAtrRS.next()) {
				currentidField1 = charsWithAtrRS.getInt(1);
				currentidField2 = charsWithAtrRS.getInt(2);
				if (currentidField1 == start_element.getCharacterId()){
					currentid = currentidField2;
					if (currentAtr.equals(Tables.parent.name())){ //that means that the currentid is the child of start_element
						atr = "child";
					}
				}
				else {
					currentid = currentidField1;
				}

				if (foundCharactersIDs.contains(currentid)){ //already found a connection in this phase.
					continue;	
				}

				foundMatch = addNewConnection(currentidField2, start_element, atr, -2, result);
				if (foundMatch){
					return true;
				}
			}


		}catch (SQLException e) {
			System.out.println(e.toString());
			setR (ConnectionResult.Exception);
			return false;
		}
		return foundMatch;
	}

	/*
	 * returns a string that will be used in the query of finding all characters that share an attribute with the character start_id.
	 */

	private int findConnectionToEnd(Tables joinedAtr,Tables currentAtr,int start_id){
		JDCConnection conn=null;
		Statement atrStmt=null;
		ResultSet atrValRS=null;
		
		try {
			conn = dbManager.getConnection();
			atrStmt = conn.createStatement();			
			atrValRS = atrStmt.executeQuery(AlgorithmUtilities.specificAttributeValuesQuery(joinedAtr.name(), currentAtr.name(), start_id));
			int unspecifiedId = dbManager.getUnspecifiedId(currentAtr);
			int currentAtrVal=-1;

			while (atrValRS.next()){ //all attributes
				currentAtrVal = atrValRS.getInt(1);
				if (currentAtrVal== unspecifiedId ) { //not relevant 
					continue;
				}
				if (end_character.getValueByAttribute(currentAtr.name()) != null && end_character.getValueByAttribute(currentAtr.name()).contains(currentAtrVal)){
					return currentAtrVal;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			setR(ConnectionResult.Exception);
			return -1;
		}
		finally {
			if (atrStmt != null){
				try {
					atrStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					setR(ConnectionResult.Close_Exception);
				}
			}
			if (atrValRS!= null){
				try {
					atrValRS.close();
				} catch (SQLException e) {
					e.printStackTrace();
					setR(ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					setR(ConnectionResult.Close_Exception);
				}
			}

		}
		return -1;
	}
	
	private String findConnectedCharacters(Tables joinedAtr,Tables currentAtr,int start_id, int unspecifiedIdOfCharacter, boolean firstConnection){
		JDCConnection conn=null;
		Statement atrStmt=null;
		ResultSet atrValRS=null;
		String charactersWithAtr = "";
		boolean isEmptyQuery = true;
		int currentAtrVal=-1;
		
		charactersWithAtr =AlgorithmUtilities.getAllValuesOfASpecificAttribute(joinedAtr.name(), currentAtr.name(), start_id, unspecifiedIdOfCharacter);
		
		if (firstConnection){
			if (start_character.getValueByAttribute(currentAtr.name())== null){
				return null;
			}
			Iterator<Integer> iter = start_character.getValueByAttribute(currentAtr.name()).iterator();
			while (iter.hasNext()){
				currentAtrVal = iter.next();

				if (!isEmptyQuery){
					charactersWithAtr += " OR ";
				}
				
				isEmptyQuery = false;
				charactersWithAtr +=  joinedAtr.name() + "_" + currentAtr.name() + "_id =" + currentAtrVal;
			}
		}
		
		else {
			
	
			try {
				conn = dbManager.getConnection();
				atrStmt = conn.createStatement();			
				atrValRS = atrStmt.executeQuery(AlgorithmUtilities.specificAttributeValuesQuery(joinedAtr.name(), currentAtr.name(), start_id));
				int unspecifiedId = dbManager.getUnspecifiedId(currentAtr);
				if (unspecifiedId == -1){
					setR(ConnectionResult.Exception);
				}
				
				//HERE I FINISHED
				
	
				while (atrValRS.next()){ //all attributes
					currentAtrVal = atrValRS.getInt(1);
					if (currentAtrVal== unspecifiedId ) { //not relevant 
						continue;
					}
					
					if (!isEmptyQuery){
						charactersWithAtr += " OR ";
					}
					isEmptyQuery = false;
					charactersWithAtr +=  joinedAtr + "_" + currentAtr + "_id =" + currentAtrVal;
				}
	
			} catch (SQLException e) {
				e.printStackTrace();
				setR(ConnectionResult.Exception);
				return null;
			}
			finally {
				if (atrStmt != null){
					try {
						atrStmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
				if (atrValRS!= null){
					try {
						atrValRS.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
				if (conn!= null){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
	
			}
		}
		
		if (!isEmptyQuery){
			charactersWithAtr += ")";
			return charactersWithAtr;
		}
		else{
			return null;
		}
	}


	/*
	 * returns the string that will be used in the query for all characters that share the same place of birth with the start_id character
	 */



	private String directConnetionPlaceOfBirth(int start_id, int unspecifiedIdOfCharacter,int[] atrVal, boolean directToEnd){
		Tables currentAtr = Tables.place_of_birth;
		int placeOfBirth = 0; 
		String selectAtrValues = 	AlgorithmUtilities.simpleQuery("character_" + currentAtr+ "_id" ,"characters","character_id =" + start_id);
		String charactersWithAtr;
		ResultSet atrValRS =null;
		Statement atrStmt = null;
		JDCConnection conn = null;
		try {
			conn = dbManager.getConnection();
			atrStmt = conn.createStatement();
			atrValRS = atrStmt.executeQuery(selectAtrValues);
			atrValRS.first();
			placeOfBirth = atrValRS.getInt(1);
			if (placeOfBirth == dbManager.getUnspecifiedId(currentAtr)){
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			setR (ConnectionResult.Exception);
			return null;
		}
		finally {
			if (atrStmt != null){
				try {
					atrStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					setR (ConnectionResult.Close_Exception);
				}
			}
			if (atrValRS!= null){
				try {
					atrValRS.close();
				} catch (SQLException e) {
					e.printStackTrace();
					setR (ConnectionResult.Close_Exception);
				}
			}
			if (conn!= null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					setR(ConnectionResult.Close_Exception);
				}
			}
		}

		atrVal[0]=placeOfBirth;
		if (directToEnd){
			return null;
		}
		else {
			charactersWithAtr = AlgorithmUtilities.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + currentAtr.name()+ "_id =" + placeOfBirth ,  "character_id != " + unspecifiedIdOfCharacter,"character_" + currentAtr+ "_id !=" +dbManager.getUnspecifiedId(currentAtr), false);
		}
		return charactersWithAtr;

	}

	
	/*
	 * This function is used only if the current phase doesn't equal to the maximum number of connections allowed.
	 * Here we find all characters that are directly connected to the character represented by start_element.
	 * For each character that hasn't been found before, we add a new charElement of this character.
	 * If character was already found, no other instance is created for this character.
	 * If an error hasn't occurred in any phase of this function, the result value will be true. Otherwise - false.
	 */


	private boolean directConnectionToAny(charElement start_element, charElement[] result){

		ResultSet charToAny = null;
		Tables 	currentAtr,joinedAtr; 
		String charactersWithAtr;
		int unspecifiedIdOfCharacter = dbManager.getUnspecifiedId(Tables.characters); 
		int attributes = tablesArr.length;
		int start_id = start_element.getCharacterId();
		boolean firstConnection = false;
		Statement charWithAtrStmt=null;
		boolean foundMatch = false;
		int[] valPlaceOfBirth = new int[1];

		if (start_id == start_character.getCharId()){
			firstConnection=true;
		}
		JDCConnection conn = null;
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			try {
				conn = dbManager.getConnection();
				currentAtr =tablesArr[atr];
				charWithAtrStmt = conn.createStatement();
				if (atr < indexOfJumps) {
					joinedAtr = tablesArr[atr+1];
					charactersWithAtr = findConnectedCharacters(joinedAtr, currentAtr, start_id, unspecifiedIdOfCharacter,firstConnection);
					if (getR() != ConnectionResult.Ok){
						return false;
					}
					if (charactersWithAtr!=null){
						charToAny = charWithAtrStmt.executeQuery(charactersWithAtr);
						foundMatch = helperForDirectConnectionToAll(charToAny, start_element, currentAtr.name(),result);
						if (getR() != ConnectionResult.Ok){
							return false;
						}
					}

					atr = atr+1;
				} 

				else if (	tablesArr[atr].equals(Tables.romantic_involvement.name()) ||
						tablesArr[atr].equals(Tables.parent.name())){

					charactersWithAtr = AlgorithmUtilities.directConnectionRealtions(currentAtr.name(), start_id, unspecifiedIdOfCharacter,end_id, false);
					charToAny = charWithAtrStmt.executeQuery(charactersWithAtr);
					foundMatch = helperForDirectConnectionToAnyInRealtions(charToAny, start_element, currentAtr.name(), result);
					if (getR() != ConnectionResult.Ok){
						return false;
					}
				}

				else if (tablesArr[atr].equals(Tables.place_of_birth.name())){
					charactersWithAtr = directConnetionPlaceOfBirth(start_id, unspecifiedIdOfCharacter,valPlaceOfBirth, false);
					if (getR() != ConnectionResult.Ok){
						return false;
					}
					if (charactersWithAtr!=null){ //place of birth of character is not unspecified
						charToAny = charWithAtrStmt.executeQuery(charactersWithAtr); 
						foundMatch = helperForDirectConnectionToAnyPlaceOfBirth(charToAny, start_element, currentAtr.name(), valPlaceOfBirth[0], result);
						if (getR() != ConnectionResult.Ok) {
							return false;
						}
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
				setR(ConnectionResult.Exception);
				return false;
			}
			finally {
				if (charWithAtrStmt != null){
					try {
						charWithAtrStmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
				if (charToAny!= null){
					try {
						charToAny.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
				if (conn!= null){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
			}

			if (foundMatch){ //found a match between start_id and end_id
				break;
			}
		}

		return foundMatch;
	}

	/*
	 * This function is used only if the current phase equals to the maximum number of connections allowed.
	 * Here we search if the end_id character share an attribute with (at least) one of the characters found during the previous phase.  
	 * If an error hasn't occurred and we found a connection to end_id - the result will be true. Otherwise - false.
	 * The charElement that represents the end_id will be returned to the calling function through the array 'result', in the first cell.
	 * This allows us to pass the element by his reference, and not by his value (not creating duplicates).
	 */

	private boolean DirectConnectionToEnd(charElement start_element, charElement[] result){

		ResultSet charsWithAtrRS=null;
		String charactersWithAtr;
		Tables currentAtr, joinedAtr;
		String currentAttribute=null;
		int attributes = tablesArr.length;
		int start_id = start_element.getCharacterId();
		boolean foundMatch = false;
		int currentAtrVal = -2;
		charElement end_element;
		Statement charAtrStmt = null;
		int valPlaceOfBirth[] = new int[1];
		JDCConnection conn = null;


		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			try {
				conn = dbManager.getConnection();
				currentAtr =tablesArr[atr];
				charAtrStmt = conn.createStatement();
				if (atr < indexOfJumps) {

					joinedAtr = tablesArr[atr+1];
					currentAtrVal = findConnectionToEnd(joinedAtr, currentAtr, start_id);
					if (getR() != ConnectionResult.Ok){
						return false;
					}
					if (currentAtrVal != -1){
						foundMatch = true;
					}
					atr=atr+1;
					currentAttribute = currentAtr.name();
				} //end of while loop

				else if (	tablesArr[atr].equals(Tables.romantic_involvement.name()) ||
						tablesArr[atr].equals(Tables.parent.name())){
					currentAttribute = currentAtr.name();
					charactersWithAtr = AlgorithmUtilities.directConnectionRealtions(currentAtr.name(), start_id, -1,end_id, true);
					charsWithAtrRS= charAtrStmt.executeQuery(charactersWithAtr);
					if (charsWithAtrRS.next()){
						if (charsWithAtrRS.getInt(1) == start_id){
							currentAttribute = "child";
							foundMatch=true;
						}	
					}
					currentAtrVal =-2;
				}	

				else if (tablesArr[atr].equals(Tables.place_of_birth.name())){
					directConnetionPlaceOfBirth(start_id, -1,valPlaceOfBirth, true);
					if (getR() != ConnectionResult.Ok){
						return false;
					}
					if (end_character.getPlaceOfBirth()!= null && end_character.getPlaceOfBirth().contains(valPlaceOfBirth[0])){
							foundMatch=true;
							currentAtrVal = valPlaceOfBirth[0];
					}
					currentAttribute = currentAtr.name();
				}

			} catch (SQLException e) {
				e.printStackTrace();
				setR (ConnectionResult.Exception);
				return false;
			}
			finally {
				if (charAtrStmt != null){
					try {
						charAtrStmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR (ConnectionResult.Close_Exception);
					}
				}
				if (charsWithAtrRS!= null){
					try {
						charsWithAtrRS.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR (ConnectionResult.Close_Exception);
					}
				}
				if (conn!= null){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
						setR(ConnectionResult.Close_Exception);
					}
				}
			}

			if (foundMatch){
				break;
			}

		}// end of external loop

		if (foundMatch){
			end_element = new charElement(end_id, start_element);
			end_element.setConnectedAttribute(tablesMap.get(currentAttribute));
			end_element.setAttributeValue(currentAtrVal);
			result[0] = end_element;
		}
		return foundMatch;
	}



	private void clearAll(){
		previousPhase.clear();
		currentPhase.clear();
		foundCharactersIDs.clear();
	}




	/*
	 * main function for looking a connection between two characters
	 */

	public ReturnElement lookForConnection(Character start_character, Character end_character){

		ReturnElement result = new ReturnElement(ConnectionResult.Exception,null);
		int start_id = start_character.getCharId();
		int end_id = end_character.getCharId();
		this.start_character= start_character;
		this.end_character= end_character;
		if (start_id == end_id){
			System.out.println("match of length 0");
			result = new ReturnElement(ConnectionResult.Found_Connection_Of_Length_0,null);
			return result;
		}

		setMaxNumOfConnection(3);
		AlgorithmUtilities.setMaxNumber(maxConnection);
		connectionElement[] connectionArray = new connectionElement[maxConnection];

		ExecutionResult exists =null;
		this.end_id = end_id;
		
		ConnectionResult checkForCharactersExistance = dbManager.checkForExistance(start_id, end_id);
		result.setResult(checkForCharactersExistance);
		
		switch (checkForCharactersExistance){
		case Ok:
				break;
		default: 
			return result;
		}

		// checks if the connection between these 2 characters already in history table
		exists = dbManager.lookForConnectionInHistory(start_id, end_id, connectionArray);
		
	
		if (exists== ExecutionResult.Exception || exists == ExecutionResult.Close_Exception){ //an error occurred while trying to extract the names of the characters
			return result;
		}

		//found a connection
		if (exists == ExecutionResult.Found_Connection_In_History){
			result = new ReturnElement(ConnectionResult.Found_Connection,connectionArray);
			dbManager.executeUpdateInSuccesRate(true);
			return result;
		}

		exists = dbManager.lookForConnectionInFailedSearchesTable(start_id, end_id);
		

		if (exists== ExecutionResult.Exception || exists == ExecutionResult.Close_Exception){ //an error occurred while trying to extract the names of the characters		
			return result;
		}
		if (exists == ExecutionResult.Found_Connection_In_Failed_Searches){
			result = new ReturnElement(ConnectionResult.Did_Not_Find_Connection,null);
			dbManager.executeUpdateInSuccesRate(false);
			return result;
		}

		//couldn't find the connection in the history table, executing a search
		boolean matchFound = false;
		charElement[] theConnection = new charElement[1];
		charElement startElement = new charElement(start_id, null);
		previousPhase.add(startElement);
		String connectionString = null;
		
		for (int level = 1; level<maxConnection+1; level++) {
			globalNumOfConnections = level;
			matchFound = findConnection(theConnection);
			if (getR() != ConnectionResult.Ok){
				result = new ReturnElement(getR(),null);
				return result; //change to return element
			}

			if (matchFound) {
				connectionString = AlgorithmUtilities.prepareConnectionsForGUI(theConnection,connectionArray);
				if (connectionString==null){//an error has occurred
					setR(ConnectionResult.Exception);
					break;
				}

				break;
			}
		}

		clearAll();

		if (getR() == ConnectionResult.Ok && !matchFound){ 
			dbManager.insertIntoFailedSearchesTable(start_id, end_id); //no catching errors needed here
			dbManager.executeUpdateInSuccesRate(false);  //no catching errors needed here
			result = new ReturnElement(ConnectionResult.Did_Not_Find_Connection,null);
			
		}
		
		else if (getR() == ConnectionResult.Ok && matchFound){ 
			dbManager.insertIntoHistory(connectionString); 
			dbManager.executeUpdateInSuccesRate(true);
			result = new ReturnElement(ConnectionResult.Found_Connection,connectionArray);
			
		}

		return result;

	}

}
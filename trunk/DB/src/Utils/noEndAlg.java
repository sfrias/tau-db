package Utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import Connection.JDCConnection;
import Enums.ConnectionResult;
import Enums.Tables;
import db.DatabaseManager;



public class noEndAlg{
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	private DatabaseManager dbManager = DatabaseManager.getInstance();
	static JDCConnection conn;
	static ConnectionResult r = ConnectionResult.Ok;

	Tables[] tbs;
	String[] tablesArr;
	int indexOfJumps;
	int maxConnection;
	int globalNumOfConnections;
	int skips=0;
	int end_id;
	
	static TreeMap<String, Short> tablesMap = new TreeMap<String, Short>();
	static TreeMap<Short, String> reverseTablesMap = new TreeMap<Short, String>();
	static HashSet<Integer> foundCharactersIDs = new HashSet<Integer>();
	List<charElement> currentPhase = new ArrayList<charElement>();
	List<charElement> previousPhase = new ArrayList<charElement>();
	TreeMap<String, Integer> unspecifiedIdOfTables = new TreeMap<String,Integer>();
	
	public noEndAlg(){
		conn = dbManager.getConnection() ;
		tbs = Tables.values();
	}
	
	public JDCConnection getConnention(){
		return conn;
	}
	
	public ConnectionResult getR (){
		return r;
	}
	
	public void setR(ConnectionResult re){
		if (r != ConnectionResult.Ok){ //we want to hold the first error that occurred
			return;
		}
		r = re;
	}
	
	private boolean findConnection(charElement[] result){
		Iterator<charElement> iterator = previousPhase.iterator();
	
		boolean resultFlag = false;
		charElement currentElement;
		if (globalNumOfConnections==maxConnection){ //last phase, only needs to check direct connection
			while (iterator.hasNext() ){
				currentElement = iterator.next();
				resultFlag = DirectConnectionToEnd(currentElement, result);
				if (getR() != ConnectionResult.Ok){
					return false;
				}
			
				if (resultFlag){
					return true;
				}
			}
			
			return false;
		}
		
		iterator = previousPhase.iterator();
		while (iterator.hasNext()){
			currentElement = iterator.next();
			resultFlag = directConnectionToAny(currentElement, result);
			if (getR() != ConnectionResult.Ok){
				return false;
			}
			if (resultFlag){
					break;
				}
			}
		
		previousPhase = currentPhase;
		currentPhase = new ArrayList<charElement>();
		return resultFlag;
	}
	
	
	
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
			System.out.println("error execute query-" + e.toString());
			setR (ConnectionResult.Exception);
			return false;
		}
		return foundMatch;
		
	}
	
	
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
			System.out.println("error execute query-" + e.toString());
			setR (ConnectionResult.Exception);
			return false;
		}
		return foundMatch;
	}
	
	
	
	private boolean addNewConnection(int currentid, charElement start_element, String currentAtr, int atrID, charElement[] result){
		charElement connection = new charElement(currentid, start_element);
		short attribute = tablesMap.get(currentAtr);
		connection.connectedAttribute = attribute;;
		connection.attributeValue = atrID;
		
		if (currentid == end_id){
			result[0] = connection;
			return true;
		}
		foundCharactersIDs.add(currentid);
		currentPhase.add(connection);
		return false;
	}
	
	
	
	private boolean helperForDirectConnectionToAnyInRealtions(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, charElement[] result){
		
		int currentidField1, currentidField2=0, currentid;
		String atr = currentAtr;
		boolean foundMatch = false;
		
		try {
			while (charsWithAtrRS.next()) {
				currentidField1 = charsWithAtrRS.getInt(1);
				currentidField2 = charsWithAtrRS.getInt(2);
				if (currentidField1 == start_element.characterId){
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
			System.out.println("error execute query-" + e.toString());
			setR (ConnectionResult.Exception);
			return false;
		}
		return foundMatch;
	}
	
	
	private String findConnectedCharacters(String joinedAtr,String currentAtr,int start_id, int unspecifiedIdOfCharacter, boolean directToEndID){
		
			Statement atrStmt=null;
			ResultSet atrValRS=null;
			String charactersWithAtr = "";
			boolean isEmptyQuery = true;
			try {
				atrStmt = conn.createStatement();			
				atrValRS = atrStmt.executeQuery(algorithmUtils.specificAttributeValuesQuery(joinedAtr, currentAtr, start_id));
				int unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
				int currentAtrVal=-1;

				if (directToEndID){
					charactersWithAtr = algorithmUtils.getDirectConnectionString(joinedAtr, currentAtr, end_id);
				}
				else{
					charactersWithAtr =algorithmUtils.getAllValuesOfASpecificAttributes(joinedAtr, currentAtr, start_id, unspecifiedIdOfCharacter);
				}
	
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
				
				//if (atrValRS != null) atrValRS.close();
				//if (atrStmt != null) atrStmt.close();
				//atrStmt = null;
				//atrValRS = null;
			
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
				
			}
			
			if (!isEmptyQuery){
				charactersWithAtr += ")";
				return charactersWithAtr;
			}
			else{
				return null;
			}
	}
	
	
	
	private String directConnectionRealtions(String currentAtr, int start_id,int unspecifiedIdOfCharacter, boolean directToEnd){
		String first = "character_id1";
		String second = "character_id2";
		String parent = "parent_character_id";
		String child = "child_character_id";
		String charactersWithAtr;
		
		if (currentAtr.equals(Tables.parent.name()) && directToEnd) { //only table that has meaning to each column
			charactersWithAtr = algorithmUtils.relationsQuery(parent, child, currentAtr,"=", start_id, end_id);
		}
		else if (currentAtr.equals(Tables.parent.name())){
			charactersWithAtr = algorithmUtils.relationsQuery(parent, child, currentAtr,"!=", start_id, unspecifiedIdOfCharacter);
		}
		else if (directToEnd){
			charactersWithAtr = algorithmUtils.relationsQuery(first, second, currentAtr,"=", start_id, end_id);			
		}
		else {
			charactersWithAtr = algorithmUtils.relationsQuery(first, second, currentAtr,"!=", start_id, unspecifiedIdOfCharacter);
		}
		return charactersWithAtr;
	}
	
	
	
	private String directConnetionPlaceOfBirth(int start_id, int unspecifiedIdOfCharacter,int[] atrVal, boolean directToEnd){
		String currentAtr = Tables.place_of_birth.name();
		int placeOfBirth = 0; 
		String selectAtrValues = 	algorithmUtils.simpleQuery("character_" + currentAtr+ "_id" ,"characters","character_id =" + start_id);
		String charactersWithAtr;
		ResultSet atrValRS =null;
		Statement atrStmt = null;
		try {
			atrStmt = conn.createStatement();
			atrValRS = atrStmt.executeQuery(selectAtrValues);
			atrValRS.first();
			placeOfBirth = atrValRS.getInt(1);
			if (placeOfBirth == unspecifiedIdOfTables.get(currentAtr)){
				return null;
			}
			//if (atrValRS != null) atrValRS.close();
			//if (atrStmt != null) atrStmt.close();
			//atrValRS = null;
			//atrStmt = null;
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
		}
		
		atrVal[0]=placeOfBirth;
		if (directToEnd){
			charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + currentAtr+ "_id =" + placeOfBirth ,  "character_id = " + end_id,null, true);
		}
		else {
			charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + currentAtr+ "_id =" + placeOfBirth ,  "character_id != " + unspecifiedIdOfCharacter,"character_" + currentAtr+ "_id !=" +unspecifiedIdOfTables.get(currentAtr), false);
		}
		return charactersWithAtr;

	}
	
	
	
	private boolean directConnectionToAny(charElement start_element, charElement[] result){
		
		ResultSet charToAny = null;
		String 	currentAtr, joinedAtr, charactersWithAtr;
		int unspecifiedIdOfCharacter = unspecifiedIdOfTables.get(Tables.characters.name()); 
		int attributes = tablesArr.length;
		int start_id = start_element.characterId;
		Statement charWithAtrStmt=null;
		boolean foundMatch = false;
		int[] valPlaceOfBirth = new int[1];
		
		
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			try {
			currentAtr =tablesArr[atr];
			charWithAtrStmt = conn.createStatement();
			if (atr < indexOfJumps) {
				joinedAtr = tablesArr[atr+1];
				charactersWithAtr = findConnectedCharacters(joinedAtr, currentAtr, start_id, unspecifiedIdOfCharacter, false);
				if (getR() != ConnectionResult.Ok){
					return false;
				}
				if (charactersWithAtr!=null){
					charToAny = charWithAtrStmt.executeQuery(charactersWithAtr);
					foundMatch = helperForDirectConnectionToAll(charToAny, start_element, currentAtr,result);
					if (getR() != ConnectionResult.Ok){
						return false;
					}
				}
				atr = atr+1;
			} 
			
			else if (	//tablesArr[atr].equals(Tables.sibling.) || 
						//tablesArr[atr].equals(Tables.marriage.) ||
						tablesArr[atr].equals(Tables.romantic_involvement.name()) ||
						tablesArr[atr].equals(Tables.parent.name())){
				
				charactersWithAtr = directConnectionRealtions(currentAtr, start_id, unspecifiedIdOfCharacter, false);
				charToAny = charWithAtrStmt.executeQuery(charactersWithAtr);
				foundMatch = helperForDirectConnectionToAnyInRealtions(charToAny, start_element, currentAtr, result);
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
					foundMatch = helperForDirectConnectionToAnyPlaceOfBirth(charToAny, start_element, currentAtr, valPlaceOfBirth[0], result);
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
		}
			
			//if (charToAny != null) charToAny.close();
			//if (charWithAtrStmt != null) charWithAtrStmt.close();
			//charToAny=null;
			//charWithAtrStmt=null;
			
			if (foundMatch){ //found a match between start_id and end_id
				break;
			}
		}
		
		return foundMatch;
	}
	
	
	
	private boolean DirectConnectionToEnd(charElement start_element, charElement[] result){
		
		ResultSet charsWithAtrRS=null;
		String 	currentAtr="", joinedAtr, charactersWithAtr; 
		int attributes = tablesArr.length;
		int start_id = start_element.characterId;
		boolean foundMatch = false;
		int currentAtrVal = -2;
		charElement end_element;
		Statement charAtrStmt = null;
		int valPlaceOfBirth[] = new int[1];
		

		
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			try {
			currentAtr =tablesArr[atr];
			charAtrStmt = conn.createStatement();
			if (atr < indexOfJumps) {

				joinedAtr = tablesArr[atr+1];
				charactersWithAtr = findConnectedCharacters(joinedAtr, currentAtr, start_id, -1, true);
				if (getR() != ConnectionResult.Ok){
					return false;
				}
				if (charactersWithAtr != null){
					charsWithAtrRS= charAtrStmt.executeQuery(charactersWithAtr);
					if (charsWithAtrRS.next()){
						currentAtrVal =charsWithAtrRS.getInt(1);
						foundMatch=true;
						}
				}
				atr=atr+1;
			} //end of while loop
			
			else if (	//tablesArr[atr].equals(Tables.sibling.) || 
						//tablesArr[atr].equals(Tables.marriage.) ||
						tablesArr[atr].equals(Tables.romantic_involvement.name()) ||
						tablesArr[atr].equals(Tables.parent.name())){
				
				charactersWithAtr = directConnectionRealtions(currentAtr, start_id, -1, true);
				charsWithAtrRS= charAtrStmt.executeQuery(charactersWithAtr);
				if (charsWithAtrRS.next()){
					if (charsWithAtrRS.getInt(1) == start_id){
						currentAtr = "child";
						foundMatch=true;
					}	
				}
				currentAtrVal =-2;
			}	
			
			else if (tablesArr[atr].equals(Tables.place_of_birth.name())){
				charactersWithAtr = directConnetionPlaceOfBirth(start_id, -1,valPlaceOfBirth, true);
				if (getR() != ConnectionResult.Ok){
					return false;
				}
				if (charactersWithAtr!= null){
					charsWithAtrRS= charAtrStmt.executeQuery(charactersWithAtr);
					if (charsWithAtrRS.first()){
						foundMatch=true;
						currentAtrVal = valPlaceOfBirth[0];
					}	
				}
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
		}
			
			//if (charAtrStmt!= null) charAtrStmt.close();
			//if (charsWithAtrRS!= null) charsWithAtrRS.close();
			//charAtrStmt=null;
			//charsWithAtrRS=null;
			
			if (foundMatch){
				break;
			}

		}// end of external loop
		
		if (foundMatch){
			end_element = new charElement(end_id, start_element);
			end_element.connectedAttribute = tablesMap.get(currentAtr);
			end_element.attributeValue = currentAtrVal;
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
	
	public ReturnElement lookForConnection(int start_id, int end_id){
		
		ReturnElement result;
		
		if (start_id == end_id){
			System.out.println("match of length 0");
			result = new ReturnElement(ConnectionResult.Found_Connection_Of_Length_0, null);
			return result;
		}

		boolean alreadyExists = false;
		this.end_id = end_id;
		String start_name=algorithmUtils.getNameFromId(start_id,conn);
		String end_name=algorithmUtils.getNameFromId(end_id,conn);
		
		// checks if the connection between these 2 characters already in history table
		//alreadyExists = algorithmUtils.lookForConnectionInHistory(start_name, end_name, start_id, end_id,conn);
		
		//found a connection
		//if (alreadyExists){
		//	return true;
		//}
		//alreadyExists = algorithmUtils.lookForConnectionInFailedSearchesTable(start_name, end_name, start_id, end_id, conn);
		//if (alreadyExists){
			//return false;
		//}
		//couldn't find the connection in the history table, executing a search
		
		boolean matchFound = false;
		charElement[] theConnection = new charElement[1];
		charElement startElement = new charElement(start_id, null);
		previousPhase.add(startElement);
		
		maxConnection=3;
		for (int level = 1; level<maxConnection+1; level++) {
			globalNumOfConnections = level;
			matchFound = findConnection(theConnection);
			if (getR() != ConnectionResult.Ok){
				result = new ReturnElement(getR(),null);
				return result; //change to return element
			}
			
			if (matchFound) {
				System.out.println("Match found between "+ start_name +" and "+ end_name);
				String connectionString = getNameAndPrintConnections(theConnection,conn);
				//algorithmUtils.insertIntoHistory(connectionString, start_id, end_id,conn);
				clearAll();
				result = new ReturnElement(ConnectionResult.Found_Connection, theConnection[0]);
				return result;
			}
			else {
				System.out.println("cannot find a connection in " + level + " num of connection\n");
			}
		}
		
	//	System.out.println(previousPhase.size());
		System.out.println(currentPhase.size());
		clearAll();
		System.out.println("couldn't find a connection");
		//algorithmUtils.insertIntoFailedSearchesTable(start_id, end_id, conn);
		result = new ReturnElement(ConnectionResult.Did_Not_Find_Connection, null);
		return result;

	}
	
	
	public static String getNameAndPrintConnections(charElement[] connection, JDCConnection conn){
			
			String startName="", endName="";
			String toPrint;
			String toHisory="";
			String atrName = "none";
			short attribute; 
			int attributeVal;
			String attributeString;
			charElement conLast = connection[0], conPrev;

			while (conLast.prevElement != null){
				conPrev = conLast.prevElement;
				attribute = conLast.connectedAttribute;
				attributeVal = conLast.attributeValue;
				attributeString = reverseTablesMap.get(attribute);
				
				startName =algorithmUtils.getNameFromId(conLast.characterId, conn);
				endName = algorithmUtils.getNameFromId(conPrev.characterId, conn);

				if (	// attributeString.equals(Tables.sibling.) || 
						//attributeString.equals(Tables.marriage.) ||
						attributeString.equals(Tables.romantic_involvement.name()) ) {
						toPrint = startName + " has a " + attributeString + " relationship with " + endName;
					}
					else if ( attributeString.equals("child")) {
							toPrint = startName + " is " + endName +"'s child";
					}
					else if (attributeString.equals("parent")) {
							toPrint = startName + " is " + endName +"'s parent";
					}
					else {
						atrName = algorithmUtils.getAttributeNameFromID(attributeString, attributeVal, conn);
						toPrint = startName + " has the same "+ attributeString + " as " + endName + " - " + atrName;
					}
					System.out.println(toPrint);
					toHisory+= conLast.characterId +","+conPrev.characterId +"," +attributeString + "," + atrName;
					if (conPrev.prevElement != null){
						toHisory+= "\t";
					}

					conLast = conPrev;
				}
			return toHisory;
			
		}

	
	public int getGlobalNumOfConnections(){
		return globalNumOfConnections;
	}
	
	
	public static void main(String[] args) throws IOException{
		//noEndAlg a = new noEndAlg();
		//algorithmUtils.prepareTablesAndHashMaps(a);
		
		//while (true){
			//a.lookForConnection(1, 6);
			//System.out.println("loop");
		//}
		//System.out.println(a.indexOfJumps);
	/*	for (int i=0;i<table.length;i++){
			System.out.println(i+ ": " + table[i]);
		}*/
		
	//a.fillTables();
	
	
	//long start = System.currentTimeMillis();	
	//long finish = System.currentTimeMillis();
	//long total = finish-start;
	//String time = String.format("%d min, %d sec",      TimeUnit.MILLISECONDS.toMinutes(total),     TimeUnit.MILLISECONDS.toSeconds(total) -      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)) );
	//System.out.println("operation took total time of " + total +"\n" + time);
	//System.out.println(a.skips);
	//System.out.println(foundCharactersIDs.size());
	//System.out.println(a.getGlobalNumOfConnections());
 
		//a.topSerches();
	
		try {
			Tester.tester();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
}

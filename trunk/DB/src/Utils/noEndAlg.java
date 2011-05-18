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
import java.util.concurrent.TimeUnit;

import Connection.JDCConnection;
import Enums.Tables;
import db.DatabaseManager;



public class noEndAlg{
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	private DatabaseManager dbManager = DatabaseManager.getInstance();
	JDCConnection conn;

	Tables[] tbs;
	String[] tablesArr;
	int indexOfJumps;
	int maxConnection;
	int globalNumOfConnections;
	//int numOfCharacters;
	int skips=0;
	int end_id;
	
	TreeMap<String, Short> tablesMap = new TreeMap<String, Short>();
	static TreeMap<Short, String> reverseTablesMap = new TreeMap<Short, String>();
	
	
	static HashSet<Integer> foundCharactersIDs = new HashSet<Integer>();
	List<charElement> currentPhase = new ArrayList<charElement>();
	List<charElement> previousPhase = new ArrayList<charElement>();
	
	//maps all unspecified ids
	TreeMap<String, Integer> unspecifiedIdOfTables = new TreeMap<String,Integer>();
	
	public noEndAlg(){
		conn = dbManager.getConnection() ;
		tbs = Tables.values();
	}
	
	public JDCConnection getConnention(){
		return conn;
	}
	

	
	
	private boolean findConnection(charElement[] result) throws SQLException{
		Iterator<charElement> iterator = previousPhase.iterator();
		System.out.println(previousPhase.size());
	
		boolean resultFlag = false;
		charElement currentElement;
		if (globalNumOfConnections==maxConnection){
			while (iterator.hasNext() ){
				currentElement = iterator.next();
	
	//	if (!currentElement.hasConnections){
				resultFlag = DirectConnectionToEnd(currentElement, result);
			//	
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
			if (resultFlag){
					break;
				}
			}
		
		
		previousPhase = currentPhase;
		currentPhase = new ArrayList<charElement>();		
		return resultFlag;
	}
	
	
	
	private boolean helperForDirectConnectionToAny	(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, int atrID, charElement[] result){
		
		int currentid=0;
		charElement connection;
		
		try {
			while (charsWithAtrRS!= null && charsWithAtrRS.next()) {
				currentid = charsWithAtrRS.getInt(1);

				if (foundCharactersIDs.contains(currentid)){ //already found a connection in this phase.
					continue;	
				}
//				System.out.println("adding to " + currentAtr +  " id number " + currentid + "in phase " + globalNumOfConnections);
				connection = new charElement(currentid, start_element);
				short attribute = tablesMap.get(currentAtr);
				connection.connectedAttribute = attribute;;
				connection.attributeValue = atrID;
				if (currentid == end_id){
					result[0] = connection;
					return true;
				}
				foundCharactersIDs.add(currentid);
				currentPhase.add(connection);
			}
			
			
		}catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}
		return false;
		
	}
	
	
	private boolean helperForDirectConnectionToAny2	(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, charElement[] result){
		
		int currentid=0, atrID;
		charElement connection;
		
		try {
			while (charsWithAtrRS!= null && charsWithAtrRS.next()) {
				currentid = charsWithAtrRS.getInt(1);
				atrID = charsWithAtrRS.getInt(2);
				

				if (foundCharactersIDs.contains(currentid)){ //already found a connection in this phase.
					continue;	
				}
//				System.out.println("adding to " + currentAtr +  " id number " + currentid + "in phase " + globalNumOfConnections);
				connection = new charElement(currentid, start_element);
				short attribute = tablesMap.get(currentAtr);
				connection.connectedAttribute = attribute;;
				connection.attributeValue = atrID;
				if (currentid == end_id){
					result[0] = connection;
					return true;
				}
				foundCharactersIDs.add(currentid);
				currentPhase.add(connection);
			}
			
			
		}catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}
		return false;
		
	}
	
	
	
	private boolean helperForDirectConnectionToAnyInRealtions(ResultSet charsWithAtrRS,charElement start_element, String currentAtr, charElement[] result){
		
		int currentidField1, currentidField2=0, currentid;
		charElement connection;
		String atr = currentAtr;
		
		try {
			while (charsWithAtrRS!= null && charsWithAtrRS.next()) {
				currentidField1 = charsWithAtrRS.getInt(1);
				currentidField2 = charsWithAtrRS.getInt(2);
				if (currentidField1 == start_element.characterId){
					currentid = currentidField2;
					if (currentAtr.equals(Tables.parent.toString())){
						atr = "child";
					}
				}
				else {
					currentid = currentidField1;
				}
				

				if (foundCharactersIDs.contains(currentid)){ //already found a connection in this phase.
					continue;	
				}
//				System.out.println("adding to " + currentAtr +  " id number " + currentid + "in phase " + globalNumOfConnections);
				connection = new charElement(currentid, start_element);
				short attribute = tablesMap.get(atr);
				connection.connectedAttribute = attribute;;
				connection.attributeValue = -2;
				
				if (currentid == end_id){
					result[0] = connection;
					return true;
				}
				foundCharactersIDs.add(currentid);
				currentPhase.add(connection);
			}
			
			
		}catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}
		return false;
	}
	
	
	private boolean directConnectionToAny(charElement start_element, charElement[] result) throws SQLException{
		
		ResultSet atrValRS=null, charToAny = null;
		String 	currentAtr, joinedAtr,selectAtrValues, charactersWithAtr;
		int unspecifiedId=0;
		int unspecifiedIdOfCharacter = unspecifiedIdOfTables.get(Tables.characters.toString()); 
	//	System.out.println("Unsepcified id is " +unspecifiedIdOfCharacter);
		int attributes = tablesArr.length;
		int start_id = start_element.characterId;
		int currentAtrVal = -2;
		Statement atrStmt=null, charWithAtrStmt=null;
		boolean foundMatch = false, isEmpryQuery=true;
		
		/*-should never happen - check is redundant-
		if (start_element.hasConnections){ //already found connections of this character
			skips++;
			return true;
		}*/
		
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			currentAtr =tablesArr[atr];
			if (atr < indexOfJumps) {
				joinedAtr = tablesArr[atr+1];
				atrValRS = algorithmUtils.getAllAtributesQuery(atrStmt,joinedAtr, currentAtr, start_id,conn);
				unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
				charactersWithAtr = "SELECT " +  joinedAtr + "_character_id ," + joinedAtr + "_" + currentAtr + "_id " +
									" FROM " + joinedAtr + 
									" WHERE " + joinedAtr +  "_character_id !=" + unspecifiedIdOfCharacter + " AND " +
									joinedAtr +  "_character_id !=" + start_id + " AND (";
				while (atrValRS.next()){ //all attributes
					currentAtrVal = atrValRS.getInt(1);
					if (currentAtrVal== unspecifiedId ) { //not relevant 
						continue;
						}
					if (!isEmpryQuery){
						charactersWithAtr += " OR ";
					}
					isEmpryQuery = false;
					charactersWithAtr +=  joinedAtr + "_" + currentAtr + "_id =" + currentAtrVal;
						
				}
				if (!isEmpryQuery){
					charactersWithAtr += ")";
					charToAny = algorithmUtils.queryToAny(charWithAtrStmt,charactersWithAtr,conn);
					foundMatch = helperForDirectConnectionToAny2(charToAny, start_element, currentAtr,result);
					isEmpryQuery=true;
					
					}
				
				if (charWithAtrStmt != null) charWithAtrStmt.close();
				if (atrValRS != null) atrValRS.close();
				if (atrStmt != null) atrStmt.close();
				if (charToAny != null) charToAny.close();
				charWithAtrStmt = null;
				atrStmt = null;
				atrValRS = null;
				charToAny = null;
				atr=atr+1;
				
				if (foundMatch){
					break;
				}
			} 
			
			else if (	//tablesArr[atr].equals(Tables.sibling.toString()) || 
						//tablesArr[atr].equals(Tables.marriage.toString()) ||
						tablesArr[atr].equals(Tables.romantic_involvement.toString()) ||
						tablesArr[atr].equals(Tables.parent.toString())){
				
				String first = "character_id1";
				String second = "character_id2";
				String parent = "parent_character_id";
				String child = "child_character_id";
				
				

				if (tablesArr[atr].equals(Tables.parent.toString())) {
					charactersWithAtr = algorithmUtils.relationsQueryToAny(parent, child, currentAtr, start_id, unspecifiedIdOfCharacter);
				}
				else {
					charactersWithAtr = algorithmUtils.relationsQueryToAny(first, second, currentAtr, start_id, unspecifiedIdOfCharacter);
				}

				charToAny = algorithmUtils.queryToAny(charWithAtrStmt,charactersWithAtr, conn); 
				foundMatch = helperForDirectConnectionToAnyInRealtions(charToAny, start_element, currentAtr, result);
				//algorithmUtils.closeQueryResurces(charToAny, charWithAtrStmt);
			}
			
			else if (tablesArr[atr].equals(Tables.place_of_birth.toString())){
				int placeOfBirth; 
				selectAtrValues = 	algorithmUtils.simpleQuery("character_" + currentAtr+ "_id" ,"characters","character_id =" + start_id);
				placeOfBirth = algorithmUtils.simpleQueryIntResult(selectAtrValues,conn);
				unspecifiedId = tablesMap.get(currentAtr);
				charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + currentAtr+ "_id =" + placeOfBirth ,  "character_id != " + unspecifiedIdOfCharacter,"character_" + currentAtr+ "_id !=" +unspecifiedIdOfTables.get(currentAtr), false);
				charToAny = algorithmUtils.queryToAny(charWithAtrStmt,charactersWithAtr,conn); 
				foundMatch = helperForDirectConnectionToAny(charToAny, start_element, currentAtr, placeOfBirth, result);
				//algorithmUtils.closeQueryResurces(charToAny, charWithAtrStmt);
			}
			if (charToAny != null) charToAny.close();
			if (charWithAtrStmt != null) charWithAtrStmt.close();
			charToAny=null;
			charWithAtrStmt=null;
			if (foundMatch){
				break;
			}
		}
//		start_element.hasConnections=true;

		return foundMatch;
	}
	
	

	private boolean DirectConnectionToEnd(charElement start_element, charElement[] result) throws SQLException{
		
		ResultSet atrValRS=null, charsWithAtrRS=null;
		String 	currentAtr="", joinedAtr,selectAtrValues, charactersWithAtr;
		int unspecifiedId=0; 
		int attributes = tablesArr.length;
		int start_id = start_element.characterId;
		boolean foundMatch = false;
		boolean isEmpryQuery = true;
		int currentAtrVal = -2;
		charElement end_element;
		Statement atrStmt = null, charAtrStmt = null;
		
		
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			currentAtr =tablesArr[atr];
			if (atr < indexOfJumps) {
				joinedAtr = tablesArr[atr+1];
				atrValRS = algorithmUtils.getAllAtributesQuery(atrStmt,joinedAtr, currentAtr, start_id,conn);
				unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
				charactersWithAtr = "SELECT " + joinedAtr + "_" + currentAtr + "_id " +
									" FROM " + joinedAtr + 
									" WHERE " + joinedAtr +  "_character_id=" + end_id + " AND (";
				while (atrValRS != null && atrValRS.next()){ //all attributes
					currentAtrVal = atrValRS.getInt(1);
					
					if (currentAtrVal== unspecifiedId ) { //not relevant 
						continue;
						}
					if (!isEmpryQuery){
						charactersWithAtr += " OR ";
					}
					isEmpryQuery = false;
					charactersWithAtr +=  joinedAtr + "_" + currentAtr + "_id =" + currentAtrVal;
				//	charactersWithAtr = 	algorithmUtils.allCharactersWithTheSameAttributeQuery(joinedAtr + "_character_id" , 
				//							joinedAtr, joinedAtr + "_" + currentAtr + "_id =" +currentAtrVal,
				//							joinedAtr +  "_character_id=" + end_id ,null, true);
				}
				if (!isEmpryQuery){
					charactersWithAtr += ")";
					charsWithAtrRS= algorithmUtils.queryToEnd(charAtrStmt,charactersWithAtr,conn);
					if (charsWithAtrRS != null && charsWithAtrRS.next()){
						currentAtrVal =charsWithAtrRS.getInt(1);
						break;
					}
					isEmpryQuery=true;
					
				}


				atr=atr+1;
				//algorithmUtils.closeQueryResurces(atrValRS, atrStmt);
				if (atrValRS != null) atrValRS.close();
				if (atrStmt != null) atrStmt.close();
				atrValRS = null;
				atrStmt = null;
			} //end of while loop
			
			else if (	//tablesArr[atr].equals(Tables.sibling.toString()) || 
						//tablesArr[atr].equals(Tables.marriage.toString()) ||
						tablesArr[atr].equals(Tables.romantic_involvement.toString()) ||
						tablesArr[atr].equals(Tables.parent.toString())){
				
				String first = "_character_id1";
				String second = "_character_id2";
				String parent = "_parent_character_id";
				String child = "_child_character_id";
				
				
				for (int i=1; i<3;i++){
					//System.out.println(atr);
					if (tablesArr[atr].equals(Tables.parent.toString())) {
						charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery(currentAtr+ child, currentAtr, currentAtr + parent + "=" +start_id, currentAtr +child + "=" + end_id,null, true);
					}
					else {
						charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery(currentAtr+ first, currentAtr, currentAtr + second  +"=" +start_id, currentAtr+ first + "=" +end_id,null, true);
					}
		
					foundMatch =algorithmUtils.queryToEnd(charactersWithAtr,conn);
					if (i==2){
						currentAtr="child";
					}

					first = "_character_id2";
					second = "_character_id1";
					parent = "_child_character_id";
					child = "_parent_character_id";
						
					//found a connection
					if (foundMatch){
						break;
						}//out of for loop
					} //end of internal for
			}	
			
			else if (tablesArr[atr].equals(Tables.place_of_birth.toString())){
				int placeOfBirth; 
				selectAtrValues = 	algorithmUtils.simpleQuery("character_" + currentAtr+ "_id" ,"characters","character_id =" + start_id );
				placeOfBirth = algorithmUtils.simpleQueryIntResult(selectAtrValues,conn);
				if (unspecifiedIdOfTables.get(currentAtr)== placeOfBirth){
					continue;
				}
				charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + currentAtr+ "_id =" + placeOfBirth ,  "character_id = " + end_id,null, true);
				foundMatch = algorithmUtils.queryToEnd(charactersWithAtr,conn);	
				
			}
			if (foundMatch){
				break;
			}

		}// end of external loop
		if (foundMatch){
			end_element = new charElement(end_id, start_element);
			end_element.connectedAttribute = tablesMap.get(currentAtr);
			end_element.attributeValue = currentAtrVal;
			result[0] = end_element;
			return true;
		}
		return foundMatch;
	}
	
	
		
		
	
	/*
	 * Searching for the connection in the history table. 
	 * If exists - prints the connection to console.
	 */
	private boolean lookForConnectionInHistory(String start_name, String end_name, int start_id, int end_id) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		boolean result = false, opposite = false;
		int count;
		
		// checks if the connection between these 2 characters already in history table
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			try {
				algorithmUtils.getNameAndPrintConnections(getConnectionOfCharacters, conn);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (stmt!=null) stmt.close();
			if (rs!=null) rs.close();
			
			return true;
		}
		
		
		else { 
			return false;
		}
		
	}
	
	/*
	 * this function add to history table the connection that found and all 
	 * the sub connections that derived from it
	 */

	/*
	 * clears all hashmaps
	 */
	
	private void clearAll(){
		currentPhase.clear();
		previousPhase.clear();
		foundCharactersIDs.clear();
	}
	

	
	
	
	/*
	 * main function for looking a connection between two characters
	 */
	
	public boolean lookForConnection(int start_id, int end_id) throws SQLException {

		if (start_id == end_id){
			System.out.println("match of length 0");
			return true;
		}

		boolean alreadyExists = false;
		this.end_id = end_id;
		String start_name=algorithmUtils.getNameFromId(start_id,conn);
		String end_name=algorithmUtils.getNameFromId(end_id,conn);
		
		// checks if the connection between these 2 characters already in history table
		//alreadyExists = lookForConnectionInHistory(start_name, end_name, start_id, end_id);
		
		//found a connection
		//if (alreadyExists){
		//	return true;
		//}

		//couldn't find the connection in the history table, executing a search
		
		boolean matchFound = false;
		charElement[] theConnection = new charElement[1];
		charElement startElement = new charElement(start_id, null);
		previousPhase.add(startElement);
		
		maxConnection=6;
		for (int level = 1; level<maxConnection+1; level++) {
			globalNumOfConnections = level;
			matchFound = findConnection(theConnection);
			
			if (matchFound) {
				System.out.println("Match found between "+ start_name +" and "+ end_name);
				String connectionString = getNameAndPrintConnections(theConnection,conn);
				algorithmUtils.insertIntoHistory(connectionString, start_id, end_id,conn);

				return true;
			}
			else {
				System.out.println("cannot find a connection in " + level + " num of connection\n");
			}
		}
		
	//	System.out.println(previousPhase.size());
		System.out.println(currentPhase.size());
		clearAll();
		System.out.println("couldn't find a connection");
		return false;

	}
	
	
	public static String getNameAndPrintConnections(charElement[] connection, JDCConnection conn) throws SQLException{
			
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

				if (	// attributeString.equals(Tables.sibling.toString()) || 
						//attributeString.equals(Tables.marriage.toString()) ||
						attributeString.equals(Tables.romantic_involvement.toString()) ) {
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

	
	
	
	
	public static void main(String[] args) throws SQLException, IOException{
		noEndAlg a = new noEndAlg();
		algorithmUtils.buildTablesArray2(a);
		//System.out.println(a.indexOfJumps);
	/*	for (int i=0;i<table.length;i++){
			System.out.println(i+ ": " + table[i]);
		}*/
		
	//a.fillTables();
	long start = System.currentTimeMillis();
	a.lookForConnection (1,290);
	long finish = System.currentTimeMillis();
	long total = start-finish;
	String time = String.format("%d min, %d sec",      TimeUnit.MILLISECONDS.toMinutes(total),     TimeUnit.MILLISECONDS.toSeconds(total) -      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)) );
	System.out.println("operation took total time of " + total +"\n" + time);
	System.out.println(a.skips);
	System.out.println(foundCharactersIDs.size());
	
 
		//a.topSerches();
	

		
	}
	
	
	
	
	
	
}

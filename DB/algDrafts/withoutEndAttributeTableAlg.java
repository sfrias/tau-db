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



public class withoutEndAttributeTableAlg{
	
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
	
	public withoutEndAttributeTableAlg(){
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
		if (globalNumOfConnections == maxConnection) {
			while (iterator.hasNext() ){
				currentElement = iterator.next();
	
				resultFlag = DirectConnectionToEnd(currentElement, result);
				
				if (resultFlag){
					return resultFlag;
				}
			}
			return resultFlag;
		}
		
		int i=1;
		iterator = previousPhase.iterator();
		while (iterator.hasNext()){
			System.out.println(i);
			if (i==431){
				i++;
				continue;
			}
			currentElement = iterator.next();
			resultFlag = directConnectionToAny(currentElement, result);
			if (resultFlag){
					break;
				}
			i++;
		}
		
		previousPhase = currentPhase;
		currentPhase = new ArrayList<charElement>();		
		return resultFlag;
	}
	
	

private boolean helperForDirectConnectionToAny2	(ResultSet charsWithAtrRS,charElement start_element, charElement[] result, short atrint, int attributeVal){
		
		int currentid=0, atrID;
		charElement connection;
		short currentAtr;
		try {
			while (charsWithAtrRS!= null && charsWithAtrRS.next()) {
				currentid = charsWithAtrRS.getInt(1);
				if (atrint == -1){
					currentAtr = (short)charsWithAtrRS.getInt(2);
					atrID = charsWithAtrRS.getInt(3);
				}
				else {
					currentAtr= atrint;
					atrID= attributeVal;
				}
				

				if (foundCharactersIDs.contains(currentid)){ //already found a connection in this phase.
					continue;	
				}
//				System.out.println("adding to " + currentAtr +  " id number " + currentid + "in phase " + globalNumOfConnections);
				connection = new charElement(currentid, start_element);
				connection.connectedAttribute = currentAtr;;
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
		String 	 selectAtrValues, charactersWithAtr;
		short currentAtr=-1;
		int unspecifiedId=0;
		int unspecifiedIdOfCharacter = unspecifiedIdOfTables.get(Tables.characters.toString()); 
	//	System.out.println("Unsepcified id is " +unspecifiedIdOfCharacter);
		int attributes = tablesArr.length;
		int start_id = start_element.characterId;
		int currentAtrVal = -2;
		Statement atrStmt=null, charWithAtrStmt=null;
		boolean isEmpryQuery = true, foundmatch = false;
		//long total= System.currentTimeMillis();
		//String time = String.format("%d min, %d sec",      TimeUnit.MILLISECONDS.toMinutes(total),     TimeUnit.MILLISECONDS.toSeconds(total) -      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)) );
		//System.out.println("checking for " + start_id + " in time " + time);
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			currentAtr =tablesMap.get(tablesArr[atr]);
			atrStmt=conn.createStatement();
			charWithAtrStmt=conn.createStatement();
			if (atr < indexOfJumps) {
				//joinedAtr = tablesArr[atr+1];
				
				atrValRS = atrStmt.executeQuery(algorithmUtils.joinedAttributeValuesQueryString(start_id));
				charactersWithAtr = "SELECT * " +
				" FROM characters_and_attributes" + 
				" WHERE characters_and_attributes_character_id !=" + unspecifiedIdOfCharacter + " AND " +
				"characters_and_attributes_character_id !=" + start_id + " AND (";
				while (atrValRS != null && atrValRS.next()){ //all attributes
					currentAtr =(short) atrValRS.getInt(1);
					currentAtrVal = atrValRS.getInt(2);
					unspecifiedId = unspecifiedIdOfTables.get(reverseTablesMap.get(currentAtr));
					if (currentAtrVal== unspecifiedId ) { //not relevant 
						continue;
						}
					if (!isEmpryQuery){
						charactersWithAtr += " OR ";
					}
					isEmpryQuery = false;
					charactersWithAtr +=  "(characters_and_attributes_attribute_name = " + currentAtr + " AND " +
					"characters_and_attributes_attribute_id =" + currentAtrVal + ")";
				}
				charactersWithAtr +=")";
				
				if (atrValRS != null) atrValRS.close();
				if (atrStmt != null) atrStmt.close();
				atrStmt = null;
				atrValRS = null;
				
				if (!isEmpryQuery){ //not every time the query needs to be checked
					charToAny = algorithmUtils.queryToAny(charWithAtrStmt,charactersWithAtr, conn); 
					foundmatch = helperForDirectConnectionToAny2(charToAny, start_element, result, (short)-1,-1);
				}
				//algorithmUtils.closeQueryResurces(atrValRS, atrStmt);
	
				
				atr=indexOfJumps-1;
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
					charactersWithAtr = algorithmUtils.relationsQueryToAny(parent, child, tablesArr[atr], start_id, unspecifiedIdOfCharacter);
				}
				else {
					charactersWithAtr = algorithmUtils.relationsQueryToAny(first, second, tablesArr[atr], start_id, unspecifiedIdOfCharacter);
				}

				charToAny = charWithAtrStmt.executeQuery(charactersWithAtr);
				if (charToAny.next()){
					foundmatch = helperForDirectConnectionToAnyInRealtions(charToAny, start_element, tablesArr[atr], result);
				}
				currentAtrVal=-1;
				//algorithmUtils.closeQueryResurces(charToAny, charWithAtrStmt);
			}
			
			else if (tablesArr[atr].equals(Tables.place_of_birth.toString())){
				int placeOfBirth; 
				selectAtrValues = 	algorithmUtils.simpleQuery("character_" + tablesArr[atr]+ "_id" ,"characters","character_id =" + start_id);
				atrValRS = atrStmt.executeQuery(selectAtrValues);
				atrValRS.first();
				placeOfBirth = atrValRS.getInt(1);
				unspecifiedId = tablesMap.get(tablesArr[atr]);
				charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + tablesArr[atr]+ "_id =" + placeOfBirth ,  "character_id != " + unspecifiedIdOfCharacter,"character_" + tablesArr[atr]+ "_id !=" +unspecifiedIdOfTables.get(tablesArr[atr]), false);
				if (atrValRS!=null) atrValRS.close();
				if (atrStmt!= null) atrStmt.close();
				charToAny = charWithAtrStmt.executeQuery(charactersWithAtr); 
				foundmatch = helperForDirectConnectionToAny2(charToAny, start_element, result,currentAtr, placeOfBirth);
				//algorithmUtils.closeQueryResurces(charToAny, charWithAtrStmt);
				currentAtrVal=placeOfBirth;
			}
			if (charToAny != null) charToAny.close();
			if (charWithAtrStmt != null) charWithAtrStmt.close();
			charToAny=null;
			charWithAtrStmt=null;
			if (foundmatch){
				break;
			}
		}

		return foundmatch;
	}
	
	

	private boolean DirectConnectionToEnd(charElement start_element, charElement[] result) throws SQLException{
		
		ResultSet atrValRS=null, charsWithAtrRS=null;
		String selectAtrValues, charactersWithAtr;
		short currentAtr=-1;
		int unspecifiedId=0; 
		int attributes = tablesArr.length;
		int start_id = start_element.characterId;
		boolean foundMatch = false, isEmpryQuery = true;
		int currentAtrVal = -2;
		charElement end_element;
		Statement atrStmt = null, charAtrStmt = null;
		String[] attribute = new String[1];
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			currentAtr =tablesMap.get(tablesArr[atr]);
			charAtrStmt = conn.createStatement();
			if (atr < indexOfJumps) {
				atrStmt = conn.createStatement();
				//joinedAtr = tablesArr[atr+1];
				atrValRS = atrStmt.executeQuery(algorithmUtils.joinedAttributeValuesQueryString(start_id)); 
				//atrValRS = algorithmUtils.joinedAttributeValuesQuery(atrStmt,start_id,conn);
				
				charactersWithAtr = "SELECT characters_and_attributes_attribute_name, characters_and_attributes_attribute_id " +
				" FROM characters_and_attributes" + 
				" WHERE characters_and_attributes_character_id=" + end_id + " AND (";
				while (atrValRS != null && atrValRS.next()){ //all attributes
					currentAtr = (short) atrValRS.getInt(1);
					currentAtrVal = atrValRS.getInt(2);
					unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
					if (currentAtrVal== unspecifiedId ) { //not relevant 
						continue;
						}
					if (!isEmpryQuery){
						charactersWithAtr += " OR ";
					}
					isEmpryQuery = false;
					charactersWithAtr +=  "(characters_and_attributes_attribute_name = " + currentAtr + " AND " +
					"characters_and_attributes_attribute_id =" + currentAtrVal + ")";
				}
				if (atrValRS!=null) atrValRS.close();
				if (atrStmt != null) atrStmt.close();
				
				if (!isEmpryQuery){
					charactersWithAtr += ")";
					
					charsWithAtrRS= algorithmUtils.queryToEnd(charAtrStmt,charactersWithAtr,conn);
					if (charsWithAtrRS != null && charsWithAtrRS.next()){
						currentAtr = (short)charsWithAtrRS.getInt(1);
						currentAtrVal =charsWithAtrRS.getInt(2);
						foundMatch=true;
					}
					
				}

				atr=indexOfJumps-1;
				//algorithmUtils.closeQueryResurces(atrValRS, atrStmt);
			} //end of while loop
			
			else if (	//tablesArr[atr].equals(Tables.sibling.toString()) || 
					//tablesArr[atr].equals(Tables.marriage.toString()) ||
					tablesArr[atr].equals(Tables.romantic_involvement.toString()) ||
					tablesArr[atr].equals(Tables.parent.toString())){
			
			String first = "character_id1";
			String second = "character_id2";
			String parent = "parent_character_id";
			String child = "child_character_id";
			
			

			if (tablesArr[atr].equals(Tables.parent.toString())) {
				charactersWithAtr = algorithmUtils.relationsQueryToEnd(parent, child, tablesArr[atr], start_id, end_id);
			}
			else {
				charactersWithAtr = algorithmUtils.relationsQueryToEnd(first, second, tablesArr[atr], start_id, end_id);
			}
			charsWithAtrRS = charAtrStmt.executeQuery(charactersWithAtr);
			if (charsWithAtrRS != null && charsWithAtrRS.next()){
				if (charsWithAtrRS.getInt(1)==end_id){
					attribute[0] = "child";
				}
				else{
					attribute[0] = "parent";
				}
				foundMatch=true;
			}
			
			currentAtr=tablesMap.get(attribute[0]);
			currentAtrVal = -1;
		}	
			
			else if (tablesArr[atr].equals(Tables.place_of_birth.toString())){
				int placeOfBirth; 
				selectAtrValues = 	algorithmUtils.simpleQuery("character_" + tablesArr[atr]+ "_id" ,"characters","character_id =" + start_id );
				atrStmt= conn.createStatement();
				atrValRS = atrStmt.executeQuery(selectAtrValues);
				atrValRS.first();
				placeOfBirth = atrValRS.getInt(1);
				if (unspecifiedIdOfTables.get(currentAtr)== placeOfBirth){
					continue;
				}
				
				if (atrValRS != null) atrValRS.close();
				if (atrStmt != null) atrStmt.close();
				atrValRS=null;
				atrStmt=null;
				
				
				charactersWithAtr = algorithmUtils.allCharactersWithTheSameAttributeQuery("character_id", "characters", "character_" + tablesArr[atr]+ "_id =" + placeOfBirth ,  "character_id = " + end_id,null, true);
				charsWithAtrRS = charAtrStmt.executeQuery(charactersWithAtr);
				if (charsWithAtrRS != null && charsWithAtrRS.next()){
					foundMatch=true;
				}
				currentAtrVal = placeOfBirth;
				
			}
			
			if (atrValRS != null) atrValRS.close();
			if (atrStmt != null) atrStmt.close();
			if (charsWithAtrRS != null) charsWithAtrRS.close();
			if (charAtrStmt != null) charAtrStmt.close();
			atrValRS = null;
			atrStmt = null;
			charAtrStmt=null;
			charsWithAtrRS= null;
			
			if (foundMatch){
				break;
			}

		}// end of external loop
		if (foundMatch){
			end_element = new charElement(end_id, start_element);
			end_element.connectedAttribute =currentAtr;
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
		withoutEndAttributeTableAlg a = new withoutEndAttributeTableAlg();
		algorithmUtils.buildTablesArray2(a);
	/*	System.out.println(a.indexOfJumps);
		for (int i=0;i<a.tablesArr.length;i++){
			System.out.println(i+ ": " + a.tablesArr[i]);
		}*/
		
	//a.fillTables();
	long start = System.currentTimeMillis();
	a.lookForConnection (1,6);
	long finish = System.currentTimeMillis();
	long total = start-finish;
	String time = String.format("%d min, %d sec",      TimeUnit.MILLISECONDS.toMinutes(total),     TimeUnit.MILLISECONDS.toSeconds(total) -      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)) );
	System.out.println("operation took total time of " + total +"\n" + time);
	System.out.println(a.skips);
	System.out.println(foundCharactersIDs.size());
	
 
		//a.topSerches();
	

		
	}
	
	
	
	
	
	
}

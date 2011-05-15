package Utils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.TreeMap;
import Connection.JDCConnection;
import Enums.Tables;
import db.DatabaseManager;



public class algorithm2 {
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	private DatabaseManager dbManager = DatabaseManager.getInstance();
	private JDCConnection conn;

	Tables[] tbs;
	String[] tablesArr;
	int indexOfJumps;
	int globalNumOfConnections;
	int numOfCharacters;
	HashSet<Integer> foundCharactersIDs = new HashSet<Integer>();
	TreeMap<Integer, ConnectionElement> recursivePhase = new TreeMap<Integer,ConnectionElement>();
	TreeMap<Integer, ConnectionElement> currentPhase = new TreeMap<Integer,ConnectionElement>();
	TreeMap<Integer, ConnectionElement> previousPhase = new TreeMap<Integer,ConnectionElement>();
	TreeMap<String, Integer> unspecifiedIdOfTables = new TreeMap<String,Integer>();
	
	public algorithm2(){
		conn = dbManager.getConnection() ;
		tbs = Tables.values();
	}
	
	public JDCConnection getConnention(){
		return conn;
	}
	

	/*
	 * This function is used in order to build an organized array of all tables from the Tables' enum.
	 * It is called from the connectionFinder. 
	 * This is the basis array for the entire algorithm.
	 * The function organize the tables such that the joined tables are first (in pairs - attribute + joined table, i.e power+ characters_and_power)
	 * and all the rest after (i.e place_of_birth, marriage).
	 * The tables 'characters' is not included in this array.
	 */
	private void buildTablesArray(){
		TreeMap<String, String> joinedAttributesMap = new TreeMap<String,String>();
		int numOfTables = tbs.length;
		int unspec=0;
		String atrTable;
		String currentTable;
		String putCouples;
		String[] attributes = new String[numOfTables-1]; 
		String[] result = new String[numOfTables-1];
		
		int indexOfAttr=0, indexOfResult=0;
		for (int i=0; i< numOfTables; i++){
			currentTable = tbs[i].toString();
			if (currentTable.equals(Tables.characters.toString())){
				unspec = algorithmUtils.getUnspecifiedId(Tables.characters.toString(), conn);
				unspecifiedIdOfTables.put(Tables.characters.toString(), unspec);
			}
//			else if (currentTable.equals(Tables.gender.toString())){
//				continue;
//			}
			else if (!currentTable.contains("and")){
				attributes[indexOfAttr]=currentTable;
				indexOfAttr++;
			}
			else {
				atrTable = currentTable.substring(15);
				joinedAttributesMap.put(atrTable, currentTable);
			}
		}
//		attributes[indexOfAttr]=Tables.gender.toString();
//		indexOfAttr++;

		for (int i=0; i<indexOfAttr;i++){ 		//first loop- looking for joinedTables
			putCouples = joinedAttributesMap.get(attributes[i]);
	
			if (	//!attributes[i].equals(Tables.marriage.toString()) &&
					!attributes[i].equals(Tables.parent.toString()) &&
					!attributes[i].equals(Tables.romantic_involvement.toString()) // &&
					//!attributes[i].equals(Tables.sibling.toString())
					){
				unspec = algorithmUtils.getUnspecifiedId(attributes[i], conn);
				unspecifiedIdOfTables.put(attributes[i], unspec);
			}

			if (putCouples != null){
				result[indexOfResult]=attributes[i];
				result[indexOfResult+1]=putCouples;
				indexOfResult = indexOfResult + 2;
				attributes[i]="ok";
			}
		}

		indexOfJumps=indexOfResult; //this index is used in connectionFinder
		
		//adding all other tables;
		for (int i=0; i<indexOfAttr;i++){
			if(!attributes[i].equals("ok")){
				result[indexOfResult]=attributes[i];
				indexOfResult++;
			}
		}
		joinedAttributesMap.clear();
		tablesArr = result;
	}
	
	

/*
 * this function runs over all characters with a direct connection to start_id.
 * If a match found to end id - returns true.
 * Otherwise- adds the character to the connections already found.	
 */
	
	
	private boolean helperForDirectConnection	(ResultSet charsWithAtrRS,int start_id,int end_id, ConnectionElement[] conResult, String currentAtr, int atrID){
		boolean resultFlag = false;
		int currentid=0;
		ConnectionElement connection, temp;

		
		try {
			while (charsWithAtrRS!= null && charsWithAtrRS.next()) {
				currentid = charsWithAtrRS.getInt(1);
				temp = new ConnectionElement(start_id, currentid,currentAtr, atrID);		
				if (previousPhase.get(start_id) != null ){
					connection = previousPhase.get(start_id);
					temp = new ConnectionElement(start_id, currentid, currentAtr, atrID);
					temp.prev=connection;
					connection.next=temp;
				}
				if (currentid == end_id) {
					resultFlag = true;
					conResult[0]=temp; //writing the connection
					break;
				}
				currentPhase.put(currentid, temp);	
			}
			
			
		}catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}

		return resultFlag;
		
	}
	
	
	/*
	 * this function is used to find connection of 2 steps or more.
	 * It runs over all connection already found in the last phase and tries to find a direct connection with each
	 * one of them to the end_id.
	 */
	
	private boolean helperForRecursiveConnection	(int start_id,int end_id, ConnectionElement[] theConnection) throws SQLException{

		boolean resultFlag = false;
		int currentid=0, prevId;
		
		if (!previousPhase.isEmpty()) {
			previousPhase.clear();
		}
		
		previousPhase.putAll(currentPhase);
		recursivePhase.putAll(currentPhase);
		currentPhase.clear();
		
		System.out.println(recursivePhase.size());
		while (!recursivePhase.isEmpty() && recursivePhase.firstKey() != null) {
			currentid = recursivePhase.firstKey();
			prevId = recursivePhase.get(currentid).start_id;
			System.out.println("trying to find a connection with " + currentid + " in " + globalNumOfConnections + " steps");
			if (DirectConnection(theConnection, currentid, end_id, prevId, true)){
					resultFlag= true;
					break;
				}
			recursivePhase.remove(currentid);
			}
		return resultFlag;
}
	
	
	
	int debugRunCount = 0;
	/*
	 * private function that finds connection between two characters in a specific number of connection.
	 */
	
	
	private String specificAttributeValuesQuery(String joinedAtr, String currentAtr){
		return 	
			"SELECT " + joinedAtr + "_" + currentAtr+ "_id"  + 
			" FROM " + joinedAtr + 
			" WHERE " +joinedAtr + "_character_id = ? ";
	}
	
	private String allCharactersWithTheSameAttributeQuery(String select, String from,String where1, String where2, boolean firstRun){
		String query;
		query = "SELECT "+select +" FROM " + from +" WHERE " + where1 + "= ? AND " ;
						
		if (!firstRun){
			query+= where2 +  "!= ?" + " AND " 
					+ where2+ " != ?";
		}
		else {
			query+= where2+" = ?";
		}
		return query;
	}
	
	
	private boolean DirectConnection(ConnectionElement[] theConnection, int start_id,int end_id, int prevId, boolean firstRun) throws SQLException{
		
		if(foundCharactersIDs.contains(start_id)){
			System.out.println("It Works!!");
			return false;
		}
		debugRunCount++;
		ResultSet atrValRS=null,charsWithAtrRS=null;
		boolean resultFlag=false;
		String 	currentAtr, joinedAtr,selectAtrValues, charactersWithAtr;
		int unspecifiedId=0, getAtrint=0;
		PreparedStatement atrStmt= null, charAtrStmt = null; 
		int attributes = tablesArr.length;
		
		
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			currentAtr =tablesArr[atr];
			if (atr < indexOfJumps) {
				joinedAtr = tablesArr[atr+1];
				selectAtrValues = 	specificAttributeValuesQuery(joinedAtr, currentAtr);
				atrStmt = conn.prepareStatement(selectAtrValues);
			
				try {
					//getting all the ids of the attributes that the character has
					atrStmt.setInt(1, start_id);
					atrValRS = atrStmt.executeQuery();
				
					unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
						
					while (atrValRS.next()){ //all attributes
						
						if (atrValRS.getInt(1)== unspecifiedId ) { //not relevant 
							continue;
						}
						
						//taking all characters with the same attribute as our character
						
						charactersWithAtr = allCharactersWithTheSameAttributeQuery(joinedAtr + "_character_id" , 
											joinedAtr, joinedAtr + "_" + currentAtr + "_id",
											joinedAtr +  "_character_id", firstRun);
						charAtrStmt =  conn.prepareStatement(charactersWithAtr);
						getAtrint = atrValRS.getInt(1);
						charAtrStmt.setInt(1, getAtrint);
						if (firstRun) {
							charAtrStmt.setInt(2, end_id);
						}
						else {
							charAtrStmt.setInt(2, prevId);
							charAtrStmt.setInt(3, start_id);
						}
						
						charsWithAtrRS = charAtrStmt.executeQuery();
					
						//looking for a connection via the specific attribute
						resultFlag = helperForDirectConnection(charsWithAtrRS, start_id, end_id, theConnection, currentAtr, getAtrint);
						
						//found a connection
						if (resultFlag){
							break; //break of the while
						}
					}
			
				//closing all open statements and result sets
				algorithmUtils.closeAllOpenResources(atrValRS, charsWithAtrRS, atrStmt, charAtrStmt);

				
				}catch (SQLException e) {
					System.out.println("error execute query-" + e.toString());
				}
				
				if (resultFlag)
					break; //break of the for
				
				atr=atr+1;
			} //end of first case
			
			else if (	//tablesArr[atr].equals(Tables.sibling.toString()) || 
						//tablesArr[atr].equals(Tables.marriage.toString()) ||
						tablesArr[atr].equals(Tables.romantic_involvement.toString()) ||
						tablesArr[atr].equals(Tables.parent.toString())){
				
				String first = "_character_id1";
				String second = "_character_id2";
				String parent = "_parent_character_id";
				String child = "_child_character_id";
				
				unspecifiedId = unspecifiedIdOfTables.get(Tables.characters.toString());
				
				for (int i=1; i<3;i++){
					//System.out.println(atr);
					if (tablesArr[atr].equals(Tables.parent.toString())) {
						charactersWithAtr = allCharactersWithTheSameAttributeQuery(currentAtr+ child, currentAtr, currentAtr + parent, currentAtr +child, firstRun);
					}
				
					else {
						charactersWithAtr = allCharactersWithTheSameAttributeQuery(currentAtr+ first, currentAtr, currentAtr + second, currentAtr+ first, firstRun);
					}
		
					charAtrStmt = conn.prepareStatement(charactersWithAtr);
					charAtrStmt.setInt(1, start_id);
					
					try{	
						//getting all the ids of the attributes that the character has
						if (!firstRun) {		
							charAtrStmt.setInt(2, unspecifiedId);
							charAtrStmt.setInt(3, prevId);
						}
						else {
							charAtrStmt.setInt(2, end_id);
						}
						charsWithAtrRS = charAtrStmt.executeQuery();
					
						//looking for a connection via the specific attribute
						if (tablesArr[atr].equals(Tables.parent.toString()) && i==2){
							currentAtr="child";
						}
						resultFlag = helperForDirectConnection(charsWithAtrRS, start_id, end_id, theConnection, currentAtr, -1);
						
					}catch (SQLException e) {
						System.out.println("error execute query-" + e.toString());
					}

					first = "_character_id2";
					second = "_character_id1";
					parent = "_child_character_id";
					child = "_parent_character_id";
						
					//found a connection
					if (resultFlag){
						break;
						}//out of for loop
					} //end of internal for

				
				//closing all open statements and result sets
				algorithmUtils.closeAllOpenResources(atrValRS, charsWithAtrRS, atrStmt, charAtrStmt);
				
				if (resultFlag){
					break; //end of external for
				}

			}	
			else if (tablesArr[atr].equals(Tables.place_of_birth.toString())){
				int placeOfBirth=0;
				
				//TODO change the query builder
				charactersWithAtr = "SELECT character_id" +
				" FROM characters" + 
				" WHERE character_" + currentAtr+ "_id = ? AND " + 
				"character_" + currentAtr+ "_id != ? AND ";
				
				if (!firstRun){
					 charactersWithAtr +=
					"character_id != ? AND "+ 
					"character_id != ?";
				}
				else {
					charactersWithAtr +="character_id = ?";
				}
					
				
				
				try{
					
					unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
					
					selectAtrValues = 	"SELECT character_" + currentAtr+ "_id"  + " FROM characters" +" WHERE character_id = ?";
					
					atrStmt = conn.prepareStatement(selectAtrValues);
					atrStmt.setInt(1, start_id);
					atrValRS=atrStmt.executeQuery();
					atrValRS.first();
					placeOfBirth = atrValRS.getInt(1);
				
				}catch (SQLException e) {
					System.out.println("error execute query-" + e.toString());
				}
				
				charAtrStmt = conn.prepareStatement(charactersWithAtr);
				charAtrStmt.setInt(1, placeOfBirth);
				charAtrStmt.setInt(2, unspecifiedId);
				if (!firstRun){
					charAtrStmt.setInt(3, prevId);
					charAtrStmt.setInt(4, start_id);
				}
				else{
					charAtrStmt.setInt(3, end_id);
				}
				charsWithAtrRS = charAtrStmt.executeQuery();
				
				resultFlag = helperForDirectConnection(charsWithAtrRS, start_id, end_id, theConnection, currentAtr, placeOfBirth);
				
				//closing all open statements and result sets
				algorithmUtils.closeAllOpenResources(atrValRS, charsWithAtrRS, atrStmt, charAtrStmt);
				
				if (resultFlag){
					break; //end of external for
				}
				
			}

		}// end of external loop
		
		System.out.println(Integer.toString(debugRunCount) + "_" +Long.toString(System.currentTimeMillis()));
		//cannot find connection in the specific number of connection required
		
		if (firstRun && !resultFlag){
			return DirectConnection(theConnection, start_id, end_id, prevId, !firstRun);
		}
		if(firstRun) {
			foundCharactersIDs.add(start_id);
		}
		return resultFlag;
	
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
	
	private void clearHashMaps(){
		currentPhase.clear();
		previousPhase.clear();
		recursivePhase.clear();
		foundCharactersIDs.clear();
	}
	


	private boolean findDirectConnection(ConnectionElement[] theConnection,int start_id,int end_id,int prevID, boolean firstRun) throws SQLException{
		return DirectConnection(theConnection, start_id, end_id, prevID, firstRun);
	}
	
	
	
	private boolean findRecursiveConnection(ConnectionElement[] theConnection,int start_id,int end_id,int prevID, boolean firstRun) throws SQLException{
		return helperForRecursiveConnection(start_id, end_id, theConnection);
		
	}
	
	private boolean findAnyConnection(ConnectionElement[] theConnection,int start_id,int end_id,int prevID,int numOfConnection, boolean firstRun) throws SQLException{
		if (numOfConnection>1){
			return findRecursiveConnection(theConnection, start_id, end_id, 0, true);
		}
		else {
			return findDirectConnection(theConnection, start_id, end_id, 0, true);
		}
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
		
		String start_name=algorithmUtils.getNameFromId(start_id,conn);
		String end_name=algorithmUtils.getNameFromId(end_id,conn);
		
		// checks if the connection between these 2 characters already in history table
		alreadyExists = lookForConnectionInHistory(start_name, end_name, start_id, end_id);
		
		//found a connection
		if (alreadyExists){
			return true;
		}

		//couldn't find the connection in the history table, executing a search
		
		boolean matchFound = false;
		ConnectionElement[] theConnection = new ConnectionElement[1];
		
		for (int num = 1; num<4; num++) {
			globalNumOfConnections = num;
			matchFound = findAnyConnection(theConnection, start_id, end_id, 0, num, true);
			if (matchFound) {
				System.out.println("Match found between "+ start_name +" and "+ end_name);
				String connectionString = algorithmUtils.getNameAndPrintConnections(theConnection[0],conn);
				algorithmUtils.insertIntoHistory(connectionString, start_id, end_id,conn);

				return true;
			}
			else {
				System.out.println("cannot find a connection in " + num + " num of connection\n");
			}
		}
		
		clearHashMaps();
		System.out.println("couldn't find a connection");
		return false;

	}
	
	
	
	
	public static void main(String[] args) throws SQLException, IOException{
		algorithm2 a = new algorithm2();
		a.buildTablesArray();
		//System.out.println(a.indexOfJumps);
	/*	for (int i=0;i<table.length;i++){
			System.out.println(i+ ": " + table[i]);
		}*/
		
	//a.fillTables();
	//	System.out.println("finished");

	a.lookForConnection (263,70);
		//a.topSerches();
	

		
	}
	
	
	
	
	
	
}

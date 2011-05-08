package Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

import Connection.JDCConnection;
import Enums.Tables;
import db.DatabaseManager;

public class algorithm {

	private DatabaseManager dbManager = DatabaseManager.getInstance();
	private JDCConnection conn;

	Tables[] tbs;
	int indexOfJumps;

	public algorithm(){
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
	private String[] buildTablesArray(){
		TreeMap<String, String> joinedAttributesMap = new TreeMap<String,String>();
		int numOfTables = tbs.length;
		String atrTable;
		String currentTable;
		String putCouples;
		String[] attributes = new String[numOfTables-1];
		String[] result = new String[numOfTables-1];
		
		int indexOfAttr=0, indexOfResult=0;
		for (int i=0; i< numOfTables; i++){
			currentTable = tbs[i].toString();
			if (currentTable.equals("characters")){
				continue;
			}
			else if (!currentTable.contains("and")){
				attributes[indexOfAttr]=tbs[i].toString();
				indexOfAttr++;
			}
			else {
				atrTable = currentTable.substring(15);
				joinedAttributesMap.put(atrTable, currentTable);
			}
		}
		

		for (int i=0; i<indexOfAttr;i++){ 		//first loop- looking for joinedTables
			putCouples = joinedAttributesMap.get(attributes[i]);
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
		return result;
	}
	
	
	
	/*
	 * A function which is used to close all the open result sets and statements.
	 * This is used a few times in connectionFinder.
	 */
	private void closeAllOpenResources(ResultSet rs1, ResultSet rs2, ResultSet rs3,PreparedStatement ps1, PreparedStatement ps2, Statement st ) {
		try {
		if (rs1!=null) rs1.close();
		if (rs2!=null) rs2.close();
		if (rs3!=null) rs3.close();
		if (ps1!=null) ps1.close();
		if (ps2!=null) ps2.close();
		if (st!=null) st.close();	
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * helper function which is called from connectionFinder which goes over all characters that has the same attribute (currentAtr) 
	 * as the current character checked (start_id) and compares them to the end_id.
	 * In case of a match - the process is stopped and returns true. Otherwise - returns false.
	 */
	private boolean helperForConnection(ResultSet charsWithAtrRS,int start_id,int end_id, int recPhase, 
										boolean firstRun, String[] fill, String currentAtr){
		boolean resultFlag = false;
		int currentid=0;
		try{
			while (charsWithAtrRS.next()){
				currentid=charsWithAtrRS.getInt(1);
				if (firstRun){ //running over all attributes in order to find a direct connection
					if (currentid == end_id) {
						resultFlag=true;
						fill[recPhase-1]=currentid + "," + currentAtr;
						break;
					}
				}
				else {
					// starting a recursive call with each character with the same attribute as the start_id
					fill[recPhase-1]= currentid + "," + currentAtr;
					if (connectionFinder(fill, currentid, end_id, recPhase+1, start_id, true)){
						resultFlag= true;
						break; //out of while loop
					}
				}
			}
		}catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}
		return resultFlag;
	}
	
	
	
	
	/*
	 * private function that finds recursively a connection between two characters if so.
	 */
	private boolean connectionFinder(String[] fill, int start_id,int end_id, int recPhase, int prevId,boolean firstRun) throws SQLException{
		
		
		ResultSet atrValRS=null,charsWithAtrRS=null,unspecifiedRS = null;

		boolean resultFlag=false;
		
		String 	currentAtr, joinedAtr;
		
		int unspecifiedId=0;
		
		//Preparing statement of a specific attribute
		String selectAtrValues;
		
		//Preparing statement of all characters with the same attribute above 		
		String charactersWithAtr;
	
		//too many phases are taken without finding any connection
		if (recPhase>6){
			return false;
		}
		
		PreparedStatement atrStmt= null; 
		PreparedStatement charAtrStmt = null; 
		Statement unspecifiedStmt = null;
		
		String[] tablesArr = buildTablesArray();
		int attributes = tablesArr.length;
				
		//running on all attributes
		for (int atr=0; atr<attributes; atr=atr+1){
			
			System.out.println("trying "+ tablesArr[atr] + "in phase " + recPhase);
			currentAtr =tablesArr[atr];
			if (atr < indexOfJumps) {
				joinedAtr = tablesArr[atr+1];
						
				selectAtrValues = 	"SELECT " + joinedAtr + "_" + currentAtr+ "_id"  + 
									" FROM " + joinedAtr +"," + currentAtr +
									" WHERE " +joinedAtr + "_character_id = ? AND " + 
									joinedAtr + "_" + currentAtr + "_id =" + currentAtr + "_id";

				
				atrStmt = conn.prepareStatement(selectAtrValues);
	
			
				try {
					//getting all the ids of the attributes that the character has
					atrStmt.setInt(1, start_id);
					atrValRS = atrStmt.executeQuery();
					
					unspecifiedStmt= conn.createStatement();
					unspecifiedRS = unspecifiedStmt.executeQuery("SELECT " + currentAtr + "_id FROM " + currentAtr + " WHERE " + currentAtr + "_name LIKE 'Unspecified'");
					unspecifiedRS.first();
					unspecifiedId = unspecifiedRS.getInt(1);
					
					
					while (atrValRS.next()){ //all attributes
						
						if (atrValRS.getInt(1)== unspecifiedId ) { //not relevant 
							continue;
						}
						
						//taking all characters with the same attribute as our character
										
						charactersWithAtr = 	"SELECT "+joinedAtr+"_character_id " +
												"FROM " + joinedAtr +  
												" WHERE " + joinedAtr + "_" + currentAtr + "_id = ? AND " +
												joinedAtr +  "_character_id != ? AND " +
												joinedAtr +  "_character_id != ?";
						
						charAtrStmt =  conn.prepareStatement(charactersWithAtr);
						charAtrStmt.setInt(1, atrValRS.getInt(1));
						charAtrStmt.setInt(2, prevId);
						charAtrStmt.setInt(3, start_id);
						charsWithAtrRS = charAtrStmt.executeQuery();
					
						//looking for a connection via the specific attribute
						resultFlag = helperForConnection(charsWithAtrRS, start_id, end_id, recPhase, firstRun, fill, currentAtr);
						
						//found a connection
						if (resultFlag){
							break;
						}
					}
			
				//closing all open statements and result sets
				closeAllOpenResources(atrValRS, charsWithAtrRS, unspecifiedRS, atrStmt, charAtrStmt, unspecifiedStmt);

				
				}catch (SQLException e) {
					System.out.println("error execute query-" + e.toString());
				}	
				atr=atr+1;
			} //end of first case
			
			else if (	tablesArr[atr].equals(Tables.sibling.toString()) || 
						tablesArr[atr].equals(Tables.marriage.toString()) ||
						tablesArr[atr].equals(Tables.romantic_involvement.toString()) ||
						tablesArr[atr].equals(Tables.parent.toString())){
				
				String first = "_character_id1";
				String second = "_character_id2";
				String parent = "_parent_character_id";
				String child = "_child_character_id";
				
				try{
					unspecifiedStmt= conn.createStatement();
					unspecifiedRS = unspecifiedStmt.executeQuery("SELECT character_id FROM characters where character_name LIKE 'Unspecified'");
					unspecifiedRS.first();
					unspecifiedId = unspecifiedRS.getInt(1);
					
				}catch (SQLException e) {
					System.out.println("error execute query-" + e.toString());
				}
				
				for (int i=1; i<3;i++){
					System.out.println(atr);
					if (tablesArr[atr].equals(Tables.parent.toString())) {
						charactersWithAtr = "SELECT " + currentAtr+ child + 
						" FROM " + currentAtr +
						" WHERE " +currentAtr + parent +" = ? AND " + 
						currentAtr +parent + " != ? AND " +
						currentAtr+ child + " != ?";
					}
					else {
						charactersWithAtr = "SELECT " + currentAtr+ first + 
						" FROM " + currentAtr+
						" WHERE " +currentAtr + second +" = ? AND " + 
						currentAtr +second + " != ? AND " + 
						currentAtr+ first + " != ?";
						;	
					}					
					charAtrStmt = conn.prepareStatement(charactersWithAtr);
					
					try{	
						//getting all the ids of the attributes that the character has
						charAtrStmt.setInt(1, start_id);
						charAtrStmt.setInt(2, prevId);
						charAtrStmt.setInt(3, unspecifiedId);
						charsWithAtrRS = charAtrStmt.executeQuery();
					
						//looking for a connection via the specific attribute
						if (tablesArr[atr].equals(Tables.parent.toString()) && i==2){
							currentAtr="child";
						}
						resultFlag = helperForConnection(charsWithAtrRS, start_id, end_id, recPhase, firstRun, fill, currentAtr);
						
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
				closeAllOpenResources(atrValRS, charsWithAtrRS, unspecifiedRS, atrStmt, charAtrStmt, unspecifiedStmt);
				
				if (resultFlag){
					break; //end of external for
				}

			}	
			else if (tablesArr[atr].equals(Tables.place_of_birth.toString())){
				int placeOfBirth=0;
				charactersWithAtr = "SELECT character_id" +
				" FROM characters" + 
				" WHERE character_" + currentAtr+ "_id = ? AND " + 
				"character_" + currentAtr+ "_id != ? AND " +
				"character_id != ? AND "+ 
				"character_id != ?";
				
				
				try{
					unspecifiedStmt= conn.createStatement();
					unspecifiedRS = unspecifiedStmt.executeQuery("SELECT " + currentAtr + "_id FROM " + currentAtr+" where " + currentAtr + "_name LIKE 'Unspecified'");
					unspecifiedRS.first();
					unspecifiedId = unspecifiedRS.getInt(1);
					
					selectAtrValues = 	"SELECT " + currentAtr+ "_id"  + 
					" FROM characters" +
					" WHERE character_id = ?";
					
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
				charAtrStmt.setInt(3, prevId);
				charAtrStmt.setInt(4, start_id);
				charsWithAtrRS = charAtrStmt.executeQuery();
				
				resultFlag = helperForConnection(charsWithAtrRS, start_id, end_id, recPhase, firstRun, fill, currentAtr);
				
				//closing all open statements and result sets
				closeAllOpenResources(atrValRS, charsWithAtrRS, unspecifiedRS, atrStmt, charAtrStmt, unspecifiedStmt);
				
				if (resultFlag){
					break; //end of external for
				}
				
			}

		}// end of external loop
		
		if (firstRun && !resultFlag){
			 return connectionFinder(fill,start_id,end_id,recPhase,prevId,!firstRun);
		}
		
		return resultFlag;
	}
	
	
	
	
	
	/* 
	 * merges sequential connections if identical.
	 * for example - A is connection to B by gender, and B is connected to C by gender --> A is connection to C by gender
	 */
	private void mergeConnection (String[] connArr){
		String[] valueArrfirst = new String[2];
		String[] valueArrSecond = new String[2];
		for (int i=0; i+1< connArr.length; i++){
			if (connArr[i] != null && connArr[i+1] != null) {
				valueArrfirst = connArr[i].split(",");
				valueArrSecond = connArr[i+1].split(",");
				if (valueArrfirst[1].equals(valueArrSecond[1])) {
					connArr[i]="merged";
				}
			}
			else {
				break;
			}
		}
	}
	
	
	
	
	/* 
	 * gets the character's name by his/her id 
	 */
	private String getNameFromId(int id){
		Statement stmt;
		String Name = null;
		try {
			stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT character_name FROM characters WHERE character_id='" +id +"'");
		rs.first();
		Name = rs.getString("character_name");
		rs.close(); 
		stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Name;
		
	}
	
	
	
	
	/* 
	 * used to print the connection chain, in case needed
	 */
	private void getNameAndPrintConnections(String[] connArr, String startName) throws SQLException{
		
		String currentName="";
		String[] valueArr = new String[2];

		for (int i=0; i< connArr.length; i++){
			
				if (connArr[i] != null ) {
					if (!connArr[i].equals("merged")){
						valueArr = connArr[i].split(",");
						currentName =getNameFromId(Integer.parseInt(valueArr[0]));
						System.out.println(startName + " has the same "+ valueArr[1] + " as" + currentName + "\n" );
						startName=currentName;
					}
				}
				else {
					break;
				}
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


		String[] connections = new String[6];
		
		String start_name=getNameFromId(start_id);
		String end_name=getNameFromId(end_id);

		//trying to find first connection
		
		if (connectionFinder(connections, start_id, end_id, 1, 0, true)){
				System.out.println("Match found between "+ start_name +" and "+ end_name);
				mergeConnection(connections);
				getNameAndPrintConnections(connections,start_name);
				return true;
		}
		else{
			System.out.println("couldn't find a connection");
			return false;
		}

	}
	
	
	
	
	public static void main(String[] args) throws SQLException{
		algorithm a = new algorithm();
		String[] table = a.buildTablesArray();
		System.out.println(a.indexOfJumps);
	/*	for (int i=0;i<table.length;i++){
			System.out.println(i+ ": " + table[i]);
		}*/

		
	}
	
	
	
}

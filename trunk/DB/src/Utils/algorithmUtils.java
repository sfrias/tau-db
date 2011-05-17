package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import Connection.JDCConnection;
import Enums.Tables;

public class algorithmUtils {
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	private static final String CREATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";
		
	private static void populateTable(String FileName, String insertStatement) throws IOException {

		File sqlFile = new File(CREATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		File dumpFile = new File("C:" + File.separatorChar +"fill" + File.separatorChar + FileName);
		FileInputStream fileReader = new FileInputStream(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));
		
		bufferedReader.readLine();
		String lineRead;
		String[] strarr;
		
		while ((lineRead = bufferedReader.readLine()) != null) {
			strarr = lineRead.split("\t", 2);
			bufferedWriter.append(insertStatement);
			bufferedWriter.append("'" + strarr[0] + "', '" + strarr[1] + "');\n");
			bufferedWriter.flush();
			}
		}
	

	
	public static void fillTables() throws IOException {
		
		populateTable( "gender.txt", "INSERT INTO gender (gender_name, gender_fb_id) values(");
		populateTable( "species.txt", "INSERT INTO species (species_name, species_fb_id) values(");
		populateTable( "creator.txt", "INSERT INTO creator (creator_name, creator_fb_id) values(");
		populateTable( "organization.txt", "INSERT INTO organization (organization_name, organization_fb_id) values(");
		populateTable( "school.txt", "INSERT INTO school (school_name, school_fb_id) values(");
		populateTable( "rank.txt", "INSERT INTO rank (rank_name, rank_fb_id) values(");
		populateTable( "ethnicity.txt", "INSERT INTO ethnicity (ethnicity_name, ethnicity_fb_id) values(");
		populateTable( "universe.txt", "INSERT INTO universe (universe_name, universe_fb_id) values(");
		populateTable( "occupation.txt", "INSERT INTO occupation (occupation_name, occupation_fb_id) values(");
		populateTable( "power.txt", "INSERT INTO power (power_name, power_fb_id) values(");
		populateTable( "disease.txt", "INSERT INTO disease (disease_name, disease_fb_id) values(");
		populateTable( "place_of_birth.txt", "INSERT INTO place_of_birth (place_of_birth_id, place_of_birth_name) values(");
		
		populateTable( "characters.txt", "INSERT INTO characters (character_name, character_fb_id) values(");
		
		populateTable("parent.txt", "INSERT IGNORE INTO parent (parent_child_character_id, parent_parent_character_id) values(");
		populateTable("marriage.txt", "INSERT IGNORE INTO marriage (marriage_character_id1, marriage_character_id2) values(");
		populateTable("romantic_involvement.txt", "INSERT IGNORE INTO romantic_involvement (romantic_involvement_character_id1, romantic_involvement_character_id2) values(");
		populateTable("sibling.txt", "INSERT IGNORE INTO sibling (sibling_character_id1, sibling_character_id2) values(");
		
		populateTable("characters_and_universe.txt", "INSERT IGNORE INTO characters_and_universe (characters_and_universe_character_id, characters_and_universe_universe_id) values(");	
		populateTable("characters_and_gender.txt", "INSERT IGNORE INTO characters_and_gender (characters_and_gender_character_id, characters_and_gender_gender_id) values(");	
		populateTable("characters_and_species.txt", "INSERT IGNORE INTO characters_and_species (characters_and_species_character_id, characters_and_species_species_id) values(");
		populateTable("characters_and_creator.txt", "INSERT IGNORE INTO characters_and_creator (characters_and_creator_character_id, characters_and_creator_creator_id) values(");	
		populateTable("characters_and_organization.txt", "INSERT IGNORE INTO characters_and_organization (characters_and_organization_character_id, characters_and_organization_organization_id) values(");	
		populateTable("characters_and_school.txt", "INSERT IGNORE INTO characters_and_school (characters_and_school_character_id, characters_and_school_school_id) values(");	
		populateTable("characters_and_rank.txt", "INSERT IGNORE INTO characters_and_rank (characters_and_rank_character_id, characters_and_rank_rank_id) values(");
		populateTable("characters_and_ethnicity.txt", "INSERT IGNORE INTO characters_and_ethnicity (characters_and_ethnicity_character_id, characters_and_ethnicity_ethnicity_id) values(");	
		populateTable("characters_and_occupation.txt", "INSERT IGNORE INTO characters_and_occupation (characters_and_occupation_character_id, characters_and_occupation_occupation_id) values(");	
		populateTable("characters_and_power.txt", "INSERT IGNORE INTO characters_and_power (characters_and_power_character_id, characters_and_power_power_id) values(");
		populateTable("characters_and_disease.txt", "INSERT IGNORE INTO characters_and_disease (characters_and_disease_character_id, characters_and_disease_disease_id) values(");
		
	}

	
	/*
	 * A function which is used to close all the open result sets and statements.
	 * This is used a few times in connectionFinder.
	 */
	public static void closeAllOpenResources(ResultSet rs1, ResultSet rs2, PreparedStatement ps1, PreparedStatement ps2 ) {
		try {
		if (rs1!=null) rs1.close();
		if (rs2!=null) rs2.close();
		if (ps1!=null) ps1.close();
		if (ps2!=null) ps2.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	

	/* 
	 * gets the character's name by his/her id 
	 */
	public static String getNameFromId(int id, JDCConnection conn){
		Statement stmt;
		String Name = null;
		try {
			stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT character_name FROM characters WHERE character_id=" +id +"");
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
	
	public static String getAttributeNameFromID(String table, int id, JDCConnection conn){
		Statement stmt;
		String Name = null;
		try {
			stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT " +table+"_name FROM " + table+ " WHERE " + table+"_id=" + id);
		rs.first();
		Name = rs.getString(1);
		rs.close(); 
		stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Name;
	}
	
	
	public static String getNameAndPrintConnections(ConnectionElement connection, JDCConnection conn) throws SQLException{
		
		String startName="", endName="";
		String toPrint;
		String toHisory="";
		String atrName = "none";
		ConnectionElement conFirst = ConnectionElement.first(connection);

		while (conFirst != null){
			atrName = "none";
			startName =getNameFromId(conFirst.start_id, conn);
			endName = getNameFromId(conFirst.end_id, conn);

			if (	// conFirst.attribute.equals(Tables.sibling.toString()) || 
					//conFirst.attribute.equals(Tables.marriage.toString()) ||
					conFirst.attribute.equals(Tables.romantic_involvement.toString()) ) {
					toPrint = startName + " has a " + conFirst.attribute + " relationship with " + endName;
				}
				else if ( conFirst.attribute.equals("child")) {
						toPrint = startName + " is " + endName +"'s child";
				}
				else if (conFirst.attribute.equals("parent")) {
						toPrint = startName + " is " + endName +"'s parent";
				}
				else {
					atrName = getAttributeNameFromID(conFirst.attribute, conFirst.attributeValueId, conn);
					toPrint = startName + " has the same "+ conFirst.attribute + " as " + endName + " - " + atrName;
				}
				System.out.println(toPrint);
				toHisory+= conFirst.start_id +","+conFirst.end_id +","+conFirst.attribute + "," + atrName;
				if (conFirst.next != null){
					toHisory+= "\t";
				}

				conFirst = conFirst.next;
			}
		return toHisory;
		
	}


	
	
	static void getNameAndPrintConnections(String connArr, JDCConnection conn) throws SQLException{
		
		String startName="", endName="";
		String[] valueArr = new String[4];
		String toPrint;
	
		
		String connections[] = connArr.split("\t");
		for (int i=0; i<connections.length; i++){
			if (connections[i] != ""){ 
				valueArr = connections[i].split(",");
				startName =getNameFromId(Integer.parseInt(valueArr[0]), conn);
				endName = getNameFromId(Integer.parseInt(valueArr[1]), conn);
				if (// valueArr[2].equals(Tables.sibling.toString()) || 
					// valueArr[2].equals(Tables.marriage.toString()) ||
					 valueArr[2].equals(Tables.romantic_involvement.toString()) ) {
						toPrint = startName + " has a " + valueArr[2] + " relationship with " + endName;
				}
				else if ( valueArr[2].equals("child")) {
						toPrint = startName + " is " + endName +"'s child";
				}
				else if (valueArr[2].equals("parent")) {
						toPrint = startName + " is " + endName +"'s parent";
				}
				else {
					int temp = Integer.parseInt(valueArr[3]);
					//System.out.println(temp);
					String atrName = getAttributeNameFromID(valueArr[2], temp, conn);
					toPrint = startName + " has the same "+ valueArr[2] + " as " + endName + " - " + atrName;
				}
				
				System.out.println(toPrint);
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
	public static void topSerches (JDCConnection conn) {
		 
		 Statement stmt;
		 ResultSet rs;
		 
		 try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM history ORDER BY count DESC LIMIT 5");
			while (rs.next()) {
				String startName = getNameFromId(rs.getInt(1), conn);
				String endName = getNameFromId(rs.getInt(2),conn);
				System.out.println("this is a connection between " + startName + " and " + endName);
				System.out.println("this connection was found in " + rs.getDate(3));
				getNameAndPrintConnections(rs.getString(4),conn);
				System.out.println();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
	 }
	
	
	public static int getUnspecifiedId(String table, JDCConnection conn) {
		 
		 Statement stmt;
		 ResultSet rs;
		 int unspecifiedID = 0;
		 String field = "";
		 if (table.equals(Tables.characters.toString())){
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
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unspecifiedID;
		 
		 
	 }
	
	
	public static void insertIntoHistory (String connections, int start_id, int end_id, JDCConnection conn) {
			
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
				//int previousCharacter = start_id;
				
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
						//previousCharacter = connectorCharacter;
						stmt.executeUpdate(toQuery);
					}
					 
				}
				
				
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	

/*	public static void closeQueryResurces(ResultSet rs, Statement st) throws SQLException{
		if (rs != null) rs.close();
		if (st != null) st.close();
		rs=null;
		st=null;
	
	}*/
	
	
	public static String specificAttributeValuesQuery(String joinedAtr, String currentAtr, int charid){
		return 	
			"SELECT " + joinedAtr + "_" + currentAtr+ "_id"  + 
			" FROM " + joinedAtr + 
			" WHERE " +joinedAtr + "_character_id = " + charid;
	}
	
	static String simpleQuery(String select, String from, String where){
		return 	
			"SELECT " + select +
			" FROM " + from + 
			" WHERE " +where;
	}
	
	
	public static int simpleQueryIntResult(String query, JDCConnection conn) throws SQLException{

		Statement st = null;
		ResultSet rs = null;
		int result = 0;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			rs.first();
			result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (rs!= null) rs.close();
		if (st!= null) st.close();
		rs = null;
		st = null;
		return result;
		
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
	
	
	public static String relationsQuery(String field1,String field2,String from,int start_id, int unspecified){

		String query = "SELECT " +from + "_" + field1+  ","  +from + "_" + field2 +
				" FROM " + from 
				+" WHERE (" + from + "_" + field1  + "=" + start_id + " AND " + from + "_" + field2 + "!=" + unspecified
				+ ") OR " + "(" + from + "_" + field2  + "=" + start_id + " AND " + from + "_" + field1 + "!=" + unspecified +")";
		
		return query;
	}
	
	public static boolean queryToEnd(String query,JDCConnection conn) throws SQLException {

		boolean found = false;
		Statement charAtrStmt = null;
		ResultSet charToEnd = null;
		try {
			charAtrStmt = conn.createStatement();
			charToEnd = charAtrStmt.executeQuery(query);
			found = charToEnd.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(charToEnd != null) charToEnd.close();
		if(charAtrStmt != null) charAtrStmt.close();
		charToEnd = null;
		charAtrStmt = null;
		
		return found;
		
	}
	
	public static ResultSet queryToAny(Statement charAtrStmt , String query, JDCConnection conn) throws SQLException {

		ResultSet charToAny = null;
		try {
			charAtrStmt = conn.createStatement();
			charToAny = charAtrStmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		return charToAny;
		
	}
	
	
	
	
	public static ResultSet getAllAtributesQuery( Statement atrStmt, String joinedAtr, String currentAtr, int id, JDCConnection conn) throws SQLException{
		
		String selectAtrValues = 	specificAttributeValuesQuery(joinedAtr, currentAtr, id);
		ResultSet atrValRS = null;

		try {
			atrStmt = conn.createStatement();
			atrValRS = atrStmt.executeQuery(selectAtrValues);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return atrValRS;


	}

	
	public static void buildTablesArray(algorithm4 alg){
		TreeMap<String, String> joinedAttributesMap = new TreeMap<String,String>();
		int numOfTables = alg.tbs.length;
		int unspec=0;
		String atrTable;
		String currentTable;
		String putCouples;
		String[] attributes = new String[numOfTables-1]; 
		String[] result = new String[numOfTables-1];
		
		int indexOfAttr=0, indexOfResult=0;
		for (int i=0; i< numOfTables; i++){
			currentTable = alg.tbs[i].toString();
			if (currentTable.equals(Tables.characters.toString())){
				unspec = algorithmUtils.getUnspecifiedId(currentTable, alg.conn);
				alg.unspecifiedIdOfTables.put(Tables.characters.toString(), unspec);
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
				unspec = algorithmUtils.getUnspecifiedId(attributes[i], alg.conn);
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
		
		for (short i=0; i<alg.tablesArr.length;i++){
			alg.tablesMap.put(alg.tablesArr[i],i);
			algorithm4.reverseTablesMap.put((short)i, alg.tablesArr[i]);
		}
		
		alg.tablesMap.put("child", (short)alg.tablesArr.length);
		algorithm4.reverseTablesMap.put((short)alg.tablesArr.length, "child");
	}
	public static void buildTablesArray2(algorithm4 alg){
		TreeMap<String, String> joinedAttributesMap = new TreeMap<String,String>();
		int numOfTables = alg.tbs.length;
		int unspec=0;
		String atrTable;
		String currentTable;
		String putCouples;
		String[] attributes = new String[numOfTables-1]; 
		String[] result = new String[numOfTables-1];
		
		int indexOfAttr=0, indexOfResult=0;
		for (int i=0; i< numOfTables; i++){
			currentTable = alg.tbs[i].toString();
			if (currentTable.equals(Tables.characters.toString())){
				unspec = algorithmUtils.getUnspecifiedId(currentTable, alg.conn);
				alg.unspecifiedIdOfTables.put(Tables.characters.toString(), unspec);
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
				unspec = algorithmUtils.getUnspecifiedId(attributes[i], alg.conn);
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
		
		for (short i=0; i<alg.tablesArr.length;i++){
			alg.tablesMap.put(alg.tablesArr[i],i);
			algorithm4.reverseTablesMap.put((short)i, alg.tablesArr[i]);
		}
		
		alg.tablesMap.put("child", (short)alg.tablesArr.length);
		algorithm4.reverseTablesMap.put((short)alg.tablesArr.length, "child");
	}


	

}

package Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import Connection.JDCConnection;
import Enums.Tables;
import db.AntUtils;
import db.DatabaseManager;
import db.AntUtils.Targets;

public class TableUtilities {

	private static final String CHARSET = "UTF-8";
	private static final String POPULATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";


	private static void downloadAndExtractDumps() throws IOException {
		File pathDir = new File(".");
		if (!pathDir.exists()) {
			pathDir.mkdir();
		}

		BufferedInputStream in = new BufferedInputStream(new java.net.URL("http://download.freebase.com/datadumps/latest/browse/fictional_universe.tar.bz2").openStream());
		File save = new File(pathDir.getAbsolutePath() + File.separatorChar + "fictional_universe.tar.bz2");

		FileOutputStream fos = new FileOutputStream(save);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int size = 0;
		System.out.println("Downloading Content...");
		while ((size = in.read(data, 0, 1024)) >= 0) {
			bout.write(data, 0, size);
		}
		bout.close();
		in.close();
		System.out.println("Finished Downloading");

		System.out.println("Starting to extract file...");
		// starting bz2 decompression
		File CompressedTar = new File(pathDir.getAbsolutePath()
				+ File.separatorChar + "fictional_universe.tar");
		FileInputStream fin = new FileInputStream(save);
		fin.skip(2);
		CBZip2InputStream bzIn = new CBZip2InputStream(fin);
		FileOutputStream fileTarStream = new FileOutputStream(CompressedTar);
		data = new byte[1024];
		size = 0;
		while ((size = bzIn.read(data, 0, 1024)) >= 0) {
			fileTarStream.write(data, 0, size);
		}
		fileTarStream.close();
		bzIn.close();
		fin.close();
		// finished bz2 decompression

		// starting tar decompression
		FileInputStream CompressedTarIn = new FileInputStream(CompressedTar);
		TarInputStream tarIn = new TarInputStream(CompressedTarIn);
		File extractDir = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp");
		if (!extractDir.exists()) {
			extractDir.mkdir();
		}
		TarEntry tarEntry = tarIn.getNextEntry();
		while (tarEntry != null) {File destPath = new File(extractDir.getAbsolutePath() + File.separatorChar + tarEntry.getName());
		System.out.println("Processing " + File.separatorChar + destPath.getAbsoluteFile());
		if (!tarEntry.isDirectory()) {
			FileOutputStream fout = new FileOutputStream(destPath);
			tarIn.copyEntryContents(fout);
			fout.close();
		} else {
			destPath.mkdir();
		}
		tarEntry = tarIn.getNextEntry();
		}
		tarIn.close();
		CompressedTarIn.close();
		// finished tar decompression
		System.out.println("finished extracting file!");
	}

	private static void populateSimpleTableUsingBatchFile(String table, String insertStatement, String dumpFileName, int splitNum, int attrNum) throws IOException {

		File pathDir = new File(".");

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		FileOutputStream fileWriter = new FileOutputStream(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileWriter));

		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + dumpFileName);
		FileInputStream fileReader = new FileInputStream(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));

		bufferedReader.readLine();
		String lineRead;
		String[] strarr;
		String tempString;

		if (table.equals(Tables.location.toString())){
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", splitNum);
				strarr[0] = strarr[0].replace("\'", "\\'");
				String [] locationarr = strarr[2].split(",");
				int len = locationarr.length;

				for (int i = 0; i < len; i++) {
					tempString = locationarr[i].replace("\'", "\\'");
					locationarr[i] = tempString;
					if (!locationarr[i].equals("")) {

						bufferedWriter.append(insertStatement);
						bufferedWriter.append("'" + locationarr[i] + "', " + "(SELECT universe_id FROM universe Where universe_fb_id LIKE '" + strarr[1] + "' AND universe_name like'" + strarr[0]+ "'));\n");
						bufferedWriter.flush();
					}

				}
			}
			bufferedWriter.append(insertStatement);
			bufferedWriter.append("'Unspecified', (SELECT universe_id FROM universe Where universe_fb_id LIKE 'Unspecified'));\n");
		}

		else if (table.equals(Tables.place_of_birth.toString())){	
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", splitNum);
				tempString = strarr[3].replace("\'", "\\'");
				strarr[3] = tempString;
				if (! strarr[3].equals("")) {
					bufferedWriter.append(insertStatement);
					bufferedWriter.append("'" +  strarr[3] + "');\n");
					bufferedWriter.flush();
				}

			}
			bufferedWriter.append(insertStatement);
			bufferedWriter.append("'Unspecified');\n");
		}

		else if (table.equals(Tables.characters.toString())){
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", 27);
				bufferedWriter.append(insertStatement);

				for (int i = 0; i <4; i++) {
					tempString = strarr[i].replace("\'", "\\'");
					strarr[i] = tempString;
					if (i == 2) {
						continue;
					}
					else if (i==3) {
						if (strarr[3].equals("")){
							strarr[3]="Unspecified";
						}

						bufferedWriter.append("(SELECT place_of_birth_id FROM place_of_birth Where place_of_birth_name LIKE '" + strarr[3] + "'));\n");
						bufferedWriter.flush();
					}
					else{
						bufferedWriter.append("'" + strarr[i] + "', ");
					}
				}
			}
			
			bufferedWriter.append(insertStatement);
			bufferedWriter.append("'Unspecified', 'Unspecified',(SELECT place_of_birth_id FROM place_of_birth Where place_of_birth_name LIKE 'Unspecified'));\n");
			bufferedWriter.flush();
		}

		else {
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", splitNum);
				bufferedWriter.append(insertStatement);
				for (int i = 0; i < attrNum - 1; i++) {
					tempString = strarr[i].replace("\'", "\\'");
					strarr[i] = tempString;
					bufferedWriter.append("'" + strarr[i] + "', ");
				}
				bufferedWriter.append("'" + strarr[attrNum - 1] + "');\n");
				bufferedWriter.flush();
			}
			bufferedWriter.append(insertStatement);
			bufferedWriter.append("'Unspecified', 'Unspecified');\n");
			bufferedWriter.flush();
		}

		bufferedWriter.close();
		bufferedReader.close();
		fileReader.close();
		fileWriter.close();
	}


	private static void populateJoinedTableUsingBatchFile(String insertStatement,String mainTable, String subtable, int splitNum, int interestingFieldNum,String file) throws IOException, SQLException{

		File pathDir = new File(".");

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + file);
		FileInputStream fis = new FileInputStream(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

		bufferedReader.readLine();
		String lineRead;

		DatabaseManager dbManager = DatabaseManager.getInstance();
		
		TreeMap<String, Integer> interstingMainValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM " + mainTable, 1, 2);
		
		TreeMap<String, Integer> interestingValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM " + subtable, 1, 3);
		
		int unspecifiedId = interestingValuesMap.get("Unspecified");

		int overallCounter = 0;
		int failuresCounter = 0;
		
		Statement addNullId = null, getNewId = null;
		String stringForStatment = null, getId = null, fieldName;
		ResultSet rs = null;
		
		JDCConnection currentConn = dbManager.getConnection();

		while ((lineRead = bufferedReader.readLine()) != null) {
			overallCounter++;
			String [] strarr = lineRead.split("\t", splitNum);
			String tempString = strarr[interestingFieldNum-1].replace(", ", "~");
			strarr[interestingFieldNum-1] = tempString;
			String [] valueArr = strarr[interestingFieldNum-1].split(",");

//			strarr[0] = new String(strarr[0].getBytes(), CHARSET);

			if (interstingMainValuesMap.get(strarr[1]) == null){
				System.out.println(mainTable + strarr[1] + " id equals null MM");
				failuresCounter++;
				continue;
			}

			boolean alreadySet = false;
			for (int i = 0; i < valueArr.length; i++) {

				tempString = valueArr[i].replace("~", ", ");
				valueArr[i] = tempString;
				valueArr[i] = new String(valueArr[i].getBytes(), CHARSET);
				
				if (valueArr[i].equals("")){
					continue;
				}
				else if (interestingValuesMap.get(valueArr[i]) == null){

					if (i+1 < valueArr.length && interestingValuesMap.get(valueArr[i] + "," + valueArr[i+1]) != null){
						valueArr[i] = valueArr[i] + "," + valueArr[i+1];
						System.out.println("found a value between two cells- " + subtable + " " + valueArr[i]);
						bufferedWriter.append(insertStatement);
						bufferedWriter.append("'" + interstingMainValuesMap.get(strarr[1]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
						bufferedWriter.flush();
						alreadySet = true;
						i++;
					}
					else {
						if (subtable.equals(Tables.characters.toString())){
							continue;
						}
						else {
							fieldName = subtable + "_name";
						}
						System.out.println(subtable + " " + valueArr[i] + " id equals null, adding it to table");
						valueArr[i] = valueArr[i].replace("\'", "\\'");
						System.out.println(valueArr[i]);
						//adding into the attribute's table
						stringForStatment = "INSERT IGNORE into " + subtable + "(" + fieldName+ ") values (\'" + valueArr[i] +"\');\n";
						addNullId = currentConn.createStatement();
						addNullId.executeUpdate(stringForStatment);
						
						//getting the new id for this value
						getId = "SELECT "+ subtable + "_id FROM " + subtable + " WHERE " + subtable + "_name = '" + valueArr[i] + "'";
						getNewId = currentConn.createStatement();
						rs = getNewId.executeQuery(getId);
						rs.first();
						int currentId = rs.getInt(1);
						System.out.println("added into " + subtable + " the values " + valueArr[i] + " with id " + currentId);
						
						//adding it into the map
						interestingValuesMap.put(valueArr[i], currentId);
						
						//adding new value + id into the joined table
						
						
						if (getNewId != null) getNewId.close();
						if (addNullId != null) addNullId.close();
						if (rs != null) rs.close();
						
						bufferedWriter.append(insertStatement);
						bufferedWriter.append("'" + interstingMainValuesMap.get(strarr[1]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
						bufferedWriter.flush();
						alreadySet = true;
						
					}
				}

				else{
					bufferedWriter.append(insertStatement);
					bufferedWriter.append("'" + interstingMainValuesMap.get(strarr[1]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
					bufferedWriter.flush();
					alreadySet = true;
				}
			}

			if (!alreadySet){
				bufferedWriter.append(insertStatement);
				bufferedWriter.append("'"+ interstingMainValuesMap.get(strarr[1]) + "', '" + unspecifiedId + "');\n");
				bufferedWriter.flush();
			}
		}

		System.out.println("OVERALL " + overallCounter + " FAILURES " + failuresCounter + "\n");
		bufferedWriter.close();
		bufferedReader.close();

	}

	
	private static void CreateTwoFieldTable(String insertStatement,String nameOfFile, int splitNum, int interestingFieldNum) throws IOException{

		File pathDir = new File(".");

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + nameOfFile);
		FileInputStream fis = new FileInputStream(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

		bufferedReader.readLine();
		String lineRead;

		DatabaseManager dbManager = DatabaseManager.getInstance();
		TreeMap<String, Integer> charactersMap = dbManager.generateHashMapFromQuery("SELECT * FROM characters", 1, 3);
		
		int triples = 0;

		while ((lineRead = bufferedReader.readLine()) != null) {
			String [] strarr = lineRead.split("\t", splitNum);
			String tempString = strarr[interestingFieldNum-1].replace(", ", "~");
			strarr[interestingFieldNum-1] = tempString;
			String [] valueArr = strarr[interestingFieldNum-1].split(",");
			int valueArrLen = valueArr.length;
			
			//not adding one-character relationship into table
			if (valueArrLen<2){
				System.out.println("Only one character in this relationship");
			}
			
			else {
				
				for (int i = 0; i < valueArr.length; i++) {
					tempString = valueArr[i].replace("~", ", ");
					valueArr[i] = tempString;
					valueArr[i] = new String(valueArr[i].getBytes(), CHARSET);
					
					//counting how many relationships have more than 2 characters
					if (valueArrLen > 2){
						triples++;
					}
					
					//adding each character with all of the rest
					for (int j=i+1; j < valueArr.length; j++){
						if (valueArr[i].equals("") || valueArr[j].equals("")){
							continue;
						}
						else if(charactersMap.get(valueArr[i])==null || charactersMap.get(valueArr[j])==null){
							System.out.println("couldn't find the character" + valueArr[i] + " and " + valueArr[j]);
						}
						
						else{
							bufferedWriter.append(insertStatement);
							bufferedWriter.append("'" + charactersMap.get(valueArr[i]) + "', '" + charactersMap.get(valueArr[j]) + "');\n");
							bufferedWriter.flush();
						}
					}
				}
			}
		}
		
		//System.out.println("number of relationship in " + nameOfFile + " is " + triples);
	}

	
	
	public static void main(String args[]) throws IOException, SQLException {

		long startTime = System.currentTimeMillis();
		
		//downloadAndExtractDumps();

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);

		if (sqlFile.exists()) {
			if(!sqlFile.delete()){
				System.out.println("Cannot delete populate-tables");
			}
		}

		populateSimpleTableUsingBatchFile("", "INSERT INTO species (species_name, species_fb_id) values(", "character_species.tsv",4, 2);
		System.out.println("Finished species");

		populateSimpleTableUsingBatchFile("", "INSERT INTO creator (creator_name, creator_fb_id) values(", "fictional_character_creator.tsv",3, 2);
		System.out.println("Finished creator");

		populateSimpleTableUsingBatchFile("", "INSERT INTO organization (organization_name, organization_fb_id) values(", "fictional_organization.tsv", 7, 2);
		System.out.println("Finished organization");

		populateSimpleTableUsingBatchFile("", "INSERT INTO gender (gender_name, gender_fb_id) values(", "character_gender.tsv", 3, 2);
		System.out.println("Finished gender");

		populateSimpleTableUsingBatchFile("", "INSERT INTO universe (universe_name, universe_fb_id) values(", "fictional_universe.tsv", 13, 2);
		System.out.println("Finished universe");

		populateSimpleTableUsingBatchFile("", "INSERT INTO school (school_name, school_fb_id) values(", "school_in_fiction.tsv", 3, 2);
		System.out.println("Finished school");

		populateSimpleTableUsingBatchFile("", "INSERT INTO rank (rank_name, rank_fb_id) values(", "character_rank.tsv", 3, 2);
		System.out.println("Finished rank");

		populateSimpleTableUsingBatchFile("", "INSERT INTO ethnicity (ethnicity_name, ethnicity_fb_id) values(", "ethnicity_in_fiction.tsv", 3, 2);
		System.out.println("Finished ethnicity");

		populateSimpleTableUsingBatchFile("", "INSERT INTO occupation (occupation_name, occupation_fb_id) values(", "character_occupation.tsv", 3, 2);
		System.out.println("Finished occupation");

		populateSimpleTableUsingBatchFile("", "INSERT INTO power (power_name, power_fb_id) values(", "character_powers.tsv", 3, 2);
		System.out.println("Finished power");

		populateSimpleTableUsingBatchFile("", "INSERT INTO job (job_name, job_fb_id) values(", "fictional_job_title.tsv", 3, 2);
		System.out.println("Finished job");

		populateSimpleTableUsingBatchFile("", "INSERT INTO disease (disease_name, disease_fb_id) values(", "medical_condition_in_fiction.tsv", 3, 2);
		System.out.println("Finished disease");

		populateSimpleTableUsingBatchFile("location", "INSERT INTO location (location_name,location_universe_id) values(", "fictional_universe.tsv",13,-1);
		System.out.println("Finished location");

		populateSimpleTableUsingBatchFile("place_of_birth","INSERT IGNORE place_of_birth (place_of_birth_name) values(", "fictional_character.tsv",27,-1);
		System.out.println("Finished place_of_birth");

		populateSimpleTableUsingBatchFile("characters", "INSERT INTO characters (character_name,character_fb_id,character_place_of_birth_id) values(","fictional_character.tsv",27,-1);
		System.out.println("Finished characters");
		
		AntUtils.executeTarget(Targets.SETUP);
		
		if (sqlFile.exists()) {
			if(!sqlFile.delete()){
				System.out.println("Cannot delete populate-tables");
			}
		}
		

		
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_universe (characters_and_universe_character_id, characters_and_universe_universe_id) values(","characters", "universe", 27, 12,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_gender (characters_and_gender_character_id, characters_and_gender_gender_id) values(","characters", "gender", 27, 5,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_species (characters_and_species_character_id, characters_and_species_species_id) values(","characters", "species", 27, 6,"fictional_character.tsv");
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_creator (characters_and_creator_character_id, characters_and_creator_creator_id) values(","characters", "creator", 27, 16,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_organization (characters_and_organization_character_id, characters_and_organization_organization_id) values(","characters", "organization", 27, 10,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_school (characters_and_school_character_id, characters_and_school_school_id) values(","characters", "school", 27, 21,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_rank (characters_and_rank_character_id, characters_and_rank_rank_id) values(","characters", "rank", 27, 9,"fictional_character.tsv");
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_ethnicity (characters_and_ethnicity_character_id, characters_and_ethnicity_ethnicity_id) values(","characters", "ethnicity", 27, 20,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_occupation (characters_and_occupation_character_id, characters_and_occupation_occupation_id) values(","characters", "occupation", 27, 8,"fictional_character.tsv");	
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_power (characters_and_power_character_id, characters_and_power_power_id) values(","characters", "power", 27, 11,"fictional_character.tsv");
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_disease (characters_and_disease_character_id, characters_and_disease_disease_id) values(","characters", "disease", 27, 23,"fictional_character.tsv");
	
		
		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO parent (parent_child_character_id, parent_parent_character_id) values(", "characters","characters", 27, 7,"fictional_character.tsv");
		CreateTwoFieldTable("INSERT IGNORE INTO marriage (marriage_character_id1, marriage_character_id2) values(", "marriage_of_fictional_characters.tsv", 3, 3);
		CreateTwoFieldTable("INSERT IGNORE INTO romantic_involvement (romantic_involvement_character_id1, romantic_involvement_character_id2) values(", "romantic_involvement.tsv", 3, 3);
		CreateTwoFieldTable("INSERT IGNORE INTO sibling (sibling_character_id1, sibling_character_id2) values(", "sibling_relationship_of_fictional_characters.tsv", 3, 3);
		
		AntUtils.executeTarget(Targets.POPULATE);

		long finishTime = System.currentTimeMillis();

		long totalTime = finishTime - startTime;
		System.out.println("operation took " + totalTime + " Millis");


	}
}

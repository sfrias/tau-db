package core;

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

import connection.JDCConnection;
import database.AntUtils;
import database.DatabaseManager;
import database.AntUtils.Targets;
import enums.ExecutionResult;
import enums.Tables;

public class TableUtilities {

	private static final String CHARSET = "UTF-8";
	private static final String POPULATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";
	static Tables[] tbs = Tables.values();
	static String[] tablesArr;

	static TreeMap<String, Short> tablesMap = new TreeMap<String, Short>();
	static TreeMap<Short, String> reverseTablesMap = new TreeMap<Short, String>();

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

	private static ExecutionResult populateSimpleTableUsingBatchFile(String table, String insertStatement, String dumpFileName, int splitNum, int attrNum, boolean update) {

		File pathDir = new File(".");
		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);

		FileOutputStream fileWriter = null;
		BufferedWriter bufferedWriter = null;
		FileInputStream fileReader = null;
		BufferedReader bufferedReader = null;

		try{

			fileWriter = new FileOutputStream(sqlFile, true);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileWriter));

			File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + dumpFileName);
			fileReader = new FileInputStream(dumpFile);
			bufferedReader = new BufferedReader(new InputStreamReader(fileReader));

			bufferedReader.readLine();
			String lineRead,tempString;
			String[] strarr;

			if (table.equals(Tables.place_of_birth.name())){	
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

			else if (table.equals(Tables.characters.name())){
				String insert = insertStatement;
				int id;
				DatabaseManager dbManager = DatabaseManager.getInstance();
				TreeMap<String, Integer> interstingMainValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM characters" , 1, 2);
				
				while ((lineRead = bufferedReader.readLine()) != null) {
					
					strarr = lineRead.split("\t", 27);
					
					if (update){ //if we update, we want to check if the character is already in the table, if so - we REPLACE it, otherwise - insert it 
						strarr = lineRead.split("\t", 27);
						tempString = strarr[1].replace("\'", "\\'");
						if (interstingMainValuesMap.get(tempString) != null) {
							id = interstingMainValuesMap.get(tempString);
							insert = "REPLACE INTO characters (character_id,character_name,character_fb_id,character_place_of_birth_id) values(" + id + ",";
						}
					}
					
					bufferedWriter.append(insert);

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

				if (!update){ //if we update we don't want to insert it again
				bufferedWriter.append(insert);
				bufferedWriter.append("'Unspecified', 'Unspecified',(SELECT place_of_birth_id FROM place_of_birth Where place_of_birth_name LIKE 'Unspecified'));\n");
				}
					
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
			}

			return ExecutionResult.Success_Populating_Simple_Table;
		}
		catch (IOException e) {
			e.printStackTrace();
			return ExecutionResult.Exception;
		}
		finally{

			if (bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileWriter != null){
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileReader != null){
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	


	private static ExecutionResult populateJoinedTableUsingBatchFile(String insertStatement,String mainTable, String subtable, int splitNum, int interestingFieldNum,String file, boolean update){
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		FileInputStream fis = null;
		BufferedReader bufferedReader = null;

		try {
			File pathDir = new File(".");
			File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);

			fileWriter = new FileWriter(sqlFile, true);
			bufferedWriter = new BufferedWriter(fileWriter);

			File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + file);
			fis = new FileInputStream(dumpFile);
			bufferedReader = new BufferedReader(new InputStreamReader(fis));

			bufferedReader.readLine();
			String lineRead;

			DatabaseManager dbManager = DatabaseManager.getInstance();

			TreeMap<String, Integer> interstingMainValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM " + mainTable, 1, 2);	
			TreeMap<String, Integer> interestingValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM " + subtable, 1, 3);

			int unspecifiedId = interestingValuesMap.get("Unspecified");

			while ((lineRead = bufferedReader.readLine()) != null) {

				String [] strarr = lineRead.split("\t", splitNum);
				strarr[interestingFieldNum-1] = strarr[interestingFieldNum-1].replace(", ", "~");
				String [] valueArr = strarr[interestingFieldNum-1].split(",");
				
				if (interstingMainValuesMap.get(strarr[1]) == null){
					System.out.println(mainTable + strarr[1] + " id equals null MM");
					continue;
				}
				
				JDCConnection conn = null;
				
				if ((update) && (!subtable.equals(Tables.characters.name()))) {
				//delete all the records from join table where the id match to the current character 
				conn = dbManager.getConnection();
				Statement deleteStmt = null;
				
				try {
					deleteStmt = conn.createStatement();
					String deleteString = "DELETE FROM " +  mainTable + "_and_" + subtable + " WHERE " + mainTable + "_and_" + subtable + "_character_id = " + interstingMainValuesMap.get(strarr[1]);
					deleteStmt.executeUpdate(deleteString);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally{
					if (deleteStmt != null){
						try {
							deleteStmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (conn != null){
						try {
							conn.setAutoCommit(true);
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
				
				
				boolean alreadySet = false;
				for (int i = 0; i < valueArr.length; i++) {
					valueArr[i] =valueArr[i].replace("~", ", ");
					valueArr[i] = new String(valueArr[i].getBytes(), CHARSET);

					if (valueArr[i].equals("")){
						continue;
					}
					else if (interestingValuesMap.get(valueArr[i]) == null){
						if (subtable.equals(Tables.characters.name())){
							continue;
						}

						String fieldName = subtable + "_name";
						System.out.println(subtable + " " + valueArr[i] + " id equals null, adding it to table");
						valueArr[i] = valueArr[i].replace("\'", "\\'");
						System.out.println(valueArr[i]);

						//adding into the attribute's table
						conn = dbManager.getConnection();
						Statement addNullIdStatement = null;
						ResultSet generatedKeys = null;

						try{
							conn.setAutoCommit(false);
							addNullIdStatement = conn.createStatement();
							addNullIdStatement.execute("INSERT IGNORE into " + subtable + "(" + fieldName+ ") values (\'" + valueArr[i] +"\')", Statement.RETURN_GENERATED_KEYS);						
							generatedKeys = addNullIdStatement.getGeneratedKeys();
							generatedKeys.first();
							int currentId = generatedKeys.getInt(1);
							addNullIdStatement.executeUpdate("UPDATE " + subtable + " SET " + subtable + "_fb_id = \'" + currentId + "\' WHERE " + subtable + "_id = " + currentId);
							conn.commit();
							System.out.println("added into " + subtable + " the values " + valueArr[i] + " with id " + currentId);
							//adding to the map
							interestingValuesMap.put(valueArr[i], currentId);
							//adding to joined table
							bufferedWriter.append(insertStatement);
							bufferedWriter.append("'" + interstingMainValuesMap.get(strarr[1]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
							bufferedWriter.flush();
							alreadySet = true;
						}
						catch (SQLException e){
							e.printStackTrace();
						}
						finally{
							if (generatedKeys != null){
								try {
									generatedKeys.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							if (addNullIdStatement != null){
								try {
									addNullIdStatement.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							if (conn != null){
								try {
									conn.setAutoCommit(true);
									conn.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}

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

			return ExecutionResult.Success_Populating_Joined_Table;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return ExecutionResult.Exception;
		}
		finally{

			if (bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileWriter != null){
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private static ExecutionResult CreateTwoFieldTable(String insertStatement,String nameOfFile, int splitNum, int interestingFieldNum, boolean update) {

		File pathDir = new File(".");
		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		FileInputStream fis = null;
		BufferedReader bufferedReader = null;
		
		try{
			fileWriter = new FileWriter(sqlFile, true);
			bufferedWriter = new BufferedWriter(fileWriter);

			File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + nameOfFile);
			fis = new FileInputStream(dumpFile);
			bufferedReader = new BufferedReader(new InputStreamReader(fis));

			bufferedReader.readLine();
			String lineRead;

			DatabaseManager dbManager = DatabaseManager.getInstance();
			TreeMap<String, Integer> charactersMap = dbManager.generateHashMapFromQuery("SELECT * FROM characters", 1, 3);

			//int triples = 0;
			String nameOfTable = nameOfFile.split(".tsv")[0];
			
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
						if ((update) && (charactersMap.get(valueArr[i]) != null)) {
							JDCConnection conn = null;
							conn = dbManager.getConnection();
							Statement deleteStmt = null;
							try {
								deleteStmt = conn.createStatement();
								String deleteString = "DELETE FROM " +  nameOfTable + " WHERE (" + nameOfTable + "_character_id1 = " + charactersMap.get(valueArr[i]) + ") OR ( " + nameOfTable + "_character_id2 = " + charactersMap.get(valueArr[i]) + " )";
								deleteStmt.executeUpdate(deleteString);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							finally{
								if (deleteStmt != null){
									try {
										deleteStmt.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
								if (conn != null){
									try {
										conn.setAutoCommit(true);
										conn.close();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							}
						}
						tempString = valueArr[i].replace("~", ", ");
						valueArr[i] = tempString;
						valueArr[i] = new String(valueArr[i].getBytes(), CHARSET);

						//counting how many relationships have more than 2 characters
						//if (valueArrLen > 2){
							//triples++;
						//}

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
			return ExecutionResult.Success_Populating_Joined_Table;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return ExecutionResult.Exception;
		}
		finally{

			if (bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileWriter != null){
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ExecutionResult createStatisticsTables(){
		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try{
			fileWriter = new FileWriter(sqlFile, true);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append("INSERT INTO success_rate(success_rate_searches,success_rate_successful_searches,success_rate_unsuccessful_searches)values(0,0,0);\n");
			bufferedWriter.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			return ExecutionResult.Exception;
		}
		finally{
			if (bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileWriter != null){
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ExecutionResult.Success_Simple_Add_Edit_Delete;
	}

	private static void updateDatabase() throws IOException{
		
		long startTime = System.currentTimeMillis();

		//downloadAndExtractDumps();

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		deleteSqlFile(sqlFile);

		createOrUpdateSimpleTables(true);
		AntUtils.executeTarget(Targets.POPULATE);

		deleteSqlFile(sqlFile);

		createOrUpdateComplexTables(true);
		AntUtils.executeTarget(Targets.POPULATE);

		long finishTime = System.currentTimeMillis();

		long totalTime = finishTime - startTime;
		System.out.println("operation took " + totalTime + " Millis");
		
	}
	
	private static void createDatabase() throws IOException{

		long startTime = System.currentTimeMillis();

		downloadAndExtractDumps();

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		deleteSqlFile(sqlFile);

		createOrUpdateSimpleTables(false);
		AntUtils.executeTarget(Targets.SETUP);
		
		deleteSqlFile(sqlFile);

		createOrUpdateComplexTables(false);
		createStatisticsTables();
		AntUtils.executeTarget(Targets.POPULATE);
		
		deleteSqlFile(sqlFile);

		long finishTime = System.currentTimeMillis();

		long totalTime = finishTime - startTime;
		System.out.println("operation took " + totalTime + " Millis");
	}

	
	private static void createOrUpdateSimpleTables(boolean update) {

		
		populateSimpleTableUsingBatchFile("", "INSERT IGNORE INTO disease (disease_name, disease_fb_id) values(", "medical_condition_in_fiction.tsv", 3, 2, update);
		System.out.println("Finished disease");

		populateSimpleTableUsingBatchFile("", "INSERT IGNORE INTO occupation (occupation_name, occupation_fb_id) values(", "character_occupation.tsv", 3, 2, update);
		System.out.println("Finished occupation");

		populateSimpleTableUsingBatchFile("", "INSERT IGNORE INTO organization (organization_name, organization_fb_id) values(", "fictional_organization.tsv", 7, 2, update);
		System.out.println("Finished organization");

		populateSimpleTableUsingBatchFile("", "INSERT IGNORE INTO power (power_name, power_fb_id) values(", "character_powers.tsv", 3, 2, update);
		System.out.println("Finished power");

		populateSimpleTableUsingBatchFile("place_of_birth","INSERT IGNORE place_of_birth (place_of_birth_name) values(", "fictional_character.tsv",27,-1, update);
		System.out.println("Finished place_of_birth");

		populateSimpleTableUsingBatchFile("", "INSERT IGNORE INTO school (school_name, school_fb_id) values(", "school_in_fiction.tsv", 3, 2, update);
		System.out.println("Finished school");

		populateSimpleTableUsingBatchFile("", "INSERT IGNORE INTO universe (universe_name, universe_fb_id) values(", "fictional_universe.tsv", 13, 2, update);
		System.out.println("Finished universe");

		populateSimpleTableUsingBatchFile("characters", "INSERT IGNORE INTO characters (character_name,character_fb_id,character_place_of_birth_id) values(","fictional_character.tsv",27,-1, update);
		System.out.println("Finished characters");
	}

	private static ExecutionResult createOrUpdateComplexTables(boolean update) {

		ExecutionResult result;
		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_disease (characters_and_disease_character_id, characters_and_disease_disease_id) values(","characters", "disease", 27, 23,"fictional_character.tsv", update);		
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_occupation (characters_and_occupation_character_id, characters_and_occupation_occupation_id) values(","characters", "occupation", 27, 8,"fictional_character.tsv", update);	
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_organization (characters_and_organization_character_id, characters_and_organization_organization_id) values(","characters", "organization", 27, 10,"fictional_character.tsv", update);	
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_power (characters_and_power_character_id, characters_and_power_power_id) values(","characters", "power", 27, 11,"fictional_character.tsv", update);
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_universe (characters_and_universe_character_id, characters_and_universe_universe_id) values(","characters", "universe", 27, 12,"fictional_character.tsv", update);	
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_school (characters_and_school_character_id, characters_and_school_school_id) values(","characters", "school", 27, 21,"fictional_character.tsv", update);	
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = populateJoinedTableUsingBatchFile("INSERT IGNORE INTO parent (parent_child_character_id, parent_parent_character_id) values(", "characters","characters", 27, 7,"fictional_character.tsv", update);
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		result = CreateTwoFieldTable("INSERT IGNORE INTO romantic_involvement (romantic_involvement_character_id1, romantic_involvement_character_id2) values(", "romantic_involvement.tsv", 3, 3, update);
		if (result.equals(ExecutionResult.Exception)){
			return ExecutionResult.Exception;
		}

		return ExecutionResult.Success_Populating_Joined_Table;
	}

	private static void deleteSqlFile(File sqlFile) {
		if (sqlFile.exists()) {
			if(!sqlFile.delete()){
				System.out.println("Cannot delete populate-tables");
			}
		}
	}

	public static void main(String args[]) throws IOException{

		//createDatabase();
		updateDatabase();
		

	}
}

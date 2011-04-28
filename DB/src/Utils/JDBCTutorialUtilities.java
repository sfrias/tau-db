package Utils;

/*
 * Copyright (c) 1995, 2011, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class JDBCTutorialUtilities {

	private static final String CHARSET = "UTF-8";
	private static final String CREATE_TABLES_SQL_FILE_PATH = "sql/mysql/create-tables.sql";
	private static final String DROP_TABLES_SQL_FILE_PATH = "sql/mysql/drop-tables.sql";
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

	private static void populateSimpleTable(String table, String insertStatement, String dumpFileName, int splitNum, int attrNum) throws IOException, SQLException {

		File pathDir = new File(".");

		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + dumpFileName);
		FileReader fileReader = new FileReader(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		JDCConnection conn = (JDCConnection) DatabaseManager.getInstance().getConnection();
		Statement stmt = conn.createStatement();


		bufferedReader.readLine();
		String lineRead;
		String[] strarr;
		String tempString;

		StringBuilder sqlCommand = new StringBuilder();
		if (table.equals("locations")){
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", splitNum);
				strarr[0] = strarr[0].replace("\'", "\\'");
				String [] locationarr = strarr[2].split(",");
				int len = locationarr.length;

				for (int i = 0; i < len; i++) {
					tempString = locationarr[i].replace("\'", "\\'");
					locationarr[i] = tempString;
					if (!locationarr[i].equals("")) {
						sqlCommand.append(insertStatement);
						sqlCommand.append("'" + locationarr[i] + "', " + "(SELECT universe_id FROM universe Where universe_fb_id LIKE ? AND universe_name like'" + strarr[0]+ "'));\n");
						stmt.addBatch(sqlCommand.toString());
						sqlCommand.setLength(0);
					}

				}
			}
			sqlCommand.append(insertStatement);
			sqlCommand.append("'Unspecified', (SELECT universe_id FROM universe Where universe_fb_id LIKE 'Unspecified'));\n");
			stmt.addBatch(sqlCommand.toString());
			sqlCommand.setLength(0);
		}

		else if (table.equals("place_of_birth")){	
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", splitNum);
				tempString = strarr[3].replace("\'", "\\'");
				strarr[3] = tempString;
				if (! strarr[3].equals("")) {
					sqlCommand.append(insertStatement);
					sqlCommand.append("'" +  strarr[3] + "');\n");
					stmt.addBatch(sqlCommand.toString());
					sqlCommand.setLength(0);
				}

			}
			sqlCommand.append(insertStatement);
			sqlCommand.append("'Unspecified');\n");
			stmt.addBatch(sqlCommand.toString());
			sqlCommand.setLength(0);
		}

		else if (table.equals("characters")){
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", 27);
				sqlCommand.append(insertStatement);

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

						sqlCommand.append("(SELECT place_of_birth_id FROM place_of_birth Where place_of_birth_name LIKE '" + strarr[3] + "'));\n");
						stmt.addBatch(sqlCommand.toString());
						sqlCommand.setLength(0);
					}
					else{
						sqlCommand.append("'" + strarr[i] + "', ");
					}
				}
			}
		}

		else {
			while ((lineRead = bufferedReader.readLine()) != null) {
				strarr = lineRead.split("\t", splitNum);
				sqlCommand.append(insertStatement);
				for (int i = 0; i < attrNum - 1; i++) {
					tempString = strarr[i].replace("\'", "\\'");
					strarr[i] = tempString;
					sqlCommand.append("'" + strarr[i] + "', ");
				}
				sqlCommand.append("'" + strarr[attrNum - 1] + "');\n");
				stmt.addBatch(sqlCommand.toString());
				sqlCommand.setLength(0);
			}

			sqlCommand.append(insertStatement);
			sqlCommand.append("'Unspecified', 'Unspecified');\n");
			stmt.addBatch(sqlCommand.toString());
			sqlCommand.setLength(0);

		}

		stmt.executeBatch();
		stmt.close();
		conn.close();

		bufferedReader.close();
		fileReader.close();

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

		if (table.equals("locations")){
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

		else if (table.equals("place_of_birth")){	
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

		else if (table.equals("characters")){
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


	private static void populateJoinedTableUsingBatchFile(String insertStatement, String interestingTable, int splitNum, int interestingFieldNum) throws IOException{

		File pathDir = new File(".");

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + "fictional_character.tsv");
		FileInputStream fis = new FileInputStream(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

		bufferedReader.readLine();
		String lineRead;

		DatabaseManager dbManager = DatabaseManager.getInstance();
		TreeMap<String, Integer> interestingValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM " + interestingTable, 1, 3);
		TreeMap<String, Integer> charactersMap = dbManager.generateHashMapFromQuery("SELECT * FROM characters", 1, 3);

		int unspecifiedId = interestingValuesMap.get("Unspecified");

		int overallCounter = 0;
		int failuresCounter = 0;

		while ((lineRead = bufferedReader.readLine()) != null) {
			overallCounter++;
			String [] strarr = lineRead.split("\t", splitNum);
			String tempString = strarr[interestingFieldNum-1].replace(", ", "~");
			strarr[interestingFieldNum-1] = tempString;
			String [] valueArr = strarr[interestingFieldNum-1].split(",");

			strarr[0] = new String(strarr[0].getBytes(), CHARSET);
			boolean alreadySet = false;
			
			for (int i = 0; i < valueArr.length; i++) {

				tempString = valueArr[i].replace("~", ", ");
				valueArr[i] = tempString;
				valueArr[i] = new String(valueArr[i].getBytes(), CHARSET);

				if (charactersMap.get(strarr[0]) == null){
					System.out.println("character " + strarr[0] + " id equals null");
					failuresCounter++;
				}
				if (valueArr[i].equals("")){
					continue;
				}
				else if (!valueArr[i].equals("") && interestingValuesMap.get(valueArr[i]) == null){
					
					if (i+1 < valueArr.length && interestingValuesMap.get(valueArr[i] + "," + valueArr[i+1]) != null){
						valueArr[i] = valueArr[i] + "," + valueArr[i+1];
						i++;
						System.out.println("found a value between two cells- " + interestingTable + " " + valueArr[i]);
						bufferedWriter.append(insertStatement);
						bufferedWriter.append("'" + charactersMap.get(strarr[0]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
						bufferedWriter.flush();
						alreadySet = true;
					}
					else {
						System.out.println(interestingTable + " " + valueArr[i] + " id equals null");
						failuresCounter++;
					}
				}
				
				else{
					bufferedWriter.append(insertStatement);
					bufferedWriter.append("'" + charactersMap.get(strarr[0]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
					bufferedWriter.flush();
					alreadySet = true;
				}
			}

			if (!alreadySet){
				bufferedWriter.append(insertStatement);
				bufferedWriter.append("'"+ charactersMap.get(strarr[0]) + "', '" + unspecifiedId + "');\n");
				bufferedWriter.flush();
			}
		}

		System.out.println("OVERALL " + overallCounter + " FAILURES " + failuresCounter);
		bufferedWriter.close();
		bufferedReader.close();

	}

	private static void populateJoinedTable(String insertStatement, String interestingTable, int splitNum, int interestingFieldNum) throws IOException, SQLException{

		File pathDir = new File(".");

		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + "fictional_character.tsv");
		FileInputStream fis = new FileInputStream(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

		DatabaseManager dbManager = DatabaseManager.getInstance();
		TreeMap<String, Integer> interestingValuesMap = dbManager.generateHashMapFromQuery("SELECT * FROM " + interestingTable, 1, 3);
		TreeMap<String, Integer> charactersMap = dbManager.generateHashMapFromQuery("SELECT * FROM characters", 1, 3);

		int unspecifiedId = interestingValuesMap.get("Unspecified");

		JDCConnection conn = (JDCConnection) dbManager.getConnection();
		Statement stmt = conn.createStatement();

		bufferedReader.readLine();
		String lineRead;
		StringBuilder sqlCommand = new StringBuilder();

		while ((lineRead = bufferedReader.readLine()) != null) {

			String [] strarr = lineRead.split("\t", splitNum);
			String tempString = strarr[interestingFieldNum-1].replace(", ", "~");
			strarr[interestingFieldNum-1] = tempString;
			String [] valueArr = strarr[interestingFieldNum-1].split(",");

			for (int i = 0; i < valueArr.length; i++) {

				tempString = valueArr[i].replace("~", ", ");
				valueArr[i] = tempString;

				if (!valueArr[i].equals("")) {
					if (interestingValuesMap.get(valueArr[i]) == null){
						if (i+1 < valueArr.length && interestingValuesMap.get(valueArr[i] + "," + valueArr[i+1]) != null){
							valueArr[i] = valueArr[i] + valueArr[i+1];
							System.out.println("found a value between two cells- " + interestingTable + " " + valueArr[i]);
						}
						else {
							System.out.println(interestingTable + " " + valueArr[i] + " id equals null");
						}
					}
				}

				else if (charactersMap.get(strarr[0])== null){
					System.out.println("character " + strarr[0] + " id equals null");
				}
				else{
					sqlCommand.append(insertStatement);
					sqlCommand.append("'" + charactersMap.get(strarr[0]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
					stmt.addBatch(sqlCommand.toString());
					sqlCommand.setLength(0);

				}
			}

			if (valueArr.length == 0){
				sqlCommand.append(insertStatement);
				sqlCommand.append("'"+ charactersMap.get(strarr[0]) + "', '" + unspecifiedId + "');");
				stmt.addBatch(sqlCommand.toString());
				sqlCommand.setLength(0);
			}
		}

		stmt.executeBatch();
		stmt.close();
		conn.close();
		bufferedReader.close();

	}

	public static void main(String args[]) throws IOException, SQLException {

		SortedMap<String, Charset> charsets = Charset.availableCharsets();

		Object [] values = charsets.values().toArray();

		for (int i=0; i < values.length; i++){
			System.out.println(((Charset) values[i]).name());
		}

		long startTime = System.currentTimeMillis();
		DatabaseManager dbManager = DatabaseManager.getInstance();

		//downloadAndExtractDumps();

		File sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);

		if(sqlFile.exists()) {
			if(!sqlFile.delete()){
				System.out.println("Cannot delete populate-tables");
			}
		}

		//dbManager.executeBatchFile(DROP_TABLES_SQL_FILE_PATH);
		//dbManager.executeBatchFile(CREATE_TABLES_SQL_FILE_PATH);

		//		populateSimpleTableUsingBatchFile("", "INSERT INTO species (species_name, species_fb_id) values(", "character_species.tsv",4, 2);
		//		System.out.println("Finished species");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO creator (creator_name, creator_fb_id) values(", "fictional_character_creator.tsv",3, 2);
		//		System.out.println("Finished creator");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO organization (organization_name, organization_fb_id) values(", "fictional_organization.tsv", 7, 2);
		//		System.out.println("Finished organization");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO gender (gender_name, gender_fb_id) values(", "character_gender.tsv", 3, 2);
		//		System.out.println("Finished gender");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO universe (universe_name, universe_fb_id) values(", "fictional_universe.tsv", 13, 2);
		//		System.out.println("Finished universe");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO school (school_name, school_fb_id) values(", "school_in_fiction.tsv", 3, 2);
		//		System.out.println("Finished school");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO rank (rank_name, rank_fb_id) values(", "character_rank.tsv", 3, 2);
		//		System.out.println("Finished rank");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO ethnicity (ethnicity_name, ethnicity_fb_id) values(", "ethnicity_in_fiction.tsv", 3, 2);
		//		System.out.println("Finished ethnicity");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO occupation (occupation_name, occupation_fb_id) values(", "character_occupation.tsv", 3, 2);
		//		System.out.println("Finished occupation");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO powers (power_name, power_fb_id) values(", "character_powers.tsv", 3, 2);
		//		System.out.println("Finished powers");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO jobs (job_name, job_fb_id) values(", "fictional_job_title.tsv", 3, 2);
		//		System.out.println("Finished jobs");
		//
		//		populateSimpleTableUsingBatchFile("", "INSERT INTO diseases (disease_name, disease_fb_id) values(", "medical_condition_in_fiction.tsv", 3, 2);
		//		System.out.println("Finished diseases");
		//
		//		populateSimpleTableUsingBatchFile("locations", "INSERT INTO locations (location_name,location_universe_id) values(", "fictional_universe.tsv",13,-1);
		//		System.out.println("Finished locations");
		//
		//		populateSimpleTableUsingBatchFile("place_of_birth","INSERT IGNORE place_of_birth (place_of_birth_name) values(", "fictional_character.tsv",27,-1);
		//		System.out.println("Finished place_of_birth");
		//
		//		populateSimpleTableUsingBatchFile("characters", "INSERT INTO characters (character_name,character_fb_id,character_place_of_birth_id) values(","fictional_character.tsv",27,-1);
		//		System.out.println("Finished characters");

		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_universes (characters_and_universes_character_id, characters_and_universes_universe_id) values(", "universe", 27, 12);	
		//
		//		System.out.println("Finished joined");

		long finishTime = System.currentTimeMillis();

		System.out.println("operation took " + (finishTime - startTime) + "Millis");
		
		tal a = new tal();
		//tal.fillTables();
		String[] arr = new String[5];
		a.openConnection();
		a.lookForConnection("Tammy", "Hila",1,0,"Tammy", arr);
		a.closeconnection();
	}
}
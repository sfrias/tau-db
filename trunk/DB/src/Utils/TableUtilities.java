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
import java.sql.SQLException;
import java.util.TreeMap;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import Utils.AntUtils.Targets;

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
		TreeMap<String, Integer> charactersMap = dbManager.generateHashMapFromQuery("SELECT * FROM characters", 1, 2);

		int unspecifiedId = interestingValuesMap.get("Unspecified");

		int overallCounter = 0;
		int failuresCounter = 0;

		while ((lineRead = bufferedReader.readLine()) != null) {
			overallCounter++;
			String [] strarr = lineRead.split("\t", splitNum);
			String tempString = strarr[interestingFieldNum-1].replace(", ", "~");
			strarr[interestingFieldNum-1] = tempString;
			String [] valueArr = strarr[interestingFieldNum-1].split(",");

//			strarr[0] = new String(strarr[0].getBytes(), CHARSET);

			if (charactersMap.get(strarr[1]) == null){
				System.out.println("character " + strarr[1] + " id equals null");
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
						System.out.println("found a value between two cells- " + interestingTable + " " + valueArr[i]);
						bufferedWriter.append(insertStatement);
						bufferedWriter.append("'" + charactersMap.get(strarr[1]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
						bufferedWriter.flush();
						alreadySet = true;
						i++;
					}
					else {
						System.out.println(interestingTable + " " + valueArr[i] + " id equals null");
						failuresCounter++;
					}
				}

				else{
					bufferedWriter.append(insertStatement);
					bufferedWriter.append("'" + charactersMap.get(strarr[1]) + "', '" + interestingValuesMap.get(valueArr[i]) + "');\n");
					bufferedWriter.flush();
					alreadySet = true;
				}
			}

			if (!alreadySet){
				bufferedWriter.append(insertStatement);
				bufferedWriter.append("'"+ charactersMap.get(strarr[1]) + "', '" + unspecifiedId + "');\n");
				bufferedWriter.flush();
			}
		}

		System.out.println("OVERALL " + overallCounter + " FAILURES " + failuresCounter + "\n");
		bufferedWriter.close();
		bufferedReader.close();

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

		//populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_universes (characters_and_universes_character_id, characters_and_universes_universe_id) values(", "universe", 27, 12);	
		//populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_genders (characters_and_genders_character_id, characters_and_genders_gender_id) values(", "gender", 27, 5);	
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_species (characters_and_species_character_id, characters_and_species_species_id) values(", "species", 27, 6);
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_creators (characters_and_creators_character_id, characters_and_creators_creator_id) values(", "creator", 27, 16);	
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_organizations (characters_and_organizations_character_id, characters_and_organizations_organization_id) values(", "organization", 27, 10);	
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_schools (characters_and_schools_character_id, characters_and_schools_school_id) values(", "school", 27, 21);	
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_ranks (characters_and_ranks_character_id, characters_and_ranks_rank_id) values(", "rank", 27, 9);
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_ethnicities (characters_and_ethnicities_character_id, characters_and_ethnicities_ethnicity_id) values(", "ethnicity", 27, 20);	
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_occupations (characters_and_occupations_character_id, characters_and_occupations_occupation_id) values(", "occupation", 27, 8);	
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_powers (characters_and_powers_character_id, characters_and_powers_power_id) values(", "powers", 27, 11);
//		populateJoinedTableUsingBatchFile("INSERT IGNORE INTO characters_and_diseases (characters_and_diseases_character_id, characters_and_diseases_disease_id) values(", "diseases", 27, 23);	

		AntUtils.executeTarget(Targets.POPULATE);

		long finishTime = System.currentTimeMillis();

		long totalTime = finishTime - startTime;
		System.out.println("operation took " + totalTime + " Millis");

//		tal a = new tal();
//		//tal.fillTables();
//		String[] arr = new String[5];
//		if (a.lookForConnection("Webster", "Pamela",1,0,"Webster", arr))
//			System.out.println("finish!!!");
//		else
//			System.out.println("didnt find any match");
//				
//		a.getConnention().close();
//		System.out.println("closed");
	}
}
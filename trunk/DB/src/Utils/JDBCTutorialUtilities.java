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



import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.w3c.dom.Document;

public class JDBCTutorialUtilities {

	private static final String CREATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";
	public String dbms;
	public String jarFile;
	public String dbName; 
	public String userName;
	public String password;
	public String urlString;

	private String driver;
	private String serverName;
	private int portNumber;
	private Properties prop;

	public static void downloadAndExtractDumps() throws IOException{
		File pathDir = new File(".");
		if (!pathDir.exists()){
			pathDir.mkdir();
		}

		BufferedInputStream in = new BufferedInputStream(new java.net.URL("http://download.freebase.com/datadumps/latest/browse/fictional_universe.tar.bz2").openStream());
		File save = new File(pathDir.getAbsolutePath() + File.separatorChar + "fictional_universe.tar.bz2");

		FileOutputStream fos = new FileOutputStream(save);
		BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
		byte[] data = new byte[1024];
		int size=0;
		System.out.println("Downloading Content...");
		while((size=in.read(data,0,1024))>=0){
			bout.write(data,0,size);
		}
		bout.close();
		in.close();
		System.out.println("Finished Downloading");

		System.out.println("Starting to extract file...");
		//starting bz2 decompression
		File CompressedTar = new File(pathDir.getAbsolutePath() + File.separatorChar + "fictional_universe.tar");
		FileInputStream fin = new FileInputStream(save);
		fin.skip(2);
		CBZip2InputStream bzIn = new CBZip2InputStream(fin);
		FileOutputStream fileTarStream = new FileOutputStream(CompressedTar);		
		data = new byte[1024];
		size=0;
		while ((size=bzIn.read(data,0,1024))>=0){
			fileTarStream.write(data, 0, size);
		}
		fileTarStream.close();
		bzIn.close();
		fin.close();
		//finished bz2 decompression

		//starting tar decompression
		FileInputStream CompressedTarIn = new FileInputStream(CompressedTar);
		TarInputStream tarIn = new TarInputStream(CompressedTarIn);
		File extractDir = new File(pathDir.getAbsolutePath() +  File.separatorChar + "temp");
		if (!extractDir.exists()){
			extractDir.mkdir();
		}
		TarEntry tarEntry = tarIn.getNextEntry();
		while (tarEntry != null){
			File destPath = new File(extractDir.getAbsolutePath() + File.separatorChar + tarEntry.getName());
			System.out.println("Processing " + File.separatorChar + destPath.getAbsoluteFile());
			if(!tarEntry.isDirectory()){
				FileOutputStream fout = new FileOutputStream(destPath);
				tarIn.copyEntryContents(fout);
				fout.close();
			}else{
				destPath.mkdir();
			}
			tarEntry = tarIn.getNextEntry();
		}
		tarIn.close();
		CompressedTarIn.close();
		//finished tar decompression
		System.out.println("finished extracting file!"); 
	}

	public static void updateSQLFiles(String insertStatement, String dumpFileName, int splitNum, int attrNum) throws IOException{
		File pathDir = new File(".");
		
		File sqlFile = new File(CREATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + dumpFileName);
		FileReader fileReader = new FileReader(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		bufferedReader.readLine();
		String lineRead ;
		String[] strarr;
		String tempString;
		
		while ((lineRead=bufferedReader.readLine()) != null){
			strarr = lineRead.split("\t",splitNum);
			bufferedWriter.append(insertStatement);
			for (int i=0; i < attrNum-1; i++){
				tempString = strarr[i].replace("\'", "\\'");
				strarr[i] = tempString;
				bufferedWriter.append("'" + strarr[i] + "', ");
			}
			bufferedWriter.append("'" + strarr[attrNum-1] + "');\n");
			bufferedWriter.flush();
		}
		
		bufferedWriter.close();
		bufferedReader.close();		
	}
	
	public static void main(String args[]) throws IOException{
		updateSQLFiles("INSERT INTO species (species_name, species_fb_id) values(", "character_species.tsv", 4, 2);
		updateSQLFiles("INSERT INTO creator (creator_name, creator_fb_id) values(", "fictional_character_creator.tsv", 4, 2);	
		updateSQLFiles("INSERT INTO organization (organization_name, organization_fb_id) values(", "character_species.tsv", 4, 2);	

	}
}
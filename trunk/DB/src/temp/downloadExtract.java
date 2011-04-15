package temp;

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

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;


public class downloadExtract {
	
	private static final String CREATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";

	
	public static void main(String args[]) throws IOException {
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
		File extractDir = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp");
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
				
		File sqlFile = new File(CREATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		File dumpFile = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp" + File.separatorChar + "fictional_universe" + File.separatorChar + "fictional_organization.tsv");
		FileReader fileReader = new FileReader(dumpFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		bufferedReader.readLine();
		String lineRead;
		String[] strarr;

		while ((lineRead=bufferedReader.readLine()) != null){
			strarr = lineRead.split("\t",8);
			bufferedWriter.append("insert into " + "FICTIONAL_ORGANIZATIONS" +" values(");
			for (int i=0; i < 7; i++){
				bufferedWriter.append("'" + strarr[i] + "', ");
			}
			bufferedWriter.append("'" + strarr[7] + "');\n");
			bufferedWriter.flush();
		}
		
		bufferedWriter.close();
		bufferedReader.close();	
	}
}

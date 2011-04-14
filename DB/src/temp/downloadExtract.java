package temp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class downloadExtract {
	
	public static void main(String args[]) throws IOException {
		String path = "c:\\fict";
		File pathDir = new File(path);
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
		
		File file = new File("C:\\fict\\temp\\fictional_universe\\fictional_organization.tsv");
		FileReader fi = new FileReader(file);
		BufferedReader bis = new BufferedReader(fi);
		bis.readLine();
		String lineRead ;
		String name,id,member,type, appears, founder, parent_org, sub_org;
		String[] strarr;
		int n;
		StringTokenizer st;
		while ((lineRead=bis.readLine()) != null){
			//not good enough
/*			st = new StringTokenizer(lineRead, "\t");
			n=st.countTokens();*/
			
			strarr = lineRead.split("\t",8);
			name = strarr[0];
			id=strarr[1];
			member = strarr[2];
			type = strarr[3];
			appears = strarr[4];
			founder = strarr[5];
			parent_org = strarr[6];
			sub_org = strarr[7];
			System.out.println(name+"\t"+id+"\t"+member+"\t"+type+"\t"+appears+"\t"+founder+"\t"+parent_org+"\t"+sub_org);				
		}	
	}
}

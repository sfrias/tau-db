package temp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
		
	}


}

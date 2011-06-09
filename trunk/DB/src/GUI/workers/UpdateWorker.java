package GUI.workers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import GUI.GuiHandler;
import GUI.panels.Manage.Tabs.UpdateTab;
import core.TableUtilities;
import database.AntUtils;
import database.AntUtils.Targets;
import database.DatabaseManager;
import enums.UpdateResult;

public class UpdateWorker extends SwingWorker<UpdateResult, UpdateResult>{
	private File pathDir, sqlFile, extractDir, fictUniverseDir;
	private File save, compressedTar ;
	private final String POPULATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";
	private UpdateTab parent;

	// ************* progressBar purpose ***************
	private double ESTIMATE_DOWNLOAD_SIZE = 2800 * 1024;
	private double ESTIMATE_EXTRACT_SIZE = 11700 * 1024;
	private double ESTIMATE_N_OF_RECORDS_IN_TAR = 40;
	// *************************************************

	public UpdateWorker(UpdateTab parent){
		super();
		this.parent = parent;
	}

	private void deleteFile(File sqlFile) {
		if (sqlFile.exists()) {
			if(!sqlFile.delete()){
				System.out.println("Cannot delete the file: " + sqlFile.toString());
			}
		}
	}

	@Override
	protected UpdateResult doInBackground() {
		pathDir = new File(".");
		if (!pathDir.exists()) {
			pathDir.mkdir();
		}
		save = new File(pathDir.getAbsolutePath() + File.separatorChar + "fictional_universe.tar.bz2");

		sqlFile = new File(POPULATE_TABLES_SQL_FILE_PATH);
		deleteFile(sqlFile);

		GuiHandler.startStatusFlash();
		try {
			downloadDumps();
			if (!isCancelled()){
				setProgress(25);
				publish(UpdateResult.finish_download);
			}

			extractDumps();
			if (!isCancelled()){			
				setProgress(50);
				publish(UpdateResult.finish_extract);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			publish(UpdateResult.start_update_table);

			if (!isCancelled()){
				TableUtilities.createOrUpdateSimpleTables(true); 
				setProgress(60);
				AntUtils.executeTarget(Targets.POPULATE); 
				deleteFile(sqlFile);
				setProgress(70);
				TableUtilities.createOrUpdateComplexTables(true);
				setProgress(80);
				AntUtils.executeTarget(Targets.POPULATE); 
				setProgress(90);

				publish(UpdateResult.finish_update_table);
			}
			clearHistoryTables();
			removeTemporaryFiles();
			if (!isCancelled()){
				setProgress(100);
				publish(UpdateResult.finish_delete);
			}
		} catch (IOException e) {
			return UpdateResult.exception;
			/*} catch (SQLException e){
			return UpdateResult.exception;*/
		}

		return UpdateResult.done;

	}

	private void clearHistoryTables(){
		DatabaseManager dbManager = DatabaseManager.getInstance();

		dbManager.executeDeleteTableContent("history"); 
		dbManager.executeDeleteTableContent("failed_searches"); 
	}

	@Override
	protected void process(List<UpdateResult> result){
		UpdateResult progress = result.get(result.size()-1);
		parent.refreshTab(progress);
	}

	private void downloadDumps() throws IOException{

		BufferedInputStream in = new BufferedInputStream(new java.net.URL("http://download.freebase.com/datadumps/latest/browse/fictional_universe.tar.bz2").openStream());

		FileOutputStream fos = new FileOutputStream(save);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int size = 0;
		double total=0;

		System.out.println("Downloading Content...");

		while (!isCancelled() && ((size = in.read(data, 0, 1024)) >= 0)) {
			bout.write(data, 0, size);
			total += size;
			setProgress((int)(total/ESTIMATE_DOWNLOAD_SIZE*25));
		}

		bout.close();
		in.close();

		if (isCancelled()){
			System.out.println("download canceled");
		} else{
			System.out.println("Finished Downloading");			
		}
	}

	private void extractDumps() throws IOException{
		// starting bz2 decompression
		compressedTar = new File(pathDir.getAbsolutePath()+ File.separatorChar + "fictional_universe.tar");

		FileInputStream fin = new FileInputStream(save);
		fin.skip(2);
		CBZip2InputStream bzIn = new CBZip2InputStream(fin);
		FileOutputStream fileTarStream = new FileOutputStream(compressedTar);

		byte[] data = new byte[1024];
		int size = 0;
		double total = 0;

		System.out.println("Starting to extract file...");

		while (!isCancelled() && ((size = bzIn.read(data, 0, 1024)) >= 0)) {
			fileTarStream.write(data, 0, size);
			total+=size;
			setProgress(25 + (int)(total/ESTIMATE_EXTRACT_SIZE*13));
		}

		fileTarStream.close();
		bzIn.close();
		fin.close();
		// finished bz2 decompression

		// starting tar decompression
		total = 0;
		FileInputStream CompressedTarIn = new FileInputStream(compressedTar);
		TarInputStream tarIn = new TarInputStream(CompressedTarIn);

		extractDir = new File(pathDir.getAbsolutePath() + File.separatorChar + "temp");
		if (!extractDir.exists()) {
			extractDir.mkdir();
		}

		TarEntry tarEntry = tarIn.getNextEntry();
		while (!isCancelled() && (tarEntry != null)) {
			File destPath = new File(extractDir.getAbsolutePath() + File.separatorChar + tarEntry.getName());
			System.out.println("Processing " + File.separatorChar + destPath.getAbsoluteFile());
			if (!tarEntry.isDirectory()) {
				FileOutputStream fout = new FileOutputStream(destPath);
				tarIn.copyEntryContents(fout);
				fout.close();

				total++;
				setProgress(38 + (int)(total/ESTIMATE_N_OF_RECORDS_IN_TAR*12));
			} else {
				destPath.mkdir();
			}
			tarEntry = tarIn.getNextEntry();
		}

		tarIn.close();
		CompressedTarIn.close();
		// finished tar decompression

		fictUniverseDir = new File(extractDir.getAbsolutePath() + File.separatorChar + "fictional_universe");

		if (isCancelled()){
			System.out.println("extractrion canceled");
		}else {
			System.out.println("finished extracting files!");
		}

	}

	private void removeTemporaryFiles (){
		if (fictUniverseDir != null && fictUniverseDir.exists()){
			File[] allFiles = fictUniverseDir.listFiles();
			for (int i=0; i<allFiles.length; i++){
				allFiles[i].delete();
			}
			fictUniverseDir.delete();
		}

		if (extractDir != null && extractDir.exists()){
			extractDir.delete();
		}

		if (save != null && save.exists()){
			save.delete();
		}
		if (compressedTar != null && compressedTar.exists()){
			compressedTar.delete();
		}

	}

	@Override
	protected void done(){

		if (isCancelled()){ 						 //terminated after user pressed cancel
			removeTemporaryFiles();
			parent.refreshTab(UpdateResult.cancel_accepted);
		} else{ 									//terminated because finished updating or exception
			UpdateResult result;
			try {
				result = get();
				switch (result){
				case done:
					parent.refreshTab(result);
					break;
				case exception:
					removeTemporaryFiles();
					parent.refreshTab(result);
					break;
				default:
					break;
				} 
			}catch (InterruptedException e) {
				parent.refreshTab(UpdateResult.exception);
			} catch (ExecutionException e) {
				parent.refreshTab(UpdateResult.exception);
			}
		}
	}
}



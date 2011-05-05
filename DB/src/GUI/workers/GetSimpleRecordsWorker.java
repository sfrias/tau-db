package GUI.workers;

import javax.swing.SwingWorker;

import Enums.Tables;
import GUI.commons.Pair;
import db.DatabaseManager;

public class GetSimpleRecordsWorker extends SwingWorker<Pair[], Void> {
	private Tables table;
	private DatabaseManager databaseManager = DatabaseManager.getInstance();
	
	public GetSimpleRecordsWorker(Tables table){
		this.table = table;
	}
	
	@Override
	protected Pair[] doInBackground() throws Exception {
		return databaseManager.executeQueryAndGetValues(table, 3);
	}
}

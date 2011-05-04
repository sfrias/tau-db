package GUI.workers;

import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import Utils.ExecutionResult;
import Utils.Tables;

public class DeleteWorker extends EditAndDeleteGenericWorker {

	public DeleteWorker(Tables table, int recordId, EditAndDeleteGenericCardPanel card){
		super("delete", table, recordId, card);
	}

	@Override
	protected ExecutionResult doInBackground() throws Exception {
		return  databaseManager.executeDelete(table, recordId);
	}
	
}

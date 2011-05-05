package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public class DeleteWorker extends EditAndDeleteGenericWorker {

	public DeleteWorker(Tables table, int recordId, EditAndDeleteGenericCardPanel card){
		super("delete", table, recordId, card);
	}

	@Override
	protected ExecutionResult doInBackground() throws Exception {
		return  databaseManager.executeDelete(table, recordId);
	}
	
}

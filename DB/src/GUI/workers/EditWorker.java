package GUI.workers;

import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;
import Utils.ExecutionResult;
import Utils.Tables;

public class EditWorker extends EditAndDeleteGenericWorker {
	
	public EditWorker(Tables table, String fieldValue, int recordId, EditAndDeleteGenericCardPanel card){
		super("edit", table, fieldValue, recordId, card);
	}

	@Override
	protected ExecutionResult doInBackground() throws Exception {
		return databaseManager.executeUpdate(table, fieldNames, values, recordId);
	}
	
}

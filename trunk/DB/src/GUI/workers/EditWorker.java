package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public class EditWorker extends GenericWorker {
	
	public EditWorker(Tables table, String [] fieldNames, String [] fieldValue, int recordId, 
			EditAndDeleteGenericCardPanel card){
		super("edit", table, fieldNames, fieldValue, recordId, card);
	}

	@Override
	protected ExecutionResult doInBackground() throws Exception {
		return databaseManager.executeUpdate(table, fieldNames, values, recordId);
	}
	
}

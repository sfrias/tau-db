package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public class EditWorker extends GenericEditDeleteWorker {
	
	public EditWorker(Tables table, String [] fieldNames, String [] fieldValue, int recordId, 
			EditAndDeleteGenericCardPanel card){
		super("edit", table, fieldNames, fieldValue, recordId, card);

	}

	@Override
	protected ResultHolder doInBackground() {
		//TODO add exceptions
		ExecutionResult exeResult =  databaseManager.executeUpdate(table, fieldNames, values, recordId);
		ResultHolder result = new ResultHolder(exeResult);
		
		return result;
	}
	
}

package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public class DeleteWorker extends GenericEditDeleteWorker {

	public DeleteWorker(Tables table, int recordId, EditAndDeleteGenericCardPanel card){
		super("delete", table, recordId, card);
	}

	protected ResultHolder doInBackground(){
		//TODO add exceptions
		ExecutionResult exeResult =  databaseManager.executeDelete(table, recordId);
		ResultHolder result = new ResultHolder(exeResult);
		
		return result;
	}
}
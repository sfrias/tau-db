package GUI.workers;

import GUI.panels.Manage.cards.edit.EditSimpleCard;
import dataTypes.ResultHolder;
import enums.ExecutionResult;
import enums.Tables;

public class EditSimpleWorker extends GenericEditDeleteWorker {
	
	public EditSimpleWorker(Tables table, String [] fieldNames, String [] fieldValue, int recordId,  EditSimpleCard card){
		super(table, fieldNames, fieldValue, recordId, card);
	}

	@Override
	protected ResultHolder doInBackground() {
		//TODO add exceptions
		ExecutionResult exeResult =  databaseManager.executeUpdate(table, fieldNames, values, recordId);
		ResultHolder result = new ResultHolder(exeResult);	
		return result;
	}
	
}

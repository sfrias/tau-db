package GUI.workers;

import enums.ExecutionResult;
import enums.Tables;
import GUI.panels.Manage.cards.delete.DeleteCard;
import dataTypes.ResultHolder;

public class DeleteWorker extends GenericEditDeleteWorker {

	public DeleteWorker(Tables table, int recordId, DeleteCard card){
		super(table, recordId, card);
	}

	protected ResultHolder doInBackground(){

		ExecutionResult exeResult =  databaseManager.executeDelete(table, recordId);
		ResultHolder result = new ResultHolder(exeResult);
		
		return result;
	}
}
package GUI.workers;

import dataTypes.ResultHolder;
import enums.ExecutionResult;
import enums.Tables;
import GUI.commons.Pair;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.delete.DeleteCard;
import GUI.panels.Manage.cards.edit.EditSimpleCard;

public class GetSimpleRecordsWorker extends GenericWorker {
	private Tables table;
	
	public GetSimpleRecordsWorker(Tables table, EditSimpleCard card){
		super(card);
		this.table = table;
	}
	
	public GetSimpleRecordsWorker(Tables table, DeleteCard card){
		super(card);
		this.table = table;
	}
	
	@Override
	protected ResultHolder doInBackground(){

		Pair[] pairs =  databaseManager.executeQueryAndGetValues(table);
		ResultHolder result;
		if (pairs != null){
			SimpleModel model = new SimpleModel(pairs);		
			result = new ResultHolder(model,ExecutionResult.Success_Simple_Query);
		}
		else{
			result = new ResultHolder(ExecutionResult.Exception);
		}
		return result;
	}
}

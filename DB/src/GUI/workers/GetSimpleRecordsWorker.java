package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
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
		//TODO add exceptions
		Pair[] pairs =  databaseManager.executeQueryAndGetValues(table);
		SimpleModel model = new SimpleModel(pairs);		
		ResultHolder result = new ResultHolder(model,ExecutionResult.Success_Simple_Query);
		return result;
	}
}

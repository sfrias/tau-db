package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.add.AddCharacters;

public class AddCharWorker extends GenericWorker{
	private String fieldName;
	private String value;
	private Tables table ;
	
	public AddCharWorker(Tables table, String fieldName, String fieldValue, AddCharacters card){
		super("add", card);
		this.fieldName = fieldName;
		this.value = fieldValue;
		this.table = table;
	}

	@Override
	protected ResultHolder doInBackground() throws Exception {
		//TODO Hila: need to make sure we send (maybe if success already query the new and updated list)
		
		ExecutionResult exeRes = databaseManager.executeSimpleInsert(table, fieldName, value);	
		ResultHolder result = new ResultHolder(exeRes);
		return result;
	}

}

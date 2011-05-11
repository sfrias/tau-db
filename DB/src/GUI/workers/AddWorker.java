package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.panels.Manage.cards.add.AddSimpleCard;

public class AddWorker extends GenericWorker{
	
	protected String fieldName;
	protected String value;
	protected Tables table;
	protected int recordId;
			
	public AddWorker(Tables table, String fieldName, String fieldValue, AddSimpleCard card){
		super("add", card);
		this.fieldName = fieldName;
		this.value = fieldValue;
		this.table = table;
	}
	
	@Override
	protected ResultHolder doInBackground() {
		//TODO add excepttions
		ExecutionResult exeRes = databaseManager.executeSimpleInsert(table, fieldName, value);	
		ResultHolder result = new ResultHolder(exeRes);
		
		return result;
	}

}

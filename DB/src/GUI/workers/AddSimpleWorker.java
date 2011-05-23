package GUI.workers;

import tempenums.ExecutionResult;
import tempenums.Tables;
import GUI.panels.Manage.cards.add.AddSimpleCard;

public class AddSimpleWorker extends GenericWorker{
	
	protected String fieldName;
	protected String value;
	protected Tables table;
	protected int recordId;
			
	public AddSimpleWorker(Tables table, String fieldName, String fieldValue, AddSimpleCard card){
		super(card);
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

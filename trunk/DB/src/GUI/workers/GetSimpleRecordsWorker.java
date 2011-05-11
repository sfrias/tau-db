package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.commons.Pair;
import GUI.model.SimpleModel;
import GUI.panels.Manage.cards.EditAndDeleteGenericCardPanel;

public class GetSimpleRecordsWorker extends GenericWorker {
	private Tables table;
	
	public GetSimpleRecordsWorker(Tables table, EditAndDeleteGenericCardPanel card){
		super(card);
		this.table = table;
	}
	
	@Override
	protected ResultHolder doInBackground(){
		//TODO add exceptions
		Pair[] pairs =  databaseManager.executeQueryAndGetValues(table, 3);
		SimpleModel model = new SimpleModel(pairs);		
		ResultHolder result = new ResultHolder(model,ExecutionResult.Success_Simple_Query);
		return result;
	}
}

package GUI.workers;

import Enums.ExecutionResult;
import Enums.Tables;
import GUI.commons.Pair;
import GUI.frames.PlayFrame;
import GUI.model.SimpleModel;

public class GetRecordsByName extends GenericWorker {

	private Tables table;
	private String queryString;
	
	
	public GetRecordsByName(Tables table, PlayFrame playFrame, String queryString){
		super(playFrame);
		
		this.table = table;	
		this.queryString = queryString;
	}
	
	protected ResultHolder doInBackground(){
		//TODO add exceptions
		Pair[] pairs =  databaseManager.executeLimetedQueryAndGetValues(table, 3, queryString);
		SimpleModel model = new SimpleModel(pairs);		
		ResultHolder result = new ResultHolder(model,ExecutionResult.Success_Simple_Query);
		return result;
	}

}

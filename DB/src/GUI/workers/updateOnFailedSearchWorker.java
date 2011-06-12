package GUI.workers;

import dataTypes.ResultHolder;
import enums.ExecutionResult;

public class updateOnFailedSearchWorker extends GenericWorker{
	
	public updateOnFailedSearchWorker(int firstid, int secondid){
		super();
	
	}
	
	protected ResultHolder doInBackground() throws Exception {
		databaseManager.executeUpdateInSuccesRate(false);
		ResultHolder result = new ResultHolder(ExecutionResult.Success_Update_Success_Rate);
		
		return result;
	}

}

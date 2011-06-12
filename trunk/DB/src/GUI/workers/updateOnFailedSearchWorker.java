package GUI.workers;

import dataTypes.ResultHolder;
import enums.ExecutionResult;

public class updateOnFailedSearchWorker extends GenericWorker{
	
	private int first;
	private int second;
	
	public updateOnFailedSearchWorker(int firstid, int secondid){
		super();
		this.first = firstid;
		this.second = secondid;
	
	}
	
	protected ResultHolder doInBackground() throws Exception {
		databaseManager.executeUpdateInSuccesRate(false);
		ResultHolder result = new ResultHolder(ExecutionResult.Success_Update_Success_Rate);
		
		return result;
	}

}

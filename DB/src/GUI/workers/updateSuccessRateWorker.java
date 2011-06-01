package GUI.workers;

import dataTypes.ResultHolder;
import enums.ExecutionResult;

public class updateSuccessRateWorker extends GenericWorker{
	private boolean succeeded;
	
	public updateSuccessRateWorker(boolean succeeded){
		super();
		this.succeeded = succeeded;
	}
	
	protected ResultHolder doInBackground() throws Exception {
		ExecutionResult res = databaseManager.executeUpdateInSuccesRate(succeeded);
		ResultHolder result = new ResultHolder(res);
		
		return result;
	}

}

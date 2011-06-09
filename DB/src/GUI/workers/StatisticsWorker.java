package GUI.workers;

import GUI.model.StatisticsModel;
import GUI.panels.Play.Tabs.StatisticsTab;
import dataTypes.ResultHolder;
import dataTypes.SearchResultObject;
import dataTypes.SuccessRateObject;
import enums.ExecutionResult;

public class StatisticsWorker extends GenericWorker{

	public StatisticsWorker (StatisticsTab statisticTab){
		super(statisticTab);
	}

	protected ResultHolder doInBackground() {
		SearchResultObject[] lastSearches = databaseManager.topSearches("date");
		SearchResultObject[] topSearches = databaseManager.topSearches("count");
		SuccessRateObject successRate = databaseManager.getSuccessRate();
		
		ResultHolder result;
		if (lastSearches == null || topSearches == null || successRate == null){
			result = new ResultHolder(ExecutionResult.Exception);
		} else {
			StatisticsModel model = new StatisticsModel(lastSearches, topSearches, successRate);
			result = new ResultHolder(model, ExecutionResult.Success_Statistics);
		}
		return result;
	}
}

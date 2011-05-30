package GUI.model;

import dataTypes.SearchResultObject;
import dataTypes.SuccessRateObject;

public class StatisticsModel {
	private SearchResultObject[] lastSuccessSearches;
	private SearchResultObject[] topPopularSearches;
	private SuccessRateObject successRate;
	
	public StatisticsModel(SearchResultObject[] lastSuccessSearches, SearchResultObject[] topPopularSearches, SuccessRateObject successRate){
		this.lastSuccessSearches = lastSuccessSearches;
		this.topPopularSearches = topPopularSearches;
		this.successRate = successRate;
	}
	
	public SearchResultObject[] getLastSuccessSearches(){
		return lastSuccessSearches;
	}
	
	public SearchResultObject[] getTopPopularSearches(){
		return topPopularSearches;
	}
	
	public SuccessRateObject getSucessRate(){
		return successRate;
	}

}

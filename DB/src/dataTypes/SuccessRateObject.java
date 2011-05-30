package dataTypes;

public class SuccessRateObject {
	private long totalSearches;
	private long successSearches;
	
	public SuccessRateObject(long totalSearches, long successSearches){
		this.totalSearches = totalSearches;
		this.successSearches = successSearches;		
	}
	
	public long getTotalSearches(){
		return totalSearches;
	}
	
	public long getSuccessSearches(){
		return successSearches;
	}
	
	
}

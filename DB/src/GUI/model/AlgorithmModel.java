package GUI.model;

import enums.ConnectionResult;

public class AlgorithmModel{

	private ConnectionResult connResult;
	private String resultString;
	
	public AlgorithmModel(ConnectionResult connResult, String resultString){
		this.connResult = connResult;
		this.resultString = resultString;
	}

	public ConnectionResult getConnResult() {
		return connResult;
	}

	public String getResultString() {
		return resultString;
	}
}

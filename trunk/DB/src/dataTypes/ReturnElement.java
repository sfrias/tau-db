package dataTypes;

import enums.ConnectionResult;

public class ReturnElement {
	
	private ConnectionResult result;
	private connectionElement[] connectionArray;
	
	
	public ReturnElement(ConnectionResult cr,connectionElement[] connectionArr) {
		result = cr;
		connectionArray = connectionArr;
	}
	
	public ConnectionResult getResult (){
		return result;
	}
	public void setResult (ConnectionResult cr){
		result = cr;
	}
	
	public connectionElement[] getConnectionArray(){
		return connectionArray;
	}
	
	

}

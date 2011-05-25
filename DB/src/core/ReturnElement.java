package core;

import enums.ConnectionResult;

public class ReturnElement {
	
	ConnectionResult result;
	connectionElement[] connectionArray;
	
	
	public ReturnElement(ConnectionResult cr,connectionElement[] connectionArr) {
		result = cr;
		connectionArray = connectionArr;
	}
	
	public ConnectionResult getResult (){
		return result;
	}
	
	public connectionElement[] getConnectionArray(){
		return connectionArray;
	}
	
	

}

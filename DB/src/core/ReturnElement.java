package core;

import enums.ConnectionResult;

public class ReturnElement {
	
	ConnectionResult result;
	charElement connection;
	
	public ReturnElement(ConnectionResult cr, charElement ce) {
		result = cr;
		connection = ce;
	}
	
	public ConnectionResult getResult (){
		return result;
	}
	
	public charElement returnConnection(){
		return connection;
	}
	
	

}

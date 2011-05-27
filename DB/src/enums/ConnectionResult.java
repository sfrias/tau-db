package enums;

public enum ConnectionResult {
	
	Ok,
	Found_Connection(0,"Found connection!"),
	Found_Connection_Of_Length_0(1,"Start character is equal to end character"),
	Did_Not_Find_Connection(2,"Cannot find the desired connection"),
	Close_Exception,
	Exception,
	Exception_In_Success_Rate;


	private int index;
	private String name;
	
	ConnectionResult(int index, String name){
		this.index = index;
		this.name = name;
	}
	
	ConnectionResult(){
		this.index = -1;
		this.name = this.name();

	}
	
	public int getIndex(){
		return index;
	}
	
	public static int getMaxIndex(){
		return 2; 
	}
	
	public String toString(){
		return name;
	}

}

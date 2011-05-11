package GUI.model;

import GUI.commons.Pair;

public class SimpleModel {
	private Pair[] records;
	
	public SimpleModel(Pair[] records){
		this.records = records;
	}
	
	public Pair[] getRecords(){
		return records;
	}
	
	public void setRecords(Pair[] newRecords){
		this.records = newRecords;
	}
}

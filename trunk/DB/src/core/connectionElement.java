package core;

import enums.Tables;

public class connectionElement {
	private String start_name;
	private String end_name;
	private String attribute;
	private String attribute_value;
	
	public connectionElement(String name1, String name2, String atr, String attributeVal){
		start_name=name1;
		end_name=name2;
		attribute = atr;
		if (!atr.equals(Tables.parent.name()) && !atr.equals(Tables.romantic_involvement.name())){
			attribute_value = attributeVal;
		}
		else{
			attribute_value=null;
		}
	}
	public String getStartName(){
		return start_name;
	}
	
	public String getEndName(){
		return end_name;
	}
	
	public String getAttribute(){
		return attribute;
	}
	
	public String getAttributeValue(){
		return attribute_value;
	}
}

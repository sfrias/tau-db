package GUI.dataTypesObjects;

import java.sql.Date;

public class SearchResultObject {
	private String firstChar;
	private String secondChar;
	private Date date;
	private long count;
	
	public SearchResultObject(String firstChar, String secondChar, Date date){
		this(firstChar, secondChar, date, -1);
	}
	
	public SearchResultObject(String firstChar, String secondChar, long count){
		this(firstChar, secondChar, null, count);
	}
	
	private SearchResultObject(String firstChar, String secondChar, Date date, long count){
		this.firstChar = firstChar;
		this.secondChar = secondChar;
		this.date = (date != null ? date : null);
		this.count = (count != -1 ? count : -1);
	}
	
	public String getFirstCharacter(){
		return firstChar;
	}
	
	public String getSecondCharacter(){
		return secondChar;
	}
	
	public Date getDate(){
		return date;
	}
	
	public long getCount(){
		return count;
	}
}

package dataTypes;

import java.sql.Timestamp;

public class SearchResultObject {
	private String firstChar;
	private String secondChar;
	private Timestamp date;
	private long count;
	
	public SearchResultObject(String firstChar, String secondChar, Timestamp date){
		this(firstChar, secondChar, date, -1);
	}
	
	public SearchResultObject(String firstChar, String secondChar, long count){
		this(firstChar, secondChar, null, count);
	}
	
	private SearchResultObject(String firstChar, String secondChar, Timestamp date, long count){
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
	
	public Timestamp getDate(){
		return date;
	}
	
	public String getFormatedDate(){
		return null;
	}
	
	public long getCount(){
		return count;
	}
}

package dataTypes;

public class Pair {
	private String name;
	private int id;
	
	public Pair(String name, int id){
		this.name = name;
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return name;
	}

}

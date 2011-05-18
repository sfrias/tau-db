package GUI.commons;

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
	
	public int getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
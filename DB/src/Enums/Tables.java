package Enums;

public enum Tables {
	characters,
	disease(0, "Disease"),
	organization(1, "Organization"),
	occupation(2, "Occupation"),
	power(3, "Power"),
	school(4, "School"),
	universe(5, "Universe"),
	parent(6, "Parent"),
	romantic_involvement(7, "Romantic Involvement"),
	place_of_birth(8, "Place of Birth"),
	characters_and_disease,
	characters_and_occupation,
	characters_and_organization,
	characters_and_power,
	characters_and_school,
	characters_and_universe;
	
	private int index;
	private String name;
	
	Tables(int index, String name){
		this.index = index;
		this.name = name;
	}
	
	Tables(){
		this.index = -1;
		this.name = this.name();

	}
	
	public int getIndex(){
		return index;
	}
	
	public static int getMaxIndex(){
		return 8; 
	}
	
	public String toString(){
		return name;
	}
}

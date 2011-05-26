package enums;

public enum Tables {
	characters,
	disease(0, "Disease"),
	organization(1, "Organization"),
	occupation(2, "Occupation"),
	power(3, "Power"),
	school(4, "School"),
	universe(5, "Universe"),
	place_of_birth(6, "Place of Birth"),
	parent(7, "Parent"),
	romantic_involvement(8, "Romantic Involvement"),

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
		return 6; 
	}
	
	public String toString(){
		return name;
	}
}

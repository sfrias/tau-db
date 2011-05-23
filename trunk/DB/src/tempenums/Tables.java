package tempenums;

public enum Tables {
	characters,
	disease(0, "Disease"),
	organization(1, "Organization"),
	occupation(2, "Occupation"),
	place_of_birth(3, "Place of Birth"),
	power(4, "Power"),
	school(5, "School"),
	universe(6, "Universe"),
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

package Enums;

public enum Tables {
	characters,
	occupation(5),
//	job(4),
	characters_and_occupation,
	power(7),
	characters_and_power,
	organization(6),
	characters_and_organization,
//	school(9),
//	characters_and_school,
//	gender(3),
//	characters_and_gender,
//	species(10),
//	characters_and_species,
	universe(11),
	characters_and_universe,
//	creator(0),
//	characters_and_creator,
//	rank(8),
//	characters_and_rank,
//	ethnicity(2),
//	characters_and_ethnicity,
//	disease(1),
//	characters_and_disease,
	parent,
//	sibling,
//	marriage,
	romantic_involvement,
	location,
	place_of_birth;
	
	private int index;
	
	Tables(int index){
		this.index = index;
	}
	
	Tables(){
		this.index = -1;
	}
	
	public int getIndex(){
		return index;
	}
	
	public static int getMaxIndex(){
		return 12; 
	}
}

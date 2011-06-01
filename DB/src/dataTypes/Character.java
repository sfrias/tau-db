package dataTypes;


public class Character{
	private int[] disease;
	private int[] occupation;
	private int[] organization;
	private int[] power;
	private int[] place_of_birth;
	private int[] school;
	private int[] universe;
	
	private int charId;
	
	public Character(int charId){
		this.charId = charId;
	}
	
	public int[] getDiseases(){
		return disease;
	}
	
	public int[] getOccupations(){
		return occupation;
	}
	
	public int[] getOrganiztions(){
		return organization;
	}
	
	public int[] getPowers(){
		return power;
	}
	
	public int[] getPlaceOfBirth(){
		return place_of_birth;
	}
	
	public int[] getSchools(){
		return school;
	}
	
	public int[] getUniverses(){
		return universe;
	}
	
	public void setDiseases(int[] diseases){
		this.disease = diseases;
	}
	
	public void setOccupations(int[] occupation){
		this.occupation = occupation;
	}
	
	public void setOrganizations(int[] organization){
		this.organization = organization;
	}
	
	public void setPowers(int[] powers){
		this.power = powers;
	}
	
	public void setPlaceOfBirth(int[] placeOfBirth){
		this.place_of_birth = placeOfBirth;
	}
	
	public void setSchools(int[] schools){
		this.school = schools;
	}
	
	public void setUniverses(int[] universes){
		this.universe = universes;
	}
	
	public int getCharId(){
		return charId;
	}
}

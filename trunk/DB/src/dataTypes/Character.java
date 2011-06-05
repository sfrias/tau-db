package dataTypes;

import java.util.HashSet;

import enums.Tables;


public class Character{
	private HashSet<Integer> disease;
	private HashSet<Integer> occupation;
	private HashSet<Integer> organization;
	private HashSet<Integer> power;
	private HashSet<Integer> place_of_birth;
	private HashSet<Integer> school;
	private HashSet<Integer> universe;
	
	private int charId;
	private String charName;
	
	public Character(int charId, String charName){
		this.charId = charId;
		this.charName = charName;
	}
	
	public HashSet<Integer> getDiseases(){
		return disease;
	}
	
	public HashSet<Integer> getOccupations(){
		return occupation;
	}
	
	public HashSet<Integer> getOrganiztions(){
		return organization;
	}
	
	public HashSet<Integer> getPowers(){
		return power;
	}
	
	public HashSet<Integer> getPlaceOfBirth(){
		return place_of_birth;
	}
	
	public HashSet<Integer> getSchools(){
		return school;
	}
	
	public HashSet<Integer> getUniverses(){
		return universe;
	}
	
	public void setDiseases(HashSet<Integer> diseases){
		this.disease = diseases;
	}
	
	public void setOccupations(HashSet<Integer> occupation){
		this.occupation = occupation;
	}
	
	public void setOrganizations(HashSet<Integer> organization){
		this.organization = organization;
	}
	
	public void setPowers(HashSet<Integer> powers){
		this.power = powers;
	}
	
	public void setPlaceOfBirth(HashSet<Integer> placeOfBirth){
		this.place_of_birth = placeOfBirth;
	}
	
	public void setSchools(HashSet<Integer> schools){
		this.school = schools;
	}
	
	public void setUniverses(HashSet<Integer> universes){
		this.universe = universes;
	}
	
	public int getCharId(){
		return charId;
	}
	
	public String getCharName(){
		return charName;
	}
	
	public HashSet<Integer> getValueByAttribute(String attribute){

		if (attribute.equals(Tables.disease.name())){
			return getDiseases();
		}
		else if (attribute.equals(Tables.occupation.name())){
			return getOccupations();
		}
		else if (attribute.equals(Tables.organization.name())){
			return getOrganiztions();
		}
		else if (attribute.equals(Tables.power.name())){
			return getPowers();
		}
		
		else if (attribute.equals(Tables.place_of_birth.name())){
			return getPlaceOfBirth();
		}
		
		else if (attribute.equals(Tables.school.name())){
			return getSchools();
		}
		else if (attribute.equals(Tables.universe.name())){
			return getUniverses();
		}
		else{
			return null;
		}
	}
}

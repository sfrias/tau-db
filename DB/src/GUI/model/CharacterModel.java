package GUI.model;

import Enums.Tables;
import GUI.commons.Pair;

public class CharacterModel{

	private Pair[][] attArr = new Pair[Tables.getMaxIndex()+1][];
	
	public CharacterModel(){
		for (int i=0; i<Tables.getMaxIndex(); i++){
			attArr[i]=null;
		}
	}
	
	public Pair[] getAttributePairs(Tables attribute){
		return attArr[attribute.getIndex()];
	}
	
	public Pair[] getAttributePairs(int i){
		return attArr[i];
	}
	
	public void setAttributePairs(Pair[] pairs, Tables attribute){
		attArr[attribute.getIndex()] = pairs;
	}
	
	public boolean isAtrributeModified(int i){
		return attArr[i] != null;
	}
	
	public void resetAttributeCell(int i){
		attArr[i] = null;
	}

}

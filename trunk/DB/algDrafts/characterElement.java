package Utils;

import java.util.LinkedList;

public class characterElement {
	int characterId;
	int prevId;
	LinkedList<Integer> connectedCharacters;
	LinkedList<Short> connectedAttribute;
	LinkedList<Integer> attributeValues;
	
	public characterElement(int id, int previd){
		this.characterId=id;
		this.prevId=previd;
		this.connectedCharacters = new LinkedList<Integer>();
		this.connectedAttribute = new LinkedList<Short>();
		this.attributeValues = new LinkedList<Integer>();
	}
}

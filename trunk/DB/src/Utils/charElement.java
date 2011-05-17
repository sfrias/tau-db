package Utils;

import java.util.LinkedList;

public class charElement {
	int characterId;
	charElement prevElement;
	Short connectedAttribute;
	Integer attributeValue;
	
	public charElement(int id, charElement prev){
		this.characterId=id;
		this.prevElement = prev;
		this.connectedAttribute=-1;
		this.attributeValue=-1;

	}
}

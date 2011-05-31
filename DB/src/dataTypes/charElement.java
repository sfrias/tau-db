package dataTypes;


public class charElement {
	private int characterId;
	private charElement prevElement;
	private Short connectedAttribute;
	private Integer attributeValue;
	
	public charElement(int id, charElement prev){
		this.setCharacterId(id);
		this.setPrevElement(prev);
		this.setConnectedAttribute((short)-1);
		this.setAttributeValue(-1);

	}

	public void setCharacterId(int characterId) {
		this.characterId = characterId;
	}

	public int getCharacterId() {
		return characterId;
	}

	public void setPrevElement(charElement prevElement) {
		this.prevElement = prevElement;
	}

	public charElement getPrevElement() {
		return prevElement;
	}

	public void setConnectedAttribute(short i) {
		this.connectedAttribute = i;
	}

	public Short getConnectedAttribute() {
		return connectedAttribute;
	}

	public void setAttributeValue(Integer attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Integer getAttributeValue() {
		return attributeValue;
	}
	
	
}

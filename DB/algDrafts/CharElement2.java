package Utils;

public class CharElement2 {
	int characterId;
	CharElement2 prevElement;
	Short connectedAttribute;
	Integer attributeValue;
		
	public CharElement2(int id, CharElement2 prev){
		this.characterId=id;
		this.prevElement = prev;
		this.connectedAttribute=-1;
		this.attributeValue=-1;
	}
}

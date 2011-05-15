package Utils;

public class ConnectionElement {

	ConnectionElement next;
	ConnectionElement prev;
	int start_id;
	int end_id;
	String attribute;
	int attributeValueId;
	
	public ConnectionElement(int start_id, int end_id, String attribute, int atrValue){
		this.start_id=start_id;
		this.end_id = end_id;
		this.attribute= attribute;
		this.attributeValueId = atrValue;
		this.next = null;
		this.prev = null;
	}
	
	public static ConnectionElement first(ConnectionElement con){
		ConnectionElement first = con;
		while (first.prev != null){
			first = first.prev;
		}
		return first;
	}

}

package Utils;


public class ConnectionElement {

	ConnectionElement next;
	ConnectionElement prev;
	int start_id;
	int end_id;
	String attribute;
	int attributeValueId;
	
	//DEBUG
//	static HashSet<Integer> usedStartID = new HashSet<Integer>();
	
	public ConnectionElement(int start_id, int end_id, String attribute, int atrValue){
		//DEBUG:
	/*	if(usedStartID.contains(start_id))
			System.out.println("double-creation:" + start_id + " to " + end_id + " with " +attribute);
		else
			usedStartID.add(start_id);
			*/
				
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

package Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.JDCConnection;

public class algorithm {

	private DatabaseManager dbManager = DatabaseManager.getInstance();
	private JDCConnection conn;
	Tables[] tbs;
	
	public int attributes = 20;

	public algorithm(){
		conn = dbManager.getConnection() ;
		tbs = Tables.values();
	}
	
	public JDCConnection getConnention(){
		return conn;
	}
	
	boolean connectionFinder(String[] fill, int start_id,int end_id, int recPhase, int prevId,boolean firstRun) throws SQLException{
		
		
		ResultSet atrValRS,charsWithAtrRS=null,unspecifiedRS = null;

		boolean resultFlag=false;
		
		String 	currentAtr, joinedAtr;
		
		int currentid,unspecifiedId=0;
		
		//Preparing statement of a specific attribute
		String selectAtrValues;
		
		//Preparing statement of all characters with the same attribute above 		
		String charactersWithAtr;
	
		//too many phases are taken without finding any connection
		if (recPhase>6){
			return false;
		}
		
		//prepare two main statements
		PreparedStatement atrStmt= null; 
		PreparedStatement charAtrStmt = null; 
		
		Statement unspecifiedStmt;
				
		//running on all attributes
		for (int atr=1; atr<attributes; atr=atr+2){
			System.out.println("trying "+ tbs[atr].toString() + "in phase " + recPhase);
			currentAtr = tbs[atr].toString();
			joinedAtr = tbs[atr+1].toString();
				
			selectAtrValues = 	"SELECT " + joinedAtr + "_" + currentAtr+ "_id"  + 
								" FROM " + joinedAtr +"," + currentAtr +
								" WHERE " +joinedAtr + "_character_id = ? AND " + 
								joinedAtr + "_" + currentAtr + "_id =" + currentAtr + "_id";
			
			atrStmt = conn.prepareStatement(selectAtrValues);

		
			try {
				//getting all the ids of the attributes that the character has
				atrStmt.setInt(1, start_id);
				atrValRS = atrStmt.executeQuery();
				
				unspecifiedStmt= conn.createStatement();
				unspecifiedRS = unspecifiedStmt.executeQuery("SELECT " + currentAtr + "_id FROM " + currentAtr + " WHERE " + currentAtr + "_name LIKE 'Unspecified'");
				unspecifiedRS.first();
				unspecifiedId = unspecifiedRS.getInt(1);
				
				
				while (atrValRS.next()){ //all attributes
					
					if (atrValRS.getInt(1)== unspecifiedId ) { //not relevant 
						continue;
					}
					
					//taking all characters with the same attribute as our character
									
					charactersWithAtr = 	"SELECT "+joinedAtr+"_character_id " +
											"FROM " + joinedAtr +  
											" WHERE " + joinedAtr + "_" + currentAtr + "_id = ? AND " +
											joinedAtr +  "_character_id != ? AND " +
											joinedAtr +  "_character_id != ?";
					
					charAtrStmt =  conn.prepareStatement(charactersWithAtr);
					charAtrStmt.setInt(1, atrValRS.getInt(1));
					charAtrStmt.setInt(2, prevId);
					charAtrStmt.setInt(3, start_id);
					charsWithAtrRS = charAtrStmt.executeQuery();
			
					//looking for a connection via the specific attribute
					while (charsWithAtrRS.next()){
						currentid=charsWithAtrRS.getInt(1);
						if (firstRun){ //running over all attributes in order to find a direct connection
							if (currentid == end_id) {
								resultFlag=true;
								fill[recPhase-1]=currentid + "," + currentAtr;
								break;
							}
						}
						else {
							// starting a recursive call with each character with the same attribute as the start_id
							fill[recPhase-1]= currentid + "," + currentAtr;
							if (connectionFinder(fill, currentid, end_id, recPhase+1, start_id, true)){
								resultFlag= true;
								break;
							}
						}
					}
					
					//found a connection
					if (resultFlag){
						break;
					}
				}

				//closing all open statements and result sets
				if (atrValRS != null) atrValRS.close();
				if (charsWithAtrRS!=null)charsWithAtrRS.close();
				if (atrStmt!=null) atrStmt.close();
				if (charAtrStmt != null) charAtrStmt.close();
				if (unspecifiedStmt != null) unspecifiedStmt.close();
				if (unspecifiedRS != null) unspecifiedRS.close();
				
			}catch (SQLException e) {
				System.out.println("error execute query-" + e.toString());
			}
			
			if (resultFlag){
				break;	
			}

		}
		if (firstRun && !resultFlag){
			 return connectionFinder(fill,start_id,end_id,recPhase,prevId,!firstRun);
		}
		
		return resultFlag;
	}
	
	
	
	
	private int getIdOfCharacter(String name) throws SQLException{
		
		int id=0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT character_id FROM characters WHERE character_name='" + name +"'");
			rs.first();
			id = rs.getInt("character_id");
			rs.close(); 
			stmt.close();
			}
			catch (SQLException e) {
				System.out.println("error execute query-" + e.toString());
			}
		return id;
		
	}
	
	private void mergeConnection (String[] connArr){
		String[] valueArrfirst = new String[2];
		String[] valueArrSecond = new String[2];
		for (int i=0; i+1< connArr.length; i++){
			if (connArr[i] != null && connArr[i+1] != null) {
				valueArrfirst = connArr[i].split(",");
				valueArrSecond = connArr[i+1].split(",");
				if (valueArrfirst[1].equals(valueArrSecond[1])) {
					connArr[i]="merged";
				}
			}
			else {
				break;
			}
		}
	}
	
	private void getNameAndPrintConnections(String[] connArr, String startName) throws SQLException{
		
		String currentName="";
		String[] valueArr = new String[2];

		try {
			
			for (int i=0; i< connArr.length; i++){
					Statement stmt = conn.createStatement();
					if (connArr[i] != null ) {
						if (!connArr[i].equals("merged")){
							valueArr = connArr[i].split(",");
							ResultSet rs = stmt.executeQuery("SELECT character_name FROM characters WHERE character_id='" + valueArr[0] +"'");
							rs.first();
							currentName = rs.getString("character_name");
							rs.close(); 
							stmt.close();	
							System.out.println(startName + " to " +currentName + " with connectcted by " + valueArr[1] + "\n" );
							startName=currentName;
						}
						else {
							continue;
						}
					}
					else {
						break;
					}
			}
			
			}
			catch (SQLException e) {
				System.out.println("error execute query-" + e.toString());
			}		
	}


	boolean lookForConnection(String name1, String name2) throws SQLException {

		if (name1.equals(name2)){
			System.out.println("match of length 0");
			return true;
		}


		String[] connections = new String[6];
		
		int start_id = getIdOfCharacter(name1);
		int end_id = getIdOfCharacter(name2);
		//trying to find first connection
		
		if (connectionFinder(connections, start_id, end_id, 1, start_id, true)){
				System.out.println("Match found between "+ name1 +" and "+ name2);
				mergeConnection(connections);
				getNameAndPrintConnections(connections,name1);
				return true;
		}
		else{
			System.out.println("couldn't find a connection");
			return false;
		}

	}
	
	public static void main(String[] args) throws SQLException{
		algorithm a = new algorithm();

		long startTime = System.currentTimeMillis();
		a.lookForConnection("Nathaniel Roberts", "Liz Burton");
		long finishTime = System.currentTimeMillis();		
		a.getConnention().close();
		System.out.println("closed " + (finishTime - startTime));
	}

	
}

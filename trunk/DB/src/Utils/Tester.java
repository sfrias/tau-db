package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import Connection.JDCConnection;

public class Tester {

	private static int loop_limit = 10;
	
	public static void tester () throws SQLException  {
		
		noEndAlg a = new noEndAlg();
		algorithmUtils.prepareTablesAndHashMaps(a);
		Statement stmt = null;
		ResultSet rs = null;
		
		int success = 0, failure = 0, currentCounter = 0;
		boolean found;
		JDCConnection conn = a.getConnention();
		stmt = conn.createStatement();
		 
		for (int i=1; i<loop_limit; i++) {
			for (int j=i+1; j<loop_limit; j++) {
				long start = System.currentTimeMillis();
				found = a.lookForConnection (i,j);
				long finish = System.currentTimeMillis();
				long total = finish-start;
				long searchTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(total);
			//	if (searchTimeInSeconds<1) {
			//		searchTimeInSeconds = 1;
			//	}
				String time = String.format("%d min, %d sec",      TimeUnit.MILLISECONDS.toMinutes(total),     TimeUnit.MILLISECONDS.toSeconds(total) -      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)) );     
			    System.out.println("the search between " + i + " and " + j + " took total time of " + total +"\n" + time + "\n");
			    //stmt.executeUpdate("UPDATE statistic SET time = " + searchTimeInSeconds + " WHERE character_id1 = " + i + " AND character_id2 = " + j);
			        if (found) {
			        	success++;
			        	algorithmUtils.insertIntoStatisticTable(i, j, a.getGlobalNumOfConnections() , searchTimeInSeconds, conn);
			        }
			        else {
			        	failure++;
			        	algorithmUtils.insertIntoStatisticTable(i, j, -1 , searchTimeInSeconds, conn);
			        }
			}
		}
		
		System.out.println("seccess is: " + success);
		System.out.println("failure is: " + failure);
		
		try {
			
			
			//k start from 1 because the tester does try connection from character to himself, and connection from length -1 (mean no connection) we get from "failure"
			for (int k=1; k<4; k++){
				rs = stmt.executeQuery("SELECT count(*) FROM statistic WHERE num_of_connections = " + k);
				rs.first();
				currentCounter = rs.getInt(1);
				System.out.println("there are " + currentCounter + " connection from length " + k);
				}
				
			// calculates avg time 
			rs = stmt.executeQuery("SELECT avg(time) FROM statistic WHERE num_of_connections = 1");
			rs.first();
			System.out.println("the avg time of 1 is " + rs.getLong(1));
			
			rs = stmt.executeQuery("SELECT avg(time) FROM statistic WHERE num_of_connections = 2");
			rs.first();
			System.out.println("the avg time of 2 is " + rs.getLong(1));
			
			rs = stmt.executeQuery("SELECT avg(time) FROM statistic WHERE num_of_connections = 3");
			rs.first();
			System.out.println("tthe avg time of 3 is " + rs.getLong(1));
			
			rs = stmt.executeQuery("SELECT avg(time) FROM statistic WHERE num_of_connections = -1");
			rs.first();
			System.out.println("the avg time of -1 is " + rs.getLong(1));
			
			rs = stmt.executeQuery("SELECT max(time) FROM statistic");
			rs.first();
			System.out.println("the max time is " + rs.getLong(1));
			
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

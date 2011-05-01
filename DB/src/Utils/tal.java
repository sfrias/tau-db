package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.datatype.DatatypeFactory;

public class tal {
	private static final String CREATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";
	private DatabaseManager dbManager = DatabaseManager.getInstance();
	private JDCConnection conn;
	Tables[] tbs;

	public tal(){
		conn = dbManager.getConnection() ;
		tbs = Tables.values();
	}
	
	public JDCConnection getConnention(){
		return conn;
	}
		
	public 
	static void fillTables() throws IOException {

		File sqlFile = new File(CREATE_TABLES_SQL_FILE_PATH);
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.append("INSERT INTO students (student_id, student_name) values('100','Tammy');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO students (student_id, student_name) values('101','Amiko');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO students (student_id, student_name) values('110','Hila');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO students (student_id, student_name) values('111','Tal');\n");
		bufferedWriter.flush();

		bufferedWriter.append("INSERT INTO courses (course_id, course_name) values('10','a');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO courses (course_id, course_name) values('11','b');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO courses (course_id, course_name) values('12','c');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO courses (course_id, course_name) values('13','d');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO courses (course_id, course_name) values('14','e');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO courses (course_id, course_name) values('15','f');\n");
		bufferedWriter.flush();

		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('100','10');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('100','11');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('100','13');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('111','12');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('111','14');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('111','15');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('101','10');\n");
		bufferedWriter.flush();
		//bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('101','12');\n");
		//bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn (student_id ,course_id) values('110','15');\n");
		bufferedWriter.flush();

		bufferedWriter.append("INSERT INTO cities (city_id ,city_name) values('1','Tel-aviv');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO cities (city_id ,city_name) values('2','Herzelia');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO cities (city_id ,city_name) values('3','Hulon');\n");
		bufferedWriter.flush();

		bufferedWriter.append("INSERT INTO conn2 (student_id ,city_id) values('100','1');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn2 (student_id ,city_id) values('101','2');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn2 (student_id ,city_id) values('110','3');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn2 (student_id ,city_id) values('111','3');\n");
		bufferedWriter.flush();

		bufferedWriter.append("INSERT INTO colors (color_id ,color_name) values('1','Blue');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO colors (color_id ,color_name) values('2','Red');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO colors (color_id ,color_name) values('3','Pink');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO colors (color_id ,color_name) values('4','Green');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO colors (color_id ,color_name) values('5','Yellow');\n");
		bufferedWriter.flush();

		bufferedWriter.append("INSERT INTO conn3 (student_id ,color_id) values('100','3');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn3 (student_id ,color_id) values('100','2');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn3 (student_id ,color_id) values('101','5');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn3 (student_id ,color_id) values('110','1');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn3 (student_id ,color_id) values('110','4');\n");
		bufferedWriter.flush();
		bufferedWriter.append("INSERT INTO conn3 (student_id ,color_id) values('111','5');\n");
		bufferedWriter.flush();

	}


	boolean helper(String[] fill, String name1,String name2,String[] arr, int length,int id, int prevId,int i, boolean rec, String firstName) {

		Statement stmt;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String s;
		
		if (tbs[i].toString().equals("diseases"))
			s = "disease";
		else if (tbs[i].toString().equals("powers"))
			s = "power";
		else
			s = tbs[i].toString();
			
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT " + s + "_id, " + s + "_name" + " FROM " + tbs[i+1].toString()+ ", " + tbs[i].toString()+ " WHERE "+ tbs[i+1].toString() + "_character_id= " + id +" AND " + tbs[i+1].toString() + "_" + s + "_id" +" = " + s + "_id");

			while (rs.next()==true){
				if (rs.getString(2).equals("Unspecified"))
					break;
				stmt1 = conn.createStatement();
				rs1 = stmt1.executeQuery("SELECT "+ tbs[i+1].toString() + "_character_id"+ " FROM " + tbs[i+1].toString()+ " WHERE "+ tbs[i+1].toString() + "_" + s + "_id"+ " = " + rs.getInt(1) + " AND " + tbs[i+1].toString() + "_character_id"+ "!= "+id +" AND " + tbs[i+1].toString() + "_character_id"+ "!= " +prevId);
				while (rs1.next()==true) {
					stmt2 = conn.createStatement();
					rs2 = stmt2.executeQuery("SELECT character_name FROM characters WHERE character_id = " + rs1.getInt(1));
					rs2.first();
					fill[0] = rs2.getString("character_name");
					arr[length-1]= name1 + " has the same " + s + " as " + fill[0];
					if (rec==false) {
						if (fill[0].equals(name2)) {
							for (int index= 0; index<length; index++)
								System.out.println(arr[index]);
							rs.close();
							rs1.close();
							stmt.close();
							stmt1.close();
							rs2.close();
							stmt2.close();
							return true;
						}
					}
					else {
							rs.close();
							rs1.close();
							stmt.close();
							stmt1.close();
							rs2.close();
							stmt2.close();
							return true;	
					}


				}
			}

			if (rs2!=null)
				rs2.close();
			if (stmt2!=null)
				stmt2.close();
			rs.close();
			if (rs1!=null)
				rs1.close();
			stmt.close();
			if (stmt1!=null)
				stmt1.close();

		} catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}

		return false;
	}

	boolean lookForConnection(String name1, String name2,int length, int prevId, String firstName,String[] arr) {

		if (length==6) 
			return false;
		if (name1.equals(name2)){
			System.out.println("match of length 0");
			return true;
		}

		Statement stmt;
		ResultSet rs;
		String[] arr1 = new String[1];
		//Tables[] tbs = Tables.values();
		int id =0;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT characters.character_id FROM characters WHERE characters.character_name='" + name1+"'");
			rs.first();
			id = rs.getInt("character_id");
			if (prevId==0)
				prevId=id;
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}

		
		for (int i=1; i<22; i=i+2){
			System.out.println("trying "+ tbs[i].toString());
			if (helper(arr1,name1, name2, arr, length,id, prevId,i,false,firstName)==true) {
				System.out.println("Match found between "+ firstName +" and "+ name2 + " in "+ length + " steps");
				return true;
			}
		}
		
		for (int i=1; i<22; i=i+2){
			System.out.println("trying recursive "+ tbs[i].toString());
			if (helper(arr1,name1, name2,arr, length,id, prevId,i,true,firstName)==true) {
				if (lookForConnection(arr1[0], name2,length+1,id,firstName,arr)==true)
					return true;
			}
		}
			
		return false;

	}

}

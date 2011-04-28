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

public class tal {
	private static final String CREATE_TABLES_SQL_FILE_PATH = "sql/mysql/populate-tables.sql";
	private Connection conn;
	
	public tal(){
		conn = null;
	}
	
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
	
	void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ubable to load the driver");
		}
		
		System.out.println("driver loaded successfully");
		System.out.println("trying to connent...");
		try {
			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb",
					"root" , "6387");
		} catch (SQLException e) {
			System.out.println("ubable to connect- " + e.toString());
		}
		System.out.println("connected");
	
	}
	
	
	void closeconnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("unable to close the connection- " + e.toString());
		}
		System.out.println("connection closed");
	}
	

	boolean helper(String[] arr1, String name1,String name2, String toPrint,String[] arr, int length,String id, boolean rec) {
		
		Statement stmt;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		try {
		stmt = conn.createStatement();
		rs = stmt.executeQuery(arr1[0]);
		
		//while (rs.next()==true){
			rs.first();
			stmt1 = conn.createStatement();
			rs1 = stmt1.executeQuery(arr1[1] + rs.getInt(id) + arr1[2]);
				while (rs1.next()==true) {
					stmt2 = conn.createStatement();
					rs2 = stmt2.executeQuery(arr1[3] + rs1.getInt("student_id") );
					rs2.first();
						arr1[4] = rs2.getString("student_name");
						arr[length-1]= name1 + toPrint + arr1[4];
						if (rec==false) {
							if (arr1[4].equals(name2)) {
								int i=0;
								while (arr[i]!=null) {
									System.out.println(arr[i]);
									i++;
								}
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
		//}
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
			
		Statement stmt3;
		ResultSet rs3;
		String[] arr1 = new String[5];
		int id =0;
		
		try {
			stmt3 = conn.createStatement();
			rs3 = stmt3.executeQuery("SELECT students.student_id FROM students WHERE students.student_name='" + name1+"'");
			rs3.first();
			id = rs3.getInt("student_id");
			if (prevId==0)
				prevId=id;
			rs3.close();
			stmt3.close();
		} catch (SQLException e) {
			System.out.println("error execute query-" + e.toString());
		}
		
			
		//1
		arr1[0] = "SELECT DISTINCT conn.course_id FROM conn, students, courses WHERE students.student_name='" + name1 + "'" +" AND students.student_id= conn.student_id";
		arr1[1]= "SELECT conn.student_id FROM conn WHERE conn.course_id= ";
		arr1[2]= " AND conn.student_id != "+id +" AND conn.student_id!= " +prevId;
		arr1[3] = "SELECT students.student_name FROM students WHERE students.student_id = ";
		if (helper(arr1, name1, name2, " takes the same course as " , arr, length, "course_id",false)==true) {
			System.out.println("Match found between "+ firstName +" and "+ name2 + " in "+ length + " steps");
			return true;
			}
			
		//2
		arr1[0] = "SELECT DISTINCT conn2.city_id FROM conn2, students, cities WHERE students.student_name='" + name1 + "'" +" AND students.student_id= conn2.student_id";
		arr1[1]= "SELECT conn2.student_id FROM conn2 WHERE conn2.city_id= ";
		arr1[2]= " AND conn2.student_id != "+id +" AND conn2.student_id!= " +prevId;
		arr1[3] = "SELECT students.student_name FROM students WHERE students.student_id = ";
		if (helper(arr1, name1, name2, " lives in the same city as " , arr, length, "city_id",false)==true) {
			System.out.println("Match found between "+ firstName +" and "+ name2 + " in "+ length + " steps");
			return true;
		}
			
		//3
		arr1[0] = "SELECT DISTINCT conn3.color_id FROM conn3, students, colors WHERE students.student_name='" + name1 + "'" +" AND students.student_id= conn3.student_id";
		arr1[1]= "SELECT conn3.student_id FROM conn3 WHERE conn3.color_id= ";
		arr1[2]= " AND conn3.student_id != "+id +" AND conn3.student_id!= " +prevId;
		arr1[3] = "SELECT students.student_name FROM students WHERE students.student_id = ";
		if (helper(arr1, name1, name2, " likes the same color as " , arr, length, "color_id",false)==true) {
			System.out.println("Match found between "+ firstName +" and "+ name2 + " in "+ length + " steps");
			return true;
		} 
		
		//4
		arr1[0] = "SELECT DISTINCT conn.course_id FROM conn, students, courses WHERE students.student_name='" + name1 + "'" +" AND students.student_id= conn.student_id";
		arr1[1]= "SELECT conn.student_id FROM conn WHERE conn.course_id= ";
		arr1[2]= " AND conn.student_id != "+id +" AND conn.student_id!= " +prevId;
		arr1[3] = "SELECT students.student_name FROM students WHERE students.student_id = ";
		if (helper(arr1, name1, name2, " takes the same course as " , arr, length, "course_id",true)==true) {
			if (lookForConnection(arr1[4], name2,length+1,id,firstName,arr)==true)
				return true;
		}
			
		//5
		arr1[0] = "SELECT DISTINCT conn2.city_id FROM conn2, students, cities WHERE students.student_name='" + name1 + "'" +" AND students.student_id= conn2.student_id";
		arr1[1]= "SELECT conn2.student_id FROM conn2 WHERE conn2.city_id= ";
		arr1[2]= " AND conn2.student_id != "+id +" AND conn2.student_id!= " +prevId;
		arr1[3] = "SELECT students.student_name FROM students WHERE students.student_id = ";
		if (helper(arr1, name1, name2, " lives in the same city as " , arr, length, "city_id",true)==true) {
			if (lookForConnection(arr1[4], name2,length+1,id,firstName,arr)==true)
				return true;
		}
		
		//6
		arr1[0] = "SELECT DISTINCT conn3.color_id FROM conn3, students, colors WHERE students.student_name='" + name1 + "'" +" AND students.student_id= conn3.student_id";
		arr1[1]= "SELECT conn3.student_id FROM conn3 WHERE conn3.color_id= ";
		arr1[2]= " AND conn3.student_id != "+id +" AND conn3.student_id!= " +prevId;
		arr1[3] = "SELECT students.student_name FROM students WHERE students.student_id = ";
		if (helper(arr1, name1, name2, " likes the same color as " , arr, length, "color_id",true)==true) {
			if (lookForConnection(arr1[4], name2,length+1,id,firstName,arr)==true)
				return true;
		} 
			
		return false;
		
	}
	
}

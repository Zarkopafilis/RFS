package com.rfslabs.rfsroles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SQLHandler {

	public static Connection con;
	
	public static int getKarma(UUID id){
		
		int a = 0;
		try{
			

		Statement s = con.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT karma FROM roles WHERE uuid='" + id.toString() +"'");
		
		
		if(rs.next()){
			a = rs.getInt("karma");
		}
		
		s.close();
		rs.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}
	
	public static void setKarma(UUID id , int karma){
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE roles SET karma="+ karma + " WHERE uuid='" + id.toString() + "' ");
			
			s.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static int getLevel(UUID id){
		
		int a = 0;
		try{
			

		Statement s = con.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT level FROM roles WHERE uuid='" + id.toString() +"'");
		
		
		if(rs.next()){
			a = rs.getInt("level");
		}
		
		s.close();
		rs.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}
	
	public static void setLevel(UUID id , int level){
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE roles SET level="+ level + " WHERE uuid='" + id.toString() + "' ");
			
			s.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static String getRole(UUID id){
		
		String a = "err";
		try{
			

		Statement s = con.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT role FROM roles WHERE uuid='" + id.toString() +"'");
		
		
		if(rs.next()){
			a = rs.getString("role");
		}
		
		s.close();
		rs.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}
	
	public static void setRole(UUID id , String role){
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE roles SET role='"+ role + "' WHERE uuid='" + id.toString() + "' ");
			
			s.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void setExperience(UUID id , int xp){
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE roles SET xp="+ xp + " WHERE uuid='" + id.toString() + "' ");
			
			s.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static int getExperience(UUID id) {
		
		int a = 0;
		try{		

		Statement s = con.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT xp FROM roles WHERE uuid='" + id.toString() +"'");
		
		
		if(rs.next()){
			a = rs.getInt("xp");
		}
		
		s.close();
		rs.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	public static void init(){
		
		try {
			Statement s = con.createStatement();
			
			s.execute("CREATE TABLE IF NOT EXISTS roles("
					+ "uuid VARCHAR(45),"
					+ "role VARCHAR(20),"
					+ "level INTEGER(20),"
					+ "karma INTEGER(20),"
					+ "xp INTEGER(10)"
					+ ")");
			
			s.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void proccessPlayer(UUID id , String name){//try register+load 
	
		try {
			boolean indb = true;
			
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT uuid FROM roles WHERE uuid='" + id.toString() +"'");
			
			if(!rs.next()){
				indb = false;
				//registerPlayer(id);
			}
			
			s.close();
			rs.close();

			if(!indb){
			registerPlayer(id);
			}
			
			/*s = con.createStatement();
			
			rs = s.executeQuery("SELECT * FROM roles WHERE uuid='" + id.toString() + "'");
			
			if(rs.next()){
				//TODO
			}
			
			rs.close();
			s.close();*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	
	
	private static void registerPlayer(UUID id){
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("INSERT INTO roles VALUES('" + id.toString() + "' , 'none' , 1, 0 , 0)");
			
			s.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private static void registerPlayer(UUID id , int level){
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("INSERT INTO roles VALUES('" + id.toString() + "' , 'none' , " + level + ", 0 , 0)");
			
			s.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	
		
}

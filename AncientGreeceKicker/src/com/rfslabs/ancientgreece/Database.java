package com.rfslabs.ancientgreece;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class Database {

	public static Connection con;
	
	public static void init(){
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3006/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean ancientPointThing(UUID id){
		check();
		try {
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT ancientPoints FROM onlinestuff WHERE name='" + id.toString() + "'");
			if(rs.next()){
				
				int pts = rs.getInt("ancientPoints");
				rs.close();
				s.close();
				
				if(pts > 0){
					
				s = con.createStatement();
				s.executeUpdate("UPDATE onlinestuff SET ancientPoints=" + (pts - 1) + " WHERE name='" + id.toString() + "'");
				s.close();
				
				return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public static boolean check(){
		try{
		if(!con.isValid(5)){
			con.close();
			init();
			return false;
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
		return true;
	}

	public static void deconstruct() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}

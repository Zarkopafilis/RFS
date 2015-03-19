package com.rfslabs.lobby.kitpvp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


public class PointsLink {

	public static Connection con;
	
	public static void init(){
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static int getPoints(UUID id){
		int a = 0;
		try {
			Statement s = con.createStatement();
			
			a = s.executeQuery("SELECT onlinePoints from onlinestuff WHERE name='" + id.toString() + "'").getInt("onlinePoints");
			
			s.close();
			
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return a;
		
	}
	
	public static void addPoints(UUID id , int amount){
		
		  try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE onlinestuff SET onlinePoints=" + (getPoints(id) + amount) + " WHERE name='" + id.toString() + "'");
		    s.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}

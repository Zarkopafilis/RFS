package com.rfslabs.battlearena.tokens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class PointsLink {

	public static Connection con;

	public static void init(){
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void shutdown(){
		
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addPoint(UUID id){
		
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE onlinestuff SET onlinePoints=" + (getPoints(id) + 1) + " WHERE name='" + id.toString() +"'");
			
			
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void decPoint(UUID id){
		
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE onlinestuff SET onlinePoints=" + (getPoints(id) - 1) + " WHERE name='" + id.toString() +"'");
			
			
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getPoints(UUID s) {
		try{
			int pts = 0;
			Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT onlinePoints from onlinestuff WHERE name='" + s.toString() + "'");
		    while (rs.next()) {
		      pts =  rs.getInt("onlinePoints");
		    }
			stmt.close();
		    rs.close();
		    return pts;
		}catch(Exception e){
		    	e.printStackTrace();
		}
			return 0;
		}
	
}

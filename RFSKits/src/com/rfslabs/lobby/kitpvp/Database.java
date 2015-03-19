package com.rfslabs.lobby.kitpvp;

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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitpvp", "c0pointsex", "xG1UM8ZghmHIxEo");
			
			Statement s = con.createStatement();
			
			s.execute("CREATE TABLE IF NOT EXISTS data("
					+ "uuid VARCHAR(45),"
					+ "kills INTEGER(10),"
					+ "deaths INTEGER(10),"
					+ "cash_kills INTEGER(10)"
					+ ")");
			
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
	}
	
	public static void registerPlayer(UUID id){
		
		try {
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT uuid FROM data WHERE uuid='" + id.toString() + "'");
			
			if(rs.next()){
				rs.close();
				s.close();
				return;
			}else{
			s.close();
			Statement st = con.createStatement();
			st.executeUpdate("INSERT INTO data VALUES ('" + id.toString() + "' , 0 , 0 , 50)");
			st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getKills(UUID id){
		int a = 0;
		
		try {
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT kills FROM data WHERE uuid='" + id.toString() + "'");
			rs.next();
			a = rs.getInt("kills");
			
			s.close();
			rs.close();
			
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return a;
	}
	
	public static int getCashKills(UUID id){
		int a = 0;
		
		try {
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT cash_kills FROM data WHERE uuid='" + id.toString() + "'");
			rs.next();
			a = rs.getInt("cash_kills");
			
			s.close();
			rs.close();
			
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return a;
	}
	
	public static int getDeaths(UUID id){
		int a = 0;
		
		try {
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT deaths FROM data WHERE uuid='" + id.toString() + "'");
			rs.next();
			a = rs.getInt("deaths");
			
			s.close();
			rs.close();
			
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return a;
	}
	
	public static void setKills(UUID id , int amount){
		
		try {
			Statement s = con.createStatement();
			s.executeUpdate("UPDATE data SET kills=" + amount + " WHERE uuid='" + id.toString() + "'");
			
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setCashKills(UUID id , int amount){
		
		try {
			Statement s = con.createStatement();
			s.executeUpdate("UPDATE data SET cash_kills=" + amount + " WHERE uuid='" + id.toString() + "'");
			
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setDeaths(UUID id , int amount){
		
		try {
			Statement s = con.createStatement();
			s.executeUpdate("UPDATE data SET deaths=" + amount + " WHERE uuid='" + id.toString() + "'");
			
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static boolean removeCash(UUID id, int amount){
		
		int curr = getCashKills(id);	
		
		if(curr >= amount){
			setCashKills(id , curr-amount);
			return true;
		}
		
		return false;
	}
	
	
}

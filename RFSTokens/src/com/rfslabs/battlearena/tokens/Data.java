package com.rfslabs.battlearena.tokens;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.plugin.Plugin;

public class Data {

	public static Connection con;
	public static Plugin p;
	
	public static void init(){
		
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("CREATE TABLE IF NOT EXISTS battlearena ("
					+ "uuid VARCHAR(45),"
					+ "tokens INTEGER(40)");
			
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void registerPlayer(UUID p){
		
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("INSERT INTO battlearena VALUES('"+ p.toString() +"',50)");
			
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

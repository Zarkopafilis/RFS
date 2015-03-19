package com.rfslabs.zombies.util;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Database {

	public static Connection con;

	public static void init(){
		try {
	    	Class.forName("com.mysql.jdbc.Driver"); //not sure if needed
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void addPoints(int level){
		
		int tokens = (level * 2) / 100;
		
		int points = tokens / 100 ;
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			
			addPoints(p , points);
			p.sendMessage(ChatColor.GREEN + "+" + points + " RFS Points for the " + level + " rounds you survived!");
		}
		
	}
	
	public static void addPoints(Player p , int amount){
		
		try {
			Statement st = con.createStatement();
			
			st.executeQuery("UPDATE onlinestuff SET onlinePoints=" + (getPoints(p) + amount) + " WHERE name='" + p.getUniqueId().toString() + "'");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getPoints(Player player){
		
		int points = 0;
		try{
		
		    Statement st = con.createStatement();
		    
		    ResultSet rs = st.executeQuery("SELECT onlinePoints from onlinestuff WHERE name='" + player.getUniqueId().toString() + "'");
		    while (rs.next()) {
		      points = rs.getInt(1);
		    }
		    rs.close();
		    st.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		    return points;
	}

}

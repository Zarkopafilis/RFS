package com.rfslabs.rfsdom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PointsLink {

	public static Connection con;
	
	public static void startup(){
		
		if(ConfigUtil.db){
		
		try{
		
		Class.forName("com.mysql.jdbc.Driver");
		
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		}
		
	}
	
	public static int getRFSPts(Player p){

		if(ConfigUtil.db){
		
		try {
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT onlinePoints FROM onlinestuff WHERE name='"+ p.getUniqueId().toString() +"'");
			
			while(rs.next()){
				
				int a = rs.getInt("onlinePoints");
				
				s.close();
				
				rs.close();
				
				return a;
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		}
		
		return 0;
	}
	
  public static void addPTS(UUID p , int amount){
		
		if(ConfigUtil.db){
		
		try {
	
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE onlinestuff SET onlinePoints="+ (getRFSPts(Bukkit.getPlayer(p)) + amount) + " WHERE name='" + p.toString() +"'");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		}
	}
	
	public static boolean rmRFSPts(Player p , int amount){
		
		if(ConfigUtil.db){
		
		try {
			
			int tmp = getRFSPts(p);
			
			if(tmp >= amount){
				
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE onlinestuff SET onlinePoints="+ (getRFSPts(p) - amount) + " WHERE name='" + p.getUniqueId().toString() +"'");
				
				p.sendMessage(ChatColor.GREEN +""+ amount + " RFS Points have been removed from your account!");
				return true;
			}else{
				p.sendMessage(ChatColor.RED + "Not enough RFS Points!");
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		}
		
		return false;
		
	}
	
}

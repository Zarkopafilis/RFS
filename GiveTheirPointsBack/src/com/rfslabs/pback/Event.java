package com.rfslabs.pback;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Event implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogn(PlayerLoginEvent e){
		try {
			//Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " -> Stage 1");
			if(not_valid()){
				try{
					//Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " -> Stage 1.5");
					Main.con.close();
					
				}catch(Exception exce){
					
				}finally{
					//Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " -> Stage 1.6");
					Main.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb" , "c0pointsex" , "xG1UM8ZghmHIxEo");
				}
			}
			//Bukkit.getPlayer("Zarkopafilis").sendMessage("d");
			Statement s = Main.con.createStatement();
			ResultSet rs = s.executeQuery("SELECT onlinePoints FROM onlinestuff WHERE name='" + e.getPlayer().getName() +"'");
			if(!rs.next()){
				//Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " has not got name-points");
				s.close();
				rs.close();
			}else{
				int name_points = rs.getInt("onlinePoints");
				rs.close();
				s.close();
				//Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " -> Stage 2");
				
				
				Statement stat = Main.con.createStatement();
				ResultSet res = stat.executeQuery("SELECT onlinePoints FROM onlinestuff WHERE name='" + e.getPlayer().getUniqueId().toString() + "'");
				if(res.next()){
					int uid_points = res.getInt("onlinePoints");
					int total_points = name_points + uid_points;
					res.close();
					stat.close();
					//Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " -> updating uuid points - dropping name-points");
					Statement sta = Main.con.createStatement();
					sta.executeUpdate("UPDATE onlinestuff SET onlinePoints="+ total_points + " WHERE name='" + e.getPlayer().getUniqueId() + "'");
					
					sta.close();
					
					Statement st = Main.con.createStatement();
					st.executeUpdate("DELETE FROM onlinestuff WHERE name='" + e.getPlayer().getName() + "'");
					st.close();
					
					Bukkit.getLogger().log(Level.INFO , "Player " + e.getPlayer().getName() + " got his rfspoints restored(+" + name_points + ")");
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}
	
	private boolean not_valid(){
		try{
		Statement st = Main.con.createStatement();
		ResultSet rs = st.executeQuery("SELECT 1 FROM onlinestuff");
		if(rs.next()){
			
			rs.close();
			st.close();
			return true;
		}
		st.close();
		rs.close();
		return false;
		}catch(Exception e){
			
		}
		return false;
	}
	
}

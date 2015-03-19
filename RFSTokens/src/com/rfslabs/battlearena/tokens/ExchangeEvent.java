package com.rfslabs.battlearena.tokens;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ExchangeEvent implements Listener{

	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		
		try {
			Statement s = Data.con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT uuid FROM battlearena WHERE uuid='" + e.getPlayer().getUniqueId().toString() +"'");
			
			if(!rs.next()){
				Data.registerPlayer(e.getPlayer().getUniqueId());
			}
			rs.close();
			s.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		
		try {
			Statement s = Data.con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT tokens FROM battlearena WHERE uuid='" + e.getPlayer().getUniqueId().toString() +"'");
			int i = 0;
			if(rs.next()){
				i = rs.getInt("tokens");
			}
			while(i >= 100){
				
				tokenToPoints(e.getPlayer().getUniqueId());
		
				i-=100;
			}
			
			rs.close();
			s.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}

	private void tokenToPoints(UUID id) {
		
		try {
			Statement s = Data.con.createStatement();
			
			s.executeUpdate("UPDATE battlearena SET tokens=" + (getTokens(id) - 100) + " WHERE uuid='" + id.toString() + "'");
			PointsLink.decPoint(id);
			
			s.close();
			
			PointsLink.addPoint(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private int getTokens(UUID id) {
		try {
			Statement s = Data.con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT tokens FROM battlearena WHERE uuid='" + id.toString() +"'");
			int i = 0;
			if(rs.next()){
				i = rs.getInt("tokens");
				
			}
			
			s.close();
			
			rs.close();
			
			return i;
			
		}catch(Exception e){
			e.printStackTrace();
			}
		
		return 0;
	}
	
	
}

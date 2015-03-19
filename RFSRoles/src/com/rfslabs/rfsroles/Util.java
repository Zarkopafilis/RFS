package com.rfslabs.rfsroles;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rfslabs.rfsroles.event.KarmaEvents;
import com.rfslabs.rfsroles.event.LevelEvents;
import com.rfslabs.rfsroles.event.ListEvents;
import com.rfslabs.rfsroles.event.SkillEvents;

public class Util {

	public static void startUp(){
		
		
		try {
			SQLHandler.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/roles" , "c0pointsex" , "xG1UM8ZghmHIxEo");
		} catch (SQLException e) {
			e.printStackTrace();
		}     
		
		SQLHandler.init();
		
		Bukkit.getPluginManager().registerEvents(new KarmaEvents(), Data.plugin);
		Bukkit.getPluginManager().registerEvents(new ListEvents(), Data.plugin);
		Bukkit.getPluginManager().registerEvents(new LevelEvents(), Data.plugin);	
		Bukkit.getPluginManager().registerEvents(new SkillEvents(), Data.plugin);	
		
		schedulers();
		
	}
	
	private static void schedulers() {
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Data.plugin, new Runnable(){

			@Override
			public void run() {
				
				for(Player p : Bukkit.getOnlinePlayers()){
					UUID id = p.getUniqueId();
					Data.combat.put(id, Data.combat.get(id)- Data.combat_time);
					if(Data.combat.get(id) < 0){
						Data.combat.put(id, 0);
					}
				}
			}
			
		}, 100L , Data.combat_time * 20);
		
	}

	public static void shutDown(){
		
		try {
			SQLHandler.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public static void xpAndLevelUp(UUID id , int xpa){
		
		int level = SQLHandler.getLevel(id);
		
		if(level >= 75){
			return;
		}
		
		int xp = Data.xp.get(id);
		xp += xpa;
		
		if(xp >= level * Data.level_xp * 2){
			
			Data.xp.put(id, 0);
			SQLHandler.setLevel(id, level + 1);
			Player p = Bukkit.getPlayer(id);
			p.setMaxHealth(20 + level);
			
		}else{
			Data.xp.put(id, xp);
		}
		
	}
	
}

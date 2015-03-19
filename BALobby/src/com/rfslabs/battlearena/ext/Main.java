package com.rfslabs.battlearena.ext;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	
	@Override
	public void onEnable(){
	
		Data.p = this;
		
		Bukkit.getPluginManager().registerEvents(new Event(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				
				for(Player p : Bukkit.getOnlinePlayers()){
					p.setFoodLevel(20);
				}
				
			}
			
			
			
		}, 20, 200);
		
	}
	
	@Override
	public void onDisable(){
		
	}
	
	
}

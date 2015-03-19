package com.rfslabs.towerrun;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin{
    
	@Override
	public void onEnable(){
		
		 Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		 DAO.plugin = this;
		
		 Util.load();
		 
		 Bukkit.getServer().getPluginManager().registerEvents(new Events() , this);
	}
	
	@Override public void onDisable(){
		
		Util.quit();
		
	}
	
}

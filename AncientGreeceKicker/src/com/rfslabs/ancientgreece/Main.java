package com.rfslabs.ancientgreece;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	
	@Override
	public void onEnable(){
		
		Database.init();
		
		Bukkit.getPluginManager().registerEvents(new Event(), this);
	}
	
	@Override
	public void onDisable(){
		Database.deconstruct();
	}
}

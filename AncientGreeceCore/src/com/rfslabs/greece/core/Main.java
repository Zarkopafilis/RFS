package com.rfslabs.greece.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.rfslabs.greece.data.ChatStuff;
import com.rfslabs.greece.data.Data;
import com.rfslabs.greece.data.DatabaseUtil;
import com.rfslabs.greece.data.Spawnpoints;
import com.rfslabs.greece.event.BuffAddEvent;
import com.rfslabs.greece.event.ChatSkillEvents;
import com.rfslabs.greece.event.ChatTagEvent;
import com.rfslabs.greece.event.CoreEvents;
import com.rfslabs.greece.event.SignEvents;
import com.rfslabs.greece.util.WaterDamageCycle;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		passInstance();
		Data.init();
		ChatStuff.init();
		Spawnpoints.init();
		DatabaseUtil.init();
		registerEvents();
		registerTimers();
		
	}
	
	@Override
	public void onDisable(){
		DatabaseUtil.deconstruct();		
	}
	
	private void registerTimers() {
		BukkitScheduler sch = Bukkit.getScheduler();
		
		sch.scheduleSyncDelayedTask(this, new WaterDamageCycle(), 40);
	}

	private void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvents(new ChatTagEvent() , this);
		pm.registerEvents(new BuffAddEvent() , this);
		pm.registerEvents(new CoreEvents() , this);
		pm.registerEvents(new SignEvents() , this);
		pm.registerEvents(new ChatSkillEvents() , this);
	}


	private void passInstance(){
		Data.plugin = this;
	}
	
}

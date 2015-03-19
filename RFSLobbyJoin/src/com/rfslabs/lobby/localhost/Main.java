package com.rfslabs.lobby.localhost;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{


	@Override
	public void onEnable(){
		Data.p = this;
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");	
		
		Bukkit.getPluginManager().registerEvents(new LobbyLocalhostJoinEvent(), this);
	}
	
	@Override
	public void onDisable(){
		
	}

	
}

package com.rfslabs.verifier;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		ConnectionHandler.init();
		Bukkit.getServer().getPluginManager().registerEvents(new Event(), this);
		
		new Thread(){
			
			@Override
			public void run(){
			
				
				try {
					ConnectionHandler.loop();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					run();
				}
				
			}	
			
		}.start();
		
	}
	
	@Override
	public void onDisable(){
		ConnectionHandler.optionalDeconstruct();
	}
	
}

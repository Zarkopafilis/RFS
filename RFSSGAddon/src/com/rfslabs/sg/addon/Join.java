package com.rfslabs.sg.addon;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class Join implements Listener {

	Plugin p;
	
	public Join(Plugin p){
		this.p = p;
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e){
		Bukkit.getScheduler().scheduleSyncDelayedTask(p , new Runnable(){

			@Override
			public void run() {
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + e.getPlayer().getName() + " world");
				
			}
			
		}, 1);
	}
	
}

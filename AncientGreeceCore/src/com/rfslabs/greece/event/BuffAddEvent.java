package com.rfslabs.greece.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.rfslabs.greece.data.Data;

public class BuffAddEvent implements Listener{

	public static void addBuff(final Player p, int delay){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Data.plugin, new Runnable(){

			@Override
			public void run() {
				
				if(p.isOp()){
					return;
				}else if(p.hasPermission("rfs.ares")){
					p.addPotionEffect(Data.ares_strength);
				}else if(p.hasPermission("rfs.hermes")){
					p.addPotionEffect(Data.hermes_speed);
				}
				
			}
			
		}, delay);
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e){
		if(e.isSneaking()){
			e.getPlayer().getLocation().add(e.getPlayer().getLocation().getDirection().normalize());
		}
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e){
		addBuff(e.getPlayer() , 5);
	}
	
	@EventHandler
	public void onJoin(final PlayerRespawnEvent e){
		addBuff(e.getPlayer() , 5);
	}
	
}

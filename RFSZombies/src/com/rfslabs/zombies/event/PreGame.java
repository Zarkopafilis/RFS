package com.rfslabs.zombies.event;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rfslabs.zombies.GameHandler;
import com.rfslabs.zombies.util.Data;
import com.rfslabs.zombies.util.Locations;

public class PreGame implements Listener{

	@EventHandler
	public void playerPreLogin(PlayerLoginEvent e){
		if(Data.finished){
			e.disallow(Result.KICK_OTHER, ChatColor.RED + "The game just finished, it's about to start again!");
		}else if(Data.started){
			e.disallow(Result.KICK_OTHER, ChatColor.RED + "The game has started , please wait for it to finish!");
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerJoin(final PlayerJoinEvent e){
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable(){
			
			@Override
			public void run(){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + e.getPlayer().getName() + " world");
				e.getPlayer().getInventory().clear();
				
				for(PotionEffect eff : e.getPlayer().getActivePotionEffects()){
					e.getPlayer().removePotionEffect(eff.getType());
				}
				
			}
			
		}, 1);
		
		
		e.getPlayer().setHealth(20);
		e.getPlayer().setFoodLevel(20);
		e.getPlayer().updateInventory();
		
		Data.bux.put(e.getPlayer(), 500);
		
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "" + Bukkit.getServer().getOnlinePlayers().length + " out of " + " 20 players!");
		
		if(Bukkit.getServer().getOnlinePlayers().length >= 1){
			if(!Data.started_cd){
				
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting the game...");
				Data.started_cd = true;
				GameHandler.startCountDown();
				
			}
		}
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		Data.reloading.remove(e.getPlayer());
		Data.bux.remove(e.getPlayer());
		Data.dead.remove(e.getPlayer());
	}
	
}

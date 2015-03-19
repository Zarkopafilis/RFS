package com.rfslabs.towerrun;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void join(final PlayerJoinEvent e){
		
		DAO.alive.add(e.getPlayer().getUniqueId());
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(DAO.plugin, new Runnable(){

			public void run() {
				e.getPlayer().teleport(DAO.start_loc);
				
			}
			
		}, 2);
		
		
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		e.getPlayer().getInventory().clear();
		e.getPlayer().updateInventory();
		
		e.getPlayer().setFoodLevel(20);
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION , Integer.MAX_VALUE , 3));
		
		
		if(Bukkit.getOnlinePlayers().length >= DAO.player_threshold){
			new TowerRun().start();
		}
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "" + e.getPlayer().getName() + " ragequitted!");
		DAO.alive.remove(e.getPlayer().getUniqueId());
		DAO.dead.remove(e.getPlayer().getUniqueId());
		
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		if(!DAO.can_move){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void login(PlayerLoginEvent e){
		if(DAO.started){
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER , "Game has already started!");
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e){
		e.getDrops().clear();
		if(DAO.started){
			Bukkit.getServer().broadcastMessage(ChatColor.RED + "" + e.getEntity().getName() + " lost");
			DAO.alive.remove(e.getEntity().getUniqueId());
			DAO.dead.add(e.getEntity().getUniqueId());
		}
		
	}
	
	@EventHandler
	public void respawn(final PlayerRespawnEvent e){
		if(DAO.started){
			e.setRespawnLocation(DAO.losers);
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(DAO.plugin, new Runnable(){

			public void run() {
				
				e.getPlayer().setGameMode(GameMode.SURVIVAL);
				
			}
			
		}, 5);
		
		}else{
			
			e.setRespawnLocation(DAO.start_loc);
			
		}
	}
	
	@EventHandler
	public void dmg(EntityDamageEvent e){
			e.setDamage(0);
	}
	
	@EventHandler
	public void blBreak(BlockBreakEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blBreak(InventoryOpenEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blPlace(BlockPlaceEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}
	
}

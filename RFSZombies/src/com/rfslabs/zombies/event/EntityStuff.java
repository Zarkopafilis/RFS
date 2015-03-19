package com.rfslabs.zombies.event;

import org.bukkit.Bukkit;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rfslabs.zombies.GameHandler;
import com.rfslabs.zombies.util.Data;
import com.rfslabs.zombies.util.Locations;
import com.rfslabs.zombies.util.MysteryBoxLogic;

public class EntityStuff implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUnathorizedSpawn(CreatureSpawnEvent e){
		
		if(e.getCreatureType() != CreatureType.ZOMBIE){
			//Bukkit.getLogger().log(Level.SEVERE, "ZOMB NOT");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void zombieDeath(EntityDeathEvent e){
		//Bukkit.getLogger().log(Level.SEVERE, "ZOMB ded");
		e.getDrops().clear();
		if(Data.started){
	
		if(e.getEntityType() == EntityType.ZOMBIE){
			if(e.getEntity().getKiller() instanceof Player){
				Player p = e.getEntity().getKiller();
				Data.bux.put(p, Data.bux.get(p) + 50);
			}
			//Bukkit.getLogger().log(Level.SEVERE, "-1");
			Data.zombieCount -=1;
		}
		
		if(Data.zombieCount <= 0){
			//Bukkit.getLogger().log(Level.SEVERE, "rnd");
			Data.level++;
			GameHandler.newRound();
		}
	}
		
	}
	
	@EventHandler
	public void guyDead(PlayerDeathEvent e){
		if(!Data.started){
			return;
		}
		Data.dead.add(e.getEntity());
		if(Data.dead.size() >= Bukkit.getServer().getOnlinePlayers().length){
			GameHandler.endGame();
		}
	}
	
	
	@EventHandler
	public void guyQuit(PlayerQuitEvent e){
		if(!Data.started){
			return;
		}
		
		if(Bukkit.getServer().getOnlinePlayers().length <= 0){
			GameHandler.endGame();
		}
	}
	
	@EventHandler
	public void respawn(final PlayerRespawnEvent e){
		
		if(!Data.started){
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable() {
			    public void run(){
			    	
			    	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + e.getPlayer().getName() + " world");
			    	
			    }
			}, 1);
			
			return;
		}
		
		if(Data.dead.contains(e.getPlayer())){
			e.setRespawnLocation(Locations.doom_box);
		}
		
	}
	
	@EventHandler
	public void invMove(InventoryInteractEvent e){
		e.setCancelled(true);
	}
	
}

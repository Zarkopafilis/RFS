package com.rfslabs.rfsroles.event;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.rfslabs.rfsroles.Util;

public class LevelEvents implements Listener{

	@EventHandler
	public void onHostileMobDeathAddXp(EntityDeathEvent e){
		
		if(e.getEntity() instanceof Player || !(e.getEntity().getKiller() instanceof Player)){
			return;
		}
		
		UUID killer = e.getEntity().getKiller().getUniqueId();
		EntityType mob = e.getEntity().getType();
		
		if(mob == EntityType.ZOMBIE){
			Util.xpAndLevelUp(killer, 8);
		}else if(mob == EntityType.SKELETON){
			Util.xpAndLevelUp(killer, 8);
		}else if(mob == EntityType.SPIDER){
			Util.xpAndLevelUp(killer, 8);
		}else if(mob == EntityType.ENDERMAN){
			Util.xpAndLevelUp(killer, 16);
		}else if(mob == EntityType.CREEPER){
			Util.xpAndLevelUp(killer, 15);
		}else if(mob == EntityType.PIG_ZOMBIE){
			Util.xpAndLevelUp(killer, 10);
		}else if(mob == EntityType.BLAZE){
			Util.xpAndLevelUp(killer, 12);
		}else if(mob == EntityType.GHAST){
			Util.xpAndLevelUp(killer, 20);
		}else if(mob == EntityType.WITHER){
			Util.xpAndLevelUp(killer, 100);
		}else if(mob == EntityType.ENDER_DRAGON){
			Util.xpAndLevelUp(killer, 100);
		}else if(mob == EntityType.WITCH){
			Util.xpAndLevelUp(killer, 7);
		}
		
	}
	
}

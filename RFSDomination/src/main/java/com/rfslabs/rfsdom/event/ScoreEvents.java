package com.rfslabs.rfsdom.event;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rfslabs.rfsdom.util.ConfigUtil;
import com.rfslabs.rfsdom.util.MainDAO;

public class ScoreEvents implements Listener{

	/*
	 * Add a death and a kill
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(final PlayerDeathEvent e){
			
		ConfigUtil.addDeath(e.getEntity().getUniqueId());
		
		if(e.getEntity().getKiller() instanceof Player){
			
		e.getEntity().playSound(e.getEntity().getLocation(), Sound.ZOMBIE_HURT, 1, 0);
		
		if(e.getEntity().getUniqueId().equals(e.getEntity().getKiller().getUniqueId())){
			e.setDeathMessage(e.getEntity().getName() + " killed himself , what a noob!");
			
			return;
			
		}			
		
		ConfigUtil.addKill(e.getEntity().getKiller().getUniqueId());
		
		ConfigUtil.tryLevelup(e.getEntity().getKiller().getUniqueId());
		
		ConfigUtil.addCerts(e.getEntity().getKiller().getUniqueId() , 10);
		
		e.getEntity().getKiller().sendMessage(ChatColor.AQUA + "Kill! +10 certs");
		
		}
		
		MainDAO.reloading.remove(e.getEntity().getUniqueId());
		MainDAO.cant_ability.remove(e.getEntity().getUniqueId());
		
		//DatabaseUtil.addDeath(e.getEntity().getUniqueId());
		//DatabaseUtil.addKill(e.getEntity().getKiller().getUniqueId());
		
	}	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(final EntityDeathEvent e){		
		
		e.getDrops().clear();
		
	}
	
}

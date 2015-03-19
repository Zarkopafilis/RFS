package com.rfslabs.rfsdom.event;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rfslabs.rfsdom.util.ConfigUtil;
import com.rfslabs.rfsdom.util.GeneralUtil;
import com.rfslabs.rfsdom.util.MainDAO;

public class ListEvents implements Listener{
	
	/*
	 * Upon login , add to the players hashmap
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(final PlayerJoinEvent e) {
			
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 0);
		
		ConfigUtil.loadPlayer(e.getPlayer().getUniqueId());
		
		if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){		
			
			e.getPlayer().setDisplayName(ChatColor.RED + "[TR] " + ChatColor.RESET + e.getPlayer().getName());
			
			MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.tr_smg);
			MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.tr_smg_a);
			
			MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
			
		}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
			
			e.getPlayer().setDisplayName(ChatColor.LIGHT_PURPLE + "[VANU] " + ChatColor.RESET + e.getPlayer().getName());
			
			MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.vn_smg);
			MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.vn_smg_a);
			
			MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
			
		}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
			
			e.getPlayer().setDisplayName(ChatColor.BLUE + "[NC] " + ChatColor.RESET + e.getPlayer().getName());
			
			MainDAO.guns.put(e.getPlayer().getUniqueId(), MainDAO.nc_smg);
			MainDAO.ammos.put(e.getPlayer().getUniqueId(), MainDAO.nc_smg_a);
			
			MainDAO.classes.put(e.getPlayer().getUniqueId(), "engineer");
			
		}

		GeneralUtil.tp(e.getPlayer(), MainDAO.players.get(e.getPlayer().getUniqueId()));
		
		GeneralUtil.armor(e.getPlayer() , MainDAO.players.get(e.getPlayer().getUniqueId()));
	
	}

	/*
	 * Upon logout , remove from the players hashmap
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLogout(final PlayerQuitEvent event) {
	   
		MainDAO.players.remove(event.getPlayer().getUniqueId());
		MainDAO.classes.remove(event.getPlayer().getUniqueId());
		MainDAO.guns.remove(event.getPlayer().getUniqueId());
		MainDAO.ammos.remove(event.getPlayer().getUniqueId());
		//MainDAO.regen.remove(event.getPlayer().getUniqueId());
		MainDAO.reloading.remove(event.getPlayer().getUniqueId());
		MainDAO.cant_ability.remove(event.getPlayer().getUniqueId());
		MainDAO.cant_shoot.remove(event.getPlayer().getUniqueId());
		
	}
	
}

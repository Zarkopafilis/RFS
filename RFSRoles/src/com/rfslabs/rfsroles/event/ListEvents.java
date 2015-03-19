package com.rfslabs.rfsroles.event;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rfslabs.rfsroles.Data;
import com.rfslabs.rfsroles.SQLHandler;

public class ListEvents implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoinLoadPlayer(final PlayerJoinEvent e){
		
		UUID id = e.getPlayer().getUniqueId();
		final Player p = e.getPlayer();
				
		SQLHandler.proccessPlayer(id , e.getPlayer().getName());
		
		Data.combat.put(id, 0);
		
		final int level = SQLHandler.getLevel(id);
		
		Data.xp.put(id, SQLHandler.getExperience(id));
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.plugin, new Runnable(){
		
		@Override
		public void run(){	
			
		if (p.hasPermission("roles.admin"))
        {
          p.setMaxHealth(20 + level - 1);
        }
        else if (p.hasPermission("roles.orc"))
        {
          p.setMaxHealth(30 + level - 1);
        }
        else if (p.hasPermission("roles.elf"))
        {
          p.setMaxHealth(20 + level - 1);
          float speed = 0.3F;
          p.setWalkSpeed(speed);
        }
        else if (p.hasPermission("roles.human"))
        {
          p.setMaxHealth(22 + level - 1);
        }
        else if (p.hasPermission("roles.dwarf"))
        {
          p.setMaxHealth(25 + level - 1);
        }
        else
        {
          p.setMaxHealth(20 + level - 1);
        }
		
		//Data.doScoreboard();
		
		}
		
		}, 1L);
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuitDeconstructAndSavePlayer(PlayerQuitEvent e){
		
		UUID id = e.getPlayer().getUniqueId();
		
		int xp = Data.xp.get(id);
		Data.xp.remove(id);
		
		SQLHandler.setExperience(id, xp);
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.plugin, new Runnable(){
			
			@Override
			public void run(){	
				//Data.doScoreboard();
			}
		
		}, 20);	
	}
	
}

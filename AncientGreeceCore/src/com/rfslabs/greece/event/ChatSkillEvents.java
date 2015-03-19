package com.rfslabs.greece.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.rfslabs.greece.data.Data;
import com.rfslabs.greece.util.GeneralUtil;

public class ChatSkillEvents implements Listener{

	@EventHandler
	public void onChatCheckAndDoSkill(final AsyncPlayerChatEvent e){
		
		if(e.getMessage().equalsIgnoreCase("smite") || e.getMessage().equalsIgnoreCase("s")){
			
			if(e.getPlayer().hasPermission("rfs.zeus")){
				
				if(!Data.lightning.contains(e.getPlayer().getUniqueId())){  
					
					Data.lightning.add(e.getPlayer().getUniqueId());
					e.setMessage("Almighty zeus please help me with your powers!");
					GeneralUtil.lightning(e.getPlayer());
				
					Bukkit.getScheduler().scheduleSyncDelayedTask(Data.plugin, new Runnable(){

						@Override
						public void run() {
						
							Data.lightning.remove(e.getPlayer().getUniqueId());
						
						}
					
					}, 20 * 60 * 2);
				
				}else{
					e.getPlayer().sendMessage(ChatColor.RED + "Zeus refuses to help you for now!");
					e.setCancelled(true);
				}
			}
		}
	}
	
}

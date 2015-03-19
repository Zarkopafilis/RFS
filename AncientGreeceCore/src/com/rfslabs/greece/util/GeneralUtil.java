package com.rfslabs.greece.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.rfslabs.greece.data.Data;

public class GeneralUtil {

	public static void teleport(final Player p , final Location loc , int delay){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Data.plugin, new Runnable(){

			@Override
			public void run() {
				
				p.teleport(loc);
				
			}
			
		}, delay);
	}
	
	@SuppressWarnings("deprecation")
	public static void lightning(Player p){
		
		Block target = p.getTargetBlock(null, 25);
		Block highest = p.getWorld().getHighestBlockAt(target.getLocation());
		
		highest.getWorld().strikeLightning(highest.getLocation());
	}
	
}

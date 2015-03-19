package com.rfslabs.greece.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.rfslabs.greece.data.Data;

public class WaterDamageCycle implements Runnable{

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			if(!p.isOp() && !p.hasPermission("rfs.poseidon")){
				Material m = p.getLocation().getBlock().getType();
			    if (m == Material.STATIONARY_WATER || m == Material.WATER) {
			    	p.damage(Data.water_damage);
			    }
					
			}
		}
		
	}

}

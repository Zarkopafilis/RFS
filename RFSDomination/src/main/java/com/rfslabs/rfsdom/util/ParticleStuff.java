package com.rfslabs.rfsdom.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rfslabs.rfsdom.proj.Particles;

public class ParticleStuff implements Runnable{

	@Override
	public void run() {
		
		for(Player p : Bukkit.getOnlinePlayers()){
			
			if(MainDAO.players.get(p.getName()) == null){
				continue;
			}
			
			if(MainDAO.players.get(p.getName()).equals("terran republic")){
				Particles.DRIP_LAVA.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_LAVA.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_LAVA.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_LAVA.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_LAVA.display(p.getLocation(), 0, 0, 0, 0, 1);
			}else if(MainDAO.players.get(p.getName()).equals("vanu sovereignty")){
				Particles.PORTAL.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.PORTAL.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.PORTAL.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.PORTAL.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.PORTAL.display(p.getLocation(), 0, 0, 0, 0, 1);
			}else if(MainDAO.players.get(p.getName()).equals("new conglomerate")){
				Particles.DRIP_WATER.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_WATER.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_WATER.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_WATER.display(p.getLocation(), 0, 0, 0, 0, 1);
				Particles.DRIP_WATER.display(p.getLocation(), 0, 0, 0, 0, 1);
			}
			
		}
		
	}

}

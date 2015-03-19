package com.rfslabs.zombies.event;


import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.rfslabs.zombies.util.Data;

public class GunHit implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void gunHit(EntityDamageByEntityEvent e){
		if(e.getEntity().getType() == EntityType.ZOMBIE){
			Zombie z = (Zombie) e.getEntity();
		if(e.getDamager() instanceof Snowball){
			Snowball s = (Snowball) e.getDamager();
			if(s.getShooter() instanceof Player){
				Player p = (Player) s.getShooter();
				Data.bux.put(p, Data.bux.get(p) + 10);
				//String s = e.getProjectile().getProjectileName();

				int dmg = Data.damage.get(p);
				
				z.damage(dmg, p);
			}
		}else if(e.getDamager() instanceof Player){
			Data.bux.put((Player) e.getDamager(), Data.bux.get((Player)e.getDamager()) + 25);
		}
		
		}else if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			e.setCancelled(true);
		}
		
	}
	
}

package com.rfslabs.rfsdom.event;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.rfslabs.rfsdom.util.MainDAO;
import com.rfslabs.rfsdom.util.ShieldHunger;

public class AttackEvents implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAttack(final EntityDamageByEntityEvent e) {
		
		if ((e.getEntityType() == EntityType.ITEM_FRAME) || (e.getEntityType() == EntityType.PAINTING)){
			if(e.getDamager() instanceof Player){
				Player attacker = (Player) e.getDamager();
				if(!attacker.isOp()){
					e.setCancelled(true);
					return;
				}
			}
		}
		
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			Player victim = (Player) e.getEntity();
			
			if(!attacker.getUniqueId().equals(victim.getUniqueId())){

			if(MainDAO.players.get(attacker.getUniqueId()).equals("none") || MainDAO.players.get(attacker.getUniqueId()).equals(MainDAO.players.get(victim.getUniqueId()))){
				e.setCancelled(true);
			}
			
			}
			
			if(attacker.getItemInHand() != null && attacker.getItemInHand().hasItemMeta()){
			
			if(attacker.getItemInHand().getItemMeta().getDisplayName().equals("Chainblade")){
				e.setDamage(5);
			}else if(attacker.getItemInHand().getItemMeta().getDisplayName().equals("Force Blade")){
				e.setDamage(5);
			}else if(attacker.getItemInHand().getItemMeta().getDisplayName().equals("Mag Cutter")){
				e.setDamage(5);	
			}
			
			}
			
			return;
		}		
		
		if(e.getCause() == DamageCause.ENTITY_EXPLOSION){
			if(e.getEntity() instanceof Fireball){
				e.setDamage(0);
				e.setCancelled(true);
				return;
			}		
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMobSpawn(CreatureSpawnEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void hungerDMG(EntityDamageEvent e){
		if(e.getCause() == DamageCause.STARVATION){
			e.setDamage(0);
			e.setCancelled(true);
			return;
	}
	}
	
}

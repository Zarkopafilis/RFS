package com.rfslabs.rfsroles.event;

import java.util.UUID;


import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
//import org.kitteh.tag.PlayerReceiveNameTagEvent;
//import org.kitteh.tag.TagAPI;

import com.rfslabs.rfsroles.Data;
import com.rfslabs.rfsroles.SQLHandler;

@SuppressWarnings("deprecation")
public class KarmaEvents implements Listener{

	@EventHandler
	public void onCreeperDeathReduceKarma(EntityDeathEvent e){
		
		if(!(e.getEntity().getKiller() instanceof Player)){
			return;
		}
		
		if(e.getEntity().getType() == EntityType.CREEPER){
			
			UUID id = e.getEntity().getKiller().getUniqueId();
			int karma = SQLHandler.getKarma(id);
			
			if(karma == 0){
				
				return;
				
			}else if(karma > 0 && karma - Data.creeper_karma_reduce_quantity > 0){
				
				SQLHandler.setKarma(id, karma - Data.creeper_karma_reduce_quantity);
				
			}else if(karma > 0 && karma - Data.creeper_karma_reduce_quantity <= 0){
				
				SQLHandler.setKarma(id, 0);
				//TagAPI.refreshPlayer(e.getEntity().getKiller());
				
			}
			
		}
		
	}
	
	@EventHandler
	public void applyKarmaOnOffensivePvP(PlayerDeathEvent e){
		
		if(!(e.getEntity().getKiller() instanceof Player)){
			return;
		}
		
		UUID killer = e.getEntity().getKiller().getUniqueId();
		UUID victim = e.getEntity().getUniqueId();
		
		if(Data.combat.get(killer) > 0 && Data.combat.get(victim) <= 0){
			
			int karma = SQLHandler.getKarma(killer);
			
			SQLHandler.setKarma(killer, karma + Data.karma_on_kill);
			//TagAPI.refreshPlayer(e.getEntity().getKiller());
		}
		
	}
	
	@EventHandler
	public void applyCombatTime(EntityDamageByEntityEvent e){
		
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			
				final UUID attacker = e.getDamager().getUniqueId();
							
				Data.combat.put(attacker,Data.combat.get(attacker) + Data.combat_time);
		
		}
		
	}
	
	@EventHandler
	public void deathDrops(PlayerDeathEvent e){
		
		UUID id = e.getEntity().getUniqueId();
		
		if(SQLHandler.getKarma(id) <= 0){
			 ItemStack[] content = e.getEntity().getInventory().getContents();
			 ItemStack[] content_armor = e.getEntity().getInventory().getArmorContents();
			 
			 Data.armor.put(id, content_armor);
			 Data.items.put(id, content);
			 
			 e.getDrops().clear();
		}else{
			SQLHandler.setKarma(id, 0);
		}
		
	}
	
	@EventHandler
	public void supercharge(CreatureSpawnEvent e){
		if(e.getEntityType() == EntityType.CREEPER){
			
			Creeper c = (Creeper) e.getEntity();
			
			c.addPotionEffect(new PotionEffect(PotionEffectType.SPEED , Integer.MAX_VALUE , 1));
			c.setPowered(true);
			
		}
	}
	
	@EventHandler
	public void getItemsAgain(PlayerRespawnEvent e){
		
		UUID id = e.getPlayer().getUniqueId();
		
		Data.combat.put(id, 0);
		
		if(Data.items.containsKey(id)){
			
			e.getPlayer().getInventory().setContents(Data.items.get(id));
			
			Data.items.remove(id);
		}
		
		if(Data.armor.containsKey(id)){
			
			e.getPlayer().getInventory().setArmorContents(Data.armor.get(id));
			
			Data.armor.remove(id);
		}
		
	}
	
	/*@EventHandler
	public void onPlayerTagCheckAndChangeColor(PlayerReceiveNameTagEvent e){
		
		UUID id = e.getNamedPlayer().getUniqueId();
		
		if(SQLHandler.getKarma(id) > 0){
			e.setTag(ChatColor.RED + e.getNamedPlayer().getName());
		}else{
			e.setTag(ChatColor.GREEN + e.getNamedPlayer().getName());
		}
		
	}*/
	
}

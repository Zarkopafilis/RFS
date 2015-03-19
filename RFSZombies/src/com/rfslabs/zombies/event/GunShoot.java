package com.rfslabs.zombies.event;

import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rfslabs.zombies.util.Data;
import com.rfslabs.zombies.util.Gun;
import com.rfslabs.zombies.util.Shotgun;

public class GunShoot implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTryShoot(PlayerInteractEvent e){
		e.setUseItemInHand(Event.Result.DENY);
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand() != null){
					 if(e.getPlayer().getItemInHand().getType() == Data.grenade.getType()){
						 shootGrenade(e.getPlayer());
						 canShootGrenade(e.getPlayer());
					 }else if(e.getPlayer().getItemInHand().getType() == Data.molotov.getType()){
						 shootMolotov(e.getPlayer());
						 canShootGrenade(e.getPlayer());
					 }
					 			 
						 if(e.getPlayer().getItemInHand().getType() == Material.STONE_HOE){
							 snowball(e.getPlayer(),5);
							 return;
						 }
						 
						 if(e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().hasItemMeta()){
							 return;
						 }
						 
						 if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null){
							 return;
						 }

						 if(canShootGun(e.getPlayer())){	 
							 
							 if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Pistol")){
								 snowball(e.getPlayer(),5);
								}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("MG 3")){
									snowball(e.getPlayer(),5);
								}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Thomson")){
									snowball(e.getPlayer(),5);
								}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Dragunov")){
									snowball(e.getPlayer(),5);
								}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Rifle (M1)")){
									snowball(e.getPlayer(),5);
								}else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Shotgun")){
									snowball(e.getPlayer(),1);
									snowball(e.getPlayer(),1);
									snowball(e.getPlayer(),1);
									snowball(e.getPlayer(),1);
								}
							 
							 e.getPlayer().updateInventory();
							 
							// new ItemProjectile(dmg + "" , e.getPlayer() , Data.bullet , 3);
						 }
					 }
		}
	}

	
	@SuppressWarnings("deprecation")
	private void snowball(Player p, float speeds) {
		
		Snowball s = p.getWorld().spawn(p.getEyeLocation(), Snowball.class);
		
		s.setShooter(p);
		s.setVelocity(p.getLocation().getDirection().normalize().multiply(speeds));
		
	}


	private void shootMolotov(final Player p){
		
		final Item i = p.getWorld().dropItem(p.getEyeLocation(),Data.molotov);
		
		i.setVelocity(p.getEyeLocation().getDirection());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable(){
			
			@Override
			public void run(){	
				
				List<Entity> es = i.getNearbyEntities(3, 3, 3);
				
				
				//Particles.FIREWORKS_SPARK.display(i.getLocation(), 0, 0, 0, 1, 1);
				i.getWorld().playSound(i.getLocation(), Sound.EXPLODE, 10 , 1);
				i.remove();
				
				for(Entity e : es){
					
					if(e instanceof Zombie){
						((Zombie) e).damage(40, p);
						((Zombie) e).setFireTicks(50);
						Data.bux.put(p, Data.bux.get(p) + 35);
					}
				}
				
			}
			
		}, 40);
		
	}
	
	
	private void shootGrenade(final Player p){
		
		final Item i = p.getWorld().dropItem(p.getEyeLocation(),Data.grenade);
		
		i.setVelocity(p.getEyeLocation().getDirection());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable(){
			
			@Override
			public void run(){	
				
				List<Entity> es = i.getNearbyEntities(3, 3, 3);
				
				
				//Particles.HUGE_EXPLOSION.display(i.getLocation(), 0, 0, 0, 1, 1);
				i.getWorld().playSound(i.getLocation(), Sound.EXPLODE, 10 , 1);
				i.remove();
				
				for(Entity e : es){
					
					if(e instanceof Zombie){
						((Zombie) e).damage(20, p);
						Data.bux.put(p, Data.bux.get(p) + 25);
					}
				}
				
			}
			
		}, 40);
		
	}
	
	private boolean canShootGun(Player p){
		
		if(p.getInventory().getItem(0) != null && p.getInventory().getItem(0).getItemMeta().getDisplayName().equalsIgnoreCase("Lazor")){
			return true;
		}
		
		if(p.getInventory().getItem(8) != null)	{
			
			if(p.getInventory().getItem(8).getAmount() > 1){
			
			p.getInventory().getItem(8).setAmount(p.getInventory().getItem(8).getAmount() - 1);
			return true;
			}else{
				p.getInventory().setItem(8, null);
				
				int ticks = 50;
				
				for(PotionEffect pe : p.getActivePotionEffects()){
					if(pe.getType() == PotionEffectType.SPEED) ticks = 20;
				}
				
				tryReload(p, ticks);
				return true;	
			}	
	  }
	
	return false;
}
	
	private boolean canShootGrenade(Player p){
		

			
			if(p.getInventory().getItem(4) != null)	{
				
				if(p.getInventory().getItem(4).getAmount() > 1){
				
				p.getInventory().getItem(4).setAmount(p.getInventory().getItem(4).getAmount() - 1);
				return true;
				}else{
					p.getInventory().setItem(4, null);
				}
			
			return true;
		  }
		
		return false;
	}
	
	@SuppressWarnings("unused")
	private void tryReload(final Player p, final long ticks) {
			
		
		if(!p.isDead()){
			
		if(!Data.reloading.contains(p)){

			if(p.getInventory().getItem(7) == null){
				
				p.sendMessage(ChatColor.RED + "No magazines!");
				return;
				
			}
			
			p.sendMessage(ChatColor.RED + "Reloading...");
			Data.reloading.add(p);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable(){

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					
					if(!p.isDead()){

					if(p.getInventory().getItem(7) != null)	{
						
					if(p.getInventory().getItem(7).getAmount() > 1){
					
					p.getInventory().getItem(7).setAmount(p.getInventory().getItem(7).getAmount() - 1);
					}else{
						p.getInventory().setItem(7, null);
					}
					
					int max_amm = 0;
					if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Pistol")){
						max_amm = 18;
					}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("MG 3")){
						max_amm = 500;
					}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Lazor")){
						max_amm = 0;
					}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Thomson")){
						max_amm = 30;
					}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Dragunov")){
						max_amm = 3;
					}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Rifle (M1)")){
						max_amm = 5;
					}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Shotgun")){
						max_amm = 5;
					}
					
					p.getInventory().setItem(8 , new ItemStack(Data.gun_ammo.getType() , max_amm));

					p.getWorld().playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
					Data.reloading.remove(p);
					
					p.updateInventory();

					}
					
					}else{
						
						Data.reloading.remove(p);

					}
					
				}
				
				
				
			}, ticks);
		
		}else{
			
			p.sendMessage(ChatColor.RED + "Already reloading!");
			
		}
		
		
		}else{
			
		Data.reloading.remove(p.getUniqueId());
		
		}
		
	}
	
}

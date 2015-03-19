package com.rfslabs.lobby.kitpvp;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener{
	
	Random r = new Random();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void respawn(final PlayerRespawnEvent e){
		
		e.getPlayer().getInventory().clear();
		e.getPlayer().setMaxHealth(20);
		
		e.getPlayer().getInventory().setHelmet(null);
		e.getPlayer().getInventory().setChestplate(null);
		e.getPlayer().getInventory().setLeggings(null);
		e.getPlayer().getInventory().setBoots(null);
		
		e.getPlayer().updateInventory();
		
		int a = r.nextInt(3);

		if(a == 0){
			e.setRespawnLocation(Data.spawn1);
		}else if(a == 1){
			e.setRespawnLocation(Data.spawn2);
		}else if(a == 2){
			e.setRespawnLocation(Data.spawn3);
		}
		Data.chose.remove(e.getPlayer().getUniqueId());
		
		if(Data.kit.containsKey(e.getPlayer().getUniqueId())){
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.p, new Runnable(){

				@Override
				public void run() {
					
					addItems(e.getPlayer() ,Data.kit.get(e.getPlayer().getUniqueId()));
					
				}
				
			} , 5);
			
		}
		
		e.getPlayer().setNoDamageTicks(60);
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
		//e.setRespawnLocation(Data.spawn);
	}
	
	@EventHandler
	public void drops(EntityDeathEvent e){
		e.getDrops().clear();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(final PlayerJoinEvent e){
		
		e.getPlayer().getInventory().clear();
		e.getPlayer().setMaxHealth(20);
		e.getPlayer().getInventory().setHelmet(null);
		e.getPlayer().getInventory().setChestplate(null);
		e.getPlayer().getInventory().setLeggings(null);
		e.getPlayer().getInventory().setBoots(null);
		
		e.getPlayer().setAllowFlight(true);
		
			Database.registerPlayer(e.getPlayer().getUniqueId());
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.p, new Runnable(){
			@Override
			public void run() {				
				int a = r.nextInt(3);
				
				for (PotionEffect effect : e.getPlayer().getActivePotionEffects()){
					e.getPlayer().removePotionEffect(effect.getType());
				}
				
				e.getPlayer().setNoDamageTicks(40);
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
				if(a == 0){
					e.getPlayer().teleport(Data.spawn1);
				}else if(a == 1){
					e.getPlayer().teleport(Data.spawn2);
				}else if(a == 2){
					e.getPlayer().teleport(Data.spawn3);
				}
			}		}, 1L);
		
	}
	
	@EventHandler
	public void onInteractPts(PlayerInteractEvent e){
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		if(e.getClickedBlock().getState() instanceof Sign){
			Sign s = (Sign) e.getClickedBlock().getState();
			if(s.getLine(0) == null){
				return;
			}
			if(s.getLine(0).equals("Kills ->") && s.getLine(1).equals("RFSPoints")){
				String[] a = s.getLine(2).split(" for ");
				
				int price = Integer.parseInt(a[0]);
				int reward = Integer.parseInt(a[1]);
				
				if(Database.removeCash(e.getPlayer().getUniqueId() , price)){
					PointsLink.addPoints(e.getPlayer().getUniqueId(), reward);
				}
			}
		}
		
	}	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteractKitSign(PlayerInteractEvent e){
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
			return;
		}
		
		if(e.getClickedBlock().getState() instanceof Sign){
			Sign s = (Sign) e.getClickedBlock().getState();
			
			String action = s.getLine(0);
			String price = s.getLine(1);
			String kit = s.getLine(2);
			//String perm = s.getLine(3);
			
			int amount = 0;
			
			if(action != null && price != null && kit != null ){
				
				if(action.equals("Choose kit")){
					
					if(price.equals("for free")){
						
						amount = 0;
						Data.kit.put(e.getPlayer().getUniqueId() , kit);
					}else{
						
						amount = Integer.parseInt(price.replace("for ", ""));
						
					}
					
					if(Data.chose.contains(e.getPlayer().getUniqueId())){
						return;
					}
					
					Data.chose.add(e.getPlayer().getUniqueId());
					
					if(amount > 0){
						if(!Database.removeCash(e.getPlayer().getUniqueId()  , amount)){
							return;
						}
					}	
					
					addItems(e.getPlayer() , kit);	
					
				}
					
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void addItems(Player p , String kit) {
		
		InvPack pack = Data.items.get(kit.toLowerCase());
		
		p.getInventory().clear();
		
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		
		p.getInventory().setArmorContents(pack.armor);
		p.getInventory().setContents(pack.inventory);
		
		p.updateInventory();
		
		p.setFoodLevel(20);
		p.setHealth(20);
		
		for (PotionEffect effect : p.getActivePotionEffects()){
			p.removePotionEffect(effect.getType());
		}
		
		if(kit.equalsIgnoreCase("bowman")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
			p.setMaxHealth(20 + 8);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
			soup(p , 16);
		}else if(kit.equalsIgnoreCase("swordsman")){
			//p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
			soup(p , 16);
		}else if(kit.equalsIgnoreCase("axeman")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
			soup(p , 16);
		}else if(kit.equalsIgnoreCase("tank")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
			p.setMaxHealth(20 + 8);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
			soup(p , 16);
		}else if(kit.equalsIgnoreCase("rogue")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
			soup(p , 5);
		}else if(kit.equalsIgnoreCase("dizzy")){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 3));
			soup(p , 5);
		}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));		
		p.updateInventory();
		soup(p , 1);
	}

	@SuppressWarnings("deprecation")
	private void soup(Player p , int amount){
		for(int i = 0 ; i < amount ; i++){
			p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
		p.updateInventory();
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		e.getItemDrop().remove();
	}
	
	@EventHandler
	public void onDeathDB(PlayerDeathEvent e){
		
		if(e.getEntity().getKiller() instanceof Player){
		
		Database.setKills(e.getEntity().getKiller().getUniqueId() , Database.getKills(e.getEntity().getKiller().getUniqueId()) + 1);
		Database.setCashKills(e.getEntity().getKiller().getUniqueId() , Database.getCashKills(e.getEntity().getKiller().getUniqueId()) + 1);
		
		}
		
		Database.setDeaths(e.getEntity().getUniqueId(), Database.getDeaths(e.getEntity().getUniqueId()) + 1);
	}
	
	@EventHandler
	public void onPlayerHitFishingrodscorpion(final PlayerFishEvent event) {
		
	final Player player = event.getPlayer();
	
	if (event.getCaught() instanceof Player) {
		final Player caught = (Player) event.getCaught();
		if (player.getItemInHand().getType() == Material.FISHING_ROD) {
			caught.teleport(player.getLocation());
		}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void leap(final PlayerToggleFlightEvent e) {
		
		e.getPlayer().setFlying(false);
		e.getPlayer().setAllowFlight(false);
		
		if(e.isFlying()){
			
			e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(0.9));
			
			e.getPlayer().sendMessage(ChatColor.RED + "Double jump is now on cooldown!");
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.p, new Runnable(){

				@Override
				public void run() {
					
					e.getPlayer().setAllowFlight(true);
					e.getPlayer().sendMessage(ChatColor.GREEN + "You can now use double jump!");
				}
			
			}, 160);
			
			
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e){
		UUID id = e.getPlayer().getUniqueId();
		Data.chose.remove(id);
		Data.kit.remove(e.getPlayer().getUniqueId());
		//Data.d_jump.remove(e.getPlayer().getUniqueId());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSoup(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP){
				
				e.setCancelled(true);
				e.setUseItemInHand(Result.DENY);
				
				e.getPlayer().setItemInHand(new ItemStack(Material.AIR));
				e.getPlayer().setItemInHand(null);
				
				e.getPlayer().updateInventory();
				
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 4));
				
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DRINK, 10, 10);
				
				e.getPlayer().getWorld().dropItem(e.getPlayer().getEyeLocation(), new ItemStack(Material.BOWL));
				
			}
			
		}
	}
	
	@EventHandler
	public void eat(PlayerItemConsumeEvent e){
		e.setCancelled(true);
	}
	
	
}
	

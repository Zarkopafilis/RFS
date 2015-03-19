package com.rfslabs.zombies.event;

import org.bukkit.ChatColor;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rfslabs.zombies.util.Data;
import com.rfslabs.zombies.util.Gun;
import com.rfslabs.zombies.util.MysteryBoxLogic;

public class GunBuy implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onGunSign(PlayerInteractEvent e){
		e.setUseItemInHand(Event.Result.DENY);
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getClickedBlock().getState() != null && e.getClickedBlock().getState() instanceof Sign){
				Sign s = (Sign) e.getClickedBlock().getState();
				if(s.getLine(0).equals("[Resupply]")){
					int prc = Integer.parseInt(s.getLine(1));
					if(Data.bux.get(e.getPlayer()) >= prc){
						Data.bux.put(e.getPlayer(), Data.bux.get(e.getPlayer()) - prc);
						resupply(e.getPlayer());
					}
				}else if(s.getLine(0).equals("[Grenade]")){
					int prc = Integer.parseInt(s.getLine(1));
					if(Data.bux.get(e.getPlayer()) >= prc){
						Data.bux.put(e.getPlayer(), Data.bux.get(e.getPlayer()) - prc);
						grenade(e.getPlayer());
					}
				}else if(s.getLine(0).equals("[Buy Gun]")){
					int prc = Integer.parseInt(s.getLine(1));
					if(Data.bux.get(e.getPlayer()) >= prc){
						Data.bux.put(e.getPlayer(), Data.bux.get(e.getPlayer()) - prc);
						gun(e.getPlayer() , s.getLine(2));
					}
				}else if(s.getLine(0).equals("[Door]")){
					
					Block block = Data.w.getBlockAt(new Location(Data.w , s.getX() , s.getY() - 2 , s.getZ()));
					
					if(block.getData() > (byte) 4){
						return;
					}
					
					int prc = Integer.parseInt(s.getLine(1));
					if(Data.bux.get(e.getPlayer()) >= prc){
						Data.bux.put(e.getPlayer(), Data.bux.get(e.getPlayer()) - prc);
						Data.doors.add(block);
						block.setData((byte) (block.getData() + 4));
						
						Data.doDoor(Integer.parseInt(s.getLine(2)));
					}
				}else if(s.getLine(0).equals("[Points]")){
					e.getPlayer().sendMessage(ChatColor.RED + "Points: " + Data.bux.get(e.getPlayer()));
				}else if(s.getLine(0).equals("[MysteryBox]")){
					MysteryBoxLogic.tryOpen(e.getPlayer());
				}else if(s.getLine(0).equals("[Perk-a-Cola]")){
					
					int prc = Integer.parseInt(s.getLine(2));
					
					if(Data.bux.get(e.getPlayer()) >= prc){
						
						Data.bux.put(e.getPlayer(), Data.bux.get(e.getPlayer()) - prc);
						
					if(s.getLine(1).equals("Juggernog")){
						
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE , Integer.MAX_VALUE , 1));
						
					}else if(s.getLine(1).equals("Speed Cola")){
						
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED , Integer.MAX_VALUE , 1));
						
					}else if(s.getLine(1).equals("Double Tap")){
						
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE , Integer.MAX_VALUE , 1));
						
					}
					
				}
					
				}
			}
		}
	}
	
	public static void gun(Player p, String s) {
		
		p.getInventory().setItem(0, (Gun) Data.guns.get(s));
		resupply(p);
	}

	@SuppressWarnings("deprecation")
	private void grenade(Player p) {
		if(p.getInventory().getItem(4)  == null || p.getInventory().getItem(4).getType() == Data.molotov.getType()){
			
		p.getInventory().setItem(4, new ItemStack(Data.grenade.getType() , 1));	
			
		}else{
		
		p.getInventory().getItem(4).setAmount(p.getInventory().getItem(4).getAmount() + 1);;
		
		}
		p.updateInventory();
	}

	@SuppressWarnings("deprecation")
	public static void resupply(Player p){
		
		int mag = 0;
		int max_amm=0;
		if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Pistol")){
			Data.damage.put(p, 5);
			mag = 20;
			max_amm = 18;
		}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("MG 3")){
			Data.damage.put(p, 8);
			mag = 0;
			max_amm = 500;
		}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Lazor")){
			Data.damage.put(p, 7);
			mag = 0;
			max_amm = 0;
		}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Thomson")){
			Data.damage.put(p, 7);
			mag = 3;
			max_amm = 30;
		}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Dragunov")){
			Data.damage.put(p, 40);
			mag = 3;
			max_amm = 3;
		}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Rifle (M1)")){
			Data.damage.put(p, 12);
			mag = 3;
			max_amm = 5;
		}else if(p.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Shotgun")){
			Data.damage.put(p, 40);
			mag = 3;
			max_amm = 5;
		}
		
		if(max_amm > 0){
		p.getInventory().setItem(8, new ItemStack(Data.gun_ammo.getType() , max_amm));
		}else{
			p.getInventory().setItem(8,null);
		}
		
		if(mag > 0){
		p.getInventory().setItem(7, new ItemStack(Data.mag.getType() , mag));
		}else{
			p.getInventory().setItem(7,null);
		}
		
		p.updateInventory();
	}
	
}

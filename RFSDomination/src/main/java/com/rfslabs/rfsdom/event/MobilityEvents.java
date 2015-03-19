package com.rfslabs.rfsdom.event;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import com.rfslabs.rfsdom.util.GeneralUtil;
import com.rfslabs.rfsdom.util.MainDAO;
import com.rfslabs.rfsdom.util.Outpost;
import com.rfslabs.rfsdom.util.Sunderer;

public class MobilityEvents implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent e){
		
		Location sign = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
		sign.setY(sign.getY() - 1);
		
		if(sign.getBlock().getState() instanceof Sign){

			Sign s = (Sign) sign.getBlock().getState();
			
			if(s.getLine(0).equals("jump_pad")){
				
				e.getPlayer().teleport(new Location(MainDAO.world , Integer.parseInt(s.getLine(1)) , Integer.parseInt(s.getLine(2)) , Integer.parseInt(s.getLine(3))), TeleportCause.PLUGIN);
				
			}
			
		}
		
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerMoveWool(final PlayerMoveEvent e){
		
		if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOL && e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getData() == DyeColor.YELLOW.getData()){
			
			Location sign = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
			sign.setY(sign.getY() - 1);
			
			if(sign.getBlock().getState() instanceof Sign){
			
			Sign s = (Sign) sign.getBlock().getState();
			
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.GHAST_FIREBALL, 1, 0);
			e.getPlayer().setVelocity(new Vector(0 , Double.parseDouble(s.getLine(0)) ,0 ));
			
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerFall(final EntityDamageEvent e){
		
		 if(e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL){
			 if(e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOL && e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getData() == DyeColor.LIME.getData()){		 
				 e.setCancelled(true);
				 e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.FALL_BIG, 1, 0);
			 }else if(e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOL && e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getData() == DyeColor.YELLOW.getData()){
				 e.setCancelled(true);
				 e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.FALL_BIG, 1, 0);
			 }
		 }
		 
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(final PlayerRespawnEvent e){
				
		boolean insidePost = false;
		Outpost post = null;
		
		for(Outpost outp : MainDAO.outposts){
		
			if(outp.inBetween(e.getPlayer())){
				insidePost = true;
				post = outp;
				break;
			}
			
		}	
			
		Location loca = Sunderer.calculateClose(e.getPlayer());
				
		if(insidePost && post.captured.equals(MainDAO.players.get(e.getPlayer().getUniqueId()))){//in outpost && his team has capturd
				
				e.setRespawnLocation(post.respawn_chamber);
				GeneralUtil.armor(e.getPlayer(), MainDAO.players.get(e.getPlayer().getUniqueId()));
				
		}else if(loca != null){
			
			e.setRespawnLocation(loca);
			GeneralUtil.armor(e.getPlayer(), MainDAO.players.get(e.getPlayer().getUniqueId()));
			
			
		}else{	
			
		if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("terran republic")){
			e.setRespawnLocation(MainDAO.terran_republicSpawn);
			GeneralUtil.addArmor(e.getPlayer(),"terran republic");
		}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("vanu sovereignty")){
			e.setRespawnLocation(MainDAO.vanu_sovereigntySpawn);
			GeneralUtil.addArmor(e.getPlayer(),"vanu sovereignty");
		}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("new conglomerate")){
			e.setRespawnLocation(MainDAO.new_conglomerateSpawn);
			GeneralUtil.addArmor(e.getPlayer(),"new conglomerate");
		}else if(MainDAO.players.get(e.getPlayer().getUniqueId()).equals("none")){
			e.setRespawnLocation(MainDAO.tutorialLoc);
		}
		
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDrag(final InventoryClickEvent e){
		
		if(e.getWhoClicked() instanceof Player){
		
			Player p = (Player) e.getWhoClicked();
			
		if(!p.isOp()){
			e.setCancelled(true);
			p.updateInventory();
		}
		
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPickup(PlayerPickupItemEvent e){
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRgn(EntityRegainHealthEvent e){
		
		e.setAmount(0);
		e.setCancelled(true);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemFrBreak(HangingBreakByEntityEvent e){
		
		if(e.getRemover() instanceof Player){
			
			Player p = (Player) e.getRemover();
			
			if(p.isOp()){
				return;
			}
			
		}
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemFrClick(PlayerInteractEntityEvent e){
			
		if(e.getRightClicked() instanceof ItemFrame){
			if(e.getPlayer().isOp()){
				return;
			}
			e.setCancelled(true);
		}
	}
	
}
	
	

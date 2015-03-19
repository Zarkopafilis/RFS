package com.rfslabs.rfsdom.util;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sunderer implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,String[] arg3) {
		
		//if(arg2.equals("sunderer") && arg3.length == 0){}else
		
		if(arg2.equals("sunderer") && arg3.length == 1){
			
			if(arg0 instanceof Player){
			
				final Player p = (Player) arg0;
				
				final Player pl = Bukkit.getPlayer(arg3[0]);
				
				if(pl == null){
					p.sendMessage(ChatColor.RED + "No such player!");
					return true;
				}
				
				if(MainDAO.sandy.containsKey(pl.getUniqueId())){
					
					if(MainDAO.players.get(p.getUniqueId()).equals(MainDAO.players.get(pl.getUniqueId()))){
					
						if(!MainDAO.can_tp_sandy.contains(p.getUniqueId())){
							p.teleport(MainDAO.sandy.get(pl.getUniqueId()));
					
							ConfigUtil.addCerts(pl.getUniqueId() , 3);
							
							MainDAO.can_tp_sandy.add(p.getUniqueId());
							
							p.sendMessage(ChatColor.RED + "You can't teleport to another sunderer for 2 minutes!");
							
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainDAO.plugin , new Runnable(){

								@Override
								public void run() {
															
									MainDAO.can_tp_sandy.remove(p.getName());
									
								}
								
								
								
							}, 2 * 60 * 20);
							
							return true;
						}else{
							p.sendMessage(ChatColor.RED + "Sunderer teleport is on cooldown!");
						}
					}
					
				}
			
			}
			
		}
		
		return true;
	}

	@SuppressWarnings("deprecation")
	public static void spawnSandy(Player p) {
		
		MainDAO.sandy.put(p.getUniqueId(),new Location(MainDAO.world , p.getLocation().getX() , p.getLocation().getY() + 3 , p.getLocation().getZ()));
		
		Location core = p.getLocation().clone();
		
		p.teleport(new Location(core.getWorld(), core.getX() , core.getY() + 3, core.getZ()));
		
		DyeColor dc = GeneralUtil.getColore(MainDAO.players.get(p.getUniqueId()));
		Sign s;
		
		
		core.getBlock().setTypeIdAndData(Material.WOOL.getId() , dc.getData(), false);
		
		core.setX(core.getX() - 1);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Resupply");
		s.setLine(1, "Free");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setX(core.getX() + 2);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Resupply");
		s.setLine(1, "Free");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setX(core.getX() - 1);
		
		core.setZ(core.getZ() - 1);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Resupply");
		s.setLine(1, "Free");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		
		
		core.setZ(core.getZ() + 2);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Resupply");
		s.setLine(1, "Free");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setZ(core.getZ() - 1);
		
		core.setY(core.getY() + 1);
		core.getBlock().setTypeIdAndData(Material.WOOL.getId() , dc.getData(), false);

		
		core.setX(core.getX() - 1);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Choose Gun");
		s.setLine(1, "SMG");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setX(core.getX() + 2);	
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Choose Gun");
		s.setLine(1, "Sniper");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setX(core.getX() - 1);
		
		core.setZ(core.getZ() - 1);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Choose Gun");
		s.setLine(1, "Shotgun");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setZ(core.getZ() + 2);
		
		core.getBlock().setTypeIdAndData(Material.SIGN_POST.getId(), (byte) 0 , false);
		s = (Sign) core.getBlock().getState();
		s.setLine(0, "Choose Gun");
		s.setLine(1, "Bazooka");
		s.setLine(3, MainDAO.players.get(p.getUniqueId()));
		s.update(true);
		
		core.setZ(core.getZ() - 1);
		
		core.setY(core.getY() + 1);
		core.getBlock().setTypeIdAndData(Material.WOOL.getId() , dc.getData(), false);
		
		core.setX(core.getX() - 1);
		
		core.getBlock().setType(Material.AIR);
		
		core.setX(core.getX() + 2);
		
		core.getBlock().setType(Material.AIR);
		
		core.setX(core.getX() - 1);
		
		core.setZ(core.getZ() - 1);
		
		core.getBlock().setType(Material.AIR);
		
		core.setZ(core.getZ() + 2);
		
		core.getBlock().setType(Material.AIR);
		
	}
	
	public static void rmSandy(Location l){
		
		l.getBlock().setType(Material.AIR);
		
		l.setX(l.getX() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setX(l.getX() + 2);
		
		l.getBlock().setType(Material.AIR);
		
		l.setX(l.getX() - 1);
		
		l.setZ(l.getZ() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() + 2);
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() - 1);
		
		l.setY(l.getY() + 1);
		l.getBlock().setType(Material.AIR);
		
		
		l.getBlock().setType(Material.AIR);
		
		l.setX(l.getX() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setX(l.getX() + 2);
		
		l.setX(l.getX() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() + 2);
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() - 1);
		
		l.setY(l.getY() + 1);
		l.getBlock().setType(Material.AIR);
		
		
		l.setX(l.getX() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setX(l.getX() + 2);
		
		l.setX(l.getX() - 1);
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() - 1);	
		
		l.getBlock().setType(Material.AIR);
		
		l.setZ(l.getZ() + 2);
		
		l.getBlock().setType(Material.AIR);
		
	}

	public static Location calculateClose(Player p) {
		
		Iterator it = MainDAO.sandy.entrySet().iterator();
		
		while(it.hasNext()){
			
			Map.Entry pair = (Map.Entry)it.next();
			
			if(MainDAO.players.get(p.getUniqueId()).equals(MainDAO.players.get((UUID) pair.getKey()))){
				
				Location pl = p.getLocation();
				
				Location sl = (Location) pair.getValue();
				
				int deltaX = (int) Math.abs(pl.getX() - sl.getX());
				int deltaY = (int) Math.abs(pl.getY() - sl.getY());
				int deltaZ = (int) Math.abs(pl.getZ() - sl.getZ());
				
				int distance = (int) Math.sqrt(deltaX^2 + deltaY^2 + deltaZ^2);
				
				if(distance <= 100){
					
						Bukkit.getPlayer((UUID)pair.getKey()).sendMessage(ChatColor.AQUA + "2 certs have been added to your account!");
						ConfigUtil.addCerts((UUID) pair.getKey(), 2);
					
					return sl;
				}
				
			}
			
			it.remove();
		}
		
	
		
		return null;
	}

}

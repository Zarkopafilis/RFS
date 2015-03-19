package com.rfslabs.lobby.localhost;

import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class LobbyLocalhostJoinEvent implements Listener {
	
	@EventHandler
	public void signplace(SignChangeEvent e){
		
		if(e.getLine(0).equalsIgnoreCase("lobbyjoin")){
			if(!e.getPlayer().isOp()){
				e.getBlock().breakNaturally();
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void rblobbyjoin(final PlayerJoinEvent e){
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.p, new Runnable(){

			@Override
			public void run() {
				
				if(e.getPlayer().getLocation().getWorld().getName().equals("rainbow")){
					
					if(e.getPlayer().getLocation().getY() >= 60){
					
						if(e.getPlayer().getLocation().getX() > -1482 && e.getPlayer().getLocation().getX() < -1373){
							
							if(e.getPlayer().getLocation().getZ() > 184 && e.getPlayer().getLocation().getZ() < 277){
								
								//Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + e.getPlayer().getName() + " add rfs.lobby.bypasscd");
								
								Bukkit.getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable(){

									@Override
									public void run() {
										
										e.getPlayer().teleport(new Location(e.getPlayer().getWorld() , -1427 , 218 , 217));
										
									}
									
								}, 2);
								
								
							}
							
						}
						
					}	
				}
				
			}
			
		}, 2);
		
	}

	@EventHandler
	public void onWalkontop(PlayerMoveEvent e){
		Block b = e.getTo().getBlock().getRelative(BlockFace.DOWN);
		Location l = b.getLocation();
		l.setY(l.getY() - 1);
		Block sb = l.getWorld().getBlockAt(l);
		if(sb.getState() instanceof Sign){
			Sign s = (Sign) sb.getState();
			if(s.getLine(0) != null && s.getLine(1) != null){
				if(s.getLine(0).equals("lobbyjoin")){
					//e.setCancelled(true);
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
		            out.writeUTF("Connect");
		            out.writeUTF(s.getLine(1)); // communicate with bungee
		            e.getPlayer().sendPluginMessage(Data.p , "BungeeCord", out.toByteArray());
		
				}
			}
		}
		
	}
	
}

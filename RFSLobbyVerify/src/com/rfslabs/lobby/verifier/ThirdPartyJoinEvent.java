package com.rfslabs.lobby.verifier;

import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class ThirdPartyJoinEvent implements Listener{
	
	@EventHandler
	public void signplace(SignChangeEvent e){
		
		if(e.getLine(0).equalsIgnoreCase("3rdpartyjoin")){
			if(!e.getPlayer().isOp()){
				e.getBlock().breakNaturally();
			}
		}
		
	}
	
	@EventHandler
	public void onWalkontop(PlayerMoveEvent e){
		
		Block b = e.getTo().getBlock().getRelative(BlockFace.DOWN);
		
		Location l = b.getLocation();
		
		l.setY(l.getY() - 1);
		
		Block sb = l.getWorld().getBlockAt(l);
		//e.getPlayer().sendMessage("1");
		if(sb.getState() instanceof Sign){
			//e.getPlayer().sendMessage("2");
			Sign s = (Sign) sb.getState();
			//e.getPlayer().sendMessage("3");
				if(s.getLine(0).equals("3rdpartyjoin")){
					//e.setCancelled(true);
					//e.getPlayer().sendMessage("4");
					//e.getPlayer().sendMessage(s.getLine(2));
					if(Data.hosts.get(s.getLine(2))){
						
						//e.getPlayer().sendMessage("5");
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
			            out.writeUTF("Connect");
			            out.writeUTF(s.getLine(1)); // = nazwa serwera dla BungeeCord
			            e.getPlayer().sendPluginMessage(Data.p , "BungeeCord", out.toByteArray());
						
					
					}else{
						if(e.getPlayer().isOp()){
							e.getPlayer().sendMessage("server " + Data.hosts.get(s.getLine(2)));
						}
					}
				}
			}
		}
		
}
	

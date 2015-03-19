package com.rfslabs.lobby.anarchy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener{

	@EventHandler
	public void onSignPlaceBlockExploit(final SignChangeEvent e){
			

			if(e.getLine(0).equals("{Spawn}")){
				if(!e.getPlayer().isOp()){
					e.getBlock().breakNaturally();
				    e.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to place such signs if you are not OP!");
				    e.setCancelled(true);
				}
				
			}
			
		}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFirstJoinTeleport(final PlayerJoinEvent e){
		if(!e.getPlayer().hasPlayedBefore()){
			
			Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Data.p, new Runnable(){

				@Override
				public void run() {
					e.getPlayer().teleport(Data.first_spawn);
					
				}
				
			}, 1L);
			
		}
	}
	
	@EventHandler
	public void onCLickSpawnSign(PlayerInteractEvent e){
		
		 if(e.getClickedBlock().getState().getType() == Material.SIGN_POST || e.getClickedBlock().getState().getType() == Material.WALL_SIGN) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if(s.getLine(0).equals("{Spawn}")){
				e.getPlayer().teleport(Data.spawn);
			}
		}
		
	}
	
}

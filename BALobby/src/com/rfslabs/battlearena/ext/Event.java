package com.rfslabs.battlearena.ext;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Event implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(final PlayerJoinEvent e){
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Data.p, new Runnable(){

			@Override
			public void run() {
				
				e.getPlayer().teleport(Data.lobby);
				
			}
			
		}, 2);
	}

	@EventHandler
	public void onStep(PlayerInteractEvent e){
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Sign){
			Sign s = (Sign) e.getClickedBlock().getState();
			if(s.getLine(0) != null && s.getLine(1) != null){
				if(s.getLine(0).endsWith("Back To Lobby")){
					Bukkit.getServer().dispatchCommand(e.getPlayer(), "pb leave");
					Bukkit.getServer().dispatchCommand(e.getPlayer(), "spleef leave");
					e.getPlayer().teleport(Data.lobby);
				}else if(s.getLine(0).equals("Go To")){
					String a = s.getLine(1);
					if(a.equals("Paintball")){
						e.getPlayer().teleport(Data.pb);
					}else if(a.equals("Spleef")){
						e.getPlayer().teleport(Data.spl);
					}
				}
			}
		}
		
	
	}
	
}

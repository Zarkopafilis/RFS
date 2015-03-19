package com.rfslabs.verifier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class Event implements Listener{

	@EventHandler
	public void onPlayerPreLogin(PlayerLoginEvent e){
		if(e.getPlayer().getAddress().equals(ConnectionHandler.rb_addr.getAddress())){
			Bukkit.getServer().broadcastMessage(ChatColor.AQUA + e.getPlayer().getName() + " logged in via the RFS Lobby!");
		}
	}
	
}

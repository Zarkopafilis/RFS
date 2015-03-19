package com.rfslabs.greece.event;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.rfslabs.greece.data.ChatStuff;

public class ChatTagEvent implements Listener{

	@EventHandler
	public void onJoinAddCustomName(PlayerJoinEvent e){
		e.getPlayer().setDisplayName(ChatColor.GRAY + "[" + ChatStuff.godPermToChatColor(e.getPlayer()) + ChatStuff.townPermToString(e.getPlayer()) + ChatColor.BLACK + "]" + ChatColor.RESET + e.getPlayer().getName());
	}
	
}

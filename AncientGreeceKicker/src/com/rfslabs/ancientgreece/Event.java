package com.rfslabs.ancientgreece;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Event implements Listener {

	@EventHandler
	public void onLoginWOPts(PlayerLoginEvent e){
		if(!e.getPlayer().isOp()){
			if(!Database.ancientPointThing(e.getPlayer().getUniqueId())){
				e.disallow(Result.KICK_OTHER, "Insufficient ancient points (1 needed)!");
			}
		}
	}
	
}

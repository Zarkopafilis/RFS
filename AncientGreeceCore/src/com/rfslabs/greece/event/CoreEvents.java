package com.rfslabs.greece.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rfslabs.greece.data.Spawnpoints;
import com.rfslabs.greece.util.GeneralUtil;

public class CoreEvents implements Listener{

	@EventHandler
	public void onRespawnTeleport(PlayerRespawnEvent e){
		GeneralUtil.teleport(e.getPlayer(), Spawnpoints.townPermToLocation(e.getPlayer()), 5);
	}
	
}

package com.rfslabs.lobby.anarchy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		Data.p = this;
		
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		
		Data.w.setSpawnLocation((int)Data.spawn.getX(), (int)Data.spawn.getY(), (int)Data.spawn.getZ());
	}
	
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender , Command cmd , String label , String[] args){
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(!p.isOp()){
				return true;
			}
		}
		
		return true;
		
	}
	
}

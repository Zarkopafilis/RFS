package com.rfslabs.rbtp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	private World w;
	private Location l;
	
	@Override
	public void onEnable(){
		 w = Bukkit.getWorld("rainbow");
		 l = new Location(w , -1427 , 218 , 217 );
	}
	
	@Override
	public void onDisable(){
		
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3){
		
		if(arg2.equals("lobby")){
			
			final Player p = (Player) arg0;
			
			if(p.hasPermission("rfs.lobby")){
				int cd = 5;
				if(p.hasPermission("rfs.lobby.bypasscd") || p.isOp()){
					cd = 0;
				}else{
					p.sendMessage(ChatColor.RED + "Don't move , you will be teleported to lobby in " + cd + "seconds!");
				}
				final Location startloc = p.getLocation();
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){

					@Override
					public void run() {
						
						if(p.getLocation().getX() != startloc.getX() || p.getLocation().getZ() != startloc.getZ()){
							p.sendMessage(ChatColor.RED + "You moved! Teleportation cancelled!");
							return;
						}
						
						p.teleport(l);
						
						p.sendMessage(ChatColor.AQUA + "Welcome to RFS Lobby!");
						
					}
					
				}, cd * 20);
				
				
			}
			
		}
			
		return true;
	}
	
}

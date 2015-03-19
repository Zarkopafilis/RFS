package com.rfslabs.rfsdom.util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if(arg2.equals("stats")){
			
			if(arg0 instanceof Player){
				
				Player p = (Player) arg0;
				
				p.sendMessage(ChatColor.YELLOW + "Certs: "  + ConfigUtil.getCerts(p.getUniqueId()));
				p.sendMessage(ChatColor.YELLOW + "RFSPoints : "  + PointsLink.getRFSPts(p));
				p.sendMessage(ChatColor.YELLOW + "Level: "  + ConfigUtil.getLevel(p.getUniqueId()));
				p.sendMessage(ChatColor.YELLOW + "Rank: "  + ConfigUtil.getRank(p.getUniqueId()));
				
				return true;
			}
			
		}
		return false;
	}

}

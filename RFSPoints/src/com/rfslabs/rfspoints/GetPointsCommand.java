package com.rfslabs.rfspoints;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetPointsCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command arg1, String cmd,
			String[] args) {
		
		if(!cmd.equals("rfspoints")){
			return true;
		}
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			try {
				p.sendMessage(ChatColor.RED + "Your RFSPoints are : " + OnlineExMain.getOnlinePoints(p));
				p.sendMessage(ChatColor.RED + "Your Ancient Points are : " + OnlineExMain.getAncient(p));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return true;
		
	}
	
}

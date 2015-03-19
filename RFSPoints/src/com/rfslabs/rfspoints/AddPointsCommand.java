package com.rfslabs.rfspoints;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPointsCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String cmd,
			String[] args) {
		
		if(!cmd.equals("addpoints")){
			return true;
		}
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(!sender.isOp() || !sender.hasPermission("rfspoints.give")){
				p.sendMessage(ChatColor.RED + "No permission!");
				return true;
			}
			
		}
		
		if(args.length != 2){
			sender.sendMessage(ChatColor.RED + "Incorrect syntax!");
		}
		
		Player receiver = Bukkit.getPlayer(args[0]);
		
		if(receiver == null){
			sender.sendMessage(ChatColor.RED + "Receiver null , perhaps offline?");
			return true;
		}
		
		int amount = 0;
		
		try{
		
		amount = Integer.parseInt(args[1]);
		
		}catch(NumberFormatException e){
			sender.sendMessage(ChatColor.RED + "This is not a correct amount!(Not number!)");
			return true;
		}
		
		if(amount == 0){
			sender.sendMessage(ChatColor.RED + "SERIOUSLY?YOU ARE SO DUMB THAT YOU SET AMOUNT"
					+ " AS 0???");
		}
		try {
			OnlineExMain.addPoints(receiver, amount);
			return true;
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.RED + "Database error!");
			return true;
		}
	}

}

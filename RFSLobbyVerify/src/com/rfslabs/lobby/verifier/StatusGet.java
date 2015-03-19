package com.rfslabs.lobby.verifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StatusGet implements CommandExecutor{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		if(arg0.isOp()){
			arg0.sendMessage(Data.hosts.size() + " servers registered");
			HashMap hm = (HashMap) Data.hosts.clone();
			Iterator it = hm.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        if((Boolean) pairs.getValue()){
		        	arg0.sendMessage(ChatColor.GREEN + "" + pairs.getKey() + " => Online");
		        }else{
		        	arg0.sendMessage(ChatColor.RED + "" + pairs.getKey() + " => Offine");
		        }
		        //it.remove();
		    }
		}
		
		return true;
	}

}

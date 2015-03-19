package com.rfslabs.lobby.verifier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddHost implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if(arg0.isOp()){
			
			if(arg2.equalsIgnoreCase("add3rdpartyhost")){
				
				if(arg3.length >= 1){

					int i = 1;
					for(;;){
						
						if(Data.p.getConfig().get("host."+ i +".ip") != null){
							i++;
						}else{
							
							break;
						}
					}
					
					Data.p.getConfig().set("host." + i + ".ip", arg3[0]);
					Data.hosts.put(arg3[0], true);
					Data.p.saveConfig();
					
				}
				
			}
			
		}
		return false;
	}

}

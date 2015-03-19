package com.rfslabs.lobby.verifier;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignBinder implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		if(arg2.equals("signbind")){
		Player p = (Player) arg0;
		
		if(!p.isOp()){
			return true;
		}
		if(arg3.length == 2){
			
			Block b = p.getTargetBlock(null, 200);
			
			if(b.getState() instanceof Sign){
				
			Sign sign = (Sign) b.getState();
			
			String ip = arg3[0];
			int port = Integer.parseInt(arg3[1]);

			int i = 1;
			
			for(;;){
				
				String s = Data.p.getConfig().getString("bind."+ i + ".x");
				
				if(s != null){				
				i++;
				}else{
					break;
				}
			}
			
			Data.p.getConfig().set("bind." + i + ".x", sign.getX());
			Data.p.getConfig().set("bind." + i + ".y", sign.getY());
			Data.p.getConfig().set("bind." + i + ".z", sign.getZ());
			
			Data.p.getConfig().set("bind." + i + ".ip", ip);
			Data.p.getConfig().set("bind." + i + ".port", port);
			Data.p.saveConfig();
			
			Data.sbps.add(new SignBindPack(sign ,
					new MinecraftServer(Data.p.getConfig().getString("bind."+ i +".ip") , Data.p.getConfig().getInt("bind."+ i +".port"))));
			
		}
		}
		}
		return true;
	}

}

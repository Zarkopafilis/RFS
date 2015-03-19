package com.rfslabs.lobby.verifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");	
		
		Data.p = this;
		
		Bukkit.getPluginManager().registerEvents(new ThirdPartyJoinEvent(), this);
		
		setUpHosts();
		
		timers();
		
		getCommand("lobbystatus").setExecutor(new StatusGet());
		
		getCommand("signbind").setExecutor(new SignBinder());
		
		getCommand("add3rdpartyhost").setExecutor(new AddHost());
		
	}
	
	@Override
	public void onDisable(){
		
	}
	
	private void setUpHosts(){
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable(){

		public void run(){
		Data.world = Bukkit.getWorld("rainbow");
		
		int i = 1;
		
		for(;;){
			
			if(Data.p.getConfig().get("bind."+ i +".x") != null){
				
			Block b = Data.world.getBlockAt(Data.p.getConfig().getInt("bind."+ i +".x"), Data.p.getConfig().getInt("bind."+ i +".y"), Data.p.getConfig().getInt("bind."+ i +".z"));
			
			if(b.getState() instanceof Sign){
				//Bukkit.getLogger().log(Level.SEVERE, "SIGN REGISRTATION:");
				Sign s = (Sign) b.getState();
				Data.sbps.add(new SignBindPack(s ,
						new MinecraftServer(Data.p.getConfig().getString("bind."+ i +".ip") , Data.p.getConfig().getInt("bind."+ i +".port"))));
			}else{
				//Bukkit.getLogger().log(Level.SEVERE, "BLOCK IS NOT SIGN AT " + b.getX() + " " + b.getY() + " "+ b.getZ());
			}
			i++;
			
			}else{
				//Bukkit.getLogger().log(Level.SEVERE, "SFIN" + " " + i);
				break;
			}
		}
		
		i = 1;
		
		for(;;){
			
			if(Data.p.getConfig().get("host."+ i +".ip") != null){
				//Data.hosts.put(Data.p.getConfig().getString("host." + i + ".ip"), true);
				Bukkit.getLogger().log(Level.SEVERE, "HOST REGISRTATION " + Data.p.getConfig().get("host."+ i +".ip"));
				Data.hosts.put(Data.p.getConfig().getString("host."+ i +".ip"), true);
				i++;
			}else{
				break;
			}
		}
		}
		}, 20);
		//Data.hosts.put("host:25565", true);
		
	}
	
	private void timers(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				HashMap hm = (HashMap) Data.hosts.clone();
				Iterator it = hm.entrySet().iterator();
			    while (it.hasNext()) {
			    	//Bukkit.getLogger().log(Level.SEVERE, "it thread start");
			        Map.Entry pairs = (Map.Entry)it.next();
			        //Bukkit.getLogger().log(Level.SEVERE, pairs.getKey().toString() + " new thread");
			        new KeepAliveThread((String) pairs.getKey()).start();
			       // it.remove();
			    }
				
			}
			
			
			
		}, 200, Data.timer * 60 * 20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				//int i = 1;
				for(SignBindPack s : Data.sbps){
					//Bukkit.getLogger().log(Level.SEVERE , s.s.getTypeId() + "");
					//Bukkit.getLogger().log(Level.SEVERE , "" + i);
					boolean online = s.server.fetchData();
					//Bukkit.getLogger().log(Level.SEVERE , "adfadfsfdsaadfs");
					if(!online){
						//Bukkit.getLogger().log(Level.SEVERE , "server online");
						Sign sign = s.s;
						sign.setLine(1, ChatColor.RED + "Offline");
						sign.update(true);
						//s.s.setLine(2, "");
						//s.s.setLine(3, "");
						
					}else{
						//Bukkit.getLogger().log(Level.SEVERE , "server offline");
						Sign sign = s.s;
						sign.setLine(1, ChatColor.GREEN + "" + s.server.getPlayersOnline() + "" + ChatColor.RESET + "/" + s.server.getMaxPlayers());
						sign.update(true);
						
					}
					
					//s.s.update(true);
					//i++;
				}
				
			}
			
			
			
		}, 100,20);
	}
	
}

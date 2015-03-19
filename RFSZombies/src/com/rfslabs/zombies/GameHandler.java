package com.rfslabs.zombies;

import java.io.DataOutputStream;

import java.io.IOException;

import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.rfslabs.zombies.event.GunBuy;
import com.rfslabs.zombies.util.Data;
import com.rfslabs.zombies.util.Database;
import com.rfslabs.zombies.util.Locations;
import com.rfslabs.zombies.util.ZombieSpawner;

public class GameHandler {

	public static void startCountDown() {
		new Thread(){
			
			public void run(){
				try{
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 30 seconds!");
				sleep(15000);
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 15 seconds!");
				sleep(5000);
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 10 seconds!");
				sleep(5000);
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 5 seconds!");
				sleep(2000);
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 3 seconds!");
				sleep(1000);
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 2 seconds!");
				sleep(1000);
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Starting in 1 seconds!");
				
				if(Bukkit.getServer().getOnlinePlayers().length >= 1){
					Data.started = true;
					Data.started_cd = false;
						startGame();
				}else{
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "Not enough players!");
					Data.started = false;
					Data.started_cd = false;
					Data.finished = false;
				}
				
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}.start();
	}

	public static void startGame(){
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable() {
		    public void run(){
				
				for(Player p : Bukkit.getOnlinePlayers()){
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + p.getName() + " Nacht_Der_Untoten");
					GunBuy.gun(p, "pistol");
					p.getInventory().setItem(1, Data.knife);
					p.setFoodLevel(20);
				}
				
				for(Entity e : Data.w.getEntities()){
					if(!(e instanceof Player)){
						e.remove();
					}
				}
				
				newRound();
		    }
		}, 1);		
		
	}
	
	public static void newRound() {
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable() {
		    public void run(){
		    	
		    	for(Player p : Data.dead){
					Data.dead.remove(p);
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + p.getName() + " Nacht_Der_Untoten");
					Data.bux.put(p, 500);
					GunBuy.gun(p, "pistol");
				}
		    	
		    	for(Player p : Bukkit.getServer().getOnlinePlayers()){
		    		p.sendMessage(ChatColor.GREEN + "Your bux = " + Data.bux.get(p));
		    	}
		
		    	Bukkit.getServer().broadcastMessage(ChatColor.RED +  "Round " + Data.level + " starting in 10 seconds!");
		
		    }
		},1);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable() {
		    public void run(){
		
		
		
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "Round " + Data.level + " starting now!");
		
		Data.dead.clear();
		Data.zombieCount = Data.level * Bukkit.getServer().getOnlinePlayers().length;
		//Bukkit.getServer().broadcastMessage("" + Data.zombieCount);
		ZombieSpawner.spawn(Data.level * Bukkit.getServer().getOnlinePlayers().length);
		
		    }
		}, 200 );
		
	}

	public static void endGame(){
		
		Data.finished = true;
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable() {
		    @SuppressWarnings("deprecation")
			public void run(){
		    	
		    	Database.addPoints(Data.level);
		    	
		    }
		}, 1);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.p, new Runnable() {
		    @SuppressWarnings("deprecation")
			public void run(){
		
		//Database.addPoints(Data.level);
		
		for(Player p: Bukkit.getServer().getOnlinePlayers()){
			kick(p);
		}
		
		for(Block block : Data.doors){
			if(block.getData() > (byte) 4)
			block.setData((byte) (block.getData() - 4));
		}
		
		Data.level = 1;
		Data.started = false;
		Data.finished = false;
		
		    }
		}, 40);
		
		//Bukkit.getServer().reload();
		
	}
	
	public static void kick(Player p){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
	    DataOutputStream out = new DataOutputStream(b);
	                   
	    try {
	          out.writeUTF("Connect");
	          out.writeUTF("rainbow");
	    } catch (IOException ex) {
	                       
	    }
	    p.sendPluginMessage(Data.p, "BungeeCord", b.toByteArray());
	}
	
}

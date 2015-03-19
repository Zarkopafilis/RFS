package com.rfslabs.towerrun;

import java.io.DataOutputStream;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;//cant be resolved idk why , maybe maven

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TowerRun extends Thread{
	
	
	public void run(){

		//DAO.can_move = true;
		
		for(int i = 10 ; i > 0 ; i-- ){
			Bukkit.getServer().broadcastMessage(ChatColor.RED + "Starting in " + i + " seconds!");
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(Bukkit.getOnlinePlayers().length < DAO.player_threshold){
			DAO.started = false;
			return;
		}
		
		DAO.started = false;
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DAO.plugin, new Runnable() {
		    public void run(){
		    	for(Player p : Bukkit.getOnlinePlayers()){
					p.teleport(DAO.run_loc);
					p.setGameMode(GameMode.SURVIVAL);
					p.setFoodLevel(20);
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION , Integer.MAX_VALUE , 3));
				}
		    }
		}, 1);	
		
		
		
	//	DAO.can_move = true;
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "Run for your life!");
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Last man standing gets an RFS Point!");
		try {
			sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		int y = 0;
		
		int y_var = (int) (DAO.two.getY() + y);
		
		int min_x = (int) DAO.two.getX();
		int min_z = (int) DAO.two.getZ();
		
		int max_x = (int) DAO.one.getX();
		int max_z = (int) DAO.one.getZ();
		
		while(DAO.started && y <= DAO.one.getY() - DAO.two.getY()){
			//Bukkit.getServer().broadcastMessage("Y offset : " + y);
			try {
				sleep(DAO.time * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			
			final int x_min_use = min_x;
			final int z_min_use = min_z;
			
			final int x_max_use = max_x;
			final int z_max_use = max_z;
			
			final int y_use = y_var;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(DAO.plugin, new Runnable(){
				
			public void run(){
				
			for(int x = x_min_use; x <= x_max_use; x++){
			for(int z = z_min_use; z <= z_max_use; z++){	
				Util.rmBlock(x , y_use , z);
			}
			}	
			
			}
			
			} , 1);
			
			y++;
			y_var++;
			
			final int y_varf = y_var;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DAO.plugin, new Runnable() {
			    public void run(){
					for(Player p : Bukkit.getOnlinePlayers()){
							if(p.getLocation().getY() < y_varf - 2){
								p.setHealth(0);
							}
					}
			    }
			}, 1);		
			

			if(DAO.alive.size() <= DAO.win_threshold){
				DAO.started = false;
			}
		}
		
		winners();
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		endGame();
		
	}

	private void endGame() {
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DAO.plugin, new Runnable() {
		    public void run(){

		    	DAO.alive.clear();
				DAO.dead.clear();
				
				for(Player p : Bukkit.getOnlinePlayers()){
					kick(p);
				}
				
				Util.rewind();
				
				//Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "reload");
				
				
				DAO.started = false;
				DAO.can_move = true;
		    	
		    }
		}, 1);		
		
	}

	private void winners() {
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DAO.plugin, new Runnable() {
		    public void run(){
		    	
		    	for(UUID id : DAO.alive){
					Player p = Bukkit.getPlayer(id);
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage("You are a winner! + " +  DAO.reward + " RFSPoints!");
				}
		    	
		    }
		}, 1);	
		
		
		
	}
	
	private static void kick(Player p){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
	    DataOutputStream out = new DataOutputStream(b);
	                   
	    try {
	          out.writeUTF("Connect");
	          out.writeUTF("rainbow");
	    } catch (IOException ex) {
	                       
	    }
	    p.sendPluginMessage(DAO.plugin, "BungeeCord", b.toByteArray());
	}
	
	
}

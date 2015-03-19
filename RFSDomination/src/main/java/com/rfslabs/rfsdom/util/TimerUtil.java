package com.rfslabs.rfsdom.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.rfslabs.rfsdom.multithreading.OutpostLooper;

public class TimerUtil {
	
	/*
	 * Timer setup
	 */
	public static void setUp(){
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		
        scheduler.scheduleSyncRepeatingTask(MainDAO.plugin, new OutpostLooper() , 60L, MainDAO.outpost_timer * 20);
        scheduler.scheduleSyncRepeatingTask(MainDAO.plugin, new ParticleStuff() , 60L, 10L);
        /*scheduler.scheduleSyncRepeatingTask(MainDAO.plugin, new Runnable(){

			@Override
			public void run() {
				
				LoopingUtil.hunger();
				
			}
        	
        	
        	
        }, 60L, 100L);*/
        
        scheduler.scheduleSyncRepeatingTask(MainDAO.plugin, new Runnable(){

			@Override
			public void run() {
				
				LoopingUtil.water();
				
			}
        	
        	
        	
        }, 10L, 20L);
        
        scheduler.scheduleSyncRepeatingTask(MainDAO.plugin, new Runnable(){

			@Override
			public void run() {
				
				LoopingUtil.drops();
				
			}
        	
        	
        	
        }, 10L, 100L);
        
        scheduler.scheduleSyncRepeatingTask(MainDAO.plugin, new Runnable(){

			@Override
			public void run() {
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()){
					ShieldHunger.startRegen(p);
				}
				
			}
        	
        	
        	
        }, 10L, 100L);
        
	}
	
}

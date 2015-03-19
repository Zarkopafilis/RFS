package com.rfslabs.zombies;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.rfslabs.zombies.event.EntityStuff;
import com.rfslabs.zombies.event.GunBuy;
import com.rfslabs.zombies.event.GunHit;
import com.rfslabs.zombies.event.GunShoot;
import com.rfslabs.zombies.event.PreGame;
import com.rfslabs.zombies.util.Data;
import com.rfslabs.zombies.util.Database;
import com.rfslabs.zombies.util.Locations;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		 Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		Data.p = this;
		
		Database.init();
		
		Data.initGuns();
		Locations.setUp();
		
		Bukkit.getServer().getPluginManager().registerEvents(new EntityStuff(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PreGame(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GunShoot(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GunHit(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GunBuy(), this);
		
	}
	
	@Override
	public void onDisable(){
		
		try {
			Database.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}

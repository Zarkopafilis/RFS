package com.rfslabs.rfsdom.core;

import java.io.File;
import java.io.FileWriter;

import org.bukkit.plugin.java.JavaPlugin;

import com.rfslabs.rfsdom.util.ConfigUtil;
import com.rfslabs.rfsdom.util.GeneralUtil;
import com.rfslabs.rfsdom.util.LogUtil;
import com.rfslabs.rfsdom.util.MainDAO;
import com.rfslabs.rfsdom.util.PointsLink;
import com.rfslabs.rfsdom.util.Stats;
import com.rfslabs.rfsdom.util.Sunderer;
import com.rfslabs.rfsdom.util.TimerUtil;

public class App extends JavaPlugin{
	
	public void onEnable(){
		LogUtil.log("Loading plugin");
		
		LogUtil.log("Setting up requirements");
		MainDAO.plugin = this;
		MainDAO.setUp();
		GeneralUtil.setUpRequirements();
		
		LogUtil.log("Loading configuration/DB");
		ConfigUtil.loadEverything();
		PointsLink.startup();
		
		LogUtil.log("Setting up timers");
		TimerUtil.setUp();
		
		LogUtil.log("Registering events & commands");
		GeneralUtil.registerEvents();
		setUpCmds();
		
		
		LogUtil.log("Successfully loaded");
	}
	
	public void onDisable(){
		LogUtil.log("Closing database connection");
		//DatabaseUtil.closeDatabase();
		
		LogUtil.log("Closing and saving stuff");
		GeneralUtil.housekeeping();
		
		LogUtil.log("Saving configuration");
		ConfigUtil.saveEverything();
		
		try{
		File f = new File(this.getDataFolder().getAbsolutePath().toString().concat(File.separator + "blockeys.txt"));
		LogUtil.log("Writing blockeys.txt");
		if(!f.exists()){
			f.createNewFile();
		}
		
		FileWriter fw = new FileWriter(f);
		
		for(String ln : MainDAO.ls){
			
			fw.append(ln + System.getProperty("line.separator"));
			LogUtil.log(ln);
			
		}
		
		fw.close();
		
		}catch(Exception e ){
			
		}
		
		
		LogUtil.log("done");
	}
	
	public void onReload(){
		
	}
	
	private void setUpCmds(){
		
		this.getCommand("stats").setExecutor(new Stats());
		this.getCommand("sunderer").setExecutor(new Sunderer());
		
	}
	
}

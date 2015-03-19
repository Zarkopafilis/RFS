package com.rfslabs.towerrun;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class Util {
	
	public static void load(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver"); //not sure if needed
			DAO.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//DAO.player_threshold = DAO.plugin.getConfig().getInt("player-threshold");
		DAO.player_threshold = 5;
		//DAO.win_threshold = DAO.plugin.getConfig().getInt("win-threshold");
		DAO.win_threshold = 1;
		//DAO.time = DAO.plugin.getConfig().getInt("sleep-time-in-secs");
		DAO.time = 2;
		//DAO.reward = DAO.plugin.getConfig().getInt("reward-points");
		DAO.reward = 1;
		//DAO.world = Bukkit.getWorld(DAO.plugin.getConfig().getString("world-name"));
		DAO.world = Bukkit.getWorld("world");
		//DAO.run_loc = new Location(DAO.world  , DAO.plugin.getConfig().getInt("loc.run.x") , DAO.plugin.getConfig().getInt("loc.run.y") , DAO.plugin.getConfig().getInt("loc.run.z")); 
		DAO.run_loc = new Location(DAO.world  , -126, 69 , 156);
		//DAO.start_loc = new Location(DAO.world  , DAO.plugin.getConfig().getInt("loc.start.x") , DAO.plugin.getConfig().getInt("loc.start.y") , DAO.plugin.getConfig().getInt("loc.start.z"));
		DAO.start_loc = new Location(DAO.world  , -157 ,73 , 135);
		//DAO.losers = new Location(DAO.world  , DAO.plugin.getConfig().getInt("loc.lose.x") , DAO.plugin.getConfig().getInt("loc.lose.y") , DAO.plugin.getConfig().getInt("loc.lose.z"));
		DAO.losers = new Location(DAO.world  , -110 , 150 , 156);
		//DAO.one = new Location(DAO.world  , DAO.plugin.getConfig().getInt("loc.one.x") , DAO.plugin.getConfig().getInt("loc.one.y") , DAO.plugin.getConfig().getInt("loc.one.z"));
		DAO.one = new Location(DAO.world  , -110 , 147 , 170);
		//DAO.two = new Location(DAO.world  , DAO.plugin.getConfig().getInt("loc.two.x") , DAO.plugin.getConfig().getInt("loc.two.y") , DAO.plugin.getConfig().getInt("loc.two.z"));
		DAO.two = new Location(DAO.world  , -136 , 65 , 150);
		
	//	DAO.backup = new File("plugins" + File.separator + "backup_maps" + File.separator + "world");
	//	DAO.to_reset = new File("world");
	}
	
	public static void copyFolder(File src, File dest)
	    	throws IOException{
	 
	    	if(src.isDirectory()){
	 
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		  // System.out.println("Directory copied from " 
	             //                 + src + "  to " + dest);
	    		}
	 
	    		//list all the directory contents
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	 
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	        System.out.println("File copied from " + src + " to " + dest);
	    	}
	    }
	
	public static void quit(){
		
		try {
			DAO.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	static void rmBlock(int x, int y, int z) {
		
		//Location loc = new Location(DAO.world , x , y , z);
		
		//loc.getBlock().breakNaturally();
		Block block = DAO.world.getBlockAt(x, y, z);
		DAO.bs.put(block, block.getType());
		block.breakNaturally();
	    //block.setType(Material.AIR);
	}


	public static void addPoint(Player p , int amount){
		
		Statement s;
		try {
			s = DAO.con.createStatement();
			s.executeUpdate("UPDATE onlinestuff SET onlinePoints="+ (getPoints(p) + amount) + " WHERE name='" + p.getName() +"'");
			
			s.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	@SuppressWarnings("rawtypes")
	public static void rewind(){
		
		
		 
		 for(int i = 0 ; i < 2 ; i++){
			 
			 HashMap<Block , Material> bs_ = (HashMap<Block, Material>) DAO.bs.clone();
				
			 Iterator it = bs_.entrySet().iterator();
			 
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Block b = (Block) pairs.getKey();
		        Material m = (Material) pairs.getValue();
		        b.setType(m);
		        it.remove();
		    }
		 }
		
		DAO.bs.clear();
	}
	
	
	private static int getPoints(Player p) {
		
		try {
			Statement s = DAO.con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT onlinePoints FROM onlinestuff WHERE name='"+ p.getName() +"'");
			
			while(rs.next()){
				
				int a = rs.getInt("onlinePoints");
				
				s.close();
				
				rs.close();
				
				return a;
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}

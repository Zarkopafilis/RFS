package com.rfslabs.lobby.kitpvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Data {

	public static Plugin p;
	
	public static World w = Bukkit.getWorld("world");
	
	public static Location spawn1 = new Location(w, -110 , 77 , 70);
	public static Location spawn2 = new Location(w, -141 , 72 , 161);
	public static Location spawn3 = new Location(w, -161 , 76 , 215);
	
	public static ConcurrentHashMap<String , InvPack> items = new ConcurrentHashMap<String, InvPack>();

	public static List<UUID> chose = new ArrayList<UUID>();
	public static HashMap<UUID , String> kit = new HashMap<UUID , String>();
	
	//public static List<UUID> d_jump = new ArrayList<UUID>();
	
	
}

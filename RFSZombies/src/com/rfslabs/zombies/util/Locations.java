package com.rfslabs.zombies.util;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Locations {

	public static List<Location> zombies = new ArrayList<Location>();
	//public static List<Location> zombies = new ArrayList<Location>();
	public static Location lobby;
	public static Location spawn;
	public static Location doom_box;
	
	public static void setUp(){
		lobby = new Location(Bukkit.getWorld("world"), 279 , 74 , 229);
		spawn = new Location(Data.w , -45 , 7 , -42);
		doom_box = new Location(Data.w , -45 , 12 , -42);
		zombies.add(new Location(Data.w , -48 , 8 , -55));
		zombies.add(new Location(Data.w , -56 , 8 , -21));
		zombies.add(new Location(Data.w , -83 , 8 , -23));
		zombies.add(new Location(Data.w , -32 , 8 , -40));
	}
	
}

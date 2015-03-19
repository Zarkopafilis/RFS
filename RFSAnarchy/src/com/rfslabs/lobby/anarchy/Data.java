package com.rfslabs.lobby.anarchy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Data {

	public static Plugin p;
	
	public static World w = Bukkit.getWorld("world");
	
	public static Location first_spawn = new Location(w , -237 , 170 , 84);
	public static Location spawn = new Location(w , -242 , 94 , 84);

}

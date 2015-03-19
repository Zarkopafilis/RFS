package com.rfslabs.battlearena.ext;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Data {
	
	public static Plugin p;

	public static final World w = Bukkit.getWorld("world");
	
	public static final Location pb = new Location(w , -755	, 8 , 1135);
	
	public static final Location spl = new Location(w , -800, 8 , 994);
	
	public static final Location lobby = new Location(w , -859 ,4 , 1107);
	
	public static final Location ctf = new Location(w , 0, 0 , 0);
	
}

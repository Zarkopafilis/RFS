package com.rfslabs.towerrun;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class DAO {

	public static Plugin plugin;
	
	public static File backup;
	public static File to_reset;
	
	public static int player_threshold = 4;
	public static int win_threshold = 1;
	
	public static World world;
	
	public static int time = 1;
	
	public static boolean started = false;
	public static boolean can_move = true;
	
	public static List<UUID> alive = new ArrayList<UUID>();
	public static List<UUID> dead = new ArrayList<UUID>();
	public static HashMap<Block , Material> bs = new HashMap<Block , Material>();
	
	public static int reward = 1;
	
	public static Location start_loc;
	public static Location run_loc;
	public static Location losers;
	
	public static Connection con;
	
	public static Location one;
	public static Location two;
	
}

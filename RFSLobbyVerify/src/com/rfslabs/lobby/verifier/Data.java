package com.rfslabs.lobby.verifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Data {

	public static Plugin p;
	
	public static HashMap<String , Boolean> hosts = new HashMap<String , Boolean>();
	public static List<SignBindPack> sbps = new ArrayList<SignBindPack>();
	public static List<UUID> cnct = new ArrayList<UUID>();
	
	public static long timer = 1;//in minutes
	public static World world;
	
}

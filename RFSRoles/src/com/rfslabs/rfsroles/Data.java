package com.rfslabs.rfsroles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Data {

	public static int creeper_karma_reduce_quantity = 50;
	public static int karma_on_kill = 100;
	public static int karma_factor = 1;
	
	public static ConcurrentHashMap<UUID , Integer> combat = new ConcurrentHashMap<UUID , Integer>();
	public static ConcurrentHashMap<UUID , Integer> xp = new ConcurrentHashMap<UUID , Integer>();
	public static ConcurrentHashMap<UUID , ItemStack[]> items = new ConcurrentHashMap<UUID , ItemStack[]>();          
	public static ConcurrentHashMap<UUID , ItemStack[]> armor = new ConcurrentHashMap<UUID , ItemStack[]>();

	public static int combat_time = 5;
	public static Plugin plugin;
	public static int level_xp = 20;

	public static List<String> warcry = new ArrayList<String>();
	public static List<String> cooldown = new ArrayList<String>();
	public static List<String> slow = new ArrayList<String>();
	public static List<String> bleed = new ArrayList<String>();
	public static List<String> arrow = new ArrayList<String>();
	public static List<String> damager = new ArrayList<String>();
	public static List<String> karma = new ArrayList<String>();
	
	public static void init(){
	}
		
}


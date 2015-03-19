package com.rfslabs.zombies.util;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiversePlugin;
import com.onarandombox.MultiverseCore.utils.WorldManager;

public class Data {

	private Data(){}
	
	public static World w;
	public static Plugin p;
	public static boolean started = false;
	public static boolean started_cd = false;
	public static boolean finished = false;
	public static int level = 1;
	public static int zombieCount = 0;
	
	public static ConcurrentHashMap<Player , Integer> damage = new ConcurrentHashMap<Player , Integer>();
	public static ConcurrentHashMap<Player , Integer> bux = new ConcurrentHashMap<Player , Integer>();
	public static ConcurrentHashMap<String , Gun> guns = new ConcurrentHashMap<String , Gun>();
	
	public static ItemStack gun_ammo;//slot 8
	public static ItemStack mag;//slot 7
	public static ItemStack grenade;//slot 4
	public static ItemStack molotov;//slot 4
	public static Gun starter_pistol;//slot 0
	
	public static ItemStack bullet;
	public static ItemStack knife;
	
	public static List<Player> dead = new ArrayList<Player>();
	public static List<Player> reloading = new ArrayList<Player>();
	
	public static List<ItemStack> mystery_box = new ArrayList<ItemStack>();
	public static List<Block> doors = new ArrayList<Block>();
	
	public static void initGuns(){
		
		finished = false;

		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv load Nacht_Der_Untoten");
		
		w = Bukkit.getWorld("Nacht_Der_Untoten");
		
		knife = new ItemStack(Material.IRON_SWORD);
		knife.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
		
		bullet = new ItemStack(Material.BAKED_POTATO);
		grenade = new ItemStack(Material.POTATO);
		molotov = new ItemStack(Material.POISONOUS_POTATO);
		gun_ammo = new ItemStack(Material.APPLE);
		mag = new ItemStack(Material.BRICK);
		
		starter_pistol = new Gun(Material.GOLD_HOE);
		starter_pistol.setTitle("Pistol");
		starter_pistol.setDamage(4);
		starter_pistol.setMax_ammo(20);
		starter_pistol.setAmmo_per_reload(18);
		guns.put("pistol",(Gun) starter_pistol);
		
		mystery_box.add(starter_pistol);
		
		Gun g = new Gun(Material.WOOD_HOE);
		g.setTitle("MG 3");
		g.setDamage(7);
		g.setMax_ammo(0);
		g.setAmmo_per_reload(500);
		mystery_box.add((Gun)g);
		
	    g = new Gun(Material.STONE_HOE);
		g.setTitle("Lazor");
		g.setDamage(5);
		g.setMax_ammo(0);
		g.setAmmo_per_reload(0);
		mystery_box.add((Gun)g);
		
		mystery_box.add(new ItemStack(molotov.getType() , 4));
		
		 g = new Gun(Material.DIAMOND_HOE);
		 g.setTitle("Thomson");
		 g.setDamage(7);
		 g.setMax_ammo(3);
		 g.setAmmo_per_reload(30);
		 guns.put("thomson", (Gun)g);
		 
		 g = new Gun(Material.WOOD_SPADE);
		 g.setTitle("Dragunov");
		 g.setDamage(40);
		 g.setMax_ammo(3);
		 g.setAmmo_per_reload(3);
		 guns.put("dragunov",(Gun) g);
		 
		 g = new Shotgun(Material.STONE_SPADE);
		 g.setTitle("Shotgun");
		 g.setDamage(40);
		 g.setMax_ammo(3);
		 g.setAmmo_per_reload(5);
		 guns.put("shotgun", (Gun)g);
		 
		 g = new Gun(Material.STONE_SWORD);
		 g.setTitle("Rifle (M1)");
		 g.setDamage(10);
		 g.setMax_ammo(3);
		 g.setAmmo_per_reload(5);
		 guns.put("rifle", (Gun)g);
		 
		 finished = false;
	}
	
	public static void doDoor(int id){
		switch(id){
		case 1:
			Locations.zombies.add(new Location(Data.w , -83 , 7 , -78));
			Locations.zombies.add(new Location(Data.w , -92 , 8 , -61));
			break;
		case 2:	
			Locations.zombies.add(new Location(Data.w , -74 , 19 , -57));
			Locations.zombies.add(new Location(Data.w , -81 , 19 , -42));
			break;
		default:
			break;
		}
	}
}

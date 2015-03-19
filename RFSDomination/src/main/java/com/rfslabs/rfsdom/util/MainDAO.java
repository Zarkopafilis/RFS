package com.rfslabs.rfsdom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class MainDAO {
	/*
	 * The main data access object
	 */
	
	public static ItemStack ammo = new ItemStack(Material.CLAY_BRICK);
	public static ItemStack tr_smg_a,vn_smg_a,nc_smg_a,tr_shot_a,vn_shot_a,nc_shot_a,tr_baz_a,vn_baz_a,nc_baz_a,tr_sn_a,vn_sn_a,nc_sn_a ,
	tr_smg,vn_smg,nc_smg,tr_shot,vn_shot,nc_shot,tr_baz,vn_baz,nc_baz,tr_sn,vn_sn,nc_sn,
	sb,egg,grenade,coal,tr_kn,vn_kn,nc_kn, mag , sunderer , healer;
	
	public static boolean alert = true;
	
	public static World world;
	public static Location tutorialLoc,terran_republicSpawn,vanu_sovereigntySpawn,new_conglomerateSpawn;
	public static Plugin plugin;
	
	public static List<String> ls = new ArrayList<String>();
	
	//public static List<UUID> regen = new ArrayList<UUID>();
	
	public static int pow = 2;
	
	public static List<Generator> generators = new ArrayList<Generator>();
	
	public static List<UUID> reloading = new ArrayList<UUID>();
	
	public static List<UUID> cant_ability = new ArrayList<UUID>();
	
	public static List<UUID> cant_shoot = new ArrayList<UUID>();
	
	public static List<UUID> regening = new ArrayList<UUID>();
	
	/* 
	 * Player Name , Player Team
	 * Name: Any
	 * Team : "terran_republic","green","blue","none" 
	 */
	public static HashMap<UUID , String> players = new HashMap<UUID , String>();
	
	public static HashMap<UUID , String> armor_up = new HashMap<UUID , String>();
	
	public static HashMap<UUID , String> classes = new HashMap<UUID , String>();
	
	public static HashMap<UUID , ItemStack> guns = new HashMap<UUID , ItemStack>();
	
	public static HashMap<UUID , ItemStack> ammos = new HashMap<UUID , ItemStack>();
	
	public static HashMap<UUID , Location> sandy = new HashMap<UUID , Location>();
	
	public static List<UUID> can_sandy = new ArrayList<UUID>();
	
	public static List<UUID> can_tp_sandy = new ArrayList<UUID>();
	
	/*
	 * List of the outposts including their coordinates and other info
	 */
	public static List<Outpost> outposts = new ArrayList<Outpost>();
	
	public static int gen_rld,outpost_timer;
	
	public static int lvl_up_kills;
	public static int throwup;	
	
	public static void setUp(){
		
		List<String> lr = new ArrayList<String>();
		
		healer = new ItemStack(Material.WHEAT, 1);
		ItemMeta healer_m = healer.getItemMeta();
		healer_m.setDisplayName("Medic Gun");
		healer.setItemMeta(healer_m);
		
		sunderer = new ItemStack(Material.PAPER, 1);
		ItemMeta sunderer_m = sunderer.getItemMeta();
		sunderer_m.setDisplayName("Sunderer");
		sunderer.setItemMeta(sunderer_m);
		
		mag = new ItemStack(Material.CLAY_BALL, 4);
		ItemMeta mag_m = mag.getItemMeta();
		mag_m.setDisplayName("Magazine");
		mag.setItemMeta(mag_m);
		
		sb = new ItemStack(Material.SNOW_BALL);
		egg = new ItemStack(Material.EGG);
		coal = new ItemStack(Material.COAL);
		
		grenade = new ItemStack(Material.SLIME_BALL);
		ItemMeta grenade_m = grenade.getItemMeta();
		grenade_m.setDisplayName("Grenade");
		grenade.setItemMeta(grenade_m);
		
		tr_smg = new ItemStack(Material.WOOD_PICKAXE, 1);
		ItemMeta tr_smg_m = tr_smg.getItemMeta();
		tr_smg_m.setDisplayName("TR SMG");
		tr_smg.setItemMeta(tr_smg_m);
		
		vn_smg = new ItemStack(Material.STONE_PICKAXE, 1);
		ItemMeta vn_smg_m = vn_smg.getItemMeta();
		vn_smg_m.setDisplayName("VN SMG");
		vn_smg.setItemMeta(vn_smg_m);
		
		nc_smg = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemMeta nc_smg_m = nc_smg.getItemMeta();
		nc_smg_m.setDisplayName("NC SMG");
		nc_smg.setItemMeta(nc_smg_m);
		
		tr_sn = new ItemStack(Material.WOOD_HOE, 1);
		ItemMeta tr_sn_m = tr_sn.getItemMeta();
		tr_sn_m.setDisplayName("TR Sniper");
		tr_sn.setItemMeta(tr_sn_m);
		
		vn_sn = new ItemStack(Material.STONE_HOE, 1);
		ItemMeta vn_sn_m = vn_sn.getItemMeta();
		vn_sn_m.setDisplayName("VN Sniper");
		vn_sn.setItemMeta(vn_sn_m);
		
		nc_sn = new ItemStack(Material.IRON_HOE, 1);
		ItemMeta nc_sn_m = nc_sn.getItemMeta();
		nc_sn_m.setDisplayName("NC Sniper");
		nc_sn.setItemMeta(nc_sn_m);
		
		tr_baz = new ItemStack(Material.WOOD_AXE, 1);
		ItemMeta tr_baz_m = tr_baz.getItemMeta();
		tr_baz_m.setDisplayName("TR Bazooka");
		tr_baz.setItemMeta(tr_baz_m);
		
		vn_baz = new ItemStack(Material.STONE_AXE, 1);
		ItemMeta vn_baz_m = vn_baz.getItemMeta();
		vn_baz_m.setDisplayName("VN Bazooka");
		vn_baz.setItemMeta(vn_baz_m);
		
		nc_baz = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta nc_baz_m = nc_baz.getItemMeta();
		nc_baz_m.setDisplayName("NC Bazooka");
		nc_baz.setItemMeta(nc_baz_m);
		
		tr_shot = new ItemStack(Material.WOOD_SPADE, 1);
		ItemMeta tr_shot_m = tr_shot.getItemMeta();
		tr_shot_m.setDisplayName("TR Shotgun");
		tr_shot.setItemMeta(tr_shot_m);
		
		vn_shot = new ItemStack(Material.STONE_SPADE, 1);
		ItemMeta vn_shot_m = tr_shot.getItemMeta();
		vn_shot_m.setDisplayName("VN Shotgun");
		vn_shot.setItemMeta(vn_shot_m);
		
		nc_shot = new ItemStack(Material.IRON_SPADE, 1);
		ItemMeta nc_shot_m = nc_shot.getItemMeta();
		nc_shot_m.setDisplayName("NC Shotgun");
		nc_shot.setItemMeta(nc_shot_m);
		
		tr_smg_a = new ItemStack(Material.CLAY_BRICK, 60);
		ItemMeta tr_smg_a_m = tr_smg_a.getItemMeta();
		tr_smg_a_m.setDisplayName("SMG Bullets");
		lr.add("TR Use only!");
		tr_smg_a_m.setLore(lr);
		tr_smg_a.setItemMeta(tr_smg_a_m);
		lr = new ArrayList<String>();
		
		vn_smg_a = new ItemStack(Material.CLAY_BRICK, 60);
		ItemMeta vn_smg_a_m = vn_smg_a.getItemMeta();
		vn_smg_a_m.setDisplayName("SMG Bullets");
		lr.add("VN Use only!");
		vn_smg_a_m.setLore(lr);
		vn_smg_a.setItemMeta(vn_smg_a_m);
		lr = new ArrayList<String>();
		
		nc_smg_a = new ItemStack(Material.CLAY_BRICK, 60);
		ItemMeta nc_smg_a_m = nc_smg_a.getItemMeta();
		nc_smg_a_m.setDisplayName("SMG Bullets");
		lr.add("NC Use only!");
		nc_smg_a_m.setLore(lr);
		nc_smg_a.setItemMeta(nc_smg_a_m);
		lr = new ArrayList<String>();
		
		tr_shot_a = new ItemStack(Material.CLAY_BRICK, 7);
		ItemMeta tr_shot_a_m = tr_shot_a.getItemMeta();
		tr_shot_a_m.setDisplayName("Shotgun Shells");
		lr.add("TR Use only!");
		tr_shot_a_m.setLore(lr);
		tr_shot_a.setItemMeta(tr_shot_a_m);
		lr = new ArrayList<String>();
		
		vn_shot_a = new ItemStack(Material.CLAY_BRICK, 5);
		ItemMeta vn_shot_a_m = vn_shot_a.getItemMeta();
		vn_shot_a_m.setDisplayName("Shotgun Shells");
		lr.add("VN Use only!");
		vn_shot_a_m.setLore(lr);
		vn_shot_a.setItemMeta(vn_shot_a_m);
		lr = new ArrayList<String>();
		
		nc_shot_a = new ItemStack(Material.CLAY_BRICK, 4);
		ItemMeta nc_shot_a_m = nc_shot_a.getItemMeta();
		nc_shot_a_m.setDisplayName("Shotgun Shells");
		lr.add("NC Use only!");
		nc_shot_a_m.setLore(lr);
		nc_shot_a.setItemMeta(nc_shot_a_m);
		lr = new ArrayList<String>();
		
		tr_sn_a = new ItemStack(Material.CLAY_BRICK, 4);
		ItemMeta tr_sn_a_m = tr_sn_a.getItemMeta();
		tr_sn_a_m.setDisplayName("Sniper Bullets");
		lr.add("TR Use only!");
		tr_sn_a_m.setLore(lr);
		tr_sn_a.setItemMeta(tr_sn_a_m);
		lr = new ArrayList<String>();
		
		vn_sn_a = new ItemStack(Material.CLAY_BRICK, 4);
		ItemMeta vn_sn_a_m = vn_sn_a.getItemMeta();
		vn_sn_a_m.setDisplayName("Sniper Bullets");
		lr.add("VN Use only!");
		vn_sn_a_m.setLore(lr);
		vn_sn_a.setItemMeta(vn_sn_a_m);
		lr = new ArrayList<String>();
		
		nc_sn_a = new ItemStack(Material.CLAY_BRICK, 4);
		ItemMeta nc_sn_a_m = vn_sn_a.getItemMeta();
		nc_sn_a_m.setDisplayName("Sniper Bullets");
		lr.add("NC Use only!");
		nc_sn_a_m.setLore(lr);
		nc_sn_a.setItemMeta(nc_sn_a_m);
		lr = new ArrayList<String>();
		
		tr_baz_a = new ItemStack(Material.CLAY_BRICK, 1);
		ItemMeta tr_baz_a_m = tr_baz_a.getItemMeta();
		tr_baz_a_m.setDisplayName("Bazooka Rockets");
		lr.add("TR Use only!");
		tr_baz_a_m.setLore(lr);
		tr_baz_a.setItemMeta(tr_baz_a_m);
		lr = new ArrayList<String>();
		
		vn_baz_a = new ItemStack(Material.CLAY_BRICK, 1);
		ItemMeta vn_baz_a_m = vn_baz_a.getItemMeta();
		vn_baz_a_m.setDisplayName("Bazooka Rockets");
		lr.add("VN Use only!");
		vn_baz_a_m.setLore(lr);
		vn_baz_a.setItemMeta(vn_baz_a_m);
		lr = new ArrayList<String>();
		
		nc_baz_a = new ItemStack(Material.CLAY_BRICK, 1);
		ItemMeta nc_baz_a_m = nc_baz_a.getItemMeta();
		nc_baz_a_m.setDisplayName("Bazooka Rockets");
		lr.add("NC Use only!");
		nc_baz_a_m.setLore(lr);
		nc_baz_a.setItemMeta(nc_baz_a_m);
		lr = new ArrayList<String>();
		
		tr_kn = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta tr_kn_m = tr_kn.getItemMeta();
		tr_kn_m.setDisplayName("Chainblade");
		lr.add("TR Use only!");
		tr_kn.setItemMeta(tr_kn_m);
		lr = new ArrayList<String>();
		
		vn_kn = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta vn_kn_m = vn_kn.getItemMeta();
		vn_kn_m.setDisplayName("Force Blade");
		lr.add("VN Use only!");
		vn_kn.setItemMeta(vn_kn_m);
		lr = new ArrayList<String>();
		
		nc_kn = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta nc_kn_m = nc_kn.getItemMeta();
		nc_kn_m.setDisplayName("Mag Cutter");
		lr.add("NC Use only!");
		nc_kn.setItemMeta(nc_kn_m);
		lr = new ArrayList<String>();
		
	}
	
}

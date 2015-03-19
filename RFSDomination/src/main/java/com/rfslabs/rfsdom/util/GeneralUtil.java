package com.rfslabs.rfsdom.util;


import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.rfslabs.rfsdom.event.AttackEvents;
import com.rfslabs.rfsdom.event.GunEvents;
import com.rfslabs.rfsdom.event.ListEvents;
import com.rfslabs.rfsdom.event.MobilityEvents;
import com.rfslabs.rfsdom.event.ScoreEvents;
import com.rfslabs.rfsdom.event.TeamPickEvents;

public class GeneralUtil {
	
	/*
	 * Sets up required republic stuff to function correctly
	 */
	public static void setUpRequirements(){
		
		
		
	}
	
	/*
	 * Register events
	 */
	public static void registerEvents(){
		Bukkit.getServer().getPluginManager().registerEvents(new ListEvents(), MainDAO.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new ScoreEvents(), MainDAO.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new TeamPickEvents(), MainDAO.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new AttackEvents(), MainDAO.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new MobilityEvents(), MainDAO.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new GunEvents(), MainDAO.plugin);
	}
	
	/*
	 * Close stuff you dont need anymore
	 */
	public static void housekeeping(){
		
		if(ConfigUtil.db){
		try {
			ConfigUtil.con.close();
			PointsLink.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		
	}
	
	public static void tp(Player p , String team){
		
		if(team.equals("terran republic")){
			p.teleport(MainDAO.terran_republicSpawn);		
		}else if(team.equals("vanu sovereignty")){
			p.teleport(MainDAO.vanu_sovereigntySpawn);
		}else if(team.equals("new conglomerate")){
			p.teleport(MainDAO.new_conglomerateSpawn);
		}else if(team.equals("none")){
			p.teleport(MainDAO.tutorialLoc);
		}
		
	}
	
	public static void tpAndArmor(Player p , String team){
		
		if(team.equals("terran republic")){
			p.teleport(MainDAO.terran_republicSpawn);	
		}else if(team.equals("vanu sovereignty")){
			p.teleport(MainDAO.vanu_sovereigntySpawn);
		}else if(team.equals("new conglomerate")){
			p.teleport(MainDAO.new_conglomerateSpawn);
		}else if(team.equals("none")){
			p.teleport(MainDAO.tutorialLoc);
		}
		
		addArmor(p , team);
	}
	
	@SuppressWarnings("deprecation")
	public static void addArmor(Player p , String team){

		p.setHealth(20);
		p.setFoodLevel(20);
		
		p.getInventory().clear();
		
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta helm = (LeatherArmorMeta)helmet.getItemMeta();
		
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta chem = (LeatherArmorMeta)chest.getItemMeta();
		
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta legm = (LeatherArmorMeta)legs.getItemMeta();
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta bootm = (LeatherArmorMeta)boots.getItemMeta();
		
		if(team.equals("terran republic")){
			
			String armor = MainDAO.armor_up.get(p.getName());
			
			if(armor.equals("0")){
			
			helm.setColor(Color.fromRGB(0xFF,0x00,0x00));
			chem.setColor(Color.fromRGB(0xFF,0x00,0x00));
			legm.setColor(Color.fromRGB(0xFF,0x00,0x00));
			bootm.setColor(Color.fromRGB(0xFF,0x00,0x00));
			
			}else{
				if(armor.contains("1")){
					helmet = new ItemStack(Material.IRON_HELMET , 1);
					helm = null;
				}
				if(armor.contains("2")){
					chest = new ItemStack(Material.IRON_CHESTPLATE , 1);
					chem = null;
				}
				if(armor.contains("3")){
					legs = new ItemStack(Material.IRON_LEGGINGS , 1);
					legm = null;
				}
				if(armor.contains("4")){
					boots = new ItemStack(Material.IRON_BOOTS , 1);
					p.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_FALL, 1);
					bootm = null;
				}
			}
		}else if(team.equals("vanu sovereignty")){
			
			String armor = MainDAO.armor_up.get(p.getName());
			
			if(armor.equals("0")){
			
			helm.setColor(Color.fromRGB(0x80,0x00,0x80));
			chem.setColor(Color.fromRGB(0x80,0x00,0x80));
			legm.setColor(Color.fromRGB(0x80,0x00,0x80));
			bootm.setColor(Color.fromRGB(0x80,0x00,0x80));
			
		}else{
			if(armor.contains("1")){
				helmet = new ItemStack(Material.DIAMOND_HELMET , 1);
				helm = null;
			}
			if(armor.contains("2")){
				chest = new ItemStack(Material.DIAMOND_CHESTPLATE , 1);
				chem = null;
			}
			if(armor.contains("3")){
				legs = new ItemStack(Material.DIAMOND_LEGGINGS , 1);
				legm = null;
			}
			if(armor.contains("4")){
				boots = new ItemStack(Material.DIAMOND_BOOTS , 1);
				p.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_FALL, 1);
				bootm = null;
			}
		}
			
		}else if(team.equals("new conglomerate")){
			
			String armor = MainDAO.armor_up.get(p.getUniqueId());
			
			if(armor.equals("0")){
			
			helm.setColor(Color.fromRGB(0x00,0x00,0xFF));
			chem.setColor(Color.fromRGB(0x00,0x00,0xFF));
			legm.setColor(Color.fromRGB(0x00,0x00,0xFF));
			bootm.setColor(Color.fromRGB(0x00,0x00,0xFF));
			
		}else{
			if(armor.contains("1")){
				helmet = new ItemStack(Material.GOLD_HELMET , 1);
				helm = null;
			}
			if(armor.contains("2")){
				chest = new ItemStack(Material.GOLD_CHESTPLATE , 1);
				chem = null;
			}
			if(armor.contains("3")){
				legs = new ItemStack(Material.GOLD_LEGGINGS , 1);
				legm = null;
			}
			if(armor.contains("4")){
				boots = new ItemStack(Material.GOLD_BOOTS , 1);
				p.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_FALL, 1);
				bootm = null;
			}
		}
			
		}else if(team.equals("none")){
			return;
		}
		
		if(helm != null){
		helmet.setItemMeta(helm);
		}
		if(chem != null){
		chest.setItemMeta(chem);
		}
		if(legm != null){
		legs.setItemMeta(legm);
		}
		if(bootm != null){
		boots.setItemMeta(bootm);
		}
		
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chest);
		p.getInventory().setLeggings(legs);
		p.getInventory().setBoots(boots);
		
		p.getInventory().setItem(0,MainDAO.guns.get(p.getUniqueId()));
		//p.getInventory().setItem(2,MainDAO.grenade);
		p.getInventory().setItem(8,MainDAO.ammos.get(p.getUniqueId()));
		
		if(team.equals("terran republic")){
			p.getInventory().setItem(1,MainDAO.tr_kn);
		}else if(team.equals("vanu sovereignty")){
			p.getInventory().setItem(1,MainDAO.vn_kn);
		}else if(team.equals("new conglomerate")){
			p.getInventory().setItem(1,MainDAO.nc_kn);
		}
		
		p.getInventory().setItem(7, MainDAO.mag);
		
		if(p.getInventory().getLeggings() != null && p.getInventory().getLeggings().getType() != Material.LEATHER_LEGGINGS){
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
		}else{
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0));
		}
		
		if(p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() != Material.LEATHER_CHESTPLATE){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,1));
		}
		
		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() != Material.LEATHER_HELMET){
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,0));
		}
		
		if(MainDAO.classes.get(p.getUniqueId()).equals("medic")){
			p.getInventory().setItem(3, MainDAO.healer);
		}
		
		p.updateInventory();

	}
	
	public static void armor(final Player p , final String team){
		new BukkitRunnable() {
			 
            @Override
            public void run() {
            	
            	addArmor(p,team);
            	p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0));
            }
 
        }.runTaskLater(MainDAO.plugin, 1);
	}

	public static Generator getGenbyLoc(Location genl) {
		
		for(Generator g: MainDAO.generators){

			if(g.loc.getBlockX() == genl.getBlockX() && g.loc.getBlockY() == genl.getBlockY() && g.loc.getBlockZ() == genl.getZ() && g.loc.getWorld() == genl.getWorld()){
				return g;
			}
		}
		return null;
	}
	
	public static DyeColor getColore(String type){
		
		if(type.equals("none")){
			return DyeColor.WHITE;
		}else if(type.equals("terran republic")){
			return DyeColor.RED;
		}else if(type.equals("vanu sovereignty")){
			return DyeColor.PURPLE;
		}else if(type.equals("new conglomerate")){
			return DyeColor.BLUE;
		}
		
		return DyeColor.WHITE;
		
	}
	
	public static Material getMat(String type){
		
		if(type.equals("none")){
			return null;
		}else if(type.equals("terran republic")){
			return Material.REDSTONE_BLOCK;
		}else if(type.equals("vanu sovereignty")){
			return Material.EMERALD_BLOCK;
		}else if(type.equals("new conglomerate")){
			return Material.LAPIS_BLOCK;
		}
		
		return null;
		
	}
	
}

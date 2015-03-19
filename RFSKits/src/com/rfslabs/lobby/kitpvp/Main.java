package com.rfslabs.lobby.kitpvp;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		Data.p = this;
		
		Database.init();
		PointsLink.init();
		
		initItems();
		
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				
				for(Player p : Bukkit.getOnlinePlayers()){
					p.setFoodLevel(20);
				}
				
			}
			
			
		}, 20, 40);
		
	}
	
	@Override
	public void onDisable(){
		
		try {
			Database.con.close();
			PointsLink.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void initItems(){
		
		InvPack pack = new InvPack();
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots.addEnchantment(Enchantment.PROTECTION_FALL, 3);
		
		ItemStack[] i = {boots , new ItemStack(Material.LEATHER_LEGGINGS) ,  new ItemStack(Material.LEATHER_CHESTPLATE),  new ItemStack(Material.LEATHER_HELMET)};    
		
		pack.armor = i.clone();
		
		ItemStack bow = new ItemStack(Material.BOW);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
		
		ItemStack[] i2 = {bow , new ItemStack(Material.FISHING_ROD) , new ItemStack(Material.ARROW , 1)};
		
		pack.inventory = i2.clone();
		
		Data.items.put("bowman", pack);
		
		pack = new InvPack();
		
		ItemStack[] i3 = {new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS)  , new ItemStack(Material.CHAINMAIL_CHESTPLATE),  new ItemStack(Material.CHAINMAIL_HELMET)};    
		
		pack.armor = i3.clone();
		
		ItemStack sord = new ItemStack(Material.STONE_SWORD);
		sord.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		
		ItemStack[] i4 = {sord ,new ItemStack(Material.FISHING_ROD)};
		
		pack.inventory = i4.clone();
		
		Data.items.put("swordsman", pack);
		
		pack = new InvPack();
		
		ItemStack[] i5 = {null ,null ,  new ItemStack(Material.DIAMOND_CHESTPLATE) , null};    
		
		pack.armor = i5.clone();
		
		ItemStack axe = new ItemStack(Material.STONE_AXE);
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		axe.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		
		ItemStack[] i6 = {axe ,new ItemStack(Material.FISHING_ROD)};
		
		pack.inventory = i6.clone();
		
		Data.items.put("axeman", pack);
		
		pack = new InvPack();
		
		ItemStack[] i7 = { new ItemStack(Material.DIAMOND_BOOTS) , new ItemStack(Material.DIAMOND_LEGGINGS) , new ItemStack(Material.DIAMOND_CHESTPLATE) ,null};    
		
		pack.armor = i7.clone();
		
		ItemStack shov = new ItemStack(Material.STONE_SPADE);
		shov.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		shov.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		ItemStack[] i8 = {shov ,new ItemStack(Material.FISHING_ROD)};
		
		pack.inventory = i8.clone();
		
		Data.items.put("tank", pack);
		
		pack = new InvPack();
		
		ItemStack[] i9 = {null , null , null , new ItemStack(Material.BEDROCK)};    
		
		pack.armor = i9.clone();
		
		ItemStack sod = new ItemStack(Material.DIAMOND_SWORD);
		sod.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		
		ItemStack[] i10 = {sod};
		
		pack.inventory = i10.clone();
		
		Data.items.put("rogue", pack);
		
		pack = new InvPack();
		
		ItemStack[] i11 = {new ItemStack(Material.IRON_BOOTS) , new ItemStack(Material.IRON_LEGGINGS) ,  new ItemStack(Material.IRON_CHESTPLATE),  new ItemStack(Material.IRON_HELMET)};    
		
		pack.armor = i11.clone();
		
		ItemStack sword1 = new ItemStack(Material.DIAMOND_SWORD);
		sword1.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		
		ItemStack[] i12 = {sword1};
		
		pack.inventory = i12.clone();
		
		Data.items.put("dizzy", pack);
		
	}
	
}

package com.rfslabs.zombies.util;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MysteryBoxLogic {

	public static Random r = new Random();
	
	public static void tryOpen(Player p){
		if(Data.bux.get(p) >= 500){
			open(p);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void open(Player p){
		Data.bux.put(p, Data.bux.get(p) - 500);
		int gun = r.nextInt(Data.mystery_box.size());
		
		ItemStack award = Data.mystery_box.get(gun);
		if(award instanceof Gun){
			Gun g = (Gun) award;
		p.getInventory().setItem(0 , g);
		p.getInventory().setItem(7, new ItemStack(Data.gun_ammo.getType() , g.getAmmo_per_reload()));
		p.getInventory().setItem(8, new ItemStack(Data.gun_ammo.getType() , g.getMax_ammo()));
		}else{
			p.getInventory().setItem(4, award);
		}
		p.updateInventory();
	}
	
}

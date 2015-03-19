package com.rfslabs.zombies.util;

import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gun extends ItemStack{

	public int damage;
	public int max_ammo;
	public int ammo_per_reload;
	
	public int getMax_ammo() {
		return max_ammo;
	}

	public void setMax_ammo(int max_ammo) {
		this.max_ammo = max_ammo;
	}

	public int getAmmo_per_reload() {
		return ammo_per_reload;
	}

	public void setAmmo_per_reload(int ammo_per_reload) {
		this.ammo_per_reload = ammo_per_reload;
	}

	public Gun(Material mat) {
		super(mat);
	}
	
	public void setDamage(int damage){
		this.damage = damage;
	}
	public int getDamage(){
		return damage;
	}
	
	public void setTitle(String title){
		
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(title);
		this.setItemMeta(meta);
		
	}
	
}

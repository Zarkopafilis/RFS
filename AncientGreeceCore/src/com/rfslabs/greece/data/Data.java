package com.rfslabs.greece.data;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Data {

	public static Plugin plugin;
	public static int water_damage;
	public static PotionEffect ares_strength , hermes_speed;
	public static List<UUID> lightning = new ArrayList<UUID>();
	
	public static void init(){
		water_damage = 4;
		ares_strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE , Integer.MAX_VALUE , 1);
		hermes_speed = new PotionEffect(PotionEffectType.SPEED , Integer.MAX_VALUE , 1);
	}
}

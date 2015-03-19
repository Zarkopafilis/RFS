package com.rfslabs.zombies.util;

import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class ZombieSpawner {
	
	public static Random r = new Random();

	public static void spawn(int amount) {
		if(Data.w == null){
			Bukkit.getLogger().log(Level.SEVERE, "world null!");
			return;
		}
		
		for(int i = 0; i < amount ; i++){
			//Bukkit.getServer().broadcastMessage("zomb");
			Zombie z = (Zombie) Data.w.spawnCreature(
					Locations.zombies.get(
							r.nextInt(
									Locations.zombies.size() - 1))
									, CreatureType.ZOMBIE);
			z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			if(r.nextBoolean()){
				z.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST , Integer.MAX_VALUE, 1));
			}
			
		}
		
	}

}

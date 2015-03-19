package com.rfslabs.rfsdom.util;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoopingUtil {
	
	/*
	 * Update positioning in relation to outpost coordinates
	 */
	public static void updatePlayersOnOutposts(){
		
		for(int i = 0;i < MainDAO.outposts.size();i++){
			
			//MainDAO.outposts.get(i).resetPs();		
			
		for (Player player : MainDAO.plugin.getServer().getOnlinePlayers()){	
			
				
				if(MainDAO.outposts.get(i).inBetween(player)){
					

					if(MainDAO.players.get(player.getUniqueId()).equals("terran republic")){
						
						//MainDAO.outposts.get(i).terran_republics++;
						MainDAO.outposts.get(i).trs.add(player.getUniqueId());
						continue;
						
					}else if(MainDAO.players.get(player.getUniqueId()).equals("vanu sovereignty")){
						
						//MainDAO.outposts.get(i).vanu_sovereigntys++;
						MainDAO.outposts.get(i).vns.add(player.getUniqueId());
						continue;
						
					}else if(MainDAO.players.get(player.getUniqueId()).equals("new conglomerate")){
						
						//MainDAO.outposts.get(i).new_conglomerates++;
						MainDAO.outposts.get(i).ncs.add(player.getUniqueId());
						continue;
					}	
					
				}
				
				continue;
				
			}
			
        }
		
	}
	
	/*
	 * Update each outpost's status
	 */
	public static void updateOutpostsStatus(){
		
		for(int i = 0;i < MainDAO.outposts.size();i++){
			
			MainDAO.outposts.get(i).doLogic();
			MainDAO.outposts.get(i).updateProgress();
		}
		
	}
	
	public static void hunger(){
		
		for (Player p : MainDAO.plugin.getServer().getOnlinePlayers()){
			
			p.setFoodLevel(20);
			
		}
		
	}
	
	public static void water(){
		
		for (Player p : MainDAO.plugin.getServer().getOnlinePlayers()){

			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0));
			
		    if (p.getLocation().getBlock().getType() == Material.STATIONARY_WATER || p.getLocation().getBlock().getType() == Material.WATER) {
		        p.damage(20);
		    }
			
		}

	}

	public static void drops() {
		
		    for (Entity e : MainDAO.world.getEntities()) {
		           if (e instanceof Item) {
		        	   Item it = (Item)e;
		        	   if(it.getItemStack().getType() != Material.SLIME_BALL){
		        		   e.remove();
		        	   }
		           }
		}
		
	}
	
}

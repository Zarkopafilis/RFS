package com.rfslabs.rfsdom.util;

import org.bukkit.entity.Player;

public class ShieldHunger {

	public static void damagePlayer(Player attacker , Player victim , int damage){
		
		if(attacker.isDead() || victim.isDead()){
			return;
		}
		
		if(victim.getFoodLevel() == 0){
			
			victim.damage(damage, attacker);
			
		}else{
			
			if(damage > victim.getFoodLevel()){
				
				int newdmg = damage - victim.getFoodLevel();
				victim.setFoodLevel(0);
				damagePlayer(attacker , victim , newdmg);
				
				//startRegen(victim);
				
			}else{
				
				victim.setFoodLevel(victim.getFoodLevel() - damage);
				
				//startRegen(victim);
			}
			
		}
		
		
	}
	
	public static void startRegen(final Player p){
		
			  
			startRegenerate(p);
		
	}
	
	public static void startRegenerate(final Player p){
		
		if(MainDAO.regening.contains(p.getUniqueId())){
			return;
		}
		
		final int foodlvl = p.getFoodLevel();
		final int hplvl = (int) p.getHealth();
		
		Thread reg = new Thread(){	
			
			@Override
			public void run(){
				
				MainDAO.regening.add(p.getUniqueId());
				try {
					sleep(3000);
				} catch (InterruptedException e1) {
				}
				
				for(;;){							
					
					if(p.getFoodLevel() >= 20){
						p.setFoodLevel(20);
						MainDAO.regening.remove(p.getUniqueId());
						break;
					}
					
					if(p.getHealth() < hplvl || p.getFoodLevel() < foodlvl){
						MainDAO.regening.remove(p.getUniqueId());
						break;
					}
					
					p.setFoodLevel(p.getFoodLevel() + 6);				
					
					try {
						sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				
			}
			
		};
		
		reg.start();
	}
	
}

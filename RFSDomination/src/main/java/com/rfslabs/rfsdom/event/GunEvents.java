package com.rfslabs.rfsdom.event;

import java.util.List;






import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.rfslabs.rfsdom.proj.CustomProjectileHitEvent;
import com.rfslabs.rfsdom.proj.CustomProjectileHitEvent.HitType;
import com.rfslabs.rfsdom.proj.Particles;
import com.rfslabs.rfsdom.util.MainDAO;
import com.rfslabs.rfsdom.util.ShieldHunger;

public class GunEvents implements Listener{

	 	@EventHandler(priority = EventPriority.HIGHEST)
	    public void onHit(final CustomProjectileHitEvent e){
	        
	        if(e.getHitType() == HitType.ENTITY && e.getHitEntity() instanceof Player && e.getProjectile().getShooter() instanceof Player){
	        	
	        	Player v = (Player) e.getHitEntity();
	        	
	        	if(e.getProjectile().getProjectileName().equals("tr_sniper")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("terran republic")){
	        		  		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 18);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("vn_sniper")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("vanu sovereignty")){
	        		
	        		ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(), (Player)e.getHitEntity(), 18);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("nc_sniper")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("new conglomerate")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 18);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("tr_smg")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("terran republic")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 6);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("vn_smg")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("vanu sovereignty")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 6);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("nc_smg")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("new conglomerate")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 8);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("tr_shotgun")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("terran republic")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 12);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("vn_shotgun")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("vanu sovereignty")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 12);	
	        		
	        		}
	        		
	        	}else if(e.getProjectile().getProjectileName().equals("nc_shotgun")){
	        		
	        		if(!MainDAO.players.get(v.getUniqueId()).equals("new conglomerate")){
	        		
	        			ShieldHunger.damagePlayer((Player)e.getProjectile().getShooter(),(Player) e.getHitEntity(), 10);	
	        		
	        		}
	        		
	        	}
	        	
	        }
	        	
	        }
	 	
	 	@EventHandler(priority = EventPriority.HIGHEST)
	 	public void onBlow(final EntityExplodeEvent e){
	 		
	 		if(e.getEntity() instanceof Fireball){
	 			
	 			
    	
    			int dmg = 0;
    			
    			Fireball f = (Fireball) e.getEntity();
    			f.setIsIncendiary(false);
    			
    			Player p = (Player) f.getShooter();
    			
    			if(MainDAO.players.get(p.getUniqueId()).equals("terran republic")){
    				dmg = 48;
    			}else if(MainDAO.players.get(p.getUniqueId()).equals("vanu sovereignty")){
    				dmg = 40;
    			}else if(MainDAO.players.get(p.getUniqueId()).equals("new conglomerate")){
    				dmg = 46;
    			}
    			
    			Particles.HUGE_EXPLOSION.display(e.getEntity().getLocation(), 0, 0, 0, 1, 1);
    			
    			e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.EXPLODE, 10 , 1);
    		
    			List<Entity> es = f.getNearbyEntities(3, 3, 3);
    			
    			for(Entity en : es){
    				
    				if(en instanceof Player){
    					
    					Player pl = (Player) en;
    					
    					if(pl.getName().equals(p.getName()) || !MainDAO.players.get(p.getUniqueId()).equals(MainDAO.players.get(pl.getUniqueId()))){
    						
    						ShieldHunger.damagePlayer(p,pl, dmg);	
    						
    					}
    					
    				}
    			}
    			
    			e.setYield(0F);
    			e.setCancelled(true);
	 			
	 		}	
	 		
	 	}
	 	
}
	

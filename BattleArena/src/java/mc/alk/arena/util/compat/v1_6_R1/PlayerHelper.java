package mc.alk.arena.util.compat.v1_6_R1;

import mc.alk.arena.controllers.plugins.HeroesController;
import mc.alk.arena.util.compat.IPlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class PlayerHelper implements IPlayerHelper{

	
	public void setHealth(Player player, double health, boolean skipHeroes) {
		if (!skipHeroes && HeroesController.enabled()){
			HeroesController.setHealth(player,health);
			return;
		}

		final double oldHealth = player.getHealth();
		if (oldHealth > health){
			EntityDamageEvent event = new EntityDamageEvent(player,  DamageCause.CUSTOM, oldHealth-health );
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()){
				player.setLastDamageCause(event);
				final double dmg = Math.max(0,oldHealth - event.getDamage());
				player.setHealth(dmg);
			}
		} else if (oldHealth < health){
			EntityRegainHealthEvent event = new EntityRegainHealthEvent(player, health-oldHealth,RegainReason.CUSTOM);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()){
				final double regen = Math.min(oldHealth + event.getAmount(),player.getMaxHealth());
				player.setHealth(regen);
			}
		}
	}

	
	public double getHealth(Player player) {
		return player.getHealth();
	}

	
	public double getMaxHealth(Player player) {
		return player.getMaxHealth();
	}

    
    public Object getScoreboard(Player player) {
        return player.getScoreboard();
    }

    
    public void setScoreboard(Player player, Object scoreboard) {
        player.setScoreboard((Scoreboard) scoreboard);
    }

    
    public UUID getID(OfflinePlayer player) {
        return new UUID(0, player.getName().hashCode());
    }


}

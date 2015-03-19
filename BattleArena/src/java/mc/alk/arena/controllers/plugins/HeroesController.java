package mc.alk.arena.controllers.plugins;

import mc.alk.arena.listeners.competition.plugins.HeroesListener;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.util.plugins.HeroesUtil;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HeroesController {
	static boolean hasHeroes = false;
	HeroesUtil heroes = null;

	public static boolean hasHeroClass(String className) {
		if (!hasHeroes) return false;
		try{return HeroesUtil.hasHeroClass(className);}catch(Exception e){Log.printStackTrace(e);}
		return false;
	}

	public static void setHeroClass(Player player, String className) {
		if (!hasHeroes) return;
		try{HeroesUtil.setHeroClass(player, className);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static void setPlugin(Plugin plugin){
		HeroesUtil.setHeroes(plugin);
		hasHeroes = true;
		HeroesListener.enable();
	}

	public static boolean enabled() {
		return hasHeroes;
	}
	public static String getHeroClassName(Player player) {
		if (!hasHeroes) return null;
		try{return HeroesUtil.getHeroClassName(player);}catch(Exception e){Log.printStackTrace(e);}
		return null;
	}

	public static int getLevel(Player player) {
		if (!hasHeroes) return -1;
		try{return HeroesUtil.getLevel(player);}catch(Exception e){Log.printStackTrace(e);}
		return -1;
	}
	public static boolean isInCombat(Player player) {
		if (!hasHeroes) return false;
		try{return HeroesUtil.isInCombat(player);}catch(Exception e){Log.printStackTrace(e);}
		return false;
	}
	public static void deEnchant(Player p) {
		if (!hasHeroes)
			return;
		try{HeroesUtil.deEnchant(p);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static void createTeam(ArenaTeam team) {
		if (!hasHeroes)
			return;
		try{HeroesUtil.createTeam(team);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static void removeTeam(ArenaTeam team) {
		if (!hasHeroes)
			return;
		try{HeroesUtil.removeTeam(team);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static void removedFromTeam(ArenaTeam team, Player player) {
		if (!hasHeroes)
			return;
		try{HeroesUtil.removedFromTeam(team, player);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static ArenaTeam getTeam(Player player) {
		if (!hasHeroes)
			return null;
		try{return HeroesUtil.getTeam(player);}catch(Exception e){Log.printStackTrace(e);}
		return null;
	}

	public static void setMagicLevel(Player p, Integer magic) {
		if (!hasHeroes) return;
		try{HeroesUtil.setMagicLevel(p,magic);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static void setMagicLevelP(Player p, Integer magic) {
		if (!hasHeroes) return;
		try{HeroesUtil.setMagicLevelP(p,magic);}catch(Exception e){Log.printStackTrace(e);}
	}

	public static Integer getMagicLevel(Player player) {
		if (!hasHeroes)
			return null;
		try{return HeroesUtil.getMagicLevel(player);}catch(Exception e){Log.printStackTrace(e);}
		return null;
	}

	public static double getHealth(Player player) {
		return hasHeroes ? HeroesUtil.getHealth(player) : player.getHealth();
	}

	public static void setHealth(Player player, double health) {
		if (hasHeroes)
			try{HeroesUtil.setHealth(player,health);}catch(Exception e){Log.printStackTrace(e);}
		else
			PlayerUtil.setHealth(player,health,true);
	}

	public static void setHealthP(Player player, double health) {
		if (hasHeroes)
			try{HeroesUtil.setHealthP(player,health);}catch(Exception e){Log.printStackTrace(e);}
		else
			PlayerUtil.setHealthP(player,health,true);
	}

	public static void cancelExpLoss(Player player, boolean cancel) {
		if (!hasHeroes)
			return;
		if (cancel)
			HeroesListener.setCancelExpLoss(player);
		else
			HeroesListener.removeCancelExpLoss(player);
	}

	public static void addDisabledCommands(List<String> disabled) {
		try{HeroesListener.addDisabledCommands(disabled);}catch(Exception e){Log.printStackTrace(e);}
	}
}

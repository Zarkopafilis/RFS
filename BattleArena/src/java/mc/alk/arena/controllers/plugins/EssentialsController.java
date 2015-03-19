package mc.alk.arena.controllers.plugins;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.util.plugins.EssentialsUtil;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EssentialsController {
	static boolean hasEssentials = false;

	public static boolean enabled() {
		return hasEssentials;
	}

	public static boolean setPlugin(Plugin plugin) {
		hasEssentials = EssentialsUtil.enableEssentials(plugin);
		return hasEssentials;
	}

	public static void setGod(Player player, boolean enable) {
		if (!hasEssentials) return;
		EssentialsUtil.setGod(player.getName(),enable);
	}

	public static void setFlight(Player player, boolean enable) {
		if (!hasEssentials) return;
		EssentialsUtil.setFlight(player.getName(),enable);
	}

	public static void setFlightSpeed(Player player, Float flightSpeed) {
		if (!hasEssentials) return;
		EssentialsUtil.setFlightSpeed(player.getName(),flightSpeed);
	}

	public static boolean inJail(ArenaPlayer player) {
		if (!hasEssentials) return false;
		return EssentialsUtil.inJail(player.getName());
	}

	public static Boolean isGod(ArenaPlayer player) {
		if (!hasEssentials) return false;
		return EssentialsUtil.isGod(player.getName());
	}

	public static Boolean isFlying(ArenaPlayer player) {
		if (!hasEssentials) return false;
		return EssentialsUtil.isFlying(player.getName());
	}

	public static Location getBackLocation(String playerName) {
		return EssentialsUtil.getBackLocation(playerName);
	}

	public static void setBackLocation(String playerName, Location loc) {
		EssentialsUtil.setBackLocation(playerName,loc);
	}

}

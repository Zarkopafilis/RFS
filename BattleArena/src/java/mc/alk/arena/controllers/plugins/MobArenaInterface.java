package mc.alk.arena.controllers.plugins;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.plugins.MobArenaUtil;

import org.bukkit.plugin.Plugin;

public class MobArenaInterface {
	static MobArenaInterface mai = null;
	MobArenaUtil ma = null;

	public static void setPlugin(Plugin plugin){
		try {
			mai = new MobArenaInterface();
			mai.ma = new MobArenaUtil(plugin);
		} catch (Error e){
			mai = null;
			Log.printStackTrace(e);
		}
	}
	public static boolean hasMobArena() {
		return mai != null;
	}

	public static boolean insideMobArena(ArenaPlayer p) {
		return mai.getMobArena().insideMobArena(p.getPlayer());
	}

	public MobArenaUtil getMobArena(){
		return ma;
	}
}

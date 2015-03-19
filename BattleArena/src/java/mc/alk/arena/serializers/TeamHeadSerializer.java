package mc.alk.arena.serializers;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.exceptions.ConfigException;
import mc.alk.arena.objects.teams.TeamAppearance;
import mc.alk.arena.util.InventoryUtil;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.MessageUtil;
import mc.alk.arena.util.TeamUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.List;

public class TeamHeadSerializer extends BaseConfig{

	public void loadAll(){
		try {config.load(file);} catch (Exception e){Log.printStackTrace(e);}
		loadTeams(config);
	}

	public static void loadTeams(ConfigurationSection cs) {
		if (cs == null){
			Log.info(BattleArena.getPluginName() +" has no teamColors");
			return;}
		List<String> keys = cs.getStringList("teams");
		boolean first = true;
		for (String teamStr : keys){
			try {
				addTeamHead(teamStr);
			} catch (Exception e) {
				Log.err("Error parsing teamHead " + teamStr);
				Log.printStackTrace(e);
				continue;
			}
			if (first) first = false;
		}
		if (first){
			Log.info(BattleArena.getPluginName() +" no predefined teamColors found. inside of " + cs.getCurrentPath());
		}
	}


	private static String addTeamHead(String str) throws Exception {
		String[] split = str.split(",");
		if (split.length != 5){
			throw new ConfigException("Team Colors must be in format 'Name,ItemStack,R,G,B'");
		}
		String name =MessageUtil.decolorChat(split[0]);
		if (name.isEmpty()){
			throw new ConfigException("Team Name must not be empty 'Name,ItemStack'");
		}
		ItemStack item = InventoryUtil.parseItem(split[1]);
		item.setAmount(1);
		Integer r = Integer.valueOf(split[2]);
		Integer g = Integer.valueOf(split[3]);
		Integer b = Integer.valueOf(split[4]);
		TeamAppearance th = new TeamAppearance(item,split[0], new Color(r, g, b));
		TeamUtil.addTeamHead(name,th);
		return name;
	}

}

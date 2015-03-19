package mc.alk.arena.util;

import mc.alk.arena.controllers.messaging.MessageHandler;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.teams.ArenaTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MessageUtil {

	public static String colorChat(String msg) {return msg.replace('&', (char) 167);}

	public static String decolorChat(String msg) {
		return msg.contains("§") || msg.contains("&") ? ChatColor.stripColor(msg).replaceAll("(&|§).", "") : msg;}

    public static boolean sendSystemMessage(CommandSender p, String nodeString, Object... varArgs) {
        return sendMessage(p, MessageHandler.getSystemMessage(nodeString,varArgs));
    }

    public static boolean sendMessage(CommandSender p, String message){
		if (message ==null || message.isEmpty()) return true;
		if (message.contains("\n"))
			return sendMultilineMessage(p,message);
		if (p instanceof Player){
			if (((Player) p).isOnline())
				p.sendMessage(colorChat(message));
		} else {
			p.sendMessage(colorChat(message));
		}
		return true;
	}

	public static boolean sendMultilineMessage(CommandSender p, String message){
		if (message ==null || message.isEmpty()) return true;
		String[] msgs = message.split("\n");
		for (String msg: msgs){
			if (p instanceof Player){
				if (((Player) p).isOnline())
					p.sendMessage(colorChat(msg));
			} else {
				p.sendMessage(colorChat(msg));
			}
		}
		return true;
	}

	public static void sendPlayerMessage(Set<Player> players, String message) {
		final String msg = colorChat(message);
		for (Player p: players){
			p.sendMessage(msg);
		}
	}

	public static String minuteOrMinutes(int minutes) {return minutes == 1 ? "minute" : "minutes";}
	public static String getTeamsOrPlayers(int teamSize) {return teamSize==1 ? "players" : "teams";}
	public static String teamsOrPlayers(int nPlayersPerTeam){return nPlayersPerTeam > 1? "teams" : "players";}
	public static String playerOrPlayers(int n) {return n> 1? "players" : "player";}
	public static String hasOrHave(int size) {return size==1 ? "has" : "have";}


	public static boolean sendMessage(final ArenaPlayer player, final String message) {
		return sendMessage(player.getPlayer(),message);
	}

	public static boolean sendMessage(final ArenaPlayer player, final List<String> messages) {
		for (String s: messages){
			sendMessage(player.getPlayer(),s);
		}
		return true;
	}

	public static void sendMessage(Collection<ArenaPlayer> players, String message) {
		if (message == null)
			return;
		final String msg = colorChat(message.trim());
		if (msg.isEmpty())
			return;
		for (ArenaPlayer p: players){
			p.getPlayer().sendMessage(msg);
		}
	}

	public static String convertToString(List<String> strs){
		if (strs == null)
			return null;
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : strs){
			if (!first) sb.append("\n");
			sb.append(s).append("\n");
			first = false;
		}
		return sb.toString();
	}

	public static ChatColor getFirstColor(String str) {
		int index = str.indexOf('§');
        if (index == -1)
            index = str.indexOf('&');
        if (index != -1 && str.length() > index+1){
			ChatColor cc = ChatColor.getByChar(str.charAt(index+1));
			if (cc != null)
				return cc;
		}
		return ChatColor.WHITE;
	}

	public static String joinTeams(Collection<ArenaTeam> teams, String joinStr){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (ArenaTeam t: teams){
			if (!first) sb.append(joinStr);
			sb.append(t.getDisplayName());
			first = false;
		}
		return sb.toString();
	}

	public static String joinPlayers(Collection<ArenaPlayer> players, String joinStr){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (ArenaPlayer p : players){
			if (!first) sb.append(joinStr);
			else first = false;
			sb.append(p.getName());
		}
		return sb.toString();
	}

    public static void broadcastMessage(String message) {
        try {
            Bukkit.broadcastMessage(message);
        } catch (Throwable e){
            /* do nothing. This has thrown concurrent errors before*/
        }
    }
}

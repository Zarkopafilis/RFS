package mc.alk.arena.util;

import mc.alk.arena.BattleArena;
import mc.alk.arena.controllers.messaging.MessageHandler;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;


public class TimeUtil {
	static long lastCheck = 0;

	public static void testClock() {
        final long start = System.currentTimeMillis();
		if (start - lastCheck < 10000)
			return;
		lastCheck = start;
        final int seconds = 2;

        final double millis = seconds * 1000;
        final int nTicks = 20 * seconds;
        Runnable task = new Runnable() {
            public void run() {
                long now = System.currentTimeMillis();

                long elapsedTime = now - start;
                double mult = millis/elapsedTime;
                if (mult < 0.2){
                	mult = 0.2;}
//                Defaults.TICK_MULT = Math.min(mult,1);
//                MatchMessageImpl.sendMessage(null, ChatColor.GRAY + "[BattleArena] multiplier: " + mult + "x");
            }
        };

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleArena.getSelf(), task, nTicks);
    }

	public static String convertMillisToString(long t){
		return convertSecondsToString(t/1000);
	}
	public static String convertSecondsToString(long t){
		long s = t % 60;
		t /= 60;
		long m = t %60;
		t /=60;
		long h = t % 24;
		t /=24;
		long d = t;
		boolean has = false;
		StringBuilder sb = new StringBuilder();
		if (d > 0) {
			has=true;
			sb.append(MessageHandler.getSystemMessage("time_format", d, dayOrDays(d)));
		}
		if (h > 0) {
			sb.append(has ? " " : "").append(MessageHandler.getSystemMessage("time_format", h, hourOrHours(h)));
			has =true;
		}
		if (m > 0) {
			sb.append(has ? " " : "").append(MessageHandler.getSystemMessage("time_format", m, minOrMins(m)));
			has=true;
		}
		if (s > 0) {
			sb.append(has ? " " : "").append(MessageHandler.getSystemMessage("time_format", s, secOrSecs(s)));
			has = true;
		}
		if (!has){
			sb.append(MessageHandler.getSystemMessage("zero_time"));
		}
		return sb.toString();
	}

	public static String convertToString(long t){
	    t = t / 1000;
	    return convertSecondsToString(t);
	}

	public static String dayOrDays(long t){
		return MessageHandler.getSystemMessage(t > 1 || t == 0? "days" : "day");
	}

	public static String hourOrHours(long t){
		return MessageHandler.getSystemMessage(t > 1 || t ==0 ? "hours" : "hour");
	}

	public static String minOrMins(long t){
		return MessageHandler.getSystemMessage(t > 1 || t == 0? "minutes" : "minute");
	}
	public static String secOrSecs(long t){
		return MessageHandler.getSystemMessage(t > 1 || t == 0? "seconds" : "second");
	}


	public static String convertLongToDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm");
		return sdf.format(time);
	}

	public static String convertLongToSimpleDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return sdf.format(time);
	}

	public static String PorP(int size) {
		return size == 1 ? "person" : "people";
	}

}

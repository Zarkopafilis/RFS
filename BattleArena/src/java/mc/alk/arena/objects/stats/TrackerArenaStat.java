package mc.alk.arena.objects.stats;

import mc.alk.tracker.objects.Stat;
import mc.alk.tracker.objects.VersusRecords.VersusRecord;

public class TrackerArenaStat implements ArenaStat{
	final Stat stat;
	final String db;

	public TrackerArenaStat(String db, Stat stat) {
		this.db = db;
		this.stat = stat;
	}

	
	public int getWinsVersus(ArenaStat ostat) {
		Stat st2 = ((TrackerArenaStat)ostat).getStat();
		VersusRecord vs = stat.getRecordVersus(st2);
		return vs == null ? 0 : vs.wins;
	}

	
	public int getLossesVersus(ArenaStat ostat) {
		Stat st2 = ((TrackerArenaStat)ostat).getStat();
		VersusRecord vs = stat.getRecordVersus(st2);
		return vs == null ? 0 : vs.losses;
	}

	
	public int getWins() {
		return stat.getWins();
	}

	
	public int getLosses() {
		return stat.getLosses();
	}

	
	public int getRanking() {
		return stat.getRating();
	}

	
	public int getRating() {
		return stat.getRating();
	}

	public Stat getStat(){
		return stat;
	}

	
	public String toString(){
		return stat.toString();
	}

	
	public String getDB() {
		return db;
	}
}

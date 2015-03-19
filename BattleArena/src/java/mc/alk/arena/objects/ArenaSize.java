package mc.alk.arena.objects;

import mc.alk.arena.util.MinMax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArenaSize implements CompetitionSize{
	int minTeamSize = 1;
	int maxTeamSize = MAX;
	int minTeams = 2;
	int maxTeams = MAX;

	public ArenaSize(){}

	public ArenaSize(CompetitionSize size) {
		minTeamSize = size.getMinTeamSize();
		maxTeamSize = size.getMaxTeamSize();
		minTeams = size.getMinTeams();
		maxTeams = size.getMaxTeams();
	}

	
	public int getMinPlayers(){
		return minTeams* minTeamSize;
	}

	
	public int getMaxPlayers(){
		return (maxTeams == MAX || maxTeamSize == MAX) ? MAX : maxTeams * maxTeamSize;
	}

	
	public boolean matchesTeamSize(int i) {
		return minTeamSize <= i && i <= maxTeamSize;
	}

	public boolean matches(ArenaSize size){
		return matchesTeamSize(this,size) && matchesNTeams(this,size);
	}

	public static boolean matchesTeamSize(ArenaSize size1, ArenaSize size2) {
        return size1 == null && size2 == null ||
                !(size1 == null || size2 == null) && size1.matchesTeamSize(size2);
    }
	public static boolean matchesNTeams(ArenaSize size1, ArenaSize size2) {
        return size1 == null && size2 == null ||
                !(size1 == null || size2 == null) && size1.matchesNTeams(size2);
    }

	public static boolean lower(MinMax child, MinMax parent) {
        return child == null || parent == null || child.max < parent.max;
    }

	
	public void setTeamSize(int size) {
		minTeamSize = maxTeamSize = size;
	}

	
	public void setTeamSizes(MinMax mm) {
		minTeamSize = mm.min;
		maxTeamSize = mm.max;
	}
	
	public void setNTeams(MinMax mm) {
		minTeams = mm.min;
		maxTeams = mm.max;
	}

	
	public int getMinTeams() {return minTeams;}

	
	public int getMaxTeams() {return maxTeams;}

	
	public void setMinTeams(int nteams) {this.minTeams = nteams;}

	
	public void setMaxTeams(int nteams) {this.maxTeams = nteams;}

	
	public void setMinTeamSize(int size) {minTeamSize=size;}

	
	public void setMaxTeamSize(int size) {maxTeamSize=size;}

	
	public int getMinTeamSize() {return minTeamSize;}

	
	public int getMaxTeamSize() {return maxTeamSize;}

	
	public boolean matchesNTeams(final CompetitionSize csize) {
		final int min = Math.max(csize.getMinTeams(), minTeams);
		final int max = Math.min(csize.getMaxTeams(), maxTeams);
		return min <= max;
	}

	
	public boolean matchesNTeams(int nteams) {
		return minTeams<= nteams && nteams<=maxTeams;
	}

	
	public boolean matchesTeamSize(final CompetitionSize csize) {
		final int min = Math.max(csize.getMinTeamSize(), minTeamSize);
		final int max = Math.min(csize.getMaxTeamSize(), maxTeamSize);
		return min <= max;
	}


	public static boolean intersect(CompetitionSize size1, CompetitionSize size2) {
		return size1.intersect(size2);
	}

	
	public boolean intersect(CompetitionSize csize) {
		minTeams = Math.max(csize.getMinTeams(), minTeams);
		maxTeams = Math.min(csize.getMaxTeams(), maxTeams);
		minTeamSize = Math.max(csize.getMinTeamSize(), minTeamSize);
		maxTeamSize = Math.min(csize.getMaxTeamSize(), maxTeamSize);
		return (minTeams <= maxTeams && minTeamSize <= maxTeamSize);
	}


	public boolean intersectMax(CompetitionSize csize) {
		maxTeams = Math.min(csize.getMaxTeams(), maxTeams);
		maxTeamSize = Math.min(csize.getMaxTeamSize(), maxTeamSize);
		return (minTeams <= maxTeams && minTeamSize <= maxTeamSize);
	}

	
	public boolean intersectTeamSize(int size) {
		if (minTeamSize > size || maxTeamSize < size)
			return false;
		minTeamSize = size;
		maxTeamSize = size;
		return true;
	}

	public static String toString(int size){
		return size == ArenaSize.MAX ? "infinite" : String.valueOf(size);
	}

    public static int toInt(String size) {
        return size.equalsIgnoreCase("infinite") ? MAX : Integer.valueOf(size);
    }

    public static int toInt(String size, int defValue) {
        if (size == null || size.isEmpty())
            return defValue;
        return size.equalsIgnoreCase("infinite") ? MAX : Integer.valueOf(size);
    }

    
	public String toString(){
		return "["+rangeString(minTeamSize,maxTeamSize)+" <-> "+rangeString(minTeams,maxTeams)+"]";
	}


	public static String rangeString(final int min,final int max){
		if (max == MAX){ return min+"+";} /// Example: 2+
		if (min == max){ return min+"";} /// Example: 2
		return min + "-" + max; //Example 2-4
	}

	public boolean valid() {
		return minTeamSize <= maxTeamSize && minTeams <= maxTeams;
	}

	public Collection<String> getInvalidReasons() {
		List<String> reasons = new ArrayList<String>();
		if (minTeamSize <= 0) reasons.add("Min Team Size is <= 0");
		if (maxTeamSize <= 0) reasons.add("Max Team Size is <= 0");
		if (minTeamSize > maxTeamSize) reasons.add("Min Team Size is greater than Max Team Size " + minTeamSize+":"+ maxTeamSize);
		if (minTeams > maxTeams) reasons.add("Min Teams is greater than Max Teams" + minTeams+":"+ maxTeams);
		return reasons;
	}

}

package mc.alk.arena.objects.scoreboard;

import mc.alk.arena.BattleArena;
import mc.alk.arena.Defaults;
import mc.alk.arena.competition.match.Match;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.scoreboardapi.ScoreboardAPI;
import mc.alk.scoreboardapi.api.SEntry;
import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.api.SScoreboard;
import mc.alk.scoreboardapi.api.STeam;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

public class ArenaScoreboard implements SScoreboard {
    final protected SScoreboard board;

    public ArenaScoreboard(String scoreboardName) {
        this.board = (Defaults.TESTSERVER || !Defaults.USE_SCOREBOARD) ?
                ScoreboardAPI.createSAPIScoreboard(getPlugin(), scoreboardName) :
                ScoreboardAPI.createScoreboard(getPlugin(), scoreboardName);
    }

    @SuppressWarnings({"unused"})
    @Deprecated
    public ArenaScoreboard(Match match, MatchParams params) {
        this.board = (Defaults.TESTSERVER || !Defaults.USE_SCOREBOARD) ?
                ScoreboardAPI.createSAPIScoreboard(getPlugin(), match.getName()) :
                ScoreboardAPI.createScoreboard(getPlugin(), match.getName());
    }

    public ArenaObjective createObjective(String id, String criteria, String displayName) {
        return createObjective(id,criteria,displayName,SAPIDisplaySlot.SIDEBAR);
    }

    public ArenaObjective createObjective(String id, String criteria, String displayName,
                                          SAPIDisplaySlot slot) {
        return createObjective(id,criteria,displayName,SAPIDisplaySlot.SIDEBAR, 50);
    }

    public ArenaObjective createObjective(String id, String criteria, String displayName,
                                          SAPIDisplaySlot slot, int priority) {
        ArenaObjective o = new ArenaObjective(id,criteria,displayName,slot,priority);
        addObjective(o);
        return o;
    }

    public STeam addTeam(ArenaTeam team) { return null;}

    public void addedToTeam(STeam team, ArenaPlayer player){/* do nothing*/}

    public STeam addedToTeam(ArenaTeam team, ArenaPlayer player) {return null;}

    public STeam removeTeam(ArenaTeam team) {return null;}

    public STeam removedFromTeam(ArenaTeam team, ArenaPlayer player) {return null;}

    public void removedFromTeam(STeam team, ArenaPlayer player){/* do nothing */}

    public void setDead(ArenaTeam t, ArenaPlayer p) {/* do nothing */}

    public void leaving(ArenaTeam t, ArenaPlayer player) {/* do nothing */}

    public void addObjective(ArenaObjective scores) {
        this.registerNewObjective(scores);
    }

    public List<STeam> getTeams() {
        return null;
    }

    
    public SObjective registerNewObjective(String objectiveName,
                                           String criteria, String displayName, SAPIDisplaySlot slot) {
        return createObjective(objectiveName,criteria, displayName,slot);
    }

    
    public void setDisplaySlot(SAPIDisplaySlot slot, SObjective objective) {
        board.setDisplaySlot(slot, objective);
    }

    
    public void setDisplaySlot(SAPIDisplaySlot slot, SObjective objective,boolean fromObjective) {
        board.setDisplaySlot(slot, objective, fromObjective);
    }

    
    public SObjective getObjective(SAPIDisplaySlot slot) {
        return board.getObjective(slot);
    }

    
    public SObjective getObjective(String id) {
        return board.getObjective(id);
    }

    
    public List<SObjective> getObjectives() {
        return board.getObjectives();
    }

    
    public String getPrintString() {
        return board.getPrintString();
    }

    
    public SEntry createEntry(OfflinePlayer p) {
        return board.createEntry(p);
    }

    
    public SEntry createEntry(OfflinePlayer p, String displayName) {
        return board.createEntry(p,displayName);
    }

    
    public SEntry createEntry(String id, String displayName) {
        return board.createEntry(id, displayName);
    }

    
    public STeam createTeamEntry(String id, String displayName) {
        return board.createTeamEntry(id, displayName);
    }

    
    public SEntry removeEntry(OfflinePlayer p) {
        return board.removeEntry(p);
    }

    
    public SEntry removeEntry(SEntry e) {
        return board.removeEntry(e);
    }

    
    public boolean setEntryDisplayName(String id, String name) {
        return board.setEntryDisplayName(id, name);
    }

    
    public void setEntryDisplayName(SEntry e, String name) {
        board.setEntryDisplayName(e, name);
    }

    public boolean setEntryDisplayName(ArenaPlayer player, String name) {
        return board.setEntryDisplayName(player.getName(), name);
    }

    
    public boolean setEntryNamePrefix(String id, String name) {
        return board.setEntryNamePrefix(id,name);
    }

    
    public void setEntryNamePrefix(SEntry entry, String name) {
        board.setEntryNamePrefix(entry,name);
    }

    public boolean setEntryNamePrefix(ArenaPlayer player, String name) {
        return board.setEntryNamePrefix(player.getName(), name);
    }

    
    public boolean setEntryNameSuffix(String id, String name) {
        return board.setEntryNameSuffix(id, name);
    }

    
    public void setEntryNameSuffix(SEntry e, String name) {
        board.setEntryNameSuffix(e, name);
    }

    
    public Plugin getPlugin() {
        return BattleArena.getSelf();
    }

    
    public boolean hasThisScoreboard(Player player) {
        return board.hasThisScoreboard(player);
    }

    public boolean setEntryNameSuffix(ArenaPlayer player, String name) {
        return board.setEntryNameSuffix(player.getName(), name);
    }

    
    public String getName() {
        return board.getName();
    }

    
    public SEntry getEntry(String id) {
        return board.getEntry(id);
    }

    
    public SEntry getEntry(OfflinePlayer player) {
        return board.getEntry(player);
    }

    
    public STeam getTeam(String id) {
        return board.getTeam(id);
    }

    
    public void clear() {
        board.clear();
    }

    
    public SObjective registerNewObjective(SObjective objective) {
        return board.registerNewObjective(objective);
    }

    
    public SEntry getOrCreateEntry(OfflinePlayer p) {
        return board.getOrCreateEntry(p);
    }

    
    public Collection<SEntry> getEntries() {
        return board.getEntries();
    }

    
    public void removeScoreboard(Player player) {
        board.removeScoreboard(player);
    }

    
    public void setScoreboard(Player player) {
        board.setScoreboard(player);
    }

    public void setObjectiveScoreboard(ArenaObjective arenaObjective) {
        arenaObjective.setScoreBoard(board);
    }

    public void setPoints(ArenaObjective objective, ArenaTeam team, int points) {
        objective.setPoints(team, points);
    }

    public void setPoints(ArenaObjective objective, ArenaPlayer player, int points) {
        objective.setPoints(player, points);
    }

    public SScoreboard getBScoreboard() {
        return null;
    }

    public void initPoints(ArenaObjective objective, List<SEntry> es, List<Integer> points) {
        objective.initPoints(es, points);
    }
}

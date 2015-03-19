package mc.alk.arena.objects.victoryconditions;

import mc.alk.arena.competition.match.Match;
import mc.alk.arena.controllers.plugins.TrackerController;
import mc.alk.arena.events.matches.MatchFindCurrentLeaderEvent;
import mc.alk.arena.events.players.ArenaPlayerKillEvent;
import mc.alk.arena.objects.WinLossDraw;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.events.EventPriority;
import mc.alk.arena.objects.scoreboard.ArenaObjective;
import mc.alk.arena.objects.scoreboard.ArenaScoreboard;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.objects.victoryconditions.interfaces.ScoreTracker;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class PlayerKills extends VictoryCondition implements ScoreTracker{
    final ArenaObjective kills;
    final TrackerController sc;
    final int points;

    public PlayerKills(Match match, ConfigurationSection section) {
        super(match);
        points = section.getInt("points.player", 1);
        String displayName = section.getString("displayName","Player Kills");
        String criteria = section.getString("criteria", "Kill Players");
        kills = new ArenaObjective(getClass().getSimpleName(),displayName, criteria,
                SAPIDisplaySlot.SIDEBAR, 60);

        boolean isRated = match.getParams().isRated();
        boolean soloRating = !match.getParams().isTeamRating();
        sc = (isRated && soloRating) ? new TrackerController(match.getParams()): null;
    }

    @ArenaEventHandler(priority=EventPriority.LOW)
    public void playerKillEvent(ArenaPlayerKillEvent event) {
        kills.addPoints(event.getPlayer(), points);
        kills.addPoints(event.getTeam(), points);
        if (sc != null)
            sc.addRecord(event.getPlayer(),event.getTarget(), WinLossDraw.WIN);
    }

    @ArenaEventHandler(priority = EventPriority.LOW)
    public void onFindCurrentLeader(MatchFindCurrentLeaderEvent event) {
        event.setResult(kills.getMatchResult(match));
    }

    
    public List<ArenaTeam> getLeaders() {
        return kills.getTeamLeaders();
    }

    
    public TreeMap<Integer,Collection<ArenaTeam>> getRanks() {
        return kills.getTeamRanks();
    }

    
    public void setScoreBoard(ArenaScoreboard scoreboard) {
        this.kills.setScoreBoard(scoreboard);
        scoreboard.addObjective(kills);
    }

    
    public void setDisplayTeams(boolean display) {
        kills.setDisplayTeams(display);
    }
}

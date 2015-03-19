package mc.alk.arena.listeners;

import mc.alk.arena.events.BAEvent;
import mc.alk.arena.events.players.ArenaPlayerTeleportEvent;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.CompetitionState;
import mc.alk.arena.objects.LocationType;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.MatchState;
import mc.alk.arena.objects.StateOption;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.options.StateOptions;
import mc.alk.arena.objects.spawns.SpawnLocation;
import mc.alk.arena.objects.teams.ArenaTeam;
import org.bukkit.event.Listener;


public interface PlayerHolder extends Listener, ArenaListener{
	/**
	 * Add an arena listener
	 * @param arenaListener ArenaListener
	 */
	public void addArenaListener(ArenaListener arenaListener);

    /**
     * Remove an arena listener
     * @param arenaListener ArenaListener
     * @return boolean if found and removed
     */
    public boolean removeArenaListener(ArenaListener arenaListener);

	public MatchParams getParams();

	public CompetitionState getState();

    /**
     * Use getState instead
     * @return MatchState
     */
    @Deprecated
	public MatchState getMatchState();

	public boolean isHandled(ArenaPlayer player);

	public boolean checkReady(ArenaPlayer player, ArenaTeam team, StateOptions mo, boolean b);

	public void callEvent(BAEvent event);

	public SpawnLocation getSpawn(int index, boolean random);

//	public SpawnLocation getSpawn(ArenaPlayer player, boolean random);

	public LocationType getLocationType();

	public ArenaTeam getTeam(ArenaPlayer player);

	public void onPreJoin(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostJoin(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreQuit(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostQuit(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreEnter(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostEnter(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreLeave(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostLeave(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

    /**
     * Checks the current CompetitionState for the given option
     * @param option the option to check for
     * @return true if the current CompetitionState has the specified options or false otherwise
     */
    boolean hasOption(StateOption option);
}

package mc.alk.arena.controllers;

import mc.alk.arena.controllers.containers.LobbyContainer;
import mc.alk.arena.controllers.containers.RoomContainer;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.LocationType;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.arenas.ArenaType;
import mc.alk.arena.objects.spawns.SpawnLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RoomController {
    INSTANCE;

    final Map<ArenaType,LobbyContainer> lobbies = new HashMap<ArenaType,LobbyContainer>();

    private RoomContainer getOrCreate(ArenaType type) {
        LobbyContainer lobby = lobbies.get(type);
        if (lobby == null) {
            MatchParams mp = new MatchParams(type);
            mp.setParent(ParamController.getMatchParams(type));
            lobby = new LobbyContainer("lb_" + type.getName(),
                    mp, LocationType.LOBBY);
            lobbies.put(type, lobby);
        }
        return lobby;
    }

    public static void addLobby(ArenaType type, int index, int spawnIndex, SpawnLocation location) {
        RoomContainer room = INSTANCE.getOrCreate(type);
        room.setSpawnLoc(index,spawnIndex, location);
    }

    public static boolean hasLobby(ArenaType type) {
        return INSTANCE.lobbies.containsKey(type);
    }

    public static LobbyContainer getLobby(ArenaType type) {
        return INSTANCE.lobbies.get(type);
    }

    public static SpawnLocation getLobbySpawn(int index, ArenaType type, boolean randomRespawn) {
        return INSTANCE.getSpawn(index,type, randomRespawn);
    }

    private SpawnLocation getSpawn(int index, ArenaType type, boolean randomRespawn) {
        RoomContainer lobby = lobbies.get(type);
        if (lobby == null)
            return null;
        return lobby.getSpawn(index, randomRespawn);
    }

    public static void setLobbyParams(MatchParams mp) {
        RoomContainer lobby = INSTANCE.getOrCreate(mp.getType());
        lobby.setParams(mp);
    }

    public static Collection<LobbyContainer> getLobbies() {
        return INSTANCE.lobbies.values();
    }

    public static void cancelAll() {
        synchronized(INSTANCE.lobbies){
            for (RoomContainer lc : INSTANCE.lobbies.values()){
                lc.cancel();
            }
        }
    }

    public static void leaveLobby(MatchParams params, ArenaPlayer p) {
        RoomContainer lobby = INSTANCE.lobbies.get(params.getType());
        if (lobby == null)
            return;
        lobby.playerLeaving(p);
    }

    public static void updateRoomParams(MatchParams matchParams) {
        synchronized(INSTANCE.lobbies) {
            LobbyContainer lc = INSTANCE.lobbies.get(matchParams.getType());
            if (lc != null) {
                lc.getParams().setParent(matchParams);
            }
        }
    }

    public static void updateArenaParams(Arena arena) {
        MatchParams arenaParams = arena.getParams();
        List<RoomContainer> rcs = new ArrayList<RoomContainer>();
        rcs.add(arena.getWaitroom());
        rcs.add(arena.getSpectatorRoom());
        rcs.add(arena.getVisitorRoom());
        for (RoomContainer rc : rcs){
            if (rc == null)
                continue;
            rc.getParams().setParent(arenaParams);
        }
    }

    public static RoomContainer getOrCreateRoom(Arena arena, LocationType locationType) {
        RoomContainer rc = null;
        switch(locationType){
            case WAITROOM: rc = arena.getWaitroom(); break;
            case SPECTATE: rc = arena.getSpectatorRoom(); break;
            case VISITOR: rc = arena.getVisitorRoom(); break;
            default: break;
        }
        if (rc != null)
            return rc;
        MatchParams ap = new MatchParams(arena.getArenaType());
        ap.setParent(arena.getParams());
        String name;
        switch(locationType){
            case WAITROOM:
                name = "wr_" + arena.getName() + "";
                rc = new RoomContainer(name, ap, locationType);
                arena.setWaitRoom(rc);
                break;
            case SPECTATE:
                name = "s_" + arena.getName() + "";
                rc = new RoomContainer(name, ap, locationType);
                arena.setSpectatorRoom(rc);
                break;
            case VISITOR:
                name = "v_" + arena.getName() + "";
                rc = new RoomContainer(name, ap, locationType);
                arena.setVisitorRoom(rc);
                break;
            default:
                break;
        }
        return rc;
    }
}

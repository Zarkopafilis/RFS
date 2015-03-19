package mc.alk.arena.competition.match;

import mc.alk.arena.BattleArena;
import mc.alk.arena.Defaults;
import mc.alk.arena.controllers.ArenaClassController;
import mc.alk.arena.controllers.RoomController;
import mc.alk.arena.controllers.Scheduler;
import mc.alk.arena.controllers.TeleportController;
import mc.alk.arena.events.matches.MatchPlayersReadyEvent;
import mc.alk.arena.events.players.ArenaPlayerDeathEvent;
import mc.alk.arena.events.players.ArenaPlayerKillEvent;
import mc.alk.arena.events.players.ArenaPlayerReadyEvent;
import mc.alk.arena.listeners.PlayerHolder;
import mc.alk.arena.objects.ArenaClass;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.ArenaSize;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.MatchState;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.events.EventPriority;
import mc.alk.arena.objects.options.StateOptions;
import mc.alk.arena.objects.options.TransitionOption;
import mc.alk.arena.objects.spawns.SpawnLocation;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.util.CommandUtil;
import mc.alk.arena.util.Countdown;
import mc.alk.arena.util.Countdown.CountdownCallback;
import mc.alk.arena.util.DmgDeathUtil;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.MessageUtil;
import mc.alk.arena.util.PermissionsUtil;
import mc.alk.arena.util.PlayerUtil;
import mc.alk.arena.util.TeamUtil;
import mc.alk.scoreboardapi.api.SEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ArenaMatch extends Match {
    static boolean disabledAllCommands;
    final static HashSet<String> disabledCommands = new HashSet<String>();
    final static HashSet<String> enabledCommands = new HashSet<String>();

    final Map<UUID, Integer> deathTimer = new HashMap<UUID, Integer>();
    final Map<UUID, Integer> respawnTimer = new HashMap<UUID, Integer>();

    public ArenaMatch(Arena arena, MatchParams mp, Collection<ArenaListener> listeners) {
        super(arena, mp,listeners);
    }


    @ArenaEventHandler(suppressCastWarnings=true,bukkitPriority=org.bukkit.event.EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final ArenaPlayer target = BattleArena.toArenaPlayer(event.getEntity());
        if (Defaults.DEBUG_TRACE) MessageUtil.sendMessage(target, " -onPlayerDeath  t=" + target.getTeam());
        if (state == MatchState.ONCANCEL || state == MatchState.ONCOMPLETE) {
            return;
        }
        final ArenaTeam t = getTeam(target);
        if (t == null)
            return;

        ArenaPlayerDeathEvent apde = new ArenaPlayerDeathEvent(target, t);
        apde.setPlayerDeathEvent(event);
        callEvent(apde);
        ArenaPlayer killer = DmgDeathUtil.getPlayerCause(event);
        if (killer != null) {
            ArenaTeam killT = getTeam(killer);
            if (killT != null) { /// they must be in the same match for this to count
                killT.addKill(killer);
                callEvent(new ArenaPlayerKillEvent(killer, killT, target));
            }
        }
    }

    @ArenaEventHandler(bukkitPriority=org.bukkit.event.EventPriority.MONITOR)
    public void onPlayerDeath(ArenaPlayerDeathEvent event){
        final ArenaPlayer target = event.getPlayer();
        if (state == MatchState.ONCANCEL || state == MatchState.ONCOMPLETE){
            return;}
        final ArenaTeam t = event.getTeam();

        Integer nDeaths = t.addDeath(target);
        boolean exiting = event.isExiting() || !respawns || nDeaths >= nLivesPerPlayer;
        event.setExiting(exiting);
        final boolean trueDeath = event.getPlayerDeathEvent() != null;
        /// Set their number of lives left on the scoreboard "if" it's not infinite or 1
        if (nLivesPerPlayer != ArenaSize.MAX) {
            int curLives = nLivesPerPlayer - nDeaths;
            SEntry e = scoreboard.getEntry(target.getPlayer());
            if (e != null)
                scoreboard.setEntryNameSuffix(e, curLives <= 1 ? "" : "&4(" + curLives + ")");
        }
        if (trueDeath){
            PlayerDeathEvent pde = event.getPlayerDeathEvent();
            if (cancelExpLoss)
                pde.setKeepLevel(true);

            /// Handle Drops from bukkitEvent
            if (clearsInventoryOnDeath || keepsInventory){ /// clear the drops
                try {pde.getDrops().clear();} catch (Exception e){
                    if (!Defaults.DEBUG_VIRTUAL)
                        Log.printStackTrace(e);
                }
            } else if (woolTeams) {  /// Get rid of the wool from teams so it doesnt drop
                final int index = t.getIndex();
                ItemStack teamHead = TeamUtil.getTeamHead(index);
                List<ItemStack> items = pde.getDrops();
                for (ItemStack is : items) {
                    if (is.getType() == teamHead.getType() && is.getDurability() == teamHead.getDurability()) {
                        final int amt = is.getAmount();
                        if (amt > 1)
                            is.setAmount(amt - 1);
                        else
                            is.setType(Material.AIR);
                        break;
                    }
                }
            }
            /// If keepInventory is specified, but not restoreAll, then we have a case
            /// where we need to give them back the current Inventory they have on them
            /// even if they log out
            if (keepsInventory){
                boolean restores = getParams().hasOptionAt(MatchState.ONLEAVE,TransitionOption.RESTOREITEMS);
                /// Restores and exiting, means clear their match inventory so they won't
                /// get their match and their already stored inventory
                if (restores && exiting){
                    psc.clearMatchItems(target);
                } else { /// keep their current inv
                    psc.storeMatchItems(target);
                }
            }
            /// We can't let them just sit on the respawn screen... schedule them to lose
            /// We will cancel this onRespawn
            final ArenaMatch am = this;
            Integer timer = deathTimer.get(target.getID());
            if (timer != null){
                Bukkit.getScheduler().cancelTask(timer);
            }
//            timer = Scheduler.scheduleSynchronousTask(new Runnable(){
//                
//                public void run() {
//                    am.performTransition(MatchState.ONCOMPLETE, target, t, true);
//                    checkAndHandleIfTeamDead(t);
//                }
//            }, 15*20L);
//            deathTimer.put(target.getID(), timer);
        }
        if (exiting){
            performTransition(MatchState.ONCOMPLETE, target, t, true);
            checkAndHandleIfTeamDead(t);
        }
    }



    //	@MatchEventHandler(suppressCastWarnings=true,priority=EventPriority.HIGHER)
    //	public void onCheckEmulateDeath(EntityDamageEvent event) {
    //		//		Log.debug("############## checking emulate   " + event.getEntity() +"    " + event.isCancelled() +"    " + event.getDamage());
    //		if (event.isCancelled() || event.getDamage() <= 0 || !(event.getEntity() instanceof Player))
    //			return;
    //		Player target = ((Player) event.getEntity());
    //		//		Log.debug("############## checking health   " + event.getDamage() +"    " + target.getHealth());
    //		if (event.getDamage() < target.getHealth()){
    //			return;}
    //
    //		PlayerInventory pinv = target.getInventory();
    //		ArenaPlayer ap = BattleArena.toArenaPlayer(target);
    //		ArenaTeam targetTeam = getTeam(ap);
    //		if (clearsInventoryOnDeath){
    //			pinv.clear();
    //			if (woolTeams){
    //				if (targetTeam != null && targetTeam.getHeadItem() != null){
    //					TeamUtil.setTeamHead(targetTeam.getHeadItem(), target);
    //				}
    //			}
    //		}
    //
    //		Integer nDeaths = targetTeam.getNDeaths(ap);
    //		boolean exiting = !respawns || (nDeaths != null && nDeaths +1 >= nLivesPerPlayer);
    //
    //		ArenaPlayerDeathEvent apde = new ArenaPlayerDeathEvent(ap,targetTeam);
    //		callEvent(apde);
    //		ArenaPlayer killer = DmgDeathUtil.getPlayerCause(event);
    //		if (killer != null){
    //			ArenaTeam killT = getTeam(killer);
    //			if (killT != null){ /// they must be in the same match for this to count
    //				killT.addKill(killer);
    //				callEvent(new ArenaPlayerKillEvent(killer,killT,ap));
    //			}
    //		}
    //		TransitionController.transition(this, MatchState.ONDEATH, ap, targetTeam , false);
    //		TransitionController.transition(this, MatchState.ONDEATH, ap, targetTeam , false);
    //
    //		EffectUtil.deEnchantAll(target);
    //		target.closeInventory();
    //		target.setFireTicks(0);
    //		target.setHealth(target.getMaxHealth());
    //		if (!exiting){
    //			final int teamIndex = indexOf(targetTeam);
    //			final Location l = TransitionController.jitter(getTeamSpawn(teamIndex,false),rand.nextInt(targetTeam.size()));
    //			TeleportController.teleportPlayer(target, l, false, true);
    //		}
    //	}

    @ArenaEventHandler(priority=EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final ArenaPlayer p = BattleArena.toArenaPlayer(event.getPlayer());
        if (Defaults.DEBUG_TRACE) MessageUtil.sendMessage(p, " -onPlayerRespawn  t=" + p.getTeam());

        if (isWon()) {
            return;
        }
        final StateOptions mo = tops.getOptions(MatchState.ONDEATH);

        if (mo == null)
            return;

        if (respawns) {
            final boolean randomRespawn = mo.randomRespawn();
            /// Lets cancel our death respawn timer
            Integer timer = deathTimer.get(p.getID());
            if (timer != null) {
                Bukkit.getScheduler().cancelTask(timer);
            }
            final SpawnLocation loc;
            final ArenaTeam t = getTeam(p);
            /// We respawn them to a different location
            if (mo.hasAnyOption(TransitionOption.TELEPORTLOBBY, TransitionOption.TELEPORTMAINLOBBY,
                    TransitionOption.TELEPORTWAITROOM, TransitionOption.TELEPORTMAINWAITROOM)) {
                final int index = t.getIndex();
                if (mo.hasOption(TransitionOption.TELEPORTLOBBY)) {
                    loc = RoomController.getLobbySpawn(index, getParams().getType(), randomRespawn);
                } else if (mo.hasOption(TransitionOption.TELEPORTMAINLOBBY)) {
                    loc = RoomController.getLobbySpawn(Defaults.MAIN_SPAWN, getParams().getType(), randomRespawn);
                } else if (mo.hasOption(TransitionOption.TELEPORTMAINWAITROOM)) {
                    loc = this.getWaitRoomSpawn(Defaults.MAIN_SPAWN, randomRespawn);
                } else {
                    loc = this.getWaitRoomSpawn(index, randomRespawn);
                }
                /// Should we respawn the player to the team spawn after a certain amount of time
                Integer respawnTime = mo.getInt(TransitionOption.RESPAWNTIME);
                if (respawnTime==null)
                    respawnTime = 15;
                final Integer finalRespawnTime = respawnTime;
                if (scoreboard != null) {
                    new Countdown(BattleArena.getSelf(), finalRespawnTime, 1, new CountdownCallback() {
                        
                        public boolean intervalTick(int secondsRemaining) {
                            if (!scoreboard.hasThisScoreboard(p.getPlayer())) {
                                scoreboard.setScoreboard(p.getPlayer());
                            }
                            SEntry e = scoreboard.getEntry(p.getPlayer());
                            if (e != null) {
                                /// Either set the seconds remaining, or back to the current lives
                                if (secondsRemaining > 0) {
                                    scoreboard.setEntryNameSuffix(e, "&e(" + secondsRemaining + ")");
                                } else {
                                    Integer curLives  = nLivesPerPlayer - t.getNDeaths(p);
                                    String s = (nLivesPerPlayer != ArenaSize.MAX && curLives != 1) ?
                                            "&4(" + curLives + ")" : "";
                                    scoreboard.setEntryNameSuffix(e, s);
                                }
                            }
                            return true;
                        }
                    });
                }

                int id = Scheduler.scheduleSynchronousTask(new Runnable() {
                    
                    public void run() {
                        Integer id = respawnTimer.remove(p.getID());
                        Bukkit.getScheduler().cancelTask(id);
                        SpawnLocation loc = getTeamSpawn(index, tops.hasOptionAt(MatchState.ONSPAWN, TransitionOption.RANDOMRESPAWN));
                        TeleportController.teleport(p.getPlayer(), loc.getLocation());

                    }
                }, respawnTime * 20);
                respawnTimer.put(p.getID(), id);

            } else {
                loc = getTeamSpawn(getTeam(p), randomRespawn);
            }

            event.setRespawnLocation(loc.getLocation());
            /// For some reason, the player from onPlayerRespawn Event isnt the one in the main thread, so we need to
            /// resync before doing any effects
            final ArenaMatch am = this;
            Scheduler.scheduleSynchronousTask(new Runnable() {
                
                public void run() {
                    ArenaTeam t = getTeam(p);
                    am.performTransition(MatchState.ONDEATH, p, t, false);
                    am.performTransition(MatchState.ONSPAWN, p, t, false);
                    if (respawnsWithClass) {
                        ArenaClass ac = null;
                        if (p.getPreferredClass() != null) {
                            ac = p.getPreferredClass();
                        } else if (p.getCurrentClass() != null) {
                            ac = p.getCurrentClass();
                        }
                        if (ac != null) {
                            ArenaClassController.giveClass(p, ac);
                        }
                    }
                    if (keepsInventory) {
                        psc.restoreMatchItems(p);
                    }
                    if (woolTeams) {
                        TeamUtil.setTeamHead(t.getIndex(), p);
                    }
                }
            });
        } else { /// This player is now out of the system now that we have given the ondeath effects
            Location l = tops.hasOptionAt(MatchState.ONLEAVE, TransitionOption.TELEPORTTO) ?
                    tops.getOptions(MatchState.ONLEAVE).getTeleportToLoc() : p.getOldLocation().getLocation();
            if (l != null)
                event.setRespawnLocation(l);
        }
    }

    /**
     * Factions has slashless commands that get handled and then set to cancelled....
     * so we need to act before them
     * @param event PlayerCommandPreprocessEvent
     */
    @ArenaEventHandler(priority=EventPriority.HIGH, bukkitPriority=org.bukkit.event.EventPriority.LOWEST)
    public void onPlayerCommandPreprocess1(PlayerCommandPreprocessEvent event){
        handlePreprocess(event);
    }

    @ArenaEventHandler(priority=EventPriority.HIGH)
    public void onPlayerCommandPreprocess2(PlayerCommandPreprocessEvent event){
        if (event.isCancelled()){
            return;}
        handlePreprocess(event);
    }

    private void handlePreprocess(PlayerCommandPreprocessEvent event) {
        if (CommandUtil.shouldCancel(event, disabledAllCommands, disabledCommands, enabledCommands)){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED+"You cannot use that command when you are in a match");
            if (PermissionsUtil.isAdmin(event.getPlayer())){
                MessageUtil.sendMessage(event.getPlayer(),"&cYou can set &6/bad allowAdminCommands true: &c to change");}
        }
    }

    @ArenaEventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event){
        playerInteract(event);
    }

    private void playerInteract(PlayerInteractEvent event){
        if (event.getClickedBlock() == null ||
                !(event.getClickedBlock().getType().equals(Material.SIGN) ||
                        event.getClickedBlock().getType().equals(Material.WALL_SIGN) ||
                        event.getClickedBlock().getType().equals(Defaults.READY_BLOCK)
                )) {
            return;
        }

        /// Check to see if it's a sign
        if (event.getClickedBlock().getType().equals(Material.SIGN) ||
                event.getClickedBlock().getType().equals(Material.WALL_SIGN)){ /// Only checking for signs
            //			signClick(event,this);
        } else { /// its a ready block
            if (respawnTimer.containsKey(PlayerUtil.getID(event.getPlayer()))){
                respawnClick(event,this,respawnTimer);
            } else {
                readyClick(event);
            }
        }
    }

    public static void respawnClick(PlayerInteractEvent event, PlayerHolder am, Map<UUID,Integer> respawnTimer) {
        ArenaPlayer ap = BattleArena.toArenaPlayer(event.getPlayer());
        Integer id = respawnTimer.remove(ap.getID());
        Bukkit.getScheduler().cancelTask(id);
        SpawnLocation loc = am.getSpawn(am.getTeam(ap).getIndex(),
                am.getParams().hasOptionAt(MatchState.ONSPAWN, TransitionOption.RANDOMRESPAWN));
        TeleportController.teleport(ap, loc.getLocation());
    }

    public static void signClick(PlayerInteractEvent event, PlayerHolder am) {
        /// Get our sign
        final Sign sign = (Sign) event.getClickedBlock().getState();
        /// Check to see if sign has correct format (is more efficient than doing string manipulation )
        if (!sign.getLine(0).matches("^.[0-9a-fA-F]\\*.*$") && !sign.getLine(0).matches("^\\[.*$")){
            return;}

        final Action action = event.getAction();
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK){
            return;}
        if (action == Action.LEFT_CLICK_BLOCK){ /// Dont let them break the sign
            event.setCancelled(true);}

        final ArenaClass ac = ArenaClassController.getClass(MessageUtil.decolorChat(
                sign.getLine(0)).replace('*',' ').replace('[',' ').replace(']',' ').trim());
        ArenaClassController.changeClass(event.getPlayer(), am, ac);
    }



    private void readyClick(PlayerInteractEvent event) {
        if (!Defaults.ENABLE_PLAYER_READY_BLOCK)
            return;
        final ArenaPlayer ap = BattleArena.toArenaPlayer(event.getPlayer());
        if (!isInWaitRoomState()){
            return;}
        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK){ /// Dont let them break the block
            event.setCancelled(true);}

        if (ap.isReady())
            return;
        ap.setReady(true);
        MessageUtil.sendMessage(ap, "&2You ready yourself for the arena");
        callEvent(new ArenaPlayerReadyEvent(ap,true));
    }

    @ArenaEventHandler
    /// TODO
    @SuppressWarnings("unused")
    public void onPlayerReady(ArenaPlayerReadyEvent event){
        if (!Defaults.ENABLE_PLAYER_READY_BLOCK){
            return;}
        int tcount = 0;
        int pcount = 0;
        for (ArenaTeam t: teams){
            if (!t.isReady() && t.size() > 0)
                return;
            tcount++;
            pcount+= t.size();
        }
        if (tcount < params.getMinTeams() || pcount < params.getMinPlayers())
            return;
        callEvent(new MatchPlayersReadyEvent(this));
    }

    public static void setDisabledCommands(List<String> commands) {
        if (commands == null)
            return;
        disabledCommands.clear();
        if (commands.contains("all")) {
            disabledAllCommands = true;
        } else {
            for (String s: commands){
                disabledCommands.add("/" + s.toLowerCase());}
        }
    }


    public static void setEnabledCommands(List<String> commands) {
        if (commands == null)
            return;
        enabledCommands.clear();
        for (String s: commands){
            enabledCommands.add("/" + s.toLowerCase());}
    }

}

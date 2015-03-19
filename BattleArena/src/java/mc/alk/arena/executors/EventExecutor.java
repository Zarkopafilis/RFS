package mc.alk.arena.executors;

import mc.alk.arena.BattleArena;
import mc.alk.arena.competition.events.Event;
import mc.alk.arena.controllers.BAEventController;
import mc.alk.arena.controllers.BAEventController.SizeEventPair;
import mc.alk.arena.controllers.TeamController;
import mc.alk.arena.controllers.messaging.MessageHandler;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.EventParams;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.StateGraph;
import mc.alk.arena.objects.exceptions.InvalidEventException;
import mc.alk.arena.objects.exceptions.InvalidOptionException;
import mc.alk.arena.objects.joining.TeamJoinObject;
import mc.alk.arena.objects.options.EventOpenOptions;
import mc.alk.arena.objects.options.EventOpenOptions.EventOpenOption;
import mc.alk.arena.objects.options.JoinOptions;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.MessageUtil;
import mc.alk.arena.util.PermissionsUtil;
import mc.alk.arena.util.TimeUtil;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


public class EventExecutor extends BAExecutor{
	protected final BAEventController controller;

	public EventExecutor(){
		super();
		controller = BattleArena.getBAEventController();
	}

	@MCCommand(cmds={"options"},admin=true, usage="options", order=2)
	public boolean eventOptions(CommandSender sender,EventParams eventParams) {
		StateGraph tops = eventParams.getThisStateGraph();
        return sendMessage(sender, tops.getOptionString());
	}

	@MCCommand(cmds={"cancel"},admin=true, order=2)
	public boolean eventCancel(CommandSender sender, EventParams eventParams) {
		Event event = findUnique(sender, eventParams);
        return event == null || cancelEvent(sender, event);
    }

	protected Event findUnique(CommandSender sender, EventParams eventParams) {
		SizeEventPair result = controller.getUniqueEvent(eventParams);
		if (result.nEvents == 0){
			sendMessage(sender, "&cThere are no events open/running of this type");}
		else if (result.nEvents > 1){
			sendMessage(sender, "&cThere are multiple events ongoing, please specify the arena of the event. \n&6/"+
					eventParams.getCommand()+" ongoing &c for a list");}
		return result.event;
	}

	@MCCommand(cmds={"cancel"},admin=true, order=4)
	public boolean eventCancel(CommandSender sender, ArenaPlayer player) {
		Event event = controller.getEvent(player);
		if (event == null){
			return sendMessage(sender, "&cThere was no event with " + player.getName() +" inside");}
		return cancelEvent(sender,event);
	}

	public boolean cancelEvent(CommandSender sender, Event event){
		if (!event.isRunning() && !event.isOpen()){
			return sendMessage(sender,"&eA "+event.getCommand()+" is not running");}
		controller.cancelEvent(event);
		return sendMessage(sender,"&eYou have canceled the &6" + event.getName());
	}

	@MCCommand(cmds={"start"},admin=true,usage="start", order=2)
	public boolean eventStart(CommandSender sender, final EventParams eventParams, String[] args) {
		Event event = controller.getOpenEvent(eventParams);
		if (event == null){
			return sendMessage(sender, "&cThere are no open events right now");
		}

		final String name = event.getName();
		if (!event.isOpen()){
			sendMessage(sender,"&eYou need to open a "+name+" before starting one");
			return sendMessage(sender,"&eType &6/"+event.getCommand()+" open <params>&e : to open one");
		}
		boolean forceStart = args.length > 1 && args[1].equalsIgnoreCase("force");
		if (!forceStart && !event.hasEnoughTeams()){
			final int nteams = event.getNTeams();
			final int neededTeams = event.getParams().getMinTeams();
			sendMessage(sender,"&cThe "+name+" only has &6" + nteams +" &cteams and it needs &6" +neededTeams);
			return sendMessage(sender,"&cIf you really want to start the bukkitEvent anyways. &6/"+event.getCommand()+" start force");
		}
		try {
			controller.startEvent(event);
			return sendMessage(sender,"&2You have started the &6" + name);
		} catch (Exception e) {
			sendMessage(sender,"&cError Starting the &6" + name);
			Log.printStackTrace(e);
			return sendMessage(sender,"&c" +e.getMessage());
		}
	}

	@MCCommand(cmds={"info"},usage="info", order=2)
	public boolean eventInfo(CommandSender sender, EventParams eventParams){
		Event event = findUnique(sender, eventParams);
		if (event == null){
			return true;}

		if (!event.isOpen() && !event.isRunning()){
			return sendMessage(sender,"&eThere is no open "+event.getCommand()+" right now");}
		int size = event.getNTeams();
		String teamOrPlayers = MessageUtil.getTeamsOrPlayers(eventParams.getMaxTeamSize());
		sendMessage(sender,"&eThere are currently &6" + size +"&e "+teamOrPlayers);
        return sendMessage(sender, event.getInfo());
	}


	@MCCommand(cmds={"check"},usage="check", order=2)
	public boolean eventCheck(CommandSender sender, EventParams eventParams) {
		Event event = findUnique(sender, eventParams);
		if (event == null){
			return true;}

		if (!event.isOpen()){
			return sendMessage(sender,"&eThere is no open &6"+event.getCommand()+"&e right now");}
		int size = event.getNTeams();
		String teamOrPlayers = MessageUtil.getTeamsOrPlayers(eventParams.getMaxTeamSize());
		return  sendMessage(sender,"&eThere are currently &6" + size +"&e "+teamOrPlayers+" that have joined");
	}

	@Override
	@MCCommand(cmds={"join"})
	public boolean join(ArenaPlayer player, MatchParams mp, String args[]) {
        //noinspection SimplifiableIfStatement
        if (mp instanceof EventParams){
			return eventJoin(player, (EventParams)mp, args);}
		return true; /// awkward, how did they get here???
	}

	@MCCommand(cmds={"join"}, order=2)
	public boolean eventJoin(ArenaPlayer player, EventParams eventParams, String[] args) {
		eventJoin(player, eventParams, args, false);
		return true;
	}

	public boolean eventJoin(ArenaPlayer p, EventParams eventParams, String[] args, boolean adminCommand) {
		if (!adminCommand && !PermissionsUtil.hasMatchPerm(p.getPlayer(), eventParams, "join")){
			sendSystemMessage(p,"no_join_perms", eventParams.getCommand());
			return false;
		}
		if (isDisabled(p.getPlayer(), eventParams)){
			return true;}
		Event event = controller.getOpenEvent(eventParams);

		/// perform add checks
		if (event == null){
			sendSystemMessage(p, "no_event_open");
			return false;
		}

		if (!event.canJoin()){
			sendSystemMessage(p, "you_cant_join_event_while", event.getCommand(), event.getState());
			return false;
		}

		if (!canJoin(p)){
			return false;}

		if (event.waitingToJoin(p)){
			sendSystemMessage(p, "you_will_join_when_matched");
			return false;
		}

		EventParams sq = event.getParams();
		StateGraph tops = sq.getStateOptions();
		/// Perform is ready check
		if(!tops.playerReady(p,null)){
			String notReadyMsg = tops.getRequiredString(MessageHandler.getSystemMessage("need_the_following")+"\n");
			MessageUtil.sendMessage(p,notReadyMsg);
			return false;
		}
		/// Get the team
		ArenaTeam t = teamc.getSelfFormedTeam(p);
		if (t==null){
			t = TeamController.createTeam(eventParams, p); }
		/// Get or Make a team for the Player

		if (!canJoin(t,true)){
			sendSystemMessage(p, "teammate_cant_join");
			return sendMessage(p,"&6/team leave: &cto leave the team");
		}

		/// Check any options specified in the add
		JoinOptions jp;
		try {
			jp = JoinOptions.parseOptions(sq, p, Arrays.copyOfRange(args, 1, args.length));
		} catch (InvalidOptionException e) {
			return sendMessage(p, e.getMessage());
		} catch (Exception e){
			Log.printStackTrace(e);
			jp = null;
		}
		if (sq.getMaxTeamSize() < t.size()){
			sendSystemMessage(p, "event_invalid_team_size", sq.getMaxTeamSize(), t.size());
			return false;
		}

		/// Now that we have options and teams, recheck the team for joining
		if (!event.canJoin(t)){
			return false;}
		/// Check fee
		if (!checkAndRemoveFee(sq, t)){
			return false;}
		TeamJoinObject tqo = new TeamJoinObject(t,sq,jp);

		/// Finally actually add the event
		event.joining(tqo);
		//		sendSystemMessage(t, "you_joined_event", event.getDisplayName());
		if (sq.getSecondsTillStart() != null){
			Long time = event.getTimeTillStart();
			if (time != null)
				sendSystemMessage(p, "event_will_start_in", TimeUtil.convertMillisToString(time));
		}
		return true;
	}


	@MCCommand(cmds={"teams"}, usage="teams", admin=true, order=2)
	public boolean eventTeams(CommandSender sender, EventParams eventParams) {
		Event event = findUnique(sender, eventParams);
        return event == null || eventTeams(sender, event);
    }

	private boolean eventTeams(CommandSender sender, Event event) {
		StringBuilder sb = new StringBuilder();
		for (ArenaTeam t: event.getTeams()){
			sb.append("\n").append(t.getTeamInfo(null)); }

		return sendMessage(sender,sb.toString());
	}

	@MCCommand(cmds={"status"}, usage="status", order=4)
	public boolean eventStatus(CommandSender sender, EventParams eventParams) {
		Event event = findUnique(sender, eventParams);
		if (event == null){
			return true;}
		StringBuilder sb = new StringBuilder(event.getStatus());
		appendTeamStatus(sender, event, sb);
		return sendMessage(sender,sb.toString());
	}


	private void appendTeamStatus(CommandSender sender, Event event, StringBuilder sb) {
		if (PermissionsUtil.isAdmin(sender) || sender.hasPermission("arena.event.status")){
			for (ArenaTeam t: event.getTeams()){
				sb.append("\n").append(t.getTeamInfo(null));
			}
		}
	}

	@MCCommand(cmds={"result"},usage="result", order=2)
	public boolean eventResult(CommandSender sender, EventParams eventParams) {
		Event event = findUnique(sender, eventParams);
		if (event == null){
			return true;}

		StringBuilder sb = new StringBuilder(event.getResultString());
		if (sb.length() == 0){
			return sendMessage(sender,"&eThere are no results for a previous &6" +event.getDisplayName() +"&e right now");
		}
		return sendMessage(sender,"&eResults for the &6" + event.getDisplayName() + "&e\n" + sb.toString());
	}

    protected void openEvent(Event event, EventOpenOptions eoo) throws InvalidEventException {
        if (eoo.hasOption(EventOpenOption.SILENT)){
            event.setSilent(true);}
        controller.addOpenEvent(event);
        if (eoo.hasOption(EventOpenOption.AUTO)) {
            event.autoEvent();
        } else {
            event.openEvent();
        }
        if (eoo.hasOption(EventOpenOption.FORCEJOIN)){
            event.addAllOnline();}
    }

}

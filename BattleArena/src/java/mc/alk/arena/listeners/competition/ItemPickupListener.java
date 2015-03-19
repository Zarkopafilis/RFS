package mc.alk.arena.listeners.competition;

import mc.alk.arena.listeners.PlayerHolder;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.events.EventPriority;
import mc.alk.arena.objects.options.TransitionOption;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemPickupListener implements ArenaListener{
    final PlayerHolder holder;

	public ItemPickupListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerItemPickupItem(PlayerPickupItemEvent event){
		if (holder.hasOption(TransitionOption.ITEMPICKUPOFF)){
			event.setCancelled(true);}
	}
}

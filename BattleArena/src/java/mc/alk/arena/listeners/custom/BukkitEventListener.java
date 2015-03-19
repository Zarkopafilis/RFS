package mc.alk.arena.listeners.custom;

import mc.alk.arena.Defaults;
import mc.alk.arena.util.Log;
import org.bukkit.event.Event;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


/**
 *
 * Bake and handling based on Bukkit and lahwran's fevents.
 * @author alkarin
 *
 */
class BukkitEventListener extends GeneralEventListener {


	/**
	 * Construct a listener to listen for the given bukkit event
	 * @param bukkitEvent : which event we will listen for
	 * @param getPlayerMethod : a method which when not null and invoked will return a Player
	 */
	public BukkitEventListener(final Class<? extends Event> bukkitEvent,
                               org.bukkit.event.EventPriority bukkitPriority, Method getPlayerMethod) {
		super(bukkitEvent, bukkitPriority);
		if (Defaults.DEBUG_EVENTS) Log.info("Registering GenericPlayerEventListener for type " +
                bukkitEvent.getSimpleName() +" pm="+(getPlayerMethod == null ? "null" : getPlayerMethod.getName()));
	}

    /**
     * add an arena listener to this bukkit event
     * @param rl RListener
     */
    
    protected synchronized void addMatchListener(RListener rl) {
        if (!isListening()){
            startListening();}

        Map<RListener,Integer> l = listeners.get(rl.getPriority());
        if (l == null){
            l = new TreeMap<RListener,Integer>(new Comparator<RListener>(){
                
                public int compare(RListener o1, RListener o2) {
                    return o1.getListener().equals(o2.getListener()) ? 0 :
                            new Integer(o1.hashCode()).compareTo(o2.hashCode());
                }
            });
            listeners.put(rl.getPriority(), l);
        }

        Integer count = l.get(rl);
        if (count == null){
            l.put(rl,1);
            handlers = null;
            bake();
        } else {
            l.put(rl,count+1);
        }
    }

    /**
     * remove an arena listener to this bukkit event
     * @param listener RListener
     * @return whether listener was found and removed
     */
    
    protected boolean removeMatchListener(RListener listener) {
        final Map<RListener,Integer> map = listeners.get(listener.getPriority());
        if (map==null)
            return false;
        Integer count = map.get(listener);
        boolean removed;
        if (count == null || count == 1){
            map.remove(listener);
            handlers = null;
            removed = true;
        } else {
            map.put(listener, count-1);
            removed = false;
        }
        if (removed && !hasListeners() && isListening()){
            stopListening();}
        return removed;
    }

	
	public void invokeEvent(final Event event){
		/// For each ArenaListener class that is listening
		for (RListener rl: getRegisteredListeners()){
			try {
                rl.getMethod().getMethod().invoke(rl.getListener(), event); /// Invoke the listening arenalisteners method
			} catch (Exception e){
				Log.printStackTrace(e);
			}
		}
	}
}

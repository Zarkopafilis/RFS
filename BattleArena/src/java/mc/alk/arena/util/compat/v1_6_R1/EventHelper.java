package mc.alk.arena.util.compat.v1_6_R1;

import mc.alk.arena.util.compat.IEventHelper;
import org.bukkit.event.entity.EntityDamageEvent;

public class EventHelper implements IEventHelper {

    
    public void setDamage(EntityDamageEvent event, double damage) {
        event.setDamage(damage);
    }
}

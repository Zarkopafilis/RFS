package mc.alk.arena.objects.spawns;

import mc.alk.arena.util.InventoryUtil;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;


public class ItemSpawn extends SpawnInstance{
    final ItemStack is;
	Entity uid;
	public ItemSpawn(ItemStack is){
		super(null);
		this.is = is;
	}

    
	public void spawn() {
		if (uid != null && !uid.isDead()){
			return;
		}
		uid = loc.getWorld().dropItemNaturally(loc, is);
	}

    
	public void despawn() {
		if (uid != null){
			uid.remove();
			uid = null;
		}
	}
	public ItemStack getItemStack() {
		return is;
	}

	
	public String toString(){
		return "[IS "+InventoryUtil.getItemString(is)+"]";
	}


}

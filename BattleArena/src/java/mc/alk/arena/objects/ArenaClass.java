package mc.alk.arena.objects;

import mc.alk.arena.objects.spawns.EntitySpawn;
import mc.alk.arena.objects.spawns.SpawnInstance;
import mc.alk.arena.util.InventoryUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArenaClass {
	public static final Integer DEFAULT = Integer.MAX_VALUE;
	public static final ArenaClass CHOSEN_CLASS = new ArenaClass("CHOSENCLASS","chosenClass", null, null);
	public static final ArenaClass SELF_CLASS = new ArenaClass("SELFCLASS","selfClass", null, null);

	/** Name of the Class*/
	final String name;

	/** DisplayName of the class*/
	final String displayName;

	/** Items that this class gives*/
	final List<ItemStack> items;

	/** Effects this class gives*/
	final List<PotionEffect> effects;

	/** Mobs for this class*/
	List<SpawnInstance> mobs;

	/** Name of a disguise for this class */
	String disguiseName;

	/** List of commands to run when class is given */
	List<CommandLineString> commands;

	boolean valid = false;

    public ArenaClass(String name){
		this(name,name,new ArrayList<ItemStack>(),new ArrayList<PotionEffect>());
		valid = false;
	}

	public ArenaClass(String name, String displayName, List<ItemStack> items, List<PotionEffect> effects){
		this.name = name;
		CopyOnWriteArrayList<ItemStack> listitems = new CopyOnWriteArrayList<ItemStack>();
		ArrayList<ItemStack> armoritems = new ArrayList<ItemStack>();
		if (items != null){
			for (ItemStack is: items){
				if (InventoryUtil.isArmor(is)){
					armoritems.add(is);
				} else {
					listitems.add(is);
				}
			}
		}
		this.items = listitems;
		this.items.addAll(armoritems);
		this.effects = effects;
		this.displayName = displayName;
		valid = true;
	}

	/**
	 * Get the name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the items
	 * @return List of items
	 */
	public List<ItemStack> getItems() {
		return items;
	}

	/**
	 * Get the effects
	 * @return list of effects
	 */
	public List<PotionEffect> getEffects() {
		return effects;
	}

	/**
	 * Get the Display Name
	 * @return displayName or name if displayName is null
	 */
	public String getDisplayName() {
		return displayName != null ? displayName : name;
	}

	/**
	 * Get the disguise name
	 * @return disguiseName
	 */
	public String getDisguiseName() {
		return disguiseName;
	}

	/**
	 * Set the disguise name
	 * @param disguiseName String
	 */
	public void setDisguiseName(String disguiseName) {
		this.disguiseName = disguiseName;
	}

	@Override
	public String toString(){
        return "[ArenaClass "+getName()+"]";
	}

	public boolean valid() {
		return valid;
	}

	public void setMobs(List<SpawnInstance> mobs) {
		this.mobs = mobs;
	}

	public List<SpawnInstance> getMobs(){
		return mobs;
	}
	public List<CommandLineString> getDoCommands(){
		return this.commands;
	}
	public void setDoCommands(List<CommandLineString> commands){
		this.commands = commands;
	}


    public List<SpawnInstance> getMobsClone() {
        List<SpawnInstance> l = new ArrayList<SpawnInstance>();
        for (SpawnInstance si: mobs){
            if (si instanceof EntitySpawn){
                l.add(new EntitySpawn((EntitySpawn)si));
            } else {
                l.add(si);
            }
        }
        return l;
    }
}

package mc.alk.arena.util.compat.pre;

import mc.alk.arena.util.compat.IInventoryHelper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.List;

public class InventoryHelper implements IInventoryHelper{
	
	public void setColor(ItemStack itemStack, Color color) {/* do nothing */}

	
	public void setLore(ItemStack itemStack, List<String> lore) {/* do nothing */}

	
	public void setDisplayName(ItemStack itemStack, String displayName) {/* do nothing */}

	
	public void setOwnerName(ItemStack itemStack, String ownerName) {/* do nothing */}

	
	public Color getColor(ItemStack itemStack) {return null;}

	
	public List<String> getLore(ItemStack itemStack) {return null;}

	
	public String getDisplayName(ItemStack itemStack) {return null;}

	
	public String getOwnerName(ItemStack itemStack) {return null;}

    
    public String getCommonNameByEnchantment(Enchantment enchantment) {return enchantment.getName();}

    
    public Enchantment getEnchantmentByCommonName(String itemName) {return null;}

    
    public boolean isEnderChest(InventoryType type) {return false;}
}

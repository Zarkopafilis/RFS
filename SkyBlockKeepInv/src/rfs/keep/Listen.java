package rfs.keep;

import org.bukkit.Bukkit;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Listen implements Listener{

	private Plugin p;
	
	public Listen(Plugin p){
		this.p = p;
	}
	
	@EventHandler
	public void onplace(final SignChangeEvent e){
		if(e.getLine(0).equalsIgnoreCase("[JoinSkyblock]")){
			if(!e.getPlayer().isOp()){
				e.getBlock().breakNaturally();
			}
		}
	}
	
	@EventHandler
	public void onSignHit(final PlayerInteractEvent e){
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			Block b = e.getClickedBlock();
			
			if(b.getState() instanceof Sign){
				
				Sign s = (Sign) b.getState();
				
				if(s.getLine(0).equals("[JoinSkyblock]")){
					
					final ItemStack[] itms = e.getPlayer().getInventory().getContents().clone();
					e.getPlayer().getInventory().clear();
					e.getPlayer().updateInventory();
					
					Bukkit.getServer().dispatchCommand(e.getPlayer(), "sky");
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable(){
						
						@SuppressWarnings("deprecation")
						@Override
						public void run(){
							
							
							for(ItemStack itm : itms){
								
								e.getPlayer().getInventory().addItem(itm);
								e.getPlayer().updateInventory();
							}
							
							
							
							
						}
						
						
					}, 20);
					
					
				}
				
			}
			
			
		}
		
	}
	
}

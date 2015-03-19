package com.rfslabs.rfspoints;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignPlace
  implements Listener
{
@EventHandler
  public void eventSignChanged(SignChangeEvent event)
  {
    String title = event.getLine(0);
    if (title.equalsIgnoreCase("RFS POINTS"))
    {
      if (!event.getPlayer().isOp()) {
        event.getBlock().breakNaturally();
      }else{
    	  event.setLine(0, ChatColor.GREEN + "RFS POINTS");
    	  event.setLine(1, ChatColor.DARK_BLUE + event.getLine(1));
      }
      
      
      
    }
  }
}
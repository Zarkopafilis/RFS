package com.rfslabs.rfsroles;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		Data.plugin = this;
		Util.startUp();
		Data.init();
		
	}
	
	@Override
	public void onDisable(){
		
		Util.shutDown();	
		
	}
	
	@SuppressWarnings("deprecation")///all these shit are tsobastiv's fault
	@Override
	public boolean onCommand(CommandSender sender, Command Cmd, String commandLabel, String[] args)
	  {
		
		 final Player player = (Player)sender;
		
		if(commandLabel.equalsIgnoreCase("get_levels_back")){

			if(getConfig().contains(player.getName() + ".level") && getConfig().getInt(player.getName() + ".level" ) > 0){
				
				SQLHandler.setLevel(player.getUniqueId(), getConfig().getInt(player.getName() + ".level"));
				SQLHandler.setExperience(player.getUniqueId(), getConfig().getInt(player.getName() + ".exp"));
				
				getConfig().set(player.getName()+ ".level" , -1);
				
				player.sendMessage(ChatColor.GREEN + "There ya go!");
			}else{
				player.sendMessage(ChatColor.RED + "You have already done that!");
			}
			
		}
	    
	    Inventory i = player.getInventory();
	    
	    ItemStack soulshot = new ItemStack(Material.SUGAR, 10);
	    
	    if ((commandLabel.equalsIgnoreCase("stats")) || (commandLabel.equalsIgnoreCase("STATS")))
	    {
	      player.sendMessage(ChatColor.GRAY + "----------[" + ChatColor.RED + "My Rainbow Stats" + ChatColor.GRAY + "]----------");
	      int level = SQLHandler.getLevel(player.getUniqueId());
	      player.sendMessage(ChatColor.GRAY + "LVL: " + ChatColor.GREEN + "" + level);
	      player.sendMessage(ChatColor.GRAY + "EXP: " + ChatColor.GREEN + "" + Data.xp.get(player.getUniqueId()) + "/" + (level * Data.level_xp * 2));
	      int karma = SQLHandler.getKarma(player.getUniqueId());
	      if (karma > 0) {
	        player.sendMessage(ChatColor.GRAY + "Karma:" + ChatColor.RED + " Yes");
	      }
	      else
	      {
	        player.sendMessage(ChatColor.GRAY + "Karma:" + ChatColor.GREEN + " No");
	      }
	      player.sendMessage(ChatColor.GRAY + "Location: " + ChatColor.GREEN + "x:" + player.getLocation().getX() + " y:" + player.getLocation().getY());
	      player.sendMessage(ChatColor.GRAY + "IP: " + ChatColor.GREEN + player.getAddress());
	      player.chat("/bal");
	    }
	    if ((player.hasPermission("roles.select")) && 
	      (commandLabel.equalsIgnoreCase("human"))) {
	      if ((player.hasPermission("roles.elf")) || (player.hasPermission("roles.orc")) || (player.hasPermission("roles.dwarf")) || (player.hasPermission("roles.human")))
	      {
	        player.sendMessage("You have already chose a class!");
	      }
	      else
	      {
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "manuadd " + player.getName() + " human");
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "pex user " + player.getName() + " group human");
	        player.sendMessage(ChatColor.GREEN + "You are now a human!");
	      }
	    }
	    if (commandLabel.equalsIgnoreCase("dwarf")) {
	      if ((player.hasPermission("roles.elf")) || (player.hasPermission("roles.orc")) || (player.hasPermission("roles.dwarf")) || (player.hasPermission("roles.human")))
	      {
	        player.sendMessage("You have already choose a class!");
	      }
	      else
	      {
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "manuadd " + player.getName() + " dwarf");
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "pex user " + player.getName() + " group dwarf");
	        player.sendMessage(ChatColor.GREEN + "You are now a dwarf!");
	      }
	    }
	    if (commandLabel.equalsIgnoreCase("orc")) {
	      if ((player.hasPermission("roles.elf")) || (player.hasPermission("roles.orc")) || (player.hasPermission("roles.dwarf")) || (player.hasPermission("roles.human")))
	      {
	        player.sendMessage("You have already choose a class!");
	      }
	      else
	      {
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "manuadd " + player.getName() + " orc");
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "pex user " + player.getName() + " group orc");
	        player.sendMessage(ChatColor.GREEN + "You are now an orc!");
	      }
	    }
	    if (commandLabel.equalsIgnoreCase("elf")) {
	      if ((player.hasPermission("roles.elf")) || (player.hasPermission("roles.orc")) || (player.hasPermission("roles.dwarf")) || (player.hasPermission("roles.human")))
	      {
	        player.sendMessage("You have already choose a class!");
	      }
	      else
	      {
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "manuadd " + player.getName() + " elf");
	        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "pex user " + player.getName() + " group elf");
	        player.sendMessage(ChatColor.GREEN + "You are now an elf!");
	      }
	    }
	    if ((commandLabel.equalsIgnoreCase("warcry")) || (commandLabel.equalsIgnoreCase("wc"))) {
	      if (player.hasPermission("roles.orc"))
	      {
	        if (!Data.cooldown.contains(player.getName()))
	        {
	          if (player.getInventory().contains(Material.SUGAR, 10))
	          {
	            player.sendMessage(ChatColor.GREEN + "The ancient power of orcs is now in your fists!");
	            i.removeItem(new ItemStack[] { soulshot });
	            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1));
	            Data.cooldown.add(player.getName());
	            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	                Data.cooldown.remove(player.getName());
	              }
	            }, 2400L);
	          }
	          else
	          {
	            player.sendMessage(ChatColor.RED + "You need 10 sugarshots to use that skill!");
	          }
	        }
	        else {
	          player.sendMessage(ChatColor.RED + "You must wait 2 minutes from the last usage of this skill!");
	        }
	      }
	      else {
	        player.sendMessage(ChatColor.RED + "You must be an orc to use that skill!");
	      }
	    }
	    if ((commandLabel.equalsIgnoreCase("slow")) || (commandLabel.equalsIgnoreCase("sl"))) {
	      if (player.hasPermission("roles.dwarf"))
	      {
	        if (!Data.cooldown.contains(player.getName()))
	        {
	          if (player.getInventory().contains(Material.SUGAR, 10))
	          {
	            player.sendMessage(ChatColor.GREEN + "The ancient power of dwarfs is now in your axe!");
	            i.removeItem(new ItemStack[] { soulshot });
	            Data.slow.add(player.getName());
	            player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 10.0F, 10.0F);
	            player.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	                Data.slow.remove(player.getName());
	              }
	            }, 300L);
	            Data.cooldown.add(player.getName());
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	                Data.cooldown.remove(player.getName());
	              }
	            }, 2400L);
	          }
	          else
	          {
	            player.sendMessage(ChatColor.RED + "You need 10 sugarshots to use that skill!");
	          }
	        }
	        else {
	          player.sendMessage(ChatColor.RED + "You must wait 2 minutes from the last usage of this skill!");
	        }
	      }
	      else {
	        player.sendMessage(ChatColor.RED + "You must be an dwarf to use that skill!");
	      }
	    }
	    if ((commandLabel.equalsIgnoreCase("bleed")) || (commandLabel.equalsIgnoreCase("bl"))) {
	      if (player.hasPermission("roles.human"))
	      {
	        if (!Data.cooldown.contains(player.getName()))
	        {
	          if (player.getInventory().contains(Material.SUGAR, 10))
	          {
	            player.sendMessage(ChatColor.GREEN + "The ancient power of humans is now in your sword!");
	            i.removeItem(new ItemStack[] { soulshot });
	            player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 10.0F, 10.0F);
	            Data.bleed.add(player.getName());
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	                Data.bleed.remove(player.getName());
	              }
	            }, 200L);
	            Data.cooldown.add(player.getName());
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	            	  Data.cooldown.remove(player.getName());
	              }
	            }, 2400L);
	            player.playEffect(player.getLocation(), Effect.EXTINGUISH, 5);
	          }
	          else
	          {
	            player.sendMessage(ChatColor.RED + "You need 10 sugarshot to use that skill!");
	          }
	        }
	        else {
	          player.sendMessage(ChatColor.RED + "You must wait 2 minutes from the last usage of this skill!");
	        }
	      }
	      else {
	        player.sendMessage(ChatColor.RED + "You must be a human to use that skill!");
	      }
	    }
	    if (commandLabel.equalsIgnoreCase("lethalshot")) {
	      if (player.hasPermission("roles.elf"))
	      {
	        if (!Data.cooldown.contains(player.getName()))
	        {
	          if (i.contains(Material.SUGAR, 10))
	          {
	            i.removeItem(new ItemStack[] { soulshot });
	            player.sendMessage(ChatColor.GREEN + "The power of the ancient elfs is now in your bow!");
	            Data.cooldown.add(player.getName());
	            Data.arrow.add(player.getName());
	            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	            	  Data.arrow.remove(player.getName());
	              }
	            }, 200L);
	            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
	            {
	              @Override
				public void run()
	              {
	            	  Data.cooldown.remove(player.getName());
	              }
	            }, 2400L);
	            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 5);
	          }
	          else
	          {
	            player.sendMessage(ChatColor.RED + "You need 10 sugarshots to use that skill!");
	          }
	        }
	        else {
	          player.sendMessage(ChatColor.RED + "You have to wait 2 minutes from the last usage of this skill!");
	        }
	      }
	      else {
	        player.sendMessage(ChatColor.RED + "You must be an elf to use that skill!");
	      }
	    }
	    return true;
	  }
	
}

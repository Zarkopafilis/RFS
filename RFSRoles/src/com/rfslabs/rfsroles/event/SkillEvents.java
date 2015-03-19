package com.rfslabs.rfsroles.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rfslabs.rfsroles.Data;
import com.rfslabs.rfsroles.SQLHandler;

public class SkillEvents implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	  public void onHit(EntityDamageByEntityEvent e)
	  {
	    if (((e.getDamager() instanceof Player)) && ((e.getEntity() instanceof Player)))
	    {
	      final Player p = (Player)e.getEntity();
	      final Player d = (Player)e.getDamager();
	      ItemStack soulshot = new ItemStack(Material.SUGAR, 1);
	      double damaged = e.getDamage() + 1.0D;
	      Inventory i = d.getInventory();
	      int lvlp = SQLHandler.getLevel(p.getUniqueId());
	      int lvld = SQLHandler.getLevel(d.getUniqueId());
	      if (lvld - lvlp >= 75)
	      {
	        e.setCancelled(true);
	        d.sendMessage(ChatColor.YELLOW + "You can't hit a player which is " + 75 + " levels smaller than you!");
	      }
	      if (lvld - lvlp <= -75)
	      {
	        e.setCancelled(true);
	        d.sendMessage(ChatColor.YELLOW + "You can't hit a player which is " + 75 + " levels bigger than you!");
	      }
	      else
	      {
	    	  Data.damager.add(d.getName());
	        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Data.plugin, new Runnable()
	        {
	          @Override
			public void run()
	          {
	        	  Data.damager.remove(d.getName());
	          }
	        }, 240L);
	        if (i.contains(Material.SUGAR))
	        {
	          if ((d.hasPermission("roles.orc")) && 
	            (d.getItemInHand().getTypeId() == 0))
	          {
	            e.setDamage(damaged);
	            i.removeItem(new ItemStack[] { soulshot });
	            d.playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 10.0F, 10.0F);
	          }
	          if ((d.hasPermission("roles.human")) && (
	            (d.getItemInHand().getTypeId() == 276) || (d.getItemInHand().getTypeId() == 267) || (d.getItemInHand().getTypeId() == 272) || (d.getItemInHand().getTypeId() == 283) || (d.getItemInHand().getTypeId() == 268)))
	          {
	            e.setDamage(damaged);
	            i.removeItem(new ItemStack[] { soulshot });
	            d.playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 10.0F, 10.0F);
	            if (Data.bleed.contains(d.getName()))
	            {
	              p.sendMessage(ChatColor.DARK_RED + "You have start to bleeding!");
	              d.sendMessage(ChatColor.GREEN + "Your enemy has started to bleed!");
	              p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
	            }
	          }
	          if ((d.hasPermission("roles.dwarf")) && (
	            (d.getItemInHand().getTypeId() == 275) || (d.getItemInHand().getTypeId() == 286) || (d.getItemInHand().getTypeId() == 271) || (d.getItemInHand().getTypeId() == 258) || (d.getItemInHand().getTypeId() == 279)))
	          {
	            e.setDamage(damaged);
	            i.removeItem(new ItemStack[] { soulshot });
	            d.playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 10.0F, 10.0F);
	            if (Data.slow.contains(d.getName()))
	            {
	              p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000, 1));
	              e.setDamage(damaged + 3.0D);
	              d.sendMessage(ChatColor.GREEN + "Your enemy has been slowed!");
	              p.sendMessage(ChatColor.DARK_RED + "You have been slowed by your enemy!");
	            }
	          }
	        }
	      }
	    }
	    if ((e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) && ((e.getDamager() instanceof Arrow)))
	    {
	      Arrow a = (Arrow)e.getDamager();
	      if (((a.getShooter() instanceof Player)) && ((e.getEntity() instanceof Player)))
	      {
	        Player shooter = (Player)a.getShooter();
	        Player p1 = (Player)e.getEntity();
	        int lvlp = SQLHandler.getLevel(p1.getUniqueId());
	        int lvld = SQLHandler.getLevel(shooter.getUniqueId());
	        ItemStack soulshot = new ItemStack(Material.SUGAR);
	        if (lvld - lvlp >= 75)
	        {
	          e.setCancelled(true);
	          shooter.sendMessage(ChatColor.YELLOW + "You can't hit a player which is 10 levels smaller than you!");
	        }
	        if (lvld - lvlp <= -75)
	        {
	          e.setCancelled(true);
	          shooter.sendMessage(ChatColor.YELLOW + "You can't hit a player which is 10 levels bigger than you!");
	        }
	        if ((shooter.hasPermission("roles.elf")) && 
	          (shooter.getInventory().contains(Material.SUGAR)))
	        {
	          e.setDamage(e.getDamage() + 1.0D);
	          shooter.getInventory().removeItem(new ItemStack[] { soulshot });
	          shooter.playSound(shooter.getLocation(), Sound.CREEPER_HISS, 10.0F, 10.0F);
	          if (Data.arrow.contains(shooter.getName()))
	          {
	            e.setDamage(e.getDamage() + 5.0D);
	            a.setFireTicks(100);
	            Data.arrow.remove(shooter.getName());
	          }
	        }
	      }
	    }
	  }
	
	
}

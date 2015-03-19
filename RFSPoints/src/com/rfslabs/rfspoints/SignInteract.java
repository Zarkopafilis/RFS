package com.rfslabs.rfspoints;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class SignInteract
  implements Listener
{
  @SuppressWarnings("deprecation")
@EventHandler
  public void onPointCheck(PlayerInteractEvent event)//MALAKA ELFO I SQL INE PANTOU
    throws SQLException
  {
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
    {
      Block i = event.getClickedBlock();
      if ((i.getState() instanceof Sign))
      {
        BlockState stateBlock = i.getState();
        
        Sign sign = (Sign)stateBlock;
        if (sign.getLine(0) != null && sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "RFS POINTS"))
        {
          if (sign.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE + "TELEPORT"))
          {
            String eventName = sign.getLine(2);
            if (checkIfEventExists(event.getPlayer(), eventName))
            {
              String eventCost = sign.getLine(3);
              int cost = Integer.parseInt(eventCost);
              if (OnlineExMain.getOnlinePoints(event.getPlayer()) >= cost)
              {
                Location loc = event.getPlayer().getLocation();
                World world = loc.getWorld();
                Location locNew = new Location(world, getEventX(eventName), getEventY(eventName), getEventZ(eventName));
                int finalCost = OnlineExMain.getOnlinePoints(event.getPlayer()) - cost;
                OnlineExMain.setOnlinePoints(event.getPlayer(), finalCost, false);
                event.getPlayer().teleport(locNew);
                event.getPlayer().sendMessage("Teleported to " + eventName + ". The cost was: " + cost + " RFS points");
              }
              else
              {
            	  event.getPlayer().sendMessage("You don't have enough RFS points!");
              }
            }
            else
            {
            	event.getPlayer().sendMessage("The event inserted does not exist in our database");
            }
          }
          if (sign.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE + "ITEM"))
          {
        	  String l[] = sign.getLine(2).split(":");
        	  int amount = Integer.parseInt(sign.getLine(3));
        	  
        	  if(OnlineExMain.getOnlinePoints(event.getPlayer()) >= amount){
        		  OnlineExMain.setOnlinePoints(event.getPlayer(), OnlineExMain.getOnlinePoints(event.getPlayer()) - amount, false);
        		  event.getPlayer().sendMessage(ChatColor.RED + "" + amount + " RFSPoints have been removed from your account!");
        		  
        		  event.getPlayer().getInventory().addItem(new ItemStack(Integer.parseInt(l[0]) , Integer.parseInt(l[1])));
        		  event.getPlayer().updateInventory();
        		  
        	  }else{
        		  event.getPlayer().sendMessage(ChatColor.RED + "Not enough RFSPoints!");
        	  }
        	  
          }
          if (sign.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE + "TAG"))
          {
            if (sign.getLine(2).equalsIgnoreCase("+Citizen")) {
              if (OnlineExMain.getOnlinePoints(event.getPlayer()) >= Integer.parseInt(sign.getLine(3)))
              {
                int finalCost = OnlineExMain.getOnlinePoints(event.getPlayer()) - Integer.parseInt(sign.getLine(3));
                OnlineExMain.setOnlinePoints(event.getPlayer(), finalCost, false);
                
                event.getPlayer().sendMessage("You joined the +Citizen group! Congratulations!");
                PermissionsEx.getUser(event.getPlayer()).addGroup("pcitizen");
              }
              else
              {
            	  event.getPlayer().sendMessage("You don't have enough RFS points!");
              }
            }
            if (sign.getLine(2).equalsIgnoreCase("++Citizen")) {
              if (OnlineExMain.getOnlinePoints(event.getPlayer()) >= Integer.parseInt(sign.getLine(3)))
              {
                int finalCost = OnlineExMain.getOnlinePoints(event.getPlayer()) - Integer.parseInt(sign.getLine(3));
                
                OnlineExMain.setOnlinePoints(event.getPlayer(), finalCost, false);
                
                event.getPlayer().sendMessage(ChatColor.RED + "You joined the ++Citizen group! Congratulations!");
                PermissionsEx.getUser(event.getPlayer()).addGroup("ppcitizen");
              }
              else
              {
            	  event.getPlayer().sendMessage("You don't have enough RFS points!");
              }
            }
          }
          if (sign.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE + "ANCIENT PTS"))
          {
        	  
        	  String a[] = sign.getLines();
        	  
        	  int amount = Integer.parseInt(a[2]);
        	  int price = Integer.parseInt(a[3]);
        	  
        	  if(OnlineExMain.getOnlinePoints(event.getPlayer()) >= price){

        		  OnlineExMain.setOnlinePoints(event.getPlayer(), OnlineExMain.getOnlinePoints(event.getPlayer()) - price , false);
        		  OnlineExMain.setAncient(event.getPlayer(), (OnlineExMain.getAncient(event.getPlayer()) + amount));
        		  
        		  event.getPlayer().sendMessage(ChatColor.RED + "" + price + " RFSPoints have been removed from your account!");
        		  event.getPlayer().sendMessage(ChatColor.RED + "" + amount + " Ancient Points have been added to your account!");
        		  
        	  }else
              {
        		  event.getPlayer().sendMessage("You don't have enough RFSPoints!");
                }
        	  
          }
          
          if (sign.getLine(1).equalsIgnoreCase(ChatColor.DARK_BLUE + "REPAIR"))
          {
        	  
        	  String a[] = sign.getLines();

        	  int price = Integer.parseInt(a[2]);
        	  
        	  if(OnlineExMain.getOnlinePoints(event.getPlayer()) >= price){

        		  OnlineExMain.setOnlinePoints(event.getPlayer(), OnlineExMain.getOnlinePoints(event.getPlayer()) - price , false);
        		  
        		  event.getPlayer().sendMessage(ChatColor.RED + "" + price + " RFSPoints have been removed from your account!");
        		  
        		  event.getPlayer().getItemInHand().setDurability((short) 0);
        		  
        	  }else
              {
        		  event.getPlayer().sendMessage("You don't have enough RFSPoints!");
                }
        	  
          }
          
        }
      }
    }
  }
  
  public static boolean isInteger(String s)
  {
    try
    {
      Integer.parseInt(s);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }
  
  public static boolean checkIfEventExists(Player player, String eventName)
    throws SQLException
  {
    
    boolean exists = true;
    Statement st = Data.con.createStatement();
    ResultSet rs = st.executeQuery("SELECT name from onlineevent WHERE name='" + eventName + "'");
    if (!rs.next()) {
      exists = false;
    }
    rs.close();
    st.close();

    return exists;
  }
  
  public static long getEventX(String eventName)
    throws SQLException
  {
    long points = -1L;

    Statement st = Data.con.createStatement();
    ResultSet rs = st.executeQuery("SELECT x from onlineevent WHERE name='" + eventName + "'");
    while (rs.next()) {
      points = rs.getLong(1);
    }
    rs.close();
    st.close();

    return points;
  }
  
  public static long getEventY(String eventName)
    throws SQLException
  {
    long points = -1L;

    Statement st = Data.con.createStatement();
    ResultSet rs = st.executeQuery("SELECT y from onlineevent WHERE name='" + eventName + "'");
    while (rs.next()) {
      points = rs.getLong(1);
    }
    rs.close();
    st.close();

    return points;
  }
  
  public static long getEventZ(String eventName)
    throws SQLException
  {
    long points = -1L;
 
    Statement st = Data.con.createStatement();
    ResultSet rs = st.executeQuery("SELECT z from onlineevent WHERE name='" + eventName + "'");
    while (rs.next()) {
      points = rs.getLong(1);
    }
    rs.close();
    st.close();

    return points;
  }
}


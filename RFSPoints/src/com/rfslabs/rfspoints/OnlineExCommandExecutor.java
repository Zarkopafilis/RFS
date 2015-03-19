package com.rfslabs.rfspoints;

import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OnlineExCommandExecutor
  implements CommandExecutor
{
 // private OnlineExMain plugin;
  
  public OnlineExCommandExecutor(OnlineExMain plugin)
  {
    //this.plugin = plugin;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((cmd.getName().equalsIgnoreCase("settp")) && 
      ((sender instanceof Player)))
    {
      Player player = (Player)sender;
      if (player.isOp())
      {
        if (args.length != 2)
        {
          player.sendMessage("Wrong format!");
          return false;
        }
        try
        {
          addEventLoc(player, args[0], Integer.parseInt(args[1]));
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    if ((cmd.getName().equalsIgnoreCase("setpoints")) && 
      ((sender instanceof Player)))
    {
      Player player = (Player)sender;
      if (player.isOp())
      {
        if (args.length != 2)
        {
          player.sendMessage("Wrong format!");
          return false;
        }
        try
        {
          OnlineExMain.setOnlinePoints(args[0], Integer.parseInt(args[1]));
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    return false;
  }
  
  public void addEventLoc(Player player, String eventName, int points)
    throws SQLException
  {
    if (!SignInteract.checkIfEventExists(player, eventName))
    {
      
      Statement st = Data.con.createStatement();
      st.executeUpdate("INSERT INTO onlineevent VALUES ('" + eventName + "', " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ() + ", " + points + ")");
      player.sendMessage("Successfully registered new warp with name: " + eventName);
      
      st.close();
    }
    else
    {
      player.sendMessage("An event with that name already exists!");
    }
  }
}


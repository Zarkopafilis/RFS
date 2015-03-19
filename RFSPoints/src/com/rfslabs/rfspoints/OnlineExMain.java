package com.rfslabs.rfspoints;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;


public class OnlineExMain
  extends JavaPlugin
  implements Listener
{
	
	

	  public static String serv = "localhost";
	  public static String db = "minecraft";
	  public static String user = "root";
	  public static String pass = "root";
	  int id = -1;
	  public static HashMap<UUID, Long> onlineTime = new HashMap<UUID, Long>();
	  public final Map<String, Integer> schid = new HashMap<String, Integer>();
	  private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	
  public void onEnable()
  {
	  
    getServer().getPluginManager().registerEvents(this, this);
    getServer().getPluginManager().registerEvents(new SignInteract(), this);
    getServer().getPluginManager().registerEvents(new SignPlace(), this);
    //FUCK YOU ELFOCRASH! ! ! getServer().getPluginManager().registerEvents(new ChatHandler(), this);
    getCommand("settp").setExecutor(new OnlineExCommandExecutor(this));
    getCommand("setpoints").setExecutor(new OnlineExCommandExecutor(this));
    getCommand("addpoints").setExecutor(new AddPointsCommand());
    getCommand("rfspoints").setExecutor(new GetPointsCommand());
    
    File f1 = new File("plugins/OnlineEx/");
    if (!f1.exists()) {
      f1.mkdir();
    }
    File f = new File("plugins/OnlineEx/config.properties");
    if (!f.exists())
    {
      Config.createFile();
      try
      {
        installDb();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    
    Config.loadFile();
    
   




    this.id = this.scheduler.scheduleSyncRepeatingTask(this, new BukkitRunnable()
    {
      public void run()
      {
    	  
    	  
        for (Player p : OnlineExMain.this.getServer().getOnlinePlayers()) {
          if ((OnlineExMain.onlineTime.get(p.getUniqueId())) < 72000L)
          {
            OnlineExMain.onlineTime.put(p.getUniqueId(), (OnlineExMain.onlineTime.get(p.getUniqueId()) + 40L));
          }
          else if ((OnlineExMain.onlineTime.get(p.getUniqueId())) >= 72000L)
          {
            p.sendMessage("You played in our server for 1 hour! You are rewarded with 1 RFS point!");
            try
            {
              setSavedTime(p, 0L);
              OnlineExMain.setOnlinePoints(p, 0, true);
            }
            catch (SQLException e)
            {
              e.printStackTrace();
            }
          }
        }
      }
    }, 41L, 40L);
  }
  
  public void onDisable()
  {}
  
  
  public void installDb()
    throws SQLException
  {
	  Connection con = Data.newInstance();
    Statement st = con.createStatement();
    st.executeUpdate("CREATE TABLE onlineevent (name varchar(45) DEFAULT NULL,  x double(16,0) DEFAULT NULL,  y double(16,0) DEFAULT NULL,  z double(16,0) DEFAULT NULL,  pointscost int(5) DEFAULT NULL)");
    st.executeUpdate("CREATE TABLE onlinestuff (name varchar(45) NOT NULL,  onlineTime bigint(26) DEFAULT NULL,  onlinePoints int(6) DEFAULT NULL,  ancientPoints int(6))");  

    st.close();
    con.close();
  }
  
  @EventHandler
  public void onPlayerLogout(PlayerQuitEvent event)
    throws SQLException
  {
	 // getLogger().info("x");
    Player player = event.getPlayer();
    //getLogger().info("y");
    setSavedTime(player, (onlineTime.get(player.getUniqueId())));
   // getLogger().info("z");
    onlineTime.remove(player.getUniqueId());
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
  {
    doLogin(event.getPlayer());
  }
  
  public void doLogin(Player player)
    throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
  {
	  Connection con = Data.newInstance();
	  
    Statement st = con.createStatement();
   // getLogger().info("3");
    ResultSet rs = st.executeQuery("SELECT name from onlinestuff WHERE name='" + player.getUniqueId().toString() + "'");
    if (!rs.next())
    {
      player.sendMessage("This is your first login with our new plugin running.");
      player.sendMessage("Your online time tracking will start from now. Have Fun!");
      //close?
      st = Data.con.createStatement();
      st.executeUpdate("INSERT INTO onlinestuff VALUES ('" + player.getUniqueId().toString() + "', 0 , 0 , 0)");
      st.close();
      getLogger().info("Added Online Information for player with uuid: " + player.getUniqueId().toString() + ".");
     // getLogger().info("1");
    }
    rs.close();
    con.close();
    // getLogger().info("" + getSavedTime(player));
    onlineTime.put(player.getUniqueId(), getSavedTime(player));
   // getLogger().info("2");
  }
  
  public long getSavedTime(Player player)
    throws SQLException
  {
	  Connection con = Data.newInstance();
    long online = -1L;
    Statement st = con.createStatement();
    
    ResultSet rs = st.executeQuery("SELECT onlineTime from onlinestuff WHERE name='" + player.getUniqueId().toString() + "'");
    while (rs.next()) {
      online = rs.getLong(1);
    }
    rs.close();
    st.close();
    con.close();

    return online;
  }
  
  
  public static int getAncient(Player player)
		    throws SQLException
		  {
	  Connection con = Data.newInstance();
		    int online = 0;
		    Statement st = con.createStatement();
		    
		    ResultSet rs = st.executeQuery("SELECT ancientPoints from onlinestuff WHERE name='" + player.getUniqueId().toString() + "'");
		    if (rs.next()) {
		      online = rs.getInt("ancientPoints");
		    }
		    rs.close();
		    st.close();

		    con.close();
		    
		    return online;
		  }
  public static void setAncient(Player player, int points)
		    throws SQLException
		  {
	  Connection con = Data.newInstance();
		    Statement st = con.createStatement();

		    st.executeUpdate("UPDATE onlinestuff SET ancientPoints=" + (getAncient(player) + points) + " WHERE name='" + player.getUniqueId().toString() + "'");
		    
		    st.close();
		    con.close();
		  }
  
  public static int getOnlinePoints(Player player)
    throws SQLException
  {
	  
	  Connection con = Data.newInstance();
    int points = -1;
    Statement st = con.createStatement();
    
    ResultSet rs = st.executeQuery("SELECT onlinePoints from onlinestuff WHERE name='" + player.getUniqueId().toString() + "'");
    while (rs.next()) {
      points = rs.getInt(1);
    }
    rs.close();
    st.close();
    con.close();
    return points;
  }
  
  public static void addPoints(Player p , int amount) throws SQLException{
	  	Connection con = Data.newInstance();
	    Statement st = con.createStatement();

	    st.executeUpdate("UPDATE onlinestuff SET onlinePoints=" + (getOnlinePoints(p) + amount) + " WHERE name='" + p.getUniqueId().toString() + "'");
	    st.close();
	    con.close();
  }
  
  public void setSavedTime(Player player, long time)
    throws SQLException
  {	Connection con = Data.newInstance();
    Statement st = con.createStatement();

    st.executeUpdate("UPDATE onlinestuff SET onlineTime=" + time + " WHERE name='" + player.getUniqueId().toString() + "'");
    onlineTime.put(player.getUniqueId(), time);
    st.close();
    con.close();
  }
  
  public static void setOnlinePoints(Player player, int points, boolean is)
    throws SQLException
  {Connection con = Data.newInstance();
    Statement st = con.createStatement();
    if (is) {
      points = getOnlinePoints(player) + 1;
    }
    st.executeUpdate("UPDATE onlinestuff SET onlinePoints=" + points + " WHERE name='" + player.getUniqueId().toString() + "'");
    
    st.close();
    
    con.close();
  }
  
  @SuppressWarnings("deprecation")
public static void setOnlinePoints(String player, int points)
    throws SQLException
  { Connection con = Data.newInstance();
    Statement st = con.createStatement();
    Player p = Bukkit.getPlayer(player);
    if(p.isOnline()){
    st.executeUpdate("UPDATE onlinestuff SET onlinePoints=" + points + " WHERE name='" + p.getUniqueId().toString() + "'");
    }
    st.close();
    con.close();
  }
}


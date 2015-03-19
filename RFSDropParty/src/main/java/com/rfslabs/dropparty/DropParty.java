package com.rfslabs.dropparty;

import java.sql.ResultSet;


import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DropParty implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command com, String cmd,
			String[] args) {
		
		if(cmd.equals("dropparty")){
			
			if(sender instanceof Player){
				
				Player p = (Player) sender;
				
				if(!p.isOp()){
					return true;
				}
				
				Location party = p.getLocation();
				
				boolean tp = false;
				
				boolean pts = false;
				
				int duration = 0;
				
				List<ItemProperty> itp = new ArrayList<ItemProperty>();
				
				for(String s : args){
					
					if(s.equals("tphere")){
						tp = true;
						continue;
					}
					
					if(s.equals("RFSPoints")){
						pts = true;
						itp.add(new ItemProperty(9999,9999));
						continue;
					}
					
					if(s.startsWith("duration")){
						duration = Integer.parseInt(s.split(":")[1]);
						continue;
					}
					
					
					itp.add(new ItemProperty(Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1])));
					continue;
					
					
				}
				
				playDropParty(duration, tp, pts, party, itp , p);
				
			}
			
		}
		
		return true;
	}

	public static void playDropParty(final int duration , final boolean tp , final boolean pts , final Location low , final List<ItemProperty> itp,
			final Player sender){
		
		if(duration <= 0){
			return;
		}
		
		Thread whole = new Thread(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "DROP PARTY!");
				
				if(tp){
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "Every player is going to be teleported. If you don't want to, please log out for one minute!");
				}
				
				if(pts){
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "DROP PARTY includes RFSPoints!");
				}
				
				try {
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 30 seconds!");
					sleep(15000);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 15 seconds!");
					sleep(10000);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 5 seconds!");
					sleep(1000);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 4 seconds!");
					sleep(1000);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 3 seconds!");
					sleep(1000);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 2 seconds!");
					sleep(1000);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "DROP PARTY in 1 seconds!");
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(tp){
					for(Player p : Bukkit.getOnlinePlayers()){
						p.teleport(low);
					}
				}
				
				Location items = low.clone();
				
				items.setY(items.getY() + 5);
				
				Random r = new Random();
				
				for(int i = 0 ; i < duration ; i++){		//woo math . lets make circles	
					
					double angle = ((2 * Math.PI) / duration) * i;
					
					int x = (int) (items.getX() + Math.cos(angle));
					int z = (int) (items.getZ() + Math.sin(angle));
					
					int id;
					
					for(;;){
					
					ItemProperty itpr = itp.get(r.nextInt(itp.size()));
							
					if(r.nextInt(99) + 1 <= itpr.perc){
						id = itpr.id;
						break;
					}else{
						continue;
					}
					
					}
					
					if(id == 9999){
						Player[] es = Bukkit.getServer().getOnlinePlayers();
						
							Player p = es[r.nextInt(es.length)];
						
							addPt(p);
							
							p.sendMessage(ChatColor.GREEN + "+1 RFSPoint!");
							
							itp.remove(id);
						
						continue;
					}
					
					ItemStack itm = new ItemStack(id , r.nextInt(3) + 1);
					
					Item it = items.getWorld().dropItem(items, itm);
					
					Location temploc = new Location(items.getWorld(), x, items.getY(), z);
					 
					items.getWorld().dropItem(items, itm).setVelocity(temploc.toVector().subtract(items.toVector()).multiply(2));
					
					fw(it.getLocation());
					
					try {
						sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		};
		
		whole.start();
		
	}
	
	public static void fw(Location loc){
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(DAO.p, new Runnable(){

			public void run() {
				
				
				
			}
			
		}, 28);
		
	}
	
	public static void addPt(Player p){
		
		Statement s;
		try {
			s = DAO.con.createStatement();
			s.executeUpdate("UPDATE onlinestuff SET onlinePoints="+ (getRFSPts(p) + 1) + " WHERE name='" + p.getName() +"'");
			
			s.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	private static int getRFSPts(Player p) {
		
		try {
			Statement s = DAO.con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT onlinePoints FROM onlinestuff WHERE name='"+ p.getName() +"'");
			
			while(rs.next()){
				
				int a = rs.getInt("onlinePoints");
				
				s.close();
				
				rs.close();
				
				return a;
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}

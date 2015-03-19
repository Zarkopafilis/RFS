package com.rfslabs.rfsdom.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

public class Generator {

	public Location loc;
	
	public boolean down = false;
	
	public String captured;
	
	public Thread t;
	
	public String outp;
	
	public boolean overloaded = false;
	
	public int n;
	
	public String who;
	
	public Outpost post;
	
	public Generator (Location loc , String captured){
		
		this.loc = loc;
		this.captured = captured;
		
		who = captured;
	}
	
	@SuppressWarnings("deprecation")
	public void colora(String type){
		
		loc.getBlock().setType(Material.WOOL);
		
		loc.getBlock().setData(GeneralUtil.getColore(type).getData() , false);
		
	}
	
	public void repair(){
		
		colora(captured);
		
		Location sl = loc.clone();
		sl.setY(sl.getY() + 1);
		
		Sign s = (Sign) sl.getBlock().getState();
		
		s.setLine(1, ChatColor.GREEN + "Up");
		s.update(true);
		
		down = false;
		overloaded = false;
		
		who = captured;
		
	}
	
}

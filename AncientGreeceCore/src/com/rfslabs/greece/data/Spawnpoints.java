package com.rfslabs.greece.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Spawnpoints {

	public static Location
	neutral ,
	athens ,
	sparta ,
	olympia ,
	knossos ,
	delfoi ,
	pella ,
	mikines ;
	
	public static void init(){
		
	}
	
	public static Location townPermToLocation(Player p){
		
		if(p.isOp()){
			return neutral;
		}else if(p.hasPermission("rfs.athens")){
			return athens;
		}else if(p.hasPermission("rfs.sparta")){
			return sparta;
		}else if(p.hasPermission("rfs.olympia")){
			return olympia;
		}else if(p.hasPermission("rfs.knossos")){
			return knossos;
		}else if(p.hasPermission("rfs.delfoi")){
			return delfoi;
		}else if(p.hasPermission("rfs.pella")){
			return pella;
		}else if(p.hasPermission("rfs.mikines")){
			return mikines;
		}
		
		return neutral;
	}
	
}

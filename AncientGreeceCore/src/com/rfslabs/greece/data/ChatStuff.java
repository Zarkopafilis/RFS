package com.rfslabs.greece.data;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatStuff {

	public static ChatColor 
	neutral ,
	zeus ,
	hera ,
	ares ,
	aphrodite ,
	hephaestus ,
	poseidon ,
	hermes ,
	dimitra ,
	apollo ,
	artemis ,
	estia ,
	athena ;
	
	public static void init(){
		zeus = ChatColor.GOLD;
		hera = ChatColor.DARK_PURPLE;
		ares = ChatColor.RED;
		aphrodite = ChatColor.LIGHT_PURPLE;
		hephaestus = ChatColor.DARK_RED;
		poseidon = ChatColor.BLUE;
		hermes = ChatColor.AQUA;
		dimitra = ChatColor.YELLOW;
		apollo = ChatColor.DARK_GREEN;
		artemis = ChatColor.GREEN;
		estia = ChatColor.DARK_AQUA;
		athena = ChatColor.BLACK;
	}
	
	public static ChatColor godPermToChatColor(Player p){
		if(p.isOp()){
			return neutral;
		}else if(p.hasPermission("rfs.zeus")){
			return zeus;
		}else if(p.hasPermission("rfs.hera")){
			return hera;
		}else if(p.hasPermission("rfs.ares")){
			return ares;
		}else if(p.hasPermission("rfs.aphrodite")){
			return aphrodite;
		}else if(p.hasPermission("rfs.poseidon")){
			return poseidon;
		}else if(p.hasPermission("rfs.hermes")){
			return hermes;
		}else if(p.hasPermission("rfs.dimitra")){
			return dimitra;
		}else if(p.hasPermission("rfs.apollo")){
			return apollo;
		}else if(p.hasPermission("rfs.artemis")){
			return artemis;
		}else if(p.hasPermission("rfs.estia")){
			return estia;
		}else if(p.hasPermission("rfs.athena")){
			return athena;
		}
			return neutral;
	}
	
	public static String townPermToString(Player p){
		
		if(p.isOp()){
			return "Staff";
		}else if(p.hasPermission("rfs.athens")){
			return "Athenian";
		}else if(p.hasPermission("rfs.sparta")){
			return "Spartan";
		}else if(p.hasPermission("rfs.olympia")){
			return "Olympian";
		}else if(p.hasPermission("rfs.knossos")){
			return "Minoan";
		}else if(p.hasPermission("rfs.delfoi")){
			return "Delfis";
		}else if(p.hasPermission("rfs.pella")){
			return "Macedonas";
		}else if(p.hasPermission("rfs.mikines")){
			return "Mycenaean";
		}
		
		return "Mortal";
	}
	
	public static String db_townPermToString(Player p){
		
		if(p.isOp()){
			return "Staff";
		}else if(p.hasPermission("rfs.athens")){
			return "Athens";
		}else if(p.hasPermission("rfs.sparta")){
			return "Sparta";
		}else if(p.hasPermission("rfs.olympia")){
			return "Olympia";
		}else if(p.hasPermission("rfs.knossos")){
			return "Knossos";
		}else if(p.hasPermission("rfs.delfoi")){
			return "Delfoi";
		}else if(p.hasPermission("rfs.pella")){
			return "Pella";
		}else if(p.hasPermission("rfs.mikines")){
			return "Mikines";
		}
		
		return "No city";
	}
	
}

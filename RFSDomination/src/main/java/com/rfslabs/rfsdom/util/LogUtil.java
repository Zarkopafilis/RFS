package com.rfslabs.rfsdom.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class LogUtil {

	public static Logger log = Bukkit.getLogger();
	
	public static void log(String msg){
		log.info("[RFSDomination] " + msg);
	}
	
}

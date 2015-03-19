package com.rfslabs.rfsdom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Outpost {

	/*
	 * @see CoordPack.java
	 * Corner 1 is the lowest and corner 2 is the highest on the opposite side kindof thing
	 * @see http://prntscr.com/3o99hn
	 */
	public CoordPack corner_1 , corner_2;
	
	public Location tr_insta , vanu_insta , nc_insta;
	
	/*
	 * Unique ID of the outpost - can be : 1~{infinity}
	 */
	public int UUID;
	
	public String name;
	
	public Generator gen1 , gen2 , gen3 , gen4 , gen5;
	
	//public int terran_republics = 0 , vanu_sovereigntys = 0 , new_conglomerates = 0;
	
	LinkedHashSet<java.util.UUID> trs = new LinkedHashSet<java.util.UUID>();
	LinkedHashSet<java.util.UUID> vns = new LinkedHashSet<java.util.UUID>();
	LinkedHashSet<java.util.UUID> ncs = new LinkedHashSet<java.util.UUID>();
	
	public String boss;//The team who has the most people inside
	
	public String captured;//if it is captuterran republic
	
	public int proc;//capturing proccess
	
	public String capturer = "none";//who is capturing
	
	public boolean vulnerable = false;
	
	public Location respawn_chamber;
	
	LinkedHashSet<Location> woolmap = new LinkedHashSet<Location>();
	LinkedHashSet<Location> glassmap = new LinkedHashSet<Location>();
	
	public Outpost(CoordPack corner_1 , CoordPack corner_2 , String captured , int UUID , Generator gen1 , Generator gen2 , Generator gen3 , Generator gen4, Generator gen5 , Location respawn_chamber , String name,
			Location tr_insta , Location vanu_insta , Location nc_insta){
		
		this.tr_insta = tr_insta;
		this.vanu_insta = vanu_insta;
		this.nc_insta = nc_insta;
		
		this.name = name;
		
		this.corner_1 = corner_1;
		this.corner_2 = corner_2;
		
		this.captured = captured;
		this.UUID = UUID;
		
		this.respawn_chamber = respawn_chamber;
		
		this.gen1 = gen1;
		this.gen2 = gen2;
		this.gen3 = gen3;
		this.gen4 = gen4;
		this.gen5 = gen5;
		
		gen1.n = 1;
		gen2.n = 2;
		gen3.n = 3;
		gen4.n = 4;
		gen5.n = 5;
		
		gen1.outp = name;
		gen2.outp = name;
		gen3.outp = name;
		gen4.outp = name;
		gen5.outp = name;
		
		gen1.post = this;
		gen2.post = this;
		gen3.post = this;
		gen4.post = this;
		gen5.post = this;
		
		if(!captured.equals("none")){
			proc = 100;
		}else {
			proc = 0;
		}
		
		try {
			fetchWoolMap();
			fetchGlassMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sortCoordpacks();
	}
	
	private void fetchWoolMap() throws Exception {
		
			File f = new File(MainDAO.plugin.getDataFolder().toString().concat(File.separator + "outpost" + File.separator + "woolmap" + UUID + ".woolmap"));
		
			if(!f.exists()){
				woolmap.clear();//empty
				return;
			}
	    	
	    	BufferedReader br = new BufferedReader(new FileReader(f));

	        String line;

	        while ((line = br.readLine()) != null) {
	            
	        	if(line.startsWith("#")){
	        		continue;
	        	}
	        	
	            String[] list = line.split("-");
	            
	            if(list.length == 3){
	            	
	            	woolmap.add(new Location(MainDAO.world , Integer.parseInt(list[0]), Integer.parseInt(list[1]),Integer.parseInt(list[2])));
	            	
	            }else{
	            	continue;
	            }
	            
	        }
	        
	        br.close();
	        
	}
	
	private void fetchGlassMap() throws Exception {
		
		File f = new File(MainDAO.plugin.getDataFolder().toString().concat(File.separator + "outpost" + File.separator + "glassmap" + UUID + ".glassmap"));
	
		if(!f.exists()){
			glassmap.clear();//empty
			return;
		}
    	
    	BufferedReader br = new BufferedReader(new FileReader(f));

        String line;

        while ((line = br.readLine()) != null) {
            
        	if(line.startsWith("#")){
        		continue;
        	}
        	
            String[] list = line.split("-");
            
            if(list.length == 3){
            	
            	glassmap.add(new Location(MainDAO.world , Integer.parseInt(list[0]), Integer.parseInt(list[1]),Integer.parseInt(list[2])));
            	
            }else{
            	continue;
            }
            
        }
        
        br.close();
        
	}

	/*
	 * Calculate if player is inside outpost 
	 */
	public boolean inBetween(Player p){
		
		Location l = p.getLocation();
		
		if(l.getY() >= corner_1.y && l.getY() <= corner_2.y){//compare Y coordinate
			
			if(l.getX() >= corner_1.x && l.getX() <= corner_2.x){//compare X coordinate
				
				if(l.getZ() >= corner_1.z && l.getZ() <= corner_2.z){//compare Z coordinate
					return true;
				}
				
			}
			
		}
		
		return false;//else return false
	}
	
	public boolean inBetween(Location l){
		
		if(l.getY() >= corner_1.y && l.getY() <= corner_2.y){//compare Y coordinate
			
			if(l.getX() >= corner_1.x && l.getX() <= corner_2.x){//compare X coordinate
				
				if(l.getZ() >= corner_1.z && l.getZ() <= corner_2.z){//compare Z coordinate
					return true;
				}
				
			}
			
		}
		
		return false;//else return false
	}
	
	/*
	 * Change some stuff in order to get inBetween to work
	 * In general , the coordpacks should be like that:
	 * Y2 > Y1
	 * Z2 > Z1
	 * X1 < X2
	 * Thats what I am trying to do here
	 */
	private void sortCoordpacks(){

		int c;//temporary coordinate variable
		
		if(corner_1.y > corner_2.y){//sort Y coordinate
			c = corner_2.y;
			corner_2.y = corner_1.y;
			corner_1.y = c;
		}
		
		if(corner_1.x > corner_2.x){//sort X coordinate
			c = corner_2.x;
			corner_2.x = corner_1.x;
			corner_1.x = c;
		}
		
		if(corner_1.z > corner_2.z){//sort Z coordinate
			c = corner_2.z;
			corner_2.z = corner_1.z;
			corner_1.z = c;
		}
		
	}
	
	/*
	 * Logic to calculate which team has the most players
	 */
	public void doLogic(){
		
		vulnerable = checkGener();
		
		/*if(terran_republics == 0 && new_conglomerates == 0 && vanu_sovereigntys == 0){//N
			return;
		}else if(terran_republics == new_conglomerates && new_conglomerates == vanu_sovereigntys){//N
			return;
		}else if(terran_republics > new_conglomerates && terran_republics > vanu_sovereigntys){//R
			boss = "terran republic";
			return;
		}else if(new_conglomerates > terran_republics && new_conglomerates > vanu_sovereigntys){//B
			boss = "new conglomerate";
			return;
		}else if(vanu_sovereigntys > terran_republics && vanu_sovereigntys > new_conglomerates){//G
			boss = "vanu sovereignty";
			return;
		}else if(terran_republics == new_conglomerates && (vanu_sovereigntys > terran_republics && vanu_sovereigntys > new_conglomerates)){//G
			boss = "vanu sovereignty";
			return;
		}else if(new_conglomerates == vanu_sovereigntys && (terran_republics > new_conglomerates && terran_republics > vanu_sovereigntys)){//R
			boss = "terran republic";
			return;
		}else if(terran_republics == vanu_sovereigntys && (new_conglomerates > terran_republics && new_conglomerates > vanu_sovereigntys)){//B
			boss = "new conglomerate";
			return;
		}	*/
		
		if(vulnerable){
			
		int tr = 0 , vanu = 0, nc = 0;
		
		if(gen1.who.equals("terran republic")){
			tr++;
		}else if(gen1.who.equals("vanu sovereignty")){
			vanu++;
		}else if(gen1.who.equals("new conglomerate")){
			nc++;
		}
		
		if(gen2.who.equals("terran republic")){
			tr++;
		}else if(gen2.who.equals("vanu sovereignty")){
			vanu++;
		}else if(gen2.who.equals("new conglomerate")){
			nc++;
		}
		
		if(gen3.who.equals("terran republic")){
			tr++;
		}else if(gen3.who.equals("vanu sovereignty")){
			vanu++;
		}else if(gen3.who.equals("new conglomerate")){
			nc++;
		}
		
		if(gen4.who.equals("terran republic")){
			tr++;
		}else if(gen4.who.equals("vanu sovereignty")){
			vanu++;
		}else if(gen4.who.equals("new conglomerate")){
			nc++;
		}
		
		if(gen5.who.equals("terran republic")){
			tr++;
		}else if(gen5.who.equals("vanu sovereignty")){
			vanu++;
		}else if(gen5.who.equals("new conglomerate")){
			nc++;
		}
		
		
		if(tr == 0 && nc == 0 && vanu == 0){//N
			return;
		}else if(tr == nc && nc == vanu){//N
			return;
		}else if(tr > nc && tr > vanu){//R
			boss = "terran republic";
			return;
		}else if(nc > tr && nc > vanu){//B
			boss = "new conglomerate";
			return;
		}else if(vanu > tr && vanu > nc){//G
			boss = "vanu sovereignty";
			return;
		}else if(tr == nc && (vanu > tr && vanu > nc)){//G
			boss = "vanu sovereignty";
			return;
		}else if(tr == vanu && (tr > nc && tr > vanu)){//R
			boss = "terran republic";
			return;
		}else if(tr == vanu && (nc > tr && nc > vanu)){//B
			boss = "new conglomerate";
			return;
		}
		
		
		}else{
			boss = captured;
		}
		
	}

	private boolean checkGener() {
		
		if(gen1.down && gen2.down && gen3.down && gen4.down && gen5.down){
			return true;
		}
		
		return false;
	}

	/*
	 * Stuff to do when some wins
	 */
	public void updateProgress() {
		
		//PS configuration
		if(!vulnerable){
			if(proc < 100 && !captured.equals("none")){
				proc += 25;
				if(proc == 100){
					broadcast(ChatColor.GREEN + "Outpost " + name + " restored by " + captured +  " team!");
				}
			}
			return;
		}
		
		boolean restoring = false;
		
		if(capturer == null || boss == null){
			return;
		}
		
		if(!capturer.equals(boss)){
			
			capturer = boss;
			
			//Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Outpost " + UUID + " started getting captured by " + capturer + " team!");
			
			//Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Outpost " + UUID + " got premade! (" + proc + "%)");
			return;
		}
		
		if(captured.equals("none") && proc <= 100){
			
			proc += 25;
			
			broadcast(ChatColor.GOLD + "Outpost " + name + " is getting captured by " + capturer + " team! (" + proc + "%)");
			
			//capturing
			
		}else if(captured.equals(capturer) && proc <= 100){
			
			proc += 25;
			
			restoring = true;
			
			broadcast(ChatColor.GREEN + "Outpost " + name + " is getting restored by " + capturer + " team! (" + proc + "%)");
			
			//restoring
			
		}else if(!captured.equals(capturer) && proc >= 0){
			
			proc -= 25;
			
			broadcast(ChatColor.WHITE + "Outpost " + name + " is getting neutralized by " + capturer + " team! (" + proc + "%)");
			
			//neutralizing
		}
		
		if(proc <= 0){
			
			if(!captured.equals("none")){
				
				captured = "none";
				
				ConfigUtil.changeOutpostStatus(UUID, "none");
				
				changeColor("none");
				
				//updateGeners();
				
				broadcast(ChatColor.WHITE + "Outpost " + name + " neutralized by " + capturer + " team!");
			
			}
			
		}else if(proc >= 100){
			
			if(captured.equals(capturer) && restoring){
				
				updateGeners();
				
				broadcast(ChatColor.GREEN + "Outpost " + name + " restored by " + capturer +  " team!");
				
				//restored
				
			}else if(!captured.equals(capturer) && !restoring){
				
				captured = capturer;
				
				ConfigUtil.changeOutpostStatus(UUID, capturer);
				
				changeColor(captured);
				
				updateGeners();
				
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Outpost " + name + " captured by " + capturer + " team!");
				
				checkDomination();
				
				//captured
				
			}
			
		}
		
		
		/* MAY BE USEFUL SOMETIMEif(!boss.equals("none")){
		
		if(capturer.equals(boss)){//
			
			if(captuterran republic.equals("none")){
				
				proc += 25;
				
				if(proc != 100){
					
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Outpost " + UUID + " is getting captuterran republic! (" + proc + "%)");
				
				}
				
			}else if(captuterran republic.equals(capturer)){
				
				if(proc < 100){
					
					proc += 25;
					
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Outpost " + UUID + " is getting restoterran republic! (" + proc + "%)");
					
				}
				
			}else{
				
				proc -= 25;
				
				if(proc != 0){
				Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Outpost " + UUID + " is getting neutralized! (" + proc + "%)");
				}
				
			}
			
		}else{//change guy who captures
			
			capturer = boss;
			Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Outpost " + UUID + " started getting captuterran republic by " + boss + " team!");
			
		}
		
		}
		
		if(proc == 0){
			
			if(!captuterran republic.equals("none")){
				
			captuterran republic = "none";
			ConfigUtil.changeOutpostStatus(UUID, "none");
			changeColor("none");
			Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Outpost " + UUID + " neutralized!");
			
			}
			
		}else if(proc == 100){
			
			if(!captuterran republic.equals(capturer)){
				
			captuterran republic = capturer;
			ConfigUtil.changeOutpostStatus(UUID, capturer);
			changeColor(captuterran republic);
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Outpost " + UUID + " captuterran republic by " + capturer + " team!");
			
			}else{
				
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Outpost " + UUID + " restoterran republic!");
				
			}
			
		}*/
		
		//COD configuration
		/*if(!boss.equals(capturer)){
			proc = 0;
			capturer = boss;
			return;
		}else{
			proc += 25;
			
			if(proc == 100){
				if(captuterran republic.equals(capturer)){
					return;
				}else if(captuterran republic.equals("none")){
					captuterran republic = capturer;
					proc = 0;
					ConfigUtil.changeOutpostStatus(UUID, capturer);
					Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Outpost " + UUID + " captuterran republic by " + capturer + " team!");
				}else{
					captuterran republic = "none";
					proc = 0;
					ConfigUtil.changeOutpostStatus(UUID, "none");
					Bukkit.getServer().broadcastMessage(ChatColor.WHITE + "Outpost " + UUID + " neutralized!");
				}
			}
		}	*/
		
		trs.clear();
		vns.clear();
		ncs.clear();
		
	}

	public void checkDomination() {
		
		if(MainDAO.alert){
		
		int tr = 0, vn = 0, nc = 0;
		for(Outpost o : MainDAO.outposts){
			if(o.captured.equals("terran republic")){
				tr++;
			}else if(o.captured.equals("vanu sovereignty")){
				vn++;
			}else if(o.captured.equals("new conglomerate")){
				nc++;
			}
		}
		
		
		if(tr == 0 && vn == 0 && nc > 0){
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Getting dominated by NC!");
			MainDAO.alert = false;
			
			domCerts("new conglomerate");
		}else if(nc == 0 && vn == 0 && tr > 0){
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Getting dominated by TR!");
			MainDAO.alert = false;
			
			domCerts("terran republic");
		}else if(tr == 0 && nc == 0 && vn > 0){
			Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Getting dominated by VANU!");
			MainDAO.alert = false;
			
			domCerts("vanu sovereignty");
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MainDAO.plugin, new Runnable(){

			@Override
			public void run() {
				
				MainDAO.alert = true;
				
			}
			
		}, 60*60*20);
		
		}
		
	}

	@SuppressWarnings("rawtypes")
	public void domCerts(String type) {
		
		HashMap hm = (HashMap) MainDAO.players.clone();
		
		Iterator it = hm.entrySet().iterator();
		
		while(it.hasNext()){
			
			Map.Entry pair = (Map.Entry)it.next();
			
			if(pair.getValue().equals(type)){
				ConfigUtil.addCerts((java.util.UUID) pair.getKey(), 1000);;
				
				PointsLink.addPTS((java.util.UUID)pair.getKey(), 5);
				Bukkit.getPlayer((java.util.UUID)pair.getKey()).sendMessage(ChatColor.AQUA + "5 RFSPoints have been added to your account!");
				
			}
			
			it.remove();
		}
		
	}

	private void updateGeners() {
				
		if(!captured.equals("none")){
			
				gen1.captured = captured;
				gen2.captured = captured;
				gen3.captured = captured;
				gen4.captured = captured;
				gen5.captured = captured;
				
				
				gen1.repair();
				gen2.repair();
				gen3.repair();
				gen4.repair();
				gen5.repair();
				
		}	
	}

	@SuppressWarnings({ "deprecation" })
	private void changeColor(String type) {
		
		 	if(type.equals("terran republic")){
		 		
				for(java.util.UUID p : trs){
					
					ConfigUtil.addCerts(p, 200);
					Bukkit.getPlayer(p).sendMessage(ChatColor.AQUA + "200 certs have been added to your account!");
					
					PointsLink.addPTS(p, 1);
					Bukkit.getPlayer(p).sendMessage(ChatColor.AQUA + "1 RFSPoint has been added to your account!");
					
				}
				
			}else if(type.equals("vanu sovereignty")){
				
				for(java.util.UUID p : vns){
					
					ConfigUtil.addCerts(p, 200);
					Bukkit.getPlayer(p).sendMessage(ChatColor.AQUA + "200 certs have been added to your account!");
					
					PointsLink.addPTS(p, 1);
					Bukkit.getPlayer(p).sendMessage(ChatColor.AQUA + "1 RFSPoint has been added to your account!");
					
				}
				
			}else if(type.equals("new conglomerate")){
				
				for(java.util.UUID p : ncs){
					
					ConfigUtil.addCerts(p, 200);
					Bukkit.getPlayer(p).sendMessage(ChatColor.AQUA + "200 certs have been added to your account!");
					
					PointsLink.addPTS(p, 1);
					Bukkit.getPlayer(p).sendMessage(ChatColor.AQUA + "1 RFSPoint has been added to your account!");
					
				}
				
			}
				
		DyeColor c = DyeColor.WHITE;
		
		c = GeneralUtil.getColore(type);
		
		for(Location l : woolmap){
			l.getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		}
		
		
		Material mater = GeneralUtil.getMat(type);
		
		if(mater != null){
		
		for(Location l : glassmap){

				l.getBlock().setType(mater);
			
		}
		
		}
		
	/*	new Location(MainDAO.world , corner_1.x, corner_1.y, corner_1.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_1.x, corner_2.y, corner_1.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_2.x, corner_1.y, corner_1.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_2.x, corner_2.y, corner_1.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_2.x, corner_2.y, corner_2.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_2.x, corner_1.y, corner_2.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_1.x, corner_2.y, corner_2.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false);
		
		new Location(MainDAO.world , corner_1.x, corner_1.y, corner_2.z).getBlock().setTypeIdAndData(Material.WOOL.getId(), c.getData() , false); */
		
	}
	
	public void broadcast(String message){
	
		for(java.util.UUID p : trs){
			
			Player pl = Bukkit.getPlayer(p);
			if(pl == null){
				continue;
			}
			pl.sendMessage(message);
			
		}
		
		for(java.util.UUID p : vns){
			
			Player pl = Bukkit.getPlayer(p);
			if(pl == null){
				continue;
			}
			pl.sendMessage(message);
			
		}
		
		for(java.util.UUID p : ncs){
			
			Player pl = Bukkit.getPlayer(p);
			if(pl == null){
				continue;
			}
			pl.sendMessage(message);
			
		}
		
	}	
	
	public Location getInstantActionLocation(String type){
		
		if(type.equals("terran republic")){
			return tr_insta;
		}else if(type.equals("vanu sovereignty")){
			return vanu_insta;
		}else if(type.equals("new conglomerate")){
			return nc_insta;
		}
		return MainDAO.tutorialLoc;
		
	}
		
	
}

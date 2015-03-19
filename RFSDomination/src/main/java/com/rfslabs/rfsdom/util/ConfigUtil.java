package com.rfslabs.rfsdom.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtil {
	
	public static File p_folder = new File(MainDAO.plugin.getDataFolder().toString().concat(File.separator + "players"));
	public static File outp_folder = new File(MainDAO.plugin.getDataFolder().toString().concat(File.separator + "outpost"));
	public static boolean db = false;
	
	public static Connection con;
	
	public static void saveEverything(){
		MainDAO.plugin.saveConfig();
	}

	public static void loadEverything(){
		
		/*if(!new File(MainDAO.plugin.getDataFolder().toString().concat(File.separator + "config.yml")).exists()){
			LogUtil.log("config.yml not found , creating default");
			MainDAO.plugin.saveDefaultConfig();
		}*/
		
		MainDAO.world = Bukkit.getServer().getWorld(MainDAO.plugin.getConfig().getString("world"));
		
		MainDAO.tutorialLoc = new Location(MainDAO.world ,
				MainDAO.plugin.getConfig().getInt("tutorial_loc.x"),
				MainDAO.plugin.getConfig().getInt("tutorial_loc.y"),
				MainDAO.plugin.getConfig().getInt("tutorial_loc.z"));
		
		MainDAO.terran_republicSpawn = new Location(MainDAO.world ,
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.terran_republic.x"),
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.terran_republic.y"),
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.terran_republic.z"));
		
		MainDAO.vanu_sovereigntySpawn = new Location(MainDAO.world ,
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.vanu_sovereignty.x"),
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.vanu_sovereignty.y"),
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.vanu_sovereignty.z"));
		
		MainDAO.new_conglomerateSpawn = new Location(MainDAO.world ,
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.new_conglomerate.x"),
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.new_conglomerate.y"),
				MainDAO.plugin.getConfig().getInt("team_spawn_loc.new_conglomerate.z"));
		
		MainDAO.lvl_up_kills = MainDAO.plugin.getConfig().getInt("lvlup-kills");
		MainDAO.throwup = MainDAO.plugin.getConfig().getInt("throwup-pow");
		
		MainDAO.gen_rld = MainDAO.plugin.getConfig().getInt("generator-delay-secs");
		MainDAO.outpost_timer = MainDAO.plugin.getConfig().getInt("outpost-per-25-percent-secs");
		
		db = MainDAO.plugin.getConfig().getBoolean("uses-db");
		
		//Bukkit.getServer().broadcastMessage("Uses-db : " + db);
		
		try {
			
			setUpOutposts();
			
				if(!db){
				
					loadPlayerFolder();
			
				}else{
					
					initDB();
					
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void initDB() {
		

		 try {
			 
			Class.forName("com.mysql.jdbc.Driver"); //not sure if needed
			 
			con = DriverManager.getConnection(MainDAO.plugin.getConfig().getString("database.host"), MainDAO.plugin.getConfig().getString("database.username"), MainDAO.plugin.getConfig().getString("database.password"));
			
			con.setAutoCommit(true);
			
			Statement sta = con.createStatement();
			
			sta.execute("CREATE TABLE IF NOT EXISTS PS2"
					+ "(uuid VARCHAR(45),"
					+ "team VARCHAR(20),"
					+ "kills INTEGER,"
					+ "deaths INTEGER,"
					+ "rank_lvl INTEGER,"
					+ "rank_name VARCHAR(25),"
					+ "certs INTEGER,"
					+ "armorUpgrade VARCHAR(10) DEFAULT '0')");

			sta.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	 
		
	}

	private static void loadPlayerFolder() throws IOException {		
		if(!p_folder.exists()){
			LogUtil.log("players folder not found , creating");
			p_folder.mkdirs();
			return;
		}
	}

	public static void setUpOutposts() throws IOException{
		
		if(!outp_folder.exists()){
			LogUtil.log("outposts folder not found , creating");
			outp_folder.mkdirs();
		}
		
		for(File f: outp_folder.listFiles()){
			if(f.getName().endsWith("yml")){
				
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			
			Generator one = new Generator(new Location(MainDAO.world , yml.getInt("gen.1.x") ,yml.getInt("gen.1.y") , yml.getInt("gen.1.z")) , yml.getString("status"));
			Generator two = new Generator(new Location(MainDAO.world , yml.getInt("gen.2.x") ,yml.getInt("gen.2.y") , yml.getInt("gen.2.z")) , yml.getString("status"));
			Generator three = new Generator(new Location(MainDAO.world , yml.getInt("gen.3.x") ,yml.getInt("gen.3.y") , yml.getInt("gen.3.z")) , yml.getString("status"));
			Generator four = new Generator(new Location(MainDAO.world , yml.getInt("gen.4.x") ,yml.getInt("gen.4.y") , yml.getInt("gen.4.z")) , yml.getString("status"));
			Generator five = new Generator(new Location(MainDAO.world , yml.getInt("gen.5.x") ,yml.getInt("gen.5.y") , yml.getInt("gen.5.z")) , yml.getString("status"));
					
			MainDAO.generators.add(one);
			MainDAO.generators.add(two);
			MainDAO.generators.add(three);
			MainDAO.generators.add(four);
			MainDAO.generators.add(five);
			
			Location r_loc = new Location(MainDAO.world , yml.getInt("respawn_chamber.x") , yml.getInt("respawn_chamber.y"), yml.getInt("respawn_chamber.z"));
			
			Location tr_insta = new Location(MainDAO.world , yml.getInt("insta.tr.x") , yml.getInt("insta.tr.y"), yml.getInt("insta.tr.z"));
			Location vanu_insta = new Location(MainDAO.world , yml.getInt("insta.vanu.x") , yml.getInt("insta.vanu.y"), yml.getInt("insta.vanu.z"));
			Location nc_insta = new Location(MainDAO.world , yml.getInt("insta.nc.x") , yml.getInt("insta.nc.y"), yml.getInt("insta.nc.z"));
			
			MainDAO.outposts.add(new Outpost(
					new CoordPack(yml.getInt("corner.1.x"),yml.getInt("corner.1.y"),yml.getInt("corner.1.z")),
					new CoordPack(yml.getInt("corner.2.x"),yml.getInt("corner.2.y"),yml.getInt("corner.2.z")),
					yml.getString("status"),
					yml.getInt("uuid"),
					one,
					two,
					three,
					four,
					five,
					r_loc,
					yml.getString("name"),
					tr_insta,
					vanu_insta,
					nc_insta));
			
			}
		}
		
	}
	
	public static void createPlayerFile(UUID name){
		
		if(!db){
			
			try{
				
			File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
			if(f.exists()){
				return;
			}
			f.createNewFile();
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		
			yml.set("team", "none");
			yml.set("kills", 0);
			yml.set("deaths", 0);
			yml.set("rank.lvl", 1);
			yml.set("rank.name", "Starter");
			yml.set("certs", 0);
		
			yml.save(f);
			}catch(IOException e){
				e.printStackTrace();
			}
		
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				ResultSet rs = sta.executeQuery("SELECT kills FROM PS2 WHERE uuid='" + name.toString() + "'");
				
				if(rs.next()){//if registered
					return;
				}	
					
				sta.executeUpdate("INSERT INTO PS2 (uuid , team , kills , deaths , rank_lvl , rank_name , certs) VALUES"
						+ "('" + name.toString() + "' , 'none' , 0 , 0 , 1 , 'Starter' , 0 , '0' )");
				
				
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			
		}
	}
	
	public static void addCerts(UUID name, int amount) {
		
		if(!db){
		try{
		File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		yml.set("certs", yml.getInt("certs") + amount);
		yml.save(f);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE PS2 SET certs="+ (getCerts(name) + amount) + " WHERE uuid='" + name.toString() +"'");
				
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static int getCerts(UUID name) {
		
		if(!db){
		File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		return yml.getInt("certs");
		
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				ResultSet rs = sta.executeQuery("SELECT certs FROM PS2 WHERE uuid='" + name.toString() +"'");
				
				if(rs.next()){
					
					int a = rs.getInt("certs");
					
					sta.close();
					rs.close();
					
					return a;
					
				}
				
				sta.close();
				rs.close();
				
				return 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		return 0;
	}
	
	public static void loadPlayer(final UUID name){
		
		if(!db){
		
		createPlayerFile(name);
		
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml")));
		MainDAO.players.put(name, yml.getString("team"));
		}else{
			
			try {
				
				createPlayerFile(name);
				
				Statement sta = con.createStatement();
				
				ResultSet rs = sta.executeQuery("SELECT team , armorUpgrade FROM PS2 WHERE uuid='" + name.toString() + "'");
				
				if(rs.next()){
					
					MainDAO.players.put(name, rs.getString("team"));
					MainDAO.armor_up.put(name, rs.getString("armorUpgrade"));
					
				}else{
					
					//Bukkit.getPlayer(name).kickPlayer("Database error!");
					MainDAO.players.put(name, "none");
				}
				
				rs.close();
				sta.close();
				//Bukkit.getServer().broadcastMessage(MainDAO.players.get(name));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	public static void setPlayerTeam(UUID name, String team) {	
		if(!db){
			
		try{
		File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		yml.set("team", team);
		yml.save(f);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		loadPlayer(name);
		
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE PS2 SET team='" + team + "' WHERE uuid='" + name.toString() + "'");
				
				sta.close();
				
				loadPlayer(name);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void changeOutpostStatus(int UUID , String status){
		File temp = new File(outp_folder.getPath().toString().concat(File.separator + "outpost" + UUID + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(temp);
		yml.set("status", status);
		try {
			yml.save(temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addKill(UUID name) {
		
		if(!db){
		
		try{
			File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			yml.set("kills", getKills(name) + 1);
			yml.save(f);
			}catch(IOException e){
				e.printStackTrace();
		}
		
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE PS2 SET kills=" + (getKills(name) + 1) + " WHERE uuid='" + name.toString() + "'");
				
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static int getKills(UUID name){
		
		if(!db){
			
			File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			return yml.getInt("kills");
			
		}else{
				
			try {
				
				Statement sta = con.createStatement();
				
				ResultSet rs = sta.executeQuery("SELECT kills FROM PS2 WHERE uuid='"+ name.toString() +"'");
				
				if(rs.next()){
					
					int a = rs.getInt("kills");
					
					rs.close();
					sta.close();
					
					return a;
				}
				
				rs.close();
				sta.close();
				
				return 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
				
		}
		return 0;
	}

	public static void addDeath(UUID name) {
		
		if(!db){
	
		try{
			File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			yml.set("deaths", getDeaths(name) + 1);
			yml.save(f);
			}catch(IOException e){
				e.printStackTrace();
		}
		
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE PS2 SET deaths=" + (getDeaths(name) + 1) + " WHERE uuid='" + name.toString() + "'");
				
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public static int getDeaths(UUID name){
		
		if(!db){
			
			File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			return yml.getInt("deaths");
			
		}else{
				
			try {
				Statement sta = con.createStatement();
				
				ResultSet rs = sta.executeQuery("SELECT deaths FROM PS2 WHERE uuid='"+ name.toString() +"'");
				
				if(rs.next()){
					
					int a = rs.getInt("deaths");
					
					rs.close();
					sta.close();
					
					return a;
				}
				
				rs.close();
				sta.close();
				
				return 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
				
		}
		return 0;
	}

	public static void tryLevelup(UUID name) {
		
		if(!db){
		
		try{
			File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			
			if(getKills(name) >= MainDAO.lvl_up_kills * getLevel(name)){
				
				yml.set("rank.lvl", getLevel(name) + 1);
				yml.set("rank.name", getRankName(getLevel(name)));
				yml.save(f);
			}
			
			}catch(IOException e){
				e.printStackTrace();
		}
		
		}else{
			
			if(getKills(name) >= MainDAO.lvl_up_kills * getLevel(name)){
			
				try {
					
					Statement sta = con.createStatement();
					
					sta.execute("UPDATE PS2 SET rank_lvl=" + (getLevel(name) + 1) +" WHERE uuid='"+ name.toString() +"'");
					sta.execute("UPDATE PS2 SET rank_name='" + getRankName(getLevel(name)) +"' WHERE uuid='"+ name.toString() +"'");
					
					sta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		}
			
	}



	public static int getLevel(UUID name) {
		
		if(!db){
		
		File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		return yml.getInt("rank.lvl");
	
		}else{
			
			try {
				
				Statement sta = con.createStatement();
				
				ResultSet rs = sta.executeQuery("SELECT rank_lvl FROM PS2 WHERE uuid='"+ name.toString() +"'");
				
				if(rs.next()){
					
					int a = rs.getInt("rank_lvl");
					
					sta.close();
					rs.close();
					
					return a;
				}
				
				sta.close();
				rs.close();
				
				return 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return 0;
	}
	
	public static String getRank(UUID name) {
		
		if(!db){
		
		File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		return yml.getString("rank.name");
		
		
		}else{
			
			try {
			Statement sta = con.createStatement();
			
			ResultSet rs = sta.executeQuery("SELECT rank_name FROM PS2 WHERE uuid='"+ name.toString() +"'");
			
			if(rs.next()){
				
				String a = rs.getString("rank_name");
				
				sta.close();
				rs.close();
				
				return a;
			}
			
			sta.close();
			rs.close();
			
			return "nil";
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return "";
	}
	
	public static void rmCerts(UUID name , int amount){
		
		if(!db){
		
		File f = new File(p_folder.getPath().toString().concat(File.separator + name.toString() + ".yml"));
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
		yml.set("certs", yml.getInt("certs") - amount);
		try {
			yml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}else{
			
			try {
				Statement sta = con.createStatement();
				
				sta.executeUpdate("UPDATE PS2 SET certs="+ (getCerts(name) - amount) + " WHERE uuid='" + name.toString() +"'");
				
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	private static String getRankName(int rank) {
		
		if(rank == 2){
			return "R";
		}else if(rank == 3){
			return "R I";
		}else if(rank == 4){
			return "R II";
		}else if(rank == 5){
			return "R III";
		}else if(rank == 6){
			return "R IV";
		}else if(rank == 7){
			return "R V";
		}else if(rank == 8){
			return "Ra";
		}else if(rank == 9){
			return "Ra I";
		}else if(rank == 10){
			return "Ra II";
		}else if(rank == 11){
			return "Ra III";
		}else if(rank == 12){
			return "Ra IV";
		}else if(rank == 13){
			return "Ra V";
		}else if(rank == 14){
			return "Rai I";
		}else if(rank == 15){
			return "Rai II";
		}else if(rank == 16){
			return "Rai III";
		}else if(rank == 17){
			return "Rai IV";
		}else if(rank == 18){
			return "Rai V";
		}else if(rank == 19){
			return "Rain";
		}else if(rank == 20){
			return "Rain I";
		}else if(rank == 21){
			return "Rain II";
		}else if(rank == 22){
			return "Rain III";
		}else if(rank == 23){
			return "Rain IV";
		}else if(rank == 24){
			return "Rain V";
		}else if(rank == 25){
			return "Rainb";
		}else if(rank == 26){
			return "Rainb I";
		}else if(rank == 27){
			return "Rainb II";
		}else if(rank == 28){
			return "Rainb III";
		}else if(rank == 29){
			return "Rainb IV";
		}else if(rank == 30){
			return "Rainb V";
		}else if(rank == 31){
			return "Rainbo";
		}else if(rank == 32){
			return "Rainbo I";
		}else if(rank == 33){
			return "Rainbo II";
		}else if(rank == 34){
			return "Rainbo III";
		}else if(rank == 35){
			return "Rainbo IV";
		}else if(rank == 36){
			return "Rainbo V";
		}else if(rank == 37){
			return "Rainbow I";
		}else if(rank == 38){
			return "Rainbow II";
		}else if(rank == 39){
			return "Rainbow III";
		}else if(rank == 40){
			return "Rainbow IV";
		}else if(rank == 41){
			return "Rainbow V";
		}else if(rank >= 42){
			return "Rainbow Soldier";
		}
		
		return "nil";
	}

	public static void armorUpgrade(UUID name, String type, int amount) {
		
		ConfigUtil.rmCerts(name, amount);
		
		String prev = ConfigUtil.getArmor(name);
		
		if(type.equalsIgnoreCase("Boots")){
			prev.concat("4");
		}else if(type.equalsIgnoreCase("Chestplate")){
			prev.concat("2");
		}else if(type.equalsIgnoreCase("Leggings")){
			prev.concat("3");
		}else if(type.equalsIgnoreCase("Helmet")){
			prev.concat("1");
		}
		
		String a = ConfigUtil.removeDuplicates(prev);
		
		try {
			Statement sta = con.createStatement();
			
			sta.executeUpdate("UPDATE PS2 SET armorUpgrade='"+ a +"' WHERE uuid='" + name.toString() +"'");
			
			sta.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static String getArmor(UUID name) {
		
		try {
			Statement sta = con.createStatement();
			
			ResultSet rs = sta.executeQuery("SELECT armorUpgrade FROM PS2 WHERE uuid='"+ name.toString() +"'");
			
			if(rs.next()){
				
				String a = rs.getString("armorUpgrade");
				
				sta.close();
				rs.close();
				
				return a;
			}
			
			sta.close();
			rs.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return "0";
	}
	
	static String removeDuplicates(String s) {
	    StringBuilder noDupes = new StringBuilder();
	    for (int i = 0; i < s.length(); i++) {
	        String si = s.substring(i, i + 1);
	        if (noDupes.indexOf(si) == -1) {
	            noDupes.append(si);
	        }
	    }
	    return noDupes.toString();
	}
	
}

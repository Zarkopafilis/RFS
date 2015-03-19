package com.rfslabs.greece.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

public class DatabaseUtil {
	
	public static Connection con;
	public static String db_url = "jdbc:mysql://localhost:3306/c0pointsexdb" , db_pass = "greece" , db_usr = "xyz" , db_tn = "greece";
	public static void init(){
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection(db_url , db_usr, db_pass);
			
			con.setAutoCommit(true);
			
			Statement sta = con.createStatement();
			
			sta.execute("CREATE TABLE IF NOT EXISTS " + db_tn
					+ "(uuid VARCHAR(45) DEFAULT 'Error',"
					+ "name VARCHAR(20) DEFAULT 'Unkown',"
					+ "kills INTEGER DEFAULT 0,"
					+ "deaths INTEGER DEFAULT 0,"
					+ "votekicks INTEGER DEFAULT 0,"
					+ "votekicked INTEGER DEFAULT 0,"
					+ "god VARCHAR(20) DEFAULT 'No god',"
					+ "city VARCHAR(20) DEFAULT 'No city' ,"
					+ "armorUpgrade VARCHAR(10) DEFAULT '0')");

			sta.close();
			
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static void setGod(Player p){
		try{
			
			Statement sta = con.createStatement();
			
			sta.execute("UPDATE " + db_tn + " SET god='"+ ChatStuff.db_townPermToString(p) +"' WHERE uuid='" + p.getUniqueId().toString() + "'");

			sta.close();
			
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static void check(){
		try {
			if(con.isValid(5) && !con.isClosed()){
				con.close();
				init();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void deconstruct(){	
		try {
			if(con.isValid(5) && !con.isClosed()){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}

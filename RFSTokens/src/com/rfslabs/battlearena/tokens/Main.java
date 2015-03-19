package com.rfslabs.battlearena.tokens;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		
		Data.p = this;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //not sure if needed
		try {
			Data.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PointsLink.init();
		Data.init();
		
		Bukkit.getServer().getPluginManager().registerEvents(new ExchangeEvent(), this);
		
	}
	
	@Override
	public void onDisable(){
		PointsLink.shutdown();
		try {
			Data.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

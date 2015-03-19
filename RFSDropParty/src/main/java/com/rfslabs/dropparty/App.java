package com.rfslabs.dropparty;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin{
	
    @Override
    public void onEnable(){
    	
    DAO.p = this;
    
    getCommand("dropparty").setExecutor(new DropParty());	
    	
   
	 
	try {
		
		 Class.forName("com.mysql.jdbc.Driver"); //not sure if needed
		DAO.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	try {
		DAO.con.setAutoCommit(true);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    @Override
    public void onDisable(){
    	
    	try {
			DAO.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}

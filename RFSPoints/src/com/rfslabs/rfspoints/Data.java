package com.rfslabs.rfspoints;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Data {

	public static Connection con;
	
	public static Connection newInstance(){

	    		try {
					return DriverManager.getConnection("jdbc:mysql://localhost:3306/c0pointsexdb", "c0pointsex", "xG1UM8ZghmHIxEo");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
	    	}
}
	


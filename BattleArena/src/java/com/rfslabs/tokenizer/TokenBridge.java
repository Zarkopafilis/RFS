package com.rfslabs.tokenizer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class TokenBridge {

	public static Connection con;
	
	public static int getTokens(UUID id) {
		try {
			Statement s = con.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT tokens FROM battlearena WHERE uuid='" + id.toString() +"'");
			int i = 0;
			if(rs.next()){
				i = rs.getInt("tokens");
				
			}
			
			s.close();
			
			rs.close();
			
			return i;
			
		}catch(Exception e){
			e.printStackTrace();
			}
		
		return 0;
	}
	
	public static void setTokens(UUID id , int amount) {
		try {
			Statement s = con.createStatement();
			
			s.executeUpdate("UPDATE battlearena SET tokens=" + amount + " WHERE uuid='" + id.toString() +"'");
			
			s.close();
			
		}catch(Exception e){
			e.printStackTrace();
			}
	}
	
}

package com.rfslabs.verifier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class ConnectionHandler {

	public static InetSocketAddress rb_addr;
	private static String rec;
	private static String keepalive;
	
	private static ServerSocket welcomeSocket;
	private static boolean terminated;
	
	public static void init() {
		try {
			rb_addr = new InetSocketAddress("rb-mc.net" , 6789);
			welcomeSocket = new ServerSocket(6789);
			keepalive = "alive\n";
			terminated = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loop() throws Exception{
		 while(!terminated){       
			 //Bukkit.getLogger().log(Level.SEVERE, "loop start");
			 Socket connectionSocket = welcomeSocket.accept();      
			// Bukkit.getLogger().log(Level.SEVERE, "accept");
			 BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));            
			 DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());    
			 //Bukkit.getLogger().log(Level.SEVERE, "reading answer");
			 rec = inFromClient.readLine();    
			// Bukkit.getLogger().log(Level.SEVERE, "got answer");
			 if(rec.equals("isalive")){
				 outToClient.writeBytes(keepalive);    
				// Bukkit.getLogger().log(Level.SEVERE, "its alive");
			 }
			 
			 inFromClient.close();
			 
			 outToClient.flush();
			 outToClient.close();
			 
			 connectionSocket.close();
			 //Bukkit.getLogger().log(Level.SEVERE, "flush flush");
		 }
	}

	public static void optionalDeconstruct() {//should add guardian
		
		if(!welcomeSocket.isClosed()){
			try {
				terminated = true;
				welcomeSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}

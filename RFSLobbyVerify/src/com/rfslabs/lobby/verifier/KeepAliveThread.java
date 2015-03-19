package com.rfslabs.lobby.verifier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class KeepAliveThread extends Thread{

	private String key;
	private String isalive = "isalive\n";
	
	public KeepAliveThread(String host){
		//Bukkit.getLogger().log(Level.SEVERE, "const");
		key = host;
	}
	
	public void run(){
		//Bukkit.getLogger().log(Level.SEVERE, "run");
		
		if(!Data.hosts.get(key)){
			Bukkit.getLogger().log(Level.SEVERE, "3rd Party Lobby server is offline : " + key);
			//return;?
		}
		
		try {
			//Bukkit.getLogger().log(Level.SEVERE, "client sock init start:");
			Socket clientSocket = new Socket(key, 6789);
			//Bukkit.getLogger().log(Level.SEVERE, "client sock:");
			if(clientSocket.isConnected() && !clientSocket.isClosed()){
			//	Bukkit.getLogger().log(Level.SEVERE, "conn");
				String in;
				
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());   
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  
			//	Bukkit.getLogger().log(Level.SEVERE, "send handshake");
				outToServer.writeBytes(isalive);   
				outToServer.flush();
				//Bukkit.getLogger().log(Level.SEVERE, "wait for answer");
				in = inFromServer.readLine();   
			//	Bukkit.getLogger().log(Level.SEVERE, "got it");
				if(in.equals("alive")){
					//Bukkit.getLogger().log(Level.SEVERE, "its alive");
					if(!Data.hosts.get(key)){
						Data.hosts.put(key, true);
						Bukkit.getLogger().log(Level.SEVERE, "3rd Party Lobby is now online : " + key);
					}
				}
				
				outToServer.close();
				inFromServer.close();
				clientSocket.close();
			}
			
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			if(Data.hosts.get(key)){
			Data.hosts.put(key, false);
			Bukkit.getLogger().log(Level.SEVERE, "3rd Party Lobby server went offline : " + key);
			}
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			if(Data.hosts.get(key)){
			Data.hosts.put(key, false);
			Bukkit.getLogger().log(Level.SEVERE, "3rd Party Lobby server went offline : " + key);
			}
			//e.printStackTrace();
		}
		
		
		
	}
	
}

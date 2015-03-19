package com.rfslabs.lobby.verifier;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class MinecraftServer {

	private String address = "localhost";
	private int port = 25565;
 
	private int timeout = 1500;
 
	private int pingVersion = -1;
	private int protocolVersion = -1;
	private String gameVersion;
	private String motd;
	private int playersOnline = -1;
	private int maxPlayers = -1;
 
	public MinecraftServer() {
 
	}
 
	public MinecraftServer(String address) {
		this();
 
		this.setAddress(address);
	}
 
	public MinecraftServer(String address,int port) {
		this(address);
 
		this.setPort(port);
	}
 
	public MinecraftServer(String address,int port,int timeout) {
		this(address,port);
 
		this.setTimeout(timeout);
	}
 
	public void setAddress(String address) {
		this.address = address;
	}
 
	public String getAddress() {
		return this.address;
	}
 
	public void setPort(int port) {
		this.port = port;
	}
 
	public int getPort() {
		return this.port;
	}
 
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
 
	public int getTimeout() {
		return this.timeout;
	}
 
	private void setPingVersion(int pingVersion) {
		this.pingVersion = pingVersion;
	}
 
	public int getPingVersion() {
		return this.pingVersion;
	}
 
	private void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
 
	public int getProtocolVersion() {
		return this.protocolVersion;
	}
 
	private void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}
 
	public String getGameVersion() {
		return this.gameVersion;
	}
 
	private void setMotd(String motd) {
		this.motd = motd;
	}
 
	public String getMotd() {
		return this.motd;
	}
 
	private void setPlayersOnline(int playersOnline) {
		this.playersOnline = playersOnline;
	}
 
	public int getPlayersOnline() {
		return this.playersOnline;
	}
 
	private void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
 
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
 
	public boolean fetchData() {
		
		try {
			Socket socket = new Socket();
			OutputStream outputStream;
			DataOutputStream dataOutputStream;
			InputStream inputStream;
			InputStreamReader inputStreamReader;
 
			socket.setSoTimeout(this.timeout);
 
			socket.connect(new InetSocketAddress(
				this.getAddress(),
				this.getPort()
			),this.getTimeout());
 
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
 
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-16BE"));
 
			dataOutputStream.write(new byte[]{
				(byte) 0xFE,
				(byte) 0x01
			});
 
			int packetId = inputStream.read();
 
			if (packetId == -1) {
				
				dataOutputStream.close();
				outputStream.close();
	 
				inputStreamReader.close();
				inputStream.close();
	 
				socket.close();
				
				return false;
			}
 
			if (packetId != 0xFF) {
				
				dataOutputStream.close();
				outputStream.close();
	 
				inputStreamReader.close();
				inputStream.close();
	 
				socket.close();
				
				return false;
			}
 
			int length = inputStreamReader.read();
 
			if (length == -1) {
				
				dataOutputStream.close();
				outputStream.close();
	 
				inputStreamReader.close();
				inputStream.close();
	 
				socket.close();
				
				return false;
			}
 
			if (length == 0) {
				
				dataOutputStream.close();
				outputStream.close();
	 
				inputStreamReader.close();
				inputStream.close();
	 
				socket.close();
				
				return false;
			}
 
			char[] chars = new char[length];
 
			if (inputStreamReader.read(chars,0,length) != length) {
				
				dataOutputStream.close();
				outputStream.close();
	 
				inputStreamReader.close();
				inputStream.close();
	 
				socket.close();
				
				return false;
			}
 
			String string = new String(chars);
 
			if (string.startsWith("§")) {
				String[] data = string.split("\0");
 
				this.setPingVersion(Integer.parseInt(data[0].substring(1)));
				this.setProtocolVersion(Integer.parseInt(data[1]));
				this.setGameVersion(data[2]);
				this.setMotd(data[3]);
				this.setPlayersOnline(Integer.parseInt(data[4]));
				this.setMaxPlayers(Integer.parseInt(data[5]));
			} else {
				String[] data = string.split("§");
 
				this.setMotd(data[0]);
				this.setPlayersOnline(Integer.parseInt(data[1]));
				this.setMaxPlayers(Integer.parseInt(data[2]));
			}
 
			dataOutputStream.close();
			outputStream.close();
 
			inputStreamReader.close();
			inputStream.close();
 
			socket.close();
		} catch (SocketException exception) {
			return false;
		} catch (IOException exception) {
			return false;
		}
 
		return true;
	}
	
}
